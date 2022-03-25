package kh.com.ipay88.sdk.utils;

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
