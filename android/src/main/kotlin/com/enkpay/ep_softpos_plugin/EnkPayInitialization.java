package com.enkpay.ep_softpos_plugin;
import android.content.Intent;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.danbamitale.epmslib.entities.CardData;
import com.danbamitale.epmslib.entities.ConfigData;
import com.danbamitale.epmslib.entities.KeyHolder;
import com.danbamitale.epmslib.entities.KeyHolderKt;
import com.danbamitale.epmslib.entities.TransactionType;
import com.enkpay.ep_softpos_plugin.UI.dialogs.LoadingDialog;
import com.enkpay.ep_softpos_plugin.api_service.purchase_api.RetrofitBuilder;
import com.enkpay.ep_softpos_plugin.data.models.CardResult;
import com.enkpay.ep_softpos_plugin.data.models.FundWalletRequestData;
import com.enkpay.ep_softpos_plugin.data.models.FundWalletResponseData;
import com.enkpay.ep_softpos_plugin.utils.AppUtils;
import com.enkpay.ep_softpos_plugin.utils.Encryption;
import com.google.gson.Gson;
import com.netpluspay.contactless.sdk.card.CardReadResult;
import com.netpluspay.contactless.sdk.start.ContactlessSdk;
import com.netpluspay.contactless.sdk.utils.ContactlessReaderResult;
import com.netpluspay.nibssclient.models.IsoAccountType;
import com.netpluspay.nibssclient.models.MakePaymentParams;
import com.netpluspay.nibssclient.models.TransactionWithRemark;
import com.netpluspay.nibssclient.models.UserData;
import com.pixplicity.easyprefs.library.Prefs;

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

public class EnkPayInitialization
{
    public  EnkPayInitialization(FragmentActivity activity){
        this.activity = activity;
    }
    private final Gson gson = new Gson();
    FragmentActivity activity;
    private  String amount;
    private final UserData userData = AppUtils.getSampleUserData();
    private final @Nullable
    CardData cardData = null;
    private @Nullable
    Long previousAmount = null;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final NetposPaymentClient netposPaymentClient = NetposPaymentClient.INSTANCE;
    private final ActivityResultLauncher<Intent> makePaymentResultLauncher =
            activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
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

                        }
                    }
                }
            });

//    private final ActivityResultLauncher<Intent> checkBalanceResultLauncher =
//            activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//                Intent data = result.getData();
//                if (result.getResultCode() == ContactlessReaderResult.RESULT_OK) {
//                    if (data != null) {
//                        String cardReadData = data.getStringExtra("data");
//                        CardResult cardResult = gson.fromJson(cardReadData, CardResult.class);
//                        checkBalance(cardResult);
//                    }
//                }
//                if (result.getResultCode() == ContactlessReaderResult.RESULT_ERROR) {
//                    if (data != null) {
//                        String error = data.getStringExtra("data");
//                        if (error != null) {
//                            Timber.d("ERROR_TAG===>%s", error);
//                            resultViewerTextView.setText(error);
//                        }
//                    }
//                }
//            });

    private void processPayment(@NonNull CardResult cardResult, Long amountToPay) {
        LoadingDialog loaderDialog = new LoadingDialog();
        loaderDialog.setLoadingMessage(activity.getString(R.string.processing_payment));
        loaderDialog.show(activity.getSupportFragmentManager(), AppUtils.TAG_MAKE_PAYMENT);
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
                        activity,
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
                        FundWalletRequestData fundWalletRequestData =   new FundWalletRequestData(transactionWithRemark, com.enkpay.ep_softpos_plugin.data.enums.TransactionType.PURCHASE, null );

                        String encryptData =   Encryption.encrypt( new Gson().toJson(fundWalletRequestData));
                        System.out.println("MMMMM====> "+ encryptData);
                        System.out.println("tttttttttttttttttt "+  new Gson().toJson(fundWalletRequestData));
                        try {
                            new RetrofitBuilder().isFundUserWallet().pushRemote(encryptData).enqueue(new Callback<FundWalletResponseData>() {
                                @Override
                                public void onResponse(Call<FundWalletResponseData> call, Response<FundWalletResponseData> response) {
                                    gson.toJson(transactionWithRemark);
                                    Intent intent = new Intent(activity, TransactionStatusActivity.class);
                                    intent.putExtra("fundWalletRequestData", new Gson().toJson(fundWalletRequestData));
                                    activity.startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<FundWalletResponseData> call, Throwable t) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        loaderDialog.dismiss();
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
                        Timber.d(
                                "$PAYMENT_ERROR_DATA_TAG%s",
                                e.getLocalizedMessage()
                        );
                    }
                });
    }
    private void launchResultLauncher(
            ActivityResultLauncher<Intent> launcher,
            Double amountToPay
    ) {
        KeyHolder savedKeyHolder = AppUtils.getSavedKeyHolder();
        if (savedKeyHolder != null) {
            ContactlessSdk.INSTANCE.readContactlessCard(
                    activity,
                    launcher,
                    KeyHolderKt.getClearPinKey(savedKeyHolder), // "86CBCDE3B0A22354853E04521686863D" // pinKey
                    amountToPay, // amount
                    0L // cashbackAmount(optional)
            );
        } else {
            Toast.makeText(
                    activity,
                    activity.getString(R.string.terminal_not_configured),
                    Toast.LENGTH_LONG
            ).show();
            configureTerminal(null);
        }
    }

    public void configureTerminal(String amount) {
        LoadingDialog loaderDialog = new LoadingDialog();
        loaderDialog.setLoadingMessage(activity.getString(R.string.configuring_terminal));
        loaderDialog.show(activity.getSupportFragmentManager(), AppUtils.TAG_TERMINAL_CONFIGURATION);
        netposPaymentClient.init(activity, new Gson().toJson(userData))
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
                                activity,
                                activity.getString(R.string.terminal_configured),
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
                                activity,
                                activity.getString(R.string.terminal_config_failed),
                                Toast.LENGTH_LONG
                        ).show();
                        loaderDialog.dismiss();
                        Timber.d("%s%s", AppUtils.ERROR_TAG, e.getLocalizedMessage());
                    }
                });
    }
    public static boolean isNotEmpty(String s) {
        return s != null && !s.isEmpty() && !s.equals("null");
    }

    public  void enkPayProcess(String amount){

        Double amountToPay = ((Long) Long.parseLong(amount)).doubleValue();
        launchResultLauncher(makePaymentResultLauncher, amountToPay);
    }
}
