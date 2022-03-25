package kh.com.ipay88.sdk.demo;

import android.util.Log;

import java.io.Serializable;

import kh.com.ipay88.sdk.models.IPay88PayResponse;
import kh.com.ipay88.sdk.utils.IPay88Callback;

public class MainActivityCallback implements IPay88Callback, Serializable {
    @Override
    public void onResponse(IPay88PayResponse payResponse) {
        Log.d("onResponse->", payResponse.getStatus());
        MainActivity.payResponse = payResponse;
    }

    @Override
    public void onFailure(String message) {
        Log.e("onFailure->", message);
        MainActivity.errorMsg = message;
    }
}
