package kh.com.ipay88.sdk.demo;

/*
 * MainActivityCallback
 * Demo App
 *
 * Created by kunTola on 10/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

import java.io.Serializable;

import kh.com.ipay88.sdk.models.IPay88PayResponse;
import kh.com.ipay88.sdk.utils.IPay88Callback;

public class MainActivityCallback implements IPay88Callback, Serializable {
    @Override
    public void onResponse(IPay88PayResponse payResponse) {
        MainActivity.payResponse = payResponse;
    }

    @Override
    public void onFailure(String message) {
        MainActivity.errorMsg = message;
    }
}
