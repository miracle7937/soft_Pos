package com.enkpay.ep_softpos_plugin.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.danbamitale.epmslib.entities.CardData;
import com.danbamitale.epmslib.entities.ConfigData;
import com.danbamitale.epmslib.entities.KeyHolder;
import com.danbamitale.epmslib.entities.KeyHolderKt;
import com.danbamitale.epmslib.entities.TransactionType;
import com.enkpay.ep_softpos_plugin.NetposPaymentClient;
import com.enkpay.ep_softpos_plugin.R;
import com.enkpay.ep_softpos_plugin.UI.dialogs.LoadingDialog;
import com.enkpay.ep_softpos_plugin.data.enums.Status;
import com.enkpay.ep_softpos_plugin.data.models.CardResult;
import com.enkpay.ep_softpos_plugin.databinding.ActivityMainBinding;
import com.enkpay.ep_softpos_plugin.utils.AppUtils;
import com.google.gson.Gson;
import com.netpluspay.contactless.sdk.card.CardReadResult;
import com.netpluspay.contactless.sdk.start.ContactlessSdk;
import com.netpluspay.contactless.sdk.utils.ContactlessReaderResult;
import com.netpluspay.nibssclient.models.AccountBalanceResponse;
import com.netpluspay.nibssclient.models.CheckAccountBalanceResponse;
import com.netpluspay.nibssclient.models.IsoAccountType;
import com.netpluspay.nibssclient.models.MakePaymentParams;
import com.netpluspay.nibssclient.models.TransactionWithRemark;
import com.netpluspay.nibssclient.models.UserData;
//import com.netpluspay.nibssclient.service.NetposPaymentClient;
import com.netpluspay.nibssclient.util.NumberExtensionsKt;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Pair;
import timber.log.Timber;

