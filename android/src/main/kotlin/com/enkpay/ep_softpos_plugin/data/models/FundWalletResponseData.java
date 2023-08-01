package com.enkpay.ep_softpos_plugin.data.models;

public class FundWalletResponseData {
    public boolean status;
    public String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
