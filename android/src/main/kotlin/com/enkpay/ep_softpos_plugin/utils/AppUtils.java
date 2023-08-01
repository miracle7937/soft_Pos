package com.enkpay.ep_softpos_plugin.utils;

import com.danbamitale.epmslib.entities.KeyHolder;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.netpluspay.nibssclient.models.UserData;

import org.jetbrains.annotations.Nullable;

public class AppUtils {
    public static final String KEY_HOLDER = "KEY_HOLDER";
    public static final String CONFIG_DATA = "CONFIG_DATA";
    public static final String ERROR_TAG = "ERROR_TAG===>";
    public static final String TAG_MAKE_PAYMENT = "TAG_MAKE_PAYMENT";
    public static final String TAG_CHECK_BALANCE = "TAG_CHECK_BALANCE";
    public static final String PAYMENT_SUCCESS_DATA_TAG = "PAYMENT_SUCCESS_DATA_TAG";
    public static final String PAYMENT_ERROR_DATA_TAG = "PAYMENT_ERROR_DATA_TAG";
    public static final String TAG_TERMINAL_CONFIGURATION = "TAG_TERMINAL_CONFIGURATION";
    public static final String CARD_HOLDER_NAME = "CUSTOMER";
    public static final String POS_ENTRY_MODE = "051";

    public static UserData getSampleUserData() {
        return new UserData(
                "Netplus",
                "Netplus",
                "5de231d9-1be0-4c31-8658-6e15892f2b83",
                "2033ALZP",
                "0123456789ABC", // getDeviceSerialNumber(),
                "Marwa Lagos",
                "Test Account",
                "",
                "",
                ""
        );
    }

    public static @Nullable KeyHolder getSavedKeyHolder() {
        String savedKeyHolderInStringFormat = Prefs.getString(KEY_HOLDER);
        return new Gson().fromJson(savedKeyHolderInStringFormat, KeyHolder.class);
    }
}
