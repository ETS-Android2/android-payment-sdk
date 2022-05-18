package kh.com.ipay88.sdk;

/*
 * IPay88Activity
 * IPay88Sdk
 *
 * Created by kunTola on 13/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import kh.com.ipay88.sdk.constants.IPay88Constants;
import kh.com.ipay88.sdk.models.IPay88Deeplink;
import kh.com.ipay88.sdk.models.IPay88PayRequest;
import kh.com.ipay88.sdk.models.IPay88PayResponse;
import kh.com.ipay88.sdk.models.IPay88PaymentInfo;
import kh.com.ipay88.sdk.utils.IPay88Callback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * IPay88's Payment screen which contains the WebView
 */
public class IPay88Activity extends AppCompatActivity {

    private IPay88Callback callback;
    private IPay88PayRequest payRequest;

    private WebView wbvPayment;
    private ProgressBar progressBar;
    private FloatingActionButton fabCancel;
    private String clientAppId;
    private String clientUserAgent;
    private String clientAppSecret;

    private List<IPay88Deeplink> IPay88DeeplinkList = new ArrayList<>();
    private byte reqCountForDeeplink = 0;

    private boolean isGetTrxId = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStyle();
        setContentView(R.layout.activity_payment_option);

        initView();
        initData();
    }

    /**
     * Style the Activity
     */
    private void initStyle() {
        // MARK: - Status bar
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(this.getResources().getColor(R.color.white));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * View setup
     */
    private void initView() {
        progressBar = findViewById(R.id.progress_loader);
        // progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        fabCancel = findViewById(R.id.fab_cancel);
        fabCancel.setOnClickListener(view -> {
            try {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Cancellation")
                        .setMessage("Please confirm cancellation of this payment")
                        .setPositiveButton(android.R.string.ok,
                                (dialog, which) -> {
                                    String js = "javascript:window.document.PaymentCancel.submit();";
                                    wbvPayment.loadUrl(js);
                                })
                        .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel())
                        .setCancelable(false)
                        .create()
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
                String errMsg = "Payment Cancelled.";
                showErrorMsg(errMsg);
            }
        });

        wbvPayment = findViewById(R.id.wbv_payment);
        wbvPayment.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                IPay88Deeplink sdkDeeplink = null;
                for (IPay88Deeplink item : IPay88DeeplinkList) {
                    if (url.contains(item.Url) && item.Status) {
                        sdkDeeplink = item;
                        break;
                    }
                }

                if (sdkDeeplink != null) {
                    String deeplinkUrl = url;
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(deeplinkUrl)));
                    } catch (ActivityNotFoundException ex01) {

                        // MARK: - Open Play Store
                        if (sdkDeeplink.PlayStoreId != null && !sdkDeeplink.PlayStoreId.isEmpty()) {
                            try {
                                ex01.printStackTrace();
                                deeplinkUrl = "market://details?id=" + sdkDeeplink.PlayStoreId;
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(deeplinkUrl)));
                            } catch (ActivityNotFoundException ex02) {
                                ex02.printStackTrace();
                                deeplinkUrl = "https://play.google.com/store/apps/details?id=" + sdkDeeplink.PlayStoreId;
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(deeplinkUrl)));
                            }
                        }
                    }
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            // *MARK: - Comment this method when publishing (Prevent Play Store Rejected)
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // MARK: - Prevent flash on receiving result
                boolean IS_TAG_GETTING_RESULT = url.contains("SDK_TYPE=Android");
                showLoading(IS_TAG_GETTING_RESULT);

                validateTrxId(null);
            }
        });
        wbvPayment.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return showJSAlertConfirm(view, message, result, true);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return showJSAlertConfirm(view, message, result, false);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                String errMsg = consoleMessage.message();
                if (errMsg.equals("Uncaught TypeError: Cannot read properties of undefined (reading 'submit')")) {
                    if (!isGetTrxId) {
                        validateTrxId(new IGetTrxIdCallback() {
                            @Override
                            public void completion(boolean isExist) {
                                if (isExist) {
                                    cancelPayment();
                                } else {
                                    showErrorMsg("Payment Cancelled.");
                                }
                            }

                            @Override
                            public void completion(IPay88PaymentInfo payInfo) {

                            }
                        });
                    } else {
                        cancelPayment();
                    }
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });
        wbvPayment.addJavascriptInterface(new IPay88ActivityCallback(new IPay88Callback() {
            @Override
            public void onResponse(IPay88PayResponse payResponse) {
                String merchantSignature = payResponse.getSignature(payRequest.getMerchantKey());
                String merchantAmountReq = payRequest.getAmountHash();

                // MARK: - Validate Signature + Amount
                boolean isValid = false;
                String errorDesc = "";

                if (!merchantSignature.equals(payResponse.getSignature())) {
                    errorDesc = "Signature not match.";
                } else {
                    if (!merchantAmountReq.equals(payResponse.getAmountHash())) {
                        errorDesc = "Amount not match.";
                    } else {
                        isValid = true;
                    }
                }

                if (!isValid) {
                    payResponse.setStatus(0);
                    payResponse.setErrDesc(errorDesc);
                }

                callback.onResponse(payResponse);

                Intent returnIntent = new Intent(IPay88Activity.this, IPay88Activity.class);
                returnIntent.putExtra(IPay88Constants.PAY_RESPONSE_DATA, payResponse);
                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            }

            @Override
            public void onFailure(String message) {
                showErrorMsg(message);
            }
        }), IPay88Constants.JS_OBJECT_NAME);

        WebSettings settings = wbvPayment.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(false);
        settings.setAllowContentAccess(false);
        settings.setAllowFileAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);

        this.clientUserAgent = settings.getUserAgentString();
    }

    /**
     * Get Data & Callback object from Intent
     */
    private void initData() {
        // MARK: - Get Data from previous screen
        Intent intent = this.getIntent();
        if (intent != null
                && intent.getSerializableExtra(IPay88Constants.PAY_REQUEST_DATA) != null
                && intent.getSerializableExtra(IPay88Constants.PAY_REQUEST_CALLBACK) != null) {

            this.payRequest = (IPay88PayRequest) intent.getSerializableExtra(IPay88Constants.PAY_REQUEST_DATA);
            this.payRequest = this.payRequest.adjust();
            this.callback = (IPay88Callback) intent.getSerializableExtra(IPay88Constants.PAY_REQUEST_CALLBACK);

            // MARK: - Get Merchant App ID
            this.clientAppId = getApplicationContext().getPackageName();
            this.clientAppSecret = getMetadata(getApplicationContext(), IPay88Constants.APP_SECRET);

            if (this.clientAppSecret == null) {
                showErrorMsg("Required ClientAppSecret!");
            } else {
                String errorMessage = this.payRequest.validate();
                if (errorMessage == null) {
                    // MARK: - Download Deeplink List
                    getDeeplinkList();
                } else {
                    showErrorMsg(errorMessage);
                }
            }
        } else {
            String errorMsg = "Invalid Payment Request!";
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * JS Alert + Confirm Dialog
     *
     * @param view
     * @param message
     * @param result
     * @param isAlert
     * @return
     */
    private boolean showJSAlertConfirm(WebView view, String message, JsResult result, boolean isAlert) {
        final JsResult finalRes = result;
        if (isAlert) {
            new AlertDialog.Builder(view.getContext())
                    .setTitle(message.contains("cancellation") ? "Cancellation" : null)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            (dialog, which) -> finalRes.confirm())
                    .setCancelable(false)
                    .create()
                    .show();
        } else {
            new AlertDialog.Builder(view.getContext())
                    .setTitle(message.contains("cancellation") ? "Cancellation" : null)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            (dialog, which) -> finalRes.confirm())
                    .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> finalRes.cancel())
                    .setCancelable(false)
                    .create()
                    .show();
        }
        return true;
    }

    /**
     * Loading indicator
     *
     * @param isShow
     */
    private void showLoading(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
        wbvPayment.setVisibility(isShow ? View.INVISIBLE : View.VISIBLE);
        fabCancel.setVisibility(isShow ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * Invoke Error Callback
     *
     * @param message
     */
    private void showErrorMsg(String message) {
        this.callback.onFailure(message);

        Intent returnIntent = new Intent(IPay88Activity.this, IPay88Activity.class);
        setResult(Activity.RESULT_CANCELED, returnIntent);

        finish();
    }

    private Request.Builder defaultHTTPHeaders(String contentType) {
        if (contentType == null)
            contentType = "application/x-www-form-urlencoded";
        String accept = !contentType.equals("application/x-www-form-urlencoded") ? contentType : "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";

        Request.Builder headers = new Request.Builder();
        headers.addHeader("Cache-Control", "max-age=0")
                .addHeader("Origin", "null")
                .addHeader("Content-Type", contentType)
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", this.clientUserAgent)
                .addHeader("Accept", accept)
                .addHeader("Accept-Language", Locale.getDefault().getLanguage())
                .addHeader("AppId", this.clientAppId)
                .addHeader("SecretKey", this.clientAppSecret)
                .addHeader("Type", "Android");
        return headers;
    }

    /**
     * Request to Payment URL
     */
    private void submitPayment() {
        String url = this.payRequest.getPaymentUrl();
        RequestBody formBody = this.payRequest.getFormBody();

        Request request = this.defaultHTTPHeaders(null)
                .url(url)
                .post(formBody)
                .build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                showErrorMsg(e.getMessage());
            }

            public void onResponse(Call call, final Response response) throws IOException {
                final String htmlString = response.body().string();

                wbvPayment.post(() -> {
                    wbvPayment.clearCache(true);
                    wbvPayment.loadDataWithBaseURL(url, htmlString, "text/html", "utf-8", null);
                });
            }
        });
    }

    /**
     * Get Deeplink List to open with external browser (not within the current WebView)
     *
     */
    private void getDeeplinkList() {
        String url = this.payRequest.getDeeplinkListUrl();
        this.reqCountForDeeplink++;

        Request request = this.defaultHTTPHeaders("application/json; charset=utf-8")
                .url(url)
                .post(new FormBody.Builder().build())
                .build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            String errorMessage = "Failed to initialize SDK.";

            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (reqCountForDeeplink <= 3)
                    getDeeplinkList();
                else {
                    errorMessage += " (" + e.getMessage() + ")";
                    showErrorMsg(errorMessage + " Please contact IPay88 Team!");
                }
            }

            public void onResponse(Call call, final Response response) throws IOException {
                final String jsonString = response.body().string();
                boolean isSuccess = false;
                if (!jsonString.equals("")) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        IPay88Deeplink[] deeplinkArray = mapper.readValue(jsonString, IPay88Deeplink[].class);
                        if (deeplinkArray != null && deeplinkArray.length > 0) {
                            isSuccess = true;
                            IPay88DeeplinkList = Arrays.asList(deeplinkArray);
                            submitPayment();
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        errorMessage += " (" + e.getMessage() + ")";
                    }
                }

                if (!isSuccess) {
                    if (reqCountForDeeplink <= 3)
                        getDeeplinkList();
                    else
                        showErrorMsg(errorMessage + " Please contact IPay88 Team!");
                }
            }
        });
    }

    // MARK: - GET TrxId
    private void validateTrxId(IGetTrxIdCallback callback) {
        // MARK: - Get EID for Cancel Payment
        if (!this.isGetTrxId) {
            this.getTrxId(new IGetTrxIdCallback() {
                @Override
                public void completion(boolean isExist) {

                }

                @Override
                public void completion(IPay88PaymentInfo payInfo) {
                    if (payInfo.getEId() != null && !payInfo.getEId().isEmpty()) {
                        Log.e("EID:", payInfo.getEId());
                        isGetTrxId = true;
                        payRequest.setEId(payInfo.getEId());
                        payRequest.setErrDesc("Customer Cancel Transaction");
                        if (callback != null)
                            callback.completion(true);
                    }
                    else {
                        if (callback != null)
                            callback.completion(false);
                    }
                }
            });
        }
    }

    private void getTrxId(IGetTrxIdCallback callback) {
        String url = this.payRequest.getQueryUrl();
        RequestBody formBody = this.payRequest.getFormBodyQuery();

        Request request = this.defaultHTTPHeaders("application/json; charset=utf-8")
                .url(url)
                .post(formBody)
                .build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            IPay88PaymentInfo payInfoResp = new IPay88PaymentInfo();
            String errorMessage = "Failed to initialize SDK.";

            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.completion(payInfoResp);
            }

            public void onResponse(Call call, final Response response) throws IOException {
                final String jsonString = response.body().string();
                if (!jsonString.equals("")) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        IPay88PaymentInfo payInfo = mapper.readValue(jsonString, IPay88PaymentInfo.class);
                        if (payInfo != null) {
                            payInfoResp = payInfo;
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        errorMessage += " (" + e.getMessage() + ")";
                    }
                }
                callback.completion(payInfoResp);
            }
        });
    }

    // MARK: - CANCEL Transaction
    private void cancelPayment() {
        String url = this.payRequest.getCancelUrl();
        this.wbvPayment.loadUrl(url);
    }

    /**
     * Interface to Handle on getting TrxId for Cancellation.
     *
     */
    interface IGetTrxIdCallback {
        void completion(boolean isExist);
        void completion(IPay88PaymentInfo payInfo);
    }

    /**
     * Get the ClientAppSecret from Merchant's AndroidManifest
     *
     * @param context
     * @param name
     * @return
     */
    public static String getMetadata(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}