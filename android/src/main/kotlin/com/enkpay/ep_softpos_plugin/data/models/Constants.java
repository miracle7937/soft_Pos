package com.enkpay.ep_softpos_plugin.data.models;

public class Constants   {
    static   public Integer MTI_INDEX = 0;
    static   public Integer BITMAP = 1;
    static   public Integer PRIMARY_ACCOUNT_NUMBER_2 = 2;
    static   public Integer PROCESSING_CODE_3 = 3;
    static   public Integer AMOUNT_TRANSACTION_4 = 4;
    static   public Integer AMOUNT_SETTLEMENT_5 = 5;
    static   public Integer TRANSMISSION_DATE_TIME_7 = 7;
    static   public Integer SYSTEMS_TRACE_AUDIT_NUMBER_11 = 11;
    static   public Integer TIME_LOCAL_TRANSACTION_12 = 12;
    static   public Integer DATE_LOCAL_TRANSACTION_13 = 13;
    static   public Integer DATE_EXPIRATION_14 = 14;
    static   public Integer DATE_SETTLEMENT_15 = 15;
    static   public Integer MERCHANT_TYPE_18 = 18;
    static   public Integer POS_ENTRY_MODE_MANDATORY_22 = 22;
    static   public Integer CARD_SEQUENCE_NUMBER_23 = 23;
    static   public Integer POS_CONDITION_CODE_25 = 25;
    static   public Integer POS_PIN_CAPTURE_CODE_26 = 26;
    static   public Integer AMOUNT_TRANSACTION_FEE_28 = 28;
    static   public Integer AMOUNT_TRANSACTION_PROCESSING_FEE_30 = 30;
    static   public Integer ACQUIRING_INSTITUTION_ID_CODE_32 = 32;
    static   public Integer FORWARDING_INSTITUTION_IDENTIFICATION_33 = 33;
    static   public Integer TRACK_2_DATA_35 = 35;
    static   public Integer RETRIEVAL_REFERENCE_NUMBER_37 = 37;
    static   public Integer AUTHORIZATION_CODE_38 = 38;
    static   public Integer RESPONSE_CODE_39 = 39;
    static   public Integer SERVICE_RESTRICTION_CODE_40 = 40;
    static   public Integer CARD_ACCEPTOR_TERMINAL_ID_41 = 41;
    static   public Integer CARD_ACCEPTOR_ID_CODE_42 = 42;
    static   public Integer CARD_ACCEPTOR_NAME_LOCATION_43 = 43;
    static   public Integer ADDITIONAL_DATA_48 = 48;
    static   public Integer CURRENCY_CODE_49 = 49;
    static   public Integer PIN_DATA_52 = 52;
    static   public Integer SECURITY_RELATED_CONTROL_INFO_53 = 53;
    static   public Integer ADDITIONAL_AMOUNTS_54 = 54;
    static   public Integer INTEGRATED_CIRCUIT_CARD_SYSTEM_RELATED_DATA_55 = 55;
    static   public Integer MESSAGE_REASON_CODE_56 = 56;
    static   public Integer TRANSPORT_ECHO_DATA_59 = 59;
    static   public Integer PAYMENT_INFORMATION_60 = 60;
    static   public Integer PRIVATE_FIELD_MGT_DATA1_62 = 62;
    static   public Integer PRIVATE_FIELD_MGT_DATA2_63 = 63;
    static   public Integer PRIMARY_MESSAGE_HASH_VALUE_64 = 64;
    static   public Integer ORIGINAL_DATA_ELEMENTS_90 = 90;
    static   public Integer REPLACEMENT_AMOUNTS_95 = 95;
    static   public Integer ACCOUNT_IDENTIFICATION1_102 = 102;
    static   public Integer ACCOUNT_IDENTIFICATION2_103 = 103;
    static   public Integer POS_DATA_CODE_123 = 123;
    static   public Integer SECONDARY_MESSAGE_HASH_VALUE_128 = 128;


    // ======= Copied ====== //

    static   public String PREF_KEYHOLDER = "pref_keyholder";
    static   public String PREF_CONFIG_DATA = "pref_config_data";
    static   public String LAST_POS_CONFIGURATION_TIME = "last_pos_configuration_time";
    public static final String TOKEN_RESPONSE_TAG = "TOKEN_RESPONSE_TAG==>";


    public String getDownloadParameterManagementData(String mgtCode, String mainString ) {
        int lengthOfTag = 2;

        int indexOfMgtCodeInMainString = mainString.indexOf(mgtCode);

        if (indexOfMgtCodeInMainString < 0){
            // throw new IllegalArgumentException("Could not locate data");
            return "";}

        String dataLenString = mgtCode.substring(lengthOfTag);

        int dataLength = Integer.parseInt(dataLenString);

        int indexOfMgtData = indexOfMgtCodeInMainString + mgtCode.length();

        return mainString.substring(indexOfMgtData, indexOfMgtData + dataLength);
    }
    public final class MTI {

        public static final String AUTHORIZATION_REQUEST_MTI = "0100";
        public static final String AUTHORIZATION_RESPONSE_MTI = "0110";

        public static final String TRANSACTION_REQUEST_MTI = "0200";
        public static final String TRANSACTION_RESPONSE_MTI = "0210";
        public static final String TRANSACTION_ADVICE_MTI = "0220";
        public static final String TRANSACTION_ADVICE_RESPONSE_MTI = "0230";

        public static final String REVERSAL_ADVICE_MTI = "0420";
        public static final String REVERSAL_ADVICE_REPEAT_MTI = "0421";
        public static final String REVERSAL_ADVICE_RESPONSE_MTI = "0430";

        public static final String NETWORK_MGT_REQUEST_MTI = "0800";
        public static final String NETWORK_MGT_REQUEST_RESPONSE_MTI = "0810";

    }

    public final class IsoTransactionTypeCode {

        public static final String PURCHASE = "00";
        public static final String CASH_ADVANCE = "01";
        public static final String REVERSAL = "00";
        public static final String REFUND = "20";
        public static final String DEPOSIT = "21";
        public static final String PURCHASE_WITH_CASH_BACK = "09";
        public static final String PURCHASE_WITH_ADDITIONAL_DATA = "4F";
        public static final String BALANCE_INQUIRY = "31";
        public static final String LINK_ACCOUNT_INQUIRY = "30";
        public static final String MINI_STATEMENT = "38";
        public static final String FUND_TRANSFER = "40";
        public static final String BILL_PAYMENTS = "48";
        public static final String PREPAID = "4A";
        public static final String BILLER_LIST_DOWNLOAD = "4B";
        public static final String PRODUCT_LIST_DOWNLOAD = "4C";
        public static final String BILLER_SUBSCRIPTION_INFORMATION_DOWNLOAD = "4D";
        public static final String PAYMENT_VALIDATION = "4E";
        public static final String PRE_AUTHORIZATION = "60";
        public static final String PRE_AUTHORIZATION_COMPLETION = "61";
        public static final String PIN_CHANGE = "90";
        public static final String TERMINAL_MASTER_KEY = "9A";
        public static final String TERMINAL_SESSION_KEY = "9B";
        public static final String TERMINAL_PIN_KEY = "9G";
        public static final String TERMINAL_PARAMETER_DOWNLOAD = "9C";
        public static final String CALL_HOME = "9D";
        public static final String CA_PUBLIC_KEY_DOWNLOAD = "9E";
        public static final String EMV_APPLICATION_AID_DOWNLOAD = "9F";
        public static final String DAILY_TRANSACTION_REPORT_DOWNLOAD = "9H";
        public static final String INITIAL_PIN_ENCRYPTION_KEY_DOWNLOAD_TRACK2_DATA = "9I";
        public static final String INITIAL_PIN_ENCRYPTION_KEY_DOWNLOAD_EMV = "9J";
        public static final String DYNAMIC_CURRENCY_CONVERSION = "9K";
        public static final String NEW_WORKING_KEY_INQUIRY_FROM_HOST = "92";
        public static final String NEW_WORKING_KEY_FOR_TRAFFIC_ENCRYPTION_INQUIRY = "95";
        public static final String ECHO_TEST = "99";

    }


    public static String getResponseMessageFromCode(String code) {
        switch (code) {
            case "00":
                return "Approved";
            case "01":
                return "Refer to card issuer";
            case "02":
                return "Refer to card issuer, special condition";
            case "03":
                return "Invalid merchant";
            case "04":
                return "Pick-up card";
            case "05":
                return "Do not honor";
            case "06":
                return "Error";
            case "07":
                return "Pick-up card, special condition";
            case "08":
                return "Honor with identification";
            case "09":
                return "Request in progress";
            case "10":
                return "Approved,partial";
            case "11":
                return "Approved,VIP";
            case "12":
                return "Invalid transaction";
            case "13":
                return "Invalid amount";
            case "14":
                return "Invalid card number";
            case "15":
                return "No such issuer";
            case "16":
                return "Approved,update track 3";
            case "17":
                return "Customer cancellation";
            case "18":
                return "Customer dispute";
            case "19":
                return "Re-enter transaction";
            case "20":
                return "Invalid response";
            case "21":
                return "No action taken";
            case "22":
                return "Suspected malfunction";
            case "23":
                return "Unacceptable transaction fee";
            case "24":
                return "File update not supported";
            case "25":
                return "Unable to locate record";
            case "26":
                return "Duplicate record";
            case "27":
                return "File update edit error";
            case "28":
                return "File update file locked";
            case "29":
                return "File update failed";
            case "30":
                return "Format error";
            case "31":
                return "Bank not supported";
            case "32":
                return "Completed, partially";
            case "33":
                return "Expired card, pick-up";
            case "34":
                return "Suspected fraud, pick-up";
            case "35":
                return "Contact acquirer, pick-up";
            case "36":
                return "Restricted card, pick-up";
            case "37":
                return "Call acquirer security, pick-up";
            case "38":
                return "PIN tries exceeded, pick-up";
            case "39":
                return "No credit account";
            case "40":
                return "Function not supported";
            case "41":
                return "Lost card";
            case "42":
                return "No universal account";
            case "43":
                return "Stolen card";
            case "44":
                return "No investment account";
            case "51":
                return "Not sufficent funds";
            case "52":
                return "No check account";
            case "53":
                return "No savings account";
            case "54":
                return "Expired card";
            case "55":
                return "Incorrect PIN";
            case "56":
                return "No card record";
            case "57":
                return "Transaction not permitted to cardholder";
            case "58":
                return "Transaction not permitted on terminal";
            case "59":
                return "Suspected fraud";
            case "60":
                return "Contact acquirer";
            case "61":
                return "Exceeds withdrawal limit";
            case "62":
                return "Restricted card";
            case "63":
                return "Security violation";
            case "64":
                return "Original amount incorrect";
            case "65":
                return "Exceeds withdrawal frequency";
            case "66":
                return "Call acquirer security";
            case "67":
                return "Hard capture";
            case "68":
                return "Response received too late";
            case "75":
                return "PIN tries exceeded";
            case "77":
                return "Intervene, bank approval required";
            case "78":
                return "Intervene, bank approval required for partial amount";
            case "90":
                return "Cut-off in progress";
            case "91":
                return "Issuer or switch inoperative";
            case "92":
                return "Routing error";
            case "93":
                return "Violation of law";
            case "94":
                return "Duplicate transaction";
            case "95":
                return "Reconcile error";
            case "96":
                return "System malfunction";
            case "98":
                return "Exceeds cash limit";
            case "A3":
                return "Request Error";

            default:
                throw new IllegalArgumentException("Code not recognized");
        }
    }


}
