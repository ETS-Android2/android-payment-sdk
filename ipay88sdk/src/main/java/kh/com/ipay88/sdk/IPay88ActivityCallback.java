package kh.com.ipay88.sdk;

/*
 * IPay88ActivityCallback
 * IPay88Sdk
 *
 * Created by kunTola on 13/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

import android.webkit.JavascriptInterface;

import kh.com.ipay88.sdk.models.IPay88PayResponse;
import kh.com.ipay88.sdk.utils.IPay88Callback;

/**
 * Javascript Interface Handler to receive payment response
 */
public class IPay88ActivityCallback {
    IPay88Callback callback;

    /**
     * Constructor
     * @param callback
     */
    public IPay88ActivityCallback(IPay88Callback callback) {
        this.callback = callback;
    }

    /**
     * JavascriptInterface
     * @param result
     */
    @JavascriptInterface
    public void postMessage(String result) {
        IPay88PayResponse payResponse = IPay88PayResponse.GenerateObjectFromJSONData(result);
        if (payResponse != null)
            this.callback.onResponse(payResponse);
        else
            this.callback.onFailure("Error Payment Response.");
    }
}
