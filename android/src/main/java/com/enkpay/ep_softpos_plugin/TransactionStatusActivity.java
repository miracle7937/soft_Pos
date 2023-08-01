package com.enkpay.ep_softpos_plugin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.enkpay.ep_softpos_plugin.data.models.FundWalletRequestData;
import com.google.gson.Gson;

import java.util.Map;

public class TransactionStatusActivity extends AppCompatActivity {
    AppCompatButton continueButton;
    private LottieAnimationView animationView;
    Map<String, String> dataMap;
    TextView transactionType, transactionMessage, amountText , statusMessage;
    boolean isSuccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_status);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        animationView = findViewById(R.id.animation_view);
        transactionType = findViewById(R.id.statusTitle);
        transactionMessage = findViewById(R.id.messageText);
        amountText = findViewById(R.id.amountText);
        statusMessage = findViewById(R.id.statusMessage);
       String dataMap   =  getIntent().getStringExtra("fundWalletRequestData");
       FundWalletRequestData fundWalletRequestData = new Gson().fromJson(dataMap, FundWalletRequestData.class);


        amountText.setText( "NGN "+ fundWalletRequestData.getAmount());
        transactionType.setText(fundWalletRequestData.getTransactionType().toString());
        statusMessage.setText(fundWalletRequestData.getMessage());
        isSuccess = fundWalletRequestData.getStatus();



        if (isSuccess) {
            transactionMessage.setText(R.string.transaction_approved);
            transactionMessage.setTextColor(getResources().getColor(R.color.green));
        } else {
            transactionMessage.setText(R.string.transaction_decline);
            transactionMessage.setTextColor(getResources().getColor(R.color.app_red));

        }

        if (isSuccess) {
            animationView.setAnimation(R.raw.success);
        } else {
            animationView.setAnimation(R.raw.error);
        }
        findViewById(R.id.continue_button).setOnClickListener(v -> {
            finish();

        });
    }
}