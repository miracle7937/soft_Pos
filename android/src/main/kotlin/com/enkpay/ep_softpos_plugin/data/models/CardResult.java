package com.enkpay.ep_softpos_plugin.data.models;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.netpluspay.contactless.sdk.card.CardReadResult;

public class CardResult {
    private CardReadResult cardReadResult;
    private String cardScheme;

    public CardReadResult getCardReadResult() {
        return cardReadResult;
    }

    public void setCardReadResult(CardReadResult cardReadResult) {
        this.cardReadResult = cardReadResult;
    }

    public String getCardScheme() {
        return cardScheme;
    }

    public void setCardScheme(String cardScheme) {
        this.cardScheme = cardScheme;
    }

    public CardResult(CardReadResult cardReadResult, String cardScheme) {
        this.cardReadResult = cardReadResult;
        this.cardScheme = cardScheme;
    }

    @NonNull
    @Override
    public String toString() {
        return "CardResult{" +
                "cardReadResult=" + (new Gson()).toJson(cardReadResult) +
                ", cardScheme='" + cardScheme + '\'' +
                '}';
    }
}
