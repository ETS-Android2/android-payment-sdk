package kh.com.ipay88.sdk.demo.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {
    private static SharedPreferences sharedPref;
    private static final String SHARED_PREF_NAME = "kh.com.ipay88.sdk.demo.sharedPref";

    public static String
            SHARED_PREF_KEYS_CURRENCY = SHARED_PREF_NAME + ".currency",
            SHARED_PREF_BALANCE_USD = SHARED_PREF_NAME + ".balance.usd",
            SHARED_PREF_BALANCE_KHR = SHARED_PREF_NAME + ".balance.khr",

            SHARED_PREF_TARGET_SERVER = SHARED_PREF_NAME + ".target.server",
            SHARED_PREF_MERCHANT_CODE = SHARED_PREF_NAME + ".merchant.code",
            SHARED_PREF_MERCHANT_KEY = SHARED_PREF_NAME + ".merchant.key",
            SHARED_PREF_PAYMENT_ID = SHARED_PREF_NAME + ".payment.id",
            SHARED_PREF_PRO_DESC = SHARED_PREF_NAME + ".pro.desc",
            SHARED_PREF_USER_NAME = SHARED_PREF_NAME + ".user.name",
            SHARED_PREF_USER_EMAIL = SHARED_PREF_NAME + ".user.email",
            SHARED_PREF_USER_CONTACT = SHARED_PREF_NAME + ".user.contact",
            SHARED_PREF_REMARK = SHARED_PREF_NAME + ".remark",
            SHARED_PREF_BACKEND_URL = SHARED_PREF_NAME + ".backend.url";

    public static SharedPreferences getInstance(Context context) {
        sharedPref = sharedPref == null ? context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE) : sharedPref;
        return sharedPref;
    }
}
