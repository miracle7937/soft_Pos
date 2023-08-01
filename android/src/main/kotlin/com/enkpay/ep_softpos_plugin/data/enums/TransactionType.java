package com.enkpay.ep_softpos_plugin.data.enums;

import com.enkpay.ep_softpos_plugin.data.models.Constants;

public enum TransactionType {
    // EFT TRAN TYPES
    PURCHASE,
    PURCHASE_WITH_CASH_BACK, PURCHASE_WITH_ADDITIONAL_DATA, CASH_ADVANCE,
    REVERSAL, REFUND, PRE_AUTHORIZATION, PRE_AUTHORIZATION_COMPLETION, BALANCE, MINI_STATEMENT, LINK_ACCOUNT_INQUIRY,
    PIN_CHANGE, DEPOSIT, TRANSFER, BILL_PAYMENT, PREPAID, VOID, BILLER_LIST_DOWNLOAD, PRODUCT_LIST_DOWNLOAD,
    BILLER_SUBSCRIPTION_INFO_DOWNLOAD, PAYMENT_VALIDATION,

    PAYXPRESS,

    // NETWORK MGT TYPE
    TERMINAL_MASTER_KEY,
    TERMINAL_SESSION_KEY, TERMINAL_PIN_KEY,
    TERMINAL_PARAMETER_DOWNLOAD, CALL_HOME, DAILY_TRANSACTION_REPORT_DOWNLOAD, CA_PUBLIC_KEY_DOWNLOAD, EMV_APPLICATION_AID_DOWNLOAD,
    DYNAMIC_CURRENCY_CONVERSION, INITIAL_PIN_ENCRYPTION_KEY_DOWNLOAD_EMV, INITIAL_PIN_ENCRYPTION_KEY_DOWNLOAD_TRACK2_DATA,
    TRANZAXIS_WORKING_KEY_INQUIRY, TRANZAXIS_TRAFFIC_ENCRYPTION_WORKING_KEY, TRANZAXIS_ECHO_TEST;

    public String getCode() {
        switch (this) {
            case PURCHASE:
                return Constants.IsoTransactionTypeCode.PURCHASE;
            case PURCHASE_WITH_CASH_BACK:
                return Constants.IsoTransactionTypeCode.PURCHASE_WITH_CASH_BACK;
            case PURCHASE_WITH_ADDITIONAL_DATA:
                return Constants.IsoTransactionTypeCode.PURCHASE_WITH_ADDITIONAL_DATA;
            case CASH_ADVANCE:
                return Constants.IsoTransactionTypeCode.CASH_ADVANCE;
            case REVERSAL:
                return Constants.IsoTransactionTypeCode.REVERSAL;
            case REFUND:
                return Constants.IsoTransactionTypeCode.REFUND;
            case PRE_AUTHORIZATION:
                return Constants.IsoTransactionTypeCode.PRE_AUTHORIZATION;
            case PRE_AUTHORIZATION_COMPLETION:
                return Constants.IsoTransactionTypeCode.PRE_AUTHORIZATION_COMPLETION;
            case BALANCE:
                return Constants.IsoTransactionTypeCode.BALANCE_INQUIRY;
            case MINI_STATEMENT:
                return Constants.IsoTransactionTypeCode.MINI_STATEMENT;
            case LINK_ACCOUNT_INQUIRY:
                return Constants.IsoTransactionTypeCode.LINK_ACCOUNT_INQUIRY;
            case PIN_CHANGE:
                return Constants.IsoTransactionTypeCode.PIN_CHANGE;
            case DEPOSIT:
                return Constants.IsoTransactionTypeCode.DEPOSIT;
            case TRANSFER:
                return Constants.IsoTransactionTypeCode.FUND_TRANSFER;
            case BILL_PAYMENT:
                return Constants.IsoTransactionTypeCode.BILL_PAYMENTS;
            case PREPAID:
                return Constants.IsoTransactionTypeCode.PREPAID;
            case BILLER_LIST_DOWNLOAD:
                return Constants.IsoTransactionTypeCode.BILLER_LIST_DOWNLOAD;
            case PRODUCT_LIST_DOWNLOAD:
                return Constants.IsoTransactionTypeCode.PRODUCT_LIST_DOWNLOAD;
            case BILLER_SUBSCRIPTION_INFO_DOWNLOAD:
                return Constants.IsoTransactionTypeCode.BILLER_SUBSCRIPTION_INFORMATION_DOWNLOAD;
            case PAYMENT_VALIDATION:
                return Constants.IsoTransactionTypeCode.PAYMENT_VALIDATION;
            case TERMINAL_MASTER_KEY:
                return Constants.IsoTransactionTypeCode.TERMINAL_MASTER_KEY;
            case TERMINAL_SESSION_KEY:
                return Constants.IsoTransactionTypeCode.TERMINAL_SESSION_KEY;
            case TERMINAL_PIN_KEY:
                return Constants.IsoTransactionTypeCode.TERMINAL_PIN_KEY;
            case TERMINAL_PARAMETER_DOWNLOAD:
                return Constants.IsoTransactionTypeCode.TERMINAL_PARAMETER_DOWNLOAD;
            case CALL_HOME:
                return Constants.IsoTransactionTypeCode.CALL_HOME;
            case DAILY_TRANSACTION_REPORT_DOWNLOAD:
                return Constants.IsoTransactionTypeCode.DAILY_TRANSACTION_REPORT_DOWNLOAD;
            case DYNAMIC_CURRENCY_CONVERSION:
                return Constants.IsoTransactionTypeCode.DYNAMIC_CURRENCY_CONVERSION;
            case CA_PUBLIC_KEY_DOWNLOAD:
                return Constants.IsoTransactionTypeCode.CA_PUBLIC_KEY_DOWNLOAD;
            case EMV_APPLICATION_AID_DOWNLOAD:
                return Constants.IsoTransactionTypeCode.EMV_APPLICATION_AID_DOWNLOAD;
            case INITIAL_PIN_ENCRYPTION_KEY_DOWNLOAD_EMV:
                return Constants.IsoTransactionTypeCode.INITIAL_PIN_ENCRYPTION_KEY_DOWNLOAD_EMV;
            case INITIAL_PIN_ENCRYPTION_KEY_DOWNLOAD_TRACK2_DATA:
                return Constants.IsoTransactionTypeCode.INITIAL_PIN_ENCRYPTION_KEY_DOWNLOAD_TRACK2_DATA;
            case TRANZAXIS_WORKING_KEY_INQUIRY:
                return Constants.IsoTransactionTypeCode.NEW_WORKING_KEY_INQUIRY_FROM_HOST;
            case TRANZAXIS_TRAFFIC_ENCRYPTION_WORKING_KEY:
                return Constants.IsoTransactionTypeCode.NEW_WORKING_KEY_FOR_TRAFFIC_ENCRYPTION_INQUIRY;
            case TRANZAXIS_ECHO_TEST:
                return Constants.IsoTransactionTypeCode.ECHO_TEST;
            default:
                return "";
        }
    }

    public String getMTI() {
        switch (this) {
            case PURCHASE:
            case PURCHASE_WITH_CASH_BACK:
            case PURCHASE_WITH_ADDITIONAL_DATA:
            case CASH_ADVANCE:
            case DEPOSIT:
            case TRANSFER:
            case BILL_PAYMENT:
            case PREPAID:
            case REFUND:
                return Constants.MTI.TRANSACTION_REQUEST_MTI;
            case PRE_AUTHORIZATION_COMPLETION:
                return Constants.MTI.TRANSACTION_ADVICE_MTI;
            case PRE_AUTHORIZATION:
            case BALANCE:
            case PIN_CHANGE:
            case MINI_STATEMENT:
            case LINK_ACCOUNT_INQUIRY:
                return Constants.MTI.AUTHORIZATION_REQUEST_MTI;
            case REVERSAL:
                return Constants.MTI.REVERSAL_ADVICE_MTI;
            default:
                return Constants.MTI.NETWORK_MGT_REQUEST_MTI;
        }
    }

    //    0210
    public String getMTIResponse() {
        switch (this) {
            case PURCHASE: //0210
            case PURCHASE_WITH_CASH_BACK: //0210
            case PURCHASE_WITH_ADDITIONAL_DATA:
            case CASH_ADVANCE: //0210
            case DEPOSIT:
            case TRANSFER:
            case BILL_PAYMENT:
            case PREPAID:
            case REFUND: //0210
                return Constants.MTI.TRANSACTION_RESPONSE_MTI;
            case PRE_AUTHORIZATION_COMPLETION:
                return Constants.MTI.TRANSACTION_ADVICE_RESPONSE_MTI; //0230
            case PRE_AUTHORIZATION: //0110
            case BALANCE://0110
            case PIN_CHANGE:
            case MINI_STATEMENT:
            case LINK_ACCOUNT_INQUIRY:
                return Constants.MTI.AUTHORIZATION_RESPONSE_MTI;
            case REVERSAL:
                return Constants.MTI.REVERSAL_ADVICE_RESPONSE_MTI;
            default:
                return Constants.MTI.NETWORK_MGT_REQUEST_RESPONSE_MTI;
        }
    }


}
