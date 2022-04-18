package kh.com.ipay88.sdk.demo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kh.com.ipay88.sdk.demo.utils.SharedPrefHelper;
import kh.com.ipay88.sdk.models.IPay88PayRequest;

public class SettingActivity extends AppCompatActivity {

    private RadioGroup rdgTargetServer;
    private RadioButton rdbDvl, rdbSandbox, rdbProd;
    private EditText edtMerchantCode, edtMerchantKey, edtPaymentId, edtProDesc, edtUserName, edtUserEmail, edtUserContact, edtRemark, edtBackendUrl;
    private Button btnSave;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Setting");

        initData();
        initView();
    }

    private void initData() {
        // MARK: - Get SharedPreference
        sharedPref = SharedPrefHelper.getInstance(getApplicationContext());
    }

    private void initView() {
        rdgTargetServer = findViewById(R.id.rdgTargetServer);
        rdbDvl = findViewById(R.id.rdbDvl);
        rdbSandbox = findViewById(R.id.rdbSandbox);
        rdbProd = findViewById(R.id.rdbProd);

        edtMerchantCode = findViewById(R.id.edtMerchantCode);
        edtMerchantKey = findViewById(R.id.edtMerchantKey);
        edtPaymentId = findViewById(R.id.edtPaymentId);
        edtProDesc = findViewById(R.id.edtProDesc);
        edtUserName = findViewById(R.id.edtUserName);
        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserContact = findViewById(R.id.edtUserContact);
        edtRemark = findViewById(R.id.edtRemark);
        edtBackendUrl = findViewById(R.id.edtBackendUrl);

        // MARK: - Fill-in form data
        String server = sharedPref.getString(SharedPrefHelper.SHARED_PREF_TARGET_SERVER, "DVL");
        rdbDvl.setChecked(server.equals(IPay88PayRequest.Environment.DVL.name()));
        rdbSandbox.setChecked(server.equals(IPay88PayRequest.Environment.SANDBOX.name()));
        rdbProd.setChecked(server.equals(IPay88PayRequest.Environment.PRODUCTION.name()));

        edtMerchantCode.setText(sharedPref.getString(SharedPrefHelper.SHARED_PREF_MERCHANT_CODE, "KH00002"));
        edtMerchantKey.setText(sharedPref.getString(SharedPrefHelper.SHARED_PREF_MERCHANT_KEY, "password"));
        edtPaymentId.setText(sharedPref.getInt(SharedPrefHelper.SHARED_PREF_PAYMENT_ID, 0) + "");
        edtProDesc.setText(sharedPref.getString(SharedPrefHelper.SHARED_PREF_PRO_DESC, "Top Up (SDK)"));
        edtUserName.setText(sharedPref.getString(SharedPrefHelper.SHARED_PREF_USER_NAME, "Tola KUN"));
        edtUserEmail.setText(sharedPref.getString(SharedPrefHelper.SHARED_PREF_USER_EMAIL, "tola.kun@ipay88.com.kh"));
        edtUserContact.setText(sharedPref.getString(SharedPrefHelper.SHARED_PREF_USER_CONTACT, "017847800"));
        edtRemark.setText(sharedPref.getString(SharedPrefHelper.SHARED_PREF_REMARK, null));
        edtBackendUrl.setText(sharedPref.getString(SharedPrefHelper.SHARED_PREF_BACKEND_URL, null));

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {

            String targetServer = "DVL";
            int checkedId = rdgTargetServer.getCheckedRadioButtonId();
            switch (checkedId) {
                case R.id.rdbDvl:
                    targetServer = IPay88PayRequest.Environment.DVL.name();
                    break;
                case R.id.rdbSandbox:
                    targetServer = IPay88PayRequest.Environment.SANDBOX.name();
                    break;
                case R.id.rdbProd:
                    targetServer = IPay88PayRequest.Environment.PRODUCTION.name();
                    break;
            }

            sharedPref.edit()
                    .putString(SharedPrefHelper.SHARED_PREF_TARGET_SERVER, targetServer)
                    .putString(SharedPrefHelper.SHARED_PREF_MERCHANT_CODE, edtMerchantCode.getText().toString())
                    .putString(SharedPrefHelper.SHARED_PREF_MERCHANT_KEY, edtMerchantKey.getText().toString())
                    .putInt(SharedPrefHelper.SHARED_PREF_PAYMENT_ID, Integer.parseInt(edtPaymentId.getText().toString()))
                    .putString(SharedPrefHelper.SHARED_PREF_PRO_DESC, edtProDesc.getText().toString())
                    .putString(SharedPrefHelper.SHARED_PREF_USER_NAME, edtUserName.getText().toString())
                    .putString(SharedPrefHelper.SHARED_PREF_USER_EMAIL, edtUserEmail.getText().toString())
                    .putString(SharedPrefHelper.SHARED_PREF_USER_CONTACT, edtUserContact.getText().toString())
                    .putString(SharedPrefHelper.SHARED_PREF_REMARK, edtRemark.getText().toString())
                    .putString(SharedPrefHelper.SHARED_PREF_BACKEND_URL, edtBackendUrl.getText().toString())
                    .apply();

            hideKeyboard(this);

            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}