package com.enkpay.ep_softpos_plugin.data.models;

import com.enkpay.ep_softpos_plugin.data.enums.TransactionType;
import com.netpluspay.nibssclient.models.TransactionWithRemark;

public class FundWalletRequestData {
    String terminalID;
    Long amount;

    TransactionType transactionType;
    String RRN, STAN, pan, cardName, deviceNO, responseCode, expireDate, message, UserID;
    Boolean status;
    public FundWalletRequestData(TransactionWithRemark requestData, TransactionType transactionType, String userID
                                 ){
        this.terminalID = requestData.getTerminalId();
        this.amount = Long.parseLong(String.valueOf(requestData.getAmount()));
        this.transactionType = transactionType;
        this.STAN = requestData.getSTAN();
        this.RRN = requestData.getRrn();
        this.deviceNO = null;
        this.pan = requestData.getMaskedPan();
        this.cardName = requestData.getCardHolder();
        this.responseCode = requestData.getResponseCode();
        this.expireDate = requestData.getCardExpiry();
        this.message = requestData.getResponseMessage();
        this.UserID = userID;

    }

    public String getMessage() {
        return message;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public Long getAmount() {
        return amount !=null? amount :  0;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getUserID() {
        return UserID;
    }

    public String getRRN() {
        return RRN;
    }

    public void setRRN(String RRN) {
        this.RRN = RRN;
    }

    public String getSTAN() {
        return STAN;
    }

    public void setSTAN(String STAN) {
        this.STAN = STAN;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getDeviceNO() {
        return deviceNO;
    }

    public void setDeviceNO(String deviceNO) {
        this.deviceNO = deviceNO;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public Boolean getStatus() {
        return responseCode.equals("00");
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
