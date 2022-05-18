package kh.com.ipay88.sdk.utils;

/*
 * IPay88Callback
 * IPay88Sdk
 *
 * Created by kunTola on 13/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

import kh.com.ipay88.sdk.models.IPay88PayResponse;

/**
 * Payment Callback
 */
public interface IPay88Callback {
    /**
     * Payment Response from IPay88
     * @param payResponse
     */
    public void onResponse(IPay88PayResponse payResponse);

    /**
     * Error Message from IPay88's WebView
     * @param message
     */
    public void onFailure(String message);
}