public class LaunchActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final Gson gson = new Gson();
    private Button makePaymentButton;
    private Button checkBalanceButton;
    private TextView resultViewerTextView;
    private EditText amountET;
    private final UserData userData = AppUtils.getSampleUserData();
    private final @Nullable
    CardData cardData = null;
    private @Nullable
    Long previousAmount = null;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final NetposPaymentClient netposPaymentClient = NetposPaymentClient.INSTANCE;
    private final ActivityResultLauncher<Intent> makePaymentResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                if (result.getResultCode() == ContactlessReaderResult.RESULT_OK) {
                    if (data != null) {
                        Long amountToPay = Long.valueOf(amountET.getText().toString());
                        previousAmount = amountToPay;
                        amountET.getText().clear();
                        String cardReadData = data.getStringExtra("data");
                        Timber.d("SUCCESS_TAG===>%s", cardReadData);
                        CardResult cardResult = gson.fromJson(cardReadData, CardResult.class);
                        processPayment(cardResult, amountToPay);
                    }
                }
                if (result.getResultCode() == ContactlessReaderResult.RESULT_ERROR) {
                    if (data != null) {
                        String error = data.getStringExtra("data");
                        if (error != null) {
                            Timber.d("ERROR_TAG===>%s", error);
                            resultViewerTextView.setText(error);
                        }
                    }
                }
            });

    private final ActivityResultLauncher<Intent> checkBalanceResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                if (result.getResultCode() == ContactlessReaderResult.RESULT_OK) {
                    if (data != null) {
                        amountET.getText().clear();
                        String cardReadData = data.getStringExtra("data");
                        CardResult cardResult = gson.fromJson(cardReadData, CardResult.class);
                        checkBalance(cardResult);
                    }
                }
                if (result.getResultCode() == ContactlessReaderResult.RESULT_ERROR) {
                    if (data != null) {
                        String error = data.getStringExtra("data");
                        if (error != null) {
                            Timber.d("ERROR_TAG===>%s", error);
                            resultViewerTextView.setText(error);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initializeViews();
        configureTerminal();
        makePaymentButton.setOnClickListener(v -> {
            resultViewerTextView.setText("");
            if (amountET.getText() == null) {
                Toast.makeText(this, getString(R.string.amount_is_empty), Toast.LENGTH_LONG)
                        .show();
                return;
            }
            String stringAmt = amountET.getText().toString();
            if (stringAmt.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_valid_amount), Toast.LENGTH_LONG)
                        .show();
                return;
            }
            long amt = Long.parseLong(amountET.getText().toString());
            if (Objects.equals(stringAmt, "") || amt < 200L) {
                Toast.makeText(this, getString(R.string.enter_valid_amount), Toast.LENGTH_LONG)
                        .show();
                return;
            }
            Double amountToPay = ((Long) Long.parseLong(amountET.getText().toString())).doubleValue();
            launchResultLauncher(makePaymentResultLauncher, amountToPay);
        });

        checkBalanceButton.setOnClickListener(v -> {
            resultViewerTextView.setText("");
            launchResultLauncher(checkBalanceResultLauncher, 200.0);
        });
    }

    private void initializeViews() {
        makePaymentButton =  findViewById(R.id.read_card_btn);
        checkBalanceButton = findViewById(R.id.check_balance );
        resultViewerTextView = findViewById(R.id.result_tv );
        amountET =  findViewById(R.id.amountToPay );
    }

    private void launchResultLauncher(
            ActivityResultLauncher<Intent> launcher,
            Double amountToPay
    ) {
        KeyHolder savedKeyHolder = AppUtils.getSavedKeyHolder();
        System.out.println(  new Gson().toJson(AppUtils.getSavedKeyHolder()) + "MIMIM");
        if (savedKeyHolder != null) {
            ContactlessSdk.INSTANCE.readContactlessCard(
                    this,
                    launcher,
                    KeyHolderKt.getClearPinKey(savedKeyHolder), // "86CBCDE3B0A22354853E04521686863D" // pinKey
                    amountToPay, // amount
                    0L // cashbackAmount(optional)
            );
        } else {
            Toast.makeText(
                    this,
                    getString(R.string.terminal_not_configured),
                    Toast.LENGTH_LONG
            ).show();
            configureTerminal();
        }
    }

    private void configureTerminal() {
        LoadingDialog loaderDialog = new LoadingDialog();
        loaderDialog.setLoadingMessage(getString(R.string.configuring_terminal));
        loaderDialog.show(getSupportFragmentManager(), AppUtils.TAG_TERMINAL_CONFIGURATION);
        netposPaymentClient.init(this, new Gson().toJson(userData))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Pair<KeyHolder, ConfigData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Pair<KeyHolder, ConfigData> response) {
                        Toast.makeText(
                                getBaseContext(),
                                getString(R.string.terminal_configured),
                                Toast.LENGTH_LONG
                        ).show();
                        loaderDialog.dismiss();
                        KeyHolder keyHolder = response.getFirst();
                        ConfigData configData = response.getSecond();
                        if (keyHolder != null) {
                            String pinKey = KeyHolderKt.getClearPinKey(keyHolder);
                            Prefs.putString(AppUtils.KEY_HOLDER, gson.toJson(keyHolder));
                            Prefs.putString(AppUtils.CONFIG_DATA, gson.toJson(configData));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                getBaseContext(),
                                getString(R.string.terminal_config_failed),
                                Toast.LENGTH_LONG
                        ).show();
                        loaderDialog.dismiss();
                        Timber.d("%s%s", AppUtils.ERROR_TAG, e.getLocalizedMessage());
                    }
                });
    }


    private void processPayment(@NonNull CardResult cardResult, Long amountToPay) {
        LoadingDialog loaderDialog = new LoadingDialog();
        loaderDialog.setLoadingMessage(getString(R.string.processing_payment));
        loaderDialog.show(getSupportFragmentManager(), AppUtils.TAG_MAKE_PAYMENT);
        CardReadResult cardReadResult = cardResult.getCardReadResult();
        CardData cardData =
                new CardData(cardReadResult.getTrack2Data(), cardReadResult.getIccString(), cardReadResult.getPan(), AppUtils.POS_ENTRY_MODE);
        System.out.println( new Gson().toJson(cardData));
        previousAmount = amountToPay;
        MakePaymentParams makePaymentParams =
                new MakePaymentParams(
                        "makePayment",
                        userData.getTerminalId(),
                        amountToPay,
                        0L,
                        TransactionType.PURCHASE,
                        IsoAccountType.SAVINGS,
                        cardData,
                        ""
                );
        System.out.println(gson.toJson(makePaymentParams)+ "MIIII");
        cardData.setPinBlock(cardResult.getCardReadResult().getPinBlock());
        netposPaymentClient.makePayment(
                        this,
                        userData.getTerminalId(),
                        gson.toJson(makePaymentParams),
                        cardResult.getCardScheme(),
                        AppUtils.CARD_HOLDER_NAME,
                        "TESTING_TESTING",
                        "231209675431", // Kindly pass empty string to make the sdk generate a unique one for you
                        "7589" // Kindly pass empty string to make the sdk generate a unique one for you
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TransactionWithRemark>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(TransactionWithRemark transactionWithRemark) {
                        loaderDialog.dismiss();
                        resultViewerTextView.setText(gson.toJson(transactionWithRemark));
                        Timber.d(
                                "$PAYMENT_SUCCESS_DATA_TAG%s",
                                gson.toJson(transactionWithRemark)
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        loaderDialog.dismiss();
                        resultViewerTextView.setText(e.getLocalizedMessage());
                        Timber.d(
                                "$PAYMENT_ERROR_DATA_TAG%s",
                                e.getLocalizedMessage()
                        );
                    }
                });
    }


    private void checkBalance(@NonNull CardResult cardResult) {
        LoadingDialog loaderDialog = new LoadingDialog();
        loaderDialog.setLoadingMessage(getString(R.string.checking_balance));
        loaderDialog.show(getSupportFragmentManager(), AppUtils.TAG_CHECK_BALANCE);
        CardReadResult cardReadResult = cardResult.getCardReadResult();
        CardData cardData =
                new CardData(cardReadResult.getTrack2Data(), cardReadResult.getIccString(), cardReadResult.getPan(), AppUtils.POS_ENTRY_MODE);

        netposPaymentClient.balanceEnquiry(this, cardData, IsoAccountType.SAVINGS.name())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CheckAccountBalanceResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(CheckAccountBalanceResponse checkAccountBalanceResponse) {
                        loaderDialog.dismiss();
                        if (checkAccountBalanceResponse.getResponseCode().equals(Status.APPROVED.getStatusCode())) {
                            StringBuilder total = new StringBuilder();
                            for (int i = 0; i < checkAccountBalanceResponse.getAccountBalances().size(); ++i) {
                                AccountBalanceResponse acctBal = checkAccountBalanceResponse.getAccountBalances().get(i);
                                Long amnt = (acctBal.getAmount() / 100);
                                String formattedAmount = NumberExtensionsKt.formatCurrencyAmount(amnt, "");
                                String accountBalance = acctBal.getAccountType() + ": " + formattedAmount;
                                total.append(accountBalance);
                            }
                            String responseString = "Response: APPROVED\nResponse Code: " + checkAccountBalanceResponse.getResponseCode() + "\n\nAccount Balance:\n" +
                                    total.toString();
                            resultViewerTextView.setText(responseString);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loaderDialog.dismiss();
                        resultViewerTextView.setText(e.getLocalizedMessage());
                    }
                });
    }


}