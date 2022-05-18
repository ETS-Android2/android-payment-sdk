package kh.com.ipay88.sdk.utils;

/*
 * IPay88SDK
 * IPay88Sdk
 *
 * Created by kunTola on 13/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

import android.app.Activity;
import android.content.Intent;

import java.io.Serializable;

import kh.com.ipay88.sdk.IPay88Activity;
import kh.com.ipay88.sdk.constants.IPay88Constants;
import kh.com.ipay88.sdk.models.IPay88PayRequest;

/**
 * Payment Intent
 */
public final class IPay88SDK {
    /**
     * Start Payment screen
     * @param context
     * @param payRequest
     * @param callback
     */
    public static void checkout(Activity context, IPay88PayRequest payRequest, IPay88Callback callback) {
        Intent intent = new Intent(context, IPay88Activity.class)
                .putExtra(IPay88Constants.PAY_REQUEST_DATA, payRequest)
                .putExtra(IPay88Constants.PAY_REQUEST_CALLBACK, (Serializable) callback);
        context.startActivityForResult(intent, IPay88Constants.PAY_REQUEST_CODE);
    }
}