package kh.com.ipay88.sdk;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import kh.com.ipay88.sdk.models.IPay88PayResponse;
import kh.com.ipay88.sdk.utils.IPay88Callback;

/**
 * Javascript Interface Handler to receive payment response
 */
public class IPay88ActivityCallback {
    IPay88Callback callback;
    Context mContext;
    private android.widget.Toast Toast;

    /**
     * Constructor
     * @param c
     * @param callback
     */
    public IPay88ActivityCallback(Context c, IPay88Callback callback) {
        mContext = c;
        this.callback = callback;
    }

    /**
     * JavascriptInterface
     * @param result
     */
    @JavascriptInterface
    public void onResponse(String result) {
        //Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
        Log.e("onResponse", result);
        IPay88PayResponse payResponse = IPay88PayResponse.GenerateObjectFromJSONData(result);
        this.callback.onResponse(payResponse);
    }
}
