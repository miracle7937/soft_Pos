package com.enkpay.ep_softpos_plugin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.danbamitale.epmslib.entities.CardData;
import com.danbamitale.epmslib.entities.ConfigData;
import com.danbamitale.epmslib.entities.KeyHolder;
import com.danbamitale.epmslib.entities.KeyHolderKt;
import com.danbamitale.epmslib.entities.TransactionType;
import com.enkpay.ep_softpos_plugin.UI.dialogs.LoadingDialog;
import com.enkpay.ep_softpos_plugin.api_service.purchase_api.RetrofitBuilder;
import com.enkpay.ep_softpos_plugin.data.enums.Status;
import com.enkpay.ep_softpos_plugin.data.models.CardResult;
import com.enkpay.ep_softpos_plugin.data.models.FundWalletRequestData;
import com.enkpay.ep_softpos_plugin.data.models.FundWalletResponseData;
import com.enkpay.ep_softpos_plugin.databinding.ActivityMainBinding;
import com.enkpay.ep_softpos_plugin.utils.AppUtils;
import com.enkpay.ep_softpos_plugin.utils.Encryption;
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
import com.netpluspay.nibssclient.util.NumberExtensionsKt;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.HashMap;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Pair;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class EPaymentActivity extends AppCompatActivity {
    private final Gson gson = new Gson();
    AppCompatButton continueButton;
    private TextView resultViewerTextView,   titleText ;
    private  String amount, userId;
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
                        Long amountToPay = Long.valueOf(amount.toString());
                        previousAmount = amountToPay;

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epayment);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initializeViews();
        HashMap<String, Object> data = (HashMap<String, Object>) getIntent().getSerializableExtra("data");
        amount = (String) data.get("amount");
        userId = (String) data.get("userId");
        continueButton.setOnClickListener(v -> {
            configureTerminal(amount);
        });
        titleText.setText("You are about to make a payment of NGN"+ amount);
//        configureTerminal(amount);
    }


    public  void  enkPayProcess(String amount){
        Double amountToPay = ((Long) Long.parseLong(amount)).doubleValue();
        launchResultLauncher(makePaymentResultLauncher, amountToPay);
    }














    private void initializeViews() {
        resultViewerTextView = findViewById(R.id.result_tv );
        continueButton=  findViewById(R.id.proceed_button );
        titleText =findViewById(R.id.value_title );

    }

    private void launchResultLauncher(
            ActivityResultLauncher<Intent> launcher,
            Double amountToPay
    ) {
        KeyHolder savedKeyHolder = AppUtils.getSavedKeyHolder();
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
            configureTerminal(null);
        }
    }

    private void configureTerminal(String amount) {
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
                        System.out.println("Hello ================> MIMI");
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
                            //start payment=======================
                        }
                        if(isNotEmpty(amount)){
                            enkPayProcess(amount);
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
        cardData.setPinBlock(cardResult.getCardReadResult().getPinBlock());
        netposPaymentClient.makePayment(
                        this,
                        userData.getTerminalId(),
                        gson.toJson(makePaymentParams),
                        cardResult.getCardScheme(),
                        AppUtils.CARD_HOLDER_NAME,
                        "TESTING_TESTING",
                        "", // Kindly pass empty string to make the sdk generate a unique one for you
                        "" // Kindly pass empty string to make the sdk generate a unique one for you
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TransactionWithRemark>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(TransactionWithRemark transactionWithRemark) {
                        System.out.println("================>  "+ transactionWithRemark);
                        FundWalletRequestData fundWalletRequestData =   new FundWalletRequestData(transactionWithRemark, com.enkpay.ep_softpos_plugin.data.enums.TransactionType.PURCHASE, userId );

                        String encryptData =   Encryption.encrypt( new Gson().toJson(fundWalletRequestData));
                        System.out.println("MMMMM====> "+ encryptData);
                        System.out.println("tttttttttttttttttt "+  new Gson().toJson(fundWalletRequestData));
                        try {
                            new RetrofitBuilder().isFundUserWallet().pushRemote(encryptData).enqueue(new Callback<FundWalletResponseData>() {
                                @Override
                                public void onResponse(Call<FundWalletResponseData> call, Response<FundWalletResponseData> response) {
                                    loaderDialog.dismiss();
                                    gson.toJson(transactionWithRemark);
                                    Intent intent = new Intent(EPaymentActivity.this, TransactionStatusActivity.class);
                                    intent.putExtra("fundWalletRequestData", new Gson().toJson(fundWalletRequestData));
                                    EPaymentActivity.this.startActivityForResult(intent, 1);
                                }

                                @Override
                                public void onFailure(Call<FundWalletResponseData> call, Throwable t) {
                                    loaderDialog.dismiss();

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        System.out.println("================> 222222222 "+ transactionWithRemark);
//                        resultViewerTextView.setText(gson.toJson(transactionWithRemark));
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


    public static boolean isNotEmpty(String s) {
        return s != null && !s.isEmpty() && !s.equals("null");
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // Make sure it matches the request code used in startActivityForResult
            finish();
        }
    }



}