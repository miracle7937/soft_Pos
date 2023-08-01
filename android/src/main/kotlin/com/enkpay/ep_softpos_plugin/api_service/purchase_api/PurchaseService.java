package com.enkpay.ep_softpos_plugin.api_service.purchase_api;

import com.enkpay.ep_softpos_plugin.data.models.FundWalletRequestData;
import com.enkpay.ep_softpos_plugin.data.models.FundWalletResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PurchaseService {
    @POST("pos")
    Call<FundWalletResponseData> pushRemote(@Body String encryptedBody);
}
