package kh.com.ipay88.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

import kh.com.ipay88.sdk.constants.IPay88Constants;
import kh.com.ipay88.sdk.demo.databases.HistoryItemDto;
import kh.com.ipay88.sdk.demo.databases.IPay88DemoDatabase;
import kh.com.ipay88.sdk.demo.databases.IPay88DemoDatabaseHelper;
import kh.com.ipay88.sdk.demo.utils.StringUtils;
import kh.com.ipay88.sdk.models.IPay88PayRequest;
import kh.com.ipay88.sdk.models.IPay88PayResponse;
import kh.com.ipay88.sdk.utils.IPay88SDK;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Switch switchCurrency;
    private EditText editTextPhone;
    private String selectedAmount = "0.50";
    private TextView textViewCurrentAccount, textViewBalance, textView050, textView100, textView125, textView200, textView500, textView1000, textView2000, textView5000;
    private Button btnCheckout;

    // MARK: - Create 2 static variables to receive IPay88 payment response
    static String errorMsg;
    static IPay88PayResponse payResponse;

    private SharedPreferences sharedPref;
    private static final String
            SHARED_PREF_NAME = "kh.com.ipay88.sdk.demo.sharedPref",
            SHARED_PREF_KEYS_CURRENCY = "kh.com.ipay88.sdk.demo.sharedPref.currency",
            SHARED_PREF_BALANCE_USD = "kh.com.ipay88.sdk.demo.sharedPref.balance.usd",
            SHARED_PREF_BALANCE_KHR = "kh.com.ipay88.sdk.demo.sharedPref.balance.khr";
    private String sharedPrefCurrency;
    private double sharedPrefBalanceUSD, sharedPrefBalanceKHR;
    private final DecimalFormat df = new DecimalFormat("0.00");

    private IPay88DemoDatabase demoDatabase;
    private HistoryItemDto historyItemDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData() {
        // MARK: - Init Db
        demoDatabase = IPay88DemoDatabaseHelper.getInstance(getApplicationContext());

        // MARK: - Get Balance
        sharedPref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        sharedPrefCurrency = sharedPref.getString(SHARED_PREF_KEYS_CURRENCY, "USD");
        sharedPrefBalanceUSD = sharedPref.getFloat(SHARED_PREF_BALANCE_USD, 0.00f);
        sharedPrefBalanceKHR = sharedPref.getFloat(SHARED_PREF_BALANCE_KHR, 0.00f);
    }

    private void initView() {
        this.switchCurrency = findViewById(R.id.switchCurrency);
        this.switchCurrency.setChecked(!sharedPrefCurrency.equals("USD"));
        this.switchCurrency.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            this.sharedPrefCurrency = isChecked ? "KHR" : "USD";
            //this.sharedPrefCurrency = this.selectedCurrency;
            sharedPref.edit().putString(SHARED_PREF_KEYS_CURRENCY, sharedPrefCurrency).apply();
            this.textViewCurrentAccount.setText(sharedPrefCurrency.equals("USD") ? "Current Balance in USD :" : "Current Balance in KHR :");
            this.textViewBalance.setText(StringUtils.GetCurrencyFormat(sharedPrefCurrency.equals("USD") ? sharedPrefBalanceUSD : sharedPrefBalanceKHR));
            this.selectedAmount(this.textView050);
        });

        this.textViewCurrentAccount = findViewById(R.id.textViewCurrentAccount);
        this.textViewBalance = findViewById(R.id.textViewBalance);

        this.editTextPhone = findViewById(R.id.editTextPhone);

        this.textView050 = findViewById(R.id.textView050);
        this.textView100 = findViewById(R.id.textView100);
        this.textView125 = findViewById(R.id.textView125);
        this.textView200 = findViewById(R.id.textView200);
        this.textView500 = findViewById(R.id.textView500);
        this.textView1000 = findViewById(R.id.textView1000);
        this.textView2000 = findViewById(R.id.textView2000);
        this.textView5000 = findViewById(R.id.textView5000);

        this.textView050.setOnClickListener(this);
        this.textView100.setOnClickListener(this);
        this.textView125.setOnClickListener(this);
        this.textView200.setOnClickListener(this);
        this.textView500.setOnClickListener(this);
        this.textView1000.setOnClickListener(this);
        this.textView2000.setOnClickListener(this);
        this.textView5000.setOnClickListener(this);

        btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(view -> {
            long milliseconds = System.currentTimeMillis();
            String refNo = "SDK-" + milliseconds;

            // MARK: - IPay88 Checkout
            IPay88PayRequest payRequest = new IPay88PayRequest();
            payRequest.setEnvironment(IPay88PayRequest.Environment.DVL);
            payRequest.setMerchantCode("KH00002");
            payRequest.setMerchantKey("password");
            payRequest.setPaymentID(0);
            payRequest.setRefNo(refNo);
            payRequest.setAmount(df.format(Double.parseDouble(this.selectedAmount)));
            payRequest.setCurrency(this.sharedPrefCurrency.equals("USD") ? IPay88PayRequest.Currency.USD : IPay88PayRequest.Currency.KHR);
            payRequest.setProdDesc("Top Up (SDK)");
            payRequest.setUserName("Tola KUN");
            payRequest.setUserEmail("tola.kun@ipay88.com.kh");
            payRequest.setUserContact(this.editTextPhone.getText().toString());
            payRequest.setRemark("aOS");
            payRequest.setBackendURL(null);

            try {
                // MARK: - Save Log Request
                this.historyItemDto = new HistoryItemDto();
                historyItemDto.RefNo = refNo;
                historyItemDto.CreatedDateMilliseconds = milliseconds;
                historyItemDto.Json = null;
                historyItemDto.Amount = Double.parseDouble(this.selectedAmount);
                historyItemDto.AmountSharedRef = this.sharedPrefCurrency.equals("USD") ? sharedPrefBalanceUSD : sharedPrefBalanceKHR;
                historyItemDto.Currency = this.sharedPrefCurrency;
                demoDatabase.historyItemDao().insertAll(historyItemDto);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            IPay88SDK.checkout(MainActivity.this, payRequest, new MainActivityCallback());

        });

        // MARK: - First call before event invoked
        this.textViewCurrentAccount.setText(sharedPrefCurrency.equals("USD") ? "Current Balance in USD :" : "Current Balance in KHR :");
        this.textViewBalance.setText(StringUtils.GetCurrencyFormat(sharedPrefCurrency.equals("USD") ? sharedPrefBalanceUSD : sharedPrefBalanceKHR));
        this.textView050.setText(sharedPrefCurrency.equals("USD") ? "0.50" : "2.00K");
        this.selectedAmount(this.textView050);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // MARK: - IPay88 Payment Response
        if (requestCode == IPay88Constants.PAY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK
                && data != null
                && MainActivity.payResponse != null) {
            String status = MainActivity.payResponse.getStatus().equals("1") ? "SUCCESS" : "FAILED";
            String json = IPay88PayResponse.GenerateJSONData(MainActivity.payResponse, false);

            if (status.equals("SUCCESS")) {
                // MARK: - Update Balance
                if (MainActivity.payResponse.getCurrency().equals("USD")) {
                    sharedPrefBalanceUSD += Double.parseDouble(MainActivity.payResponse.getAmount());
                    sharedPref.edit().putFloat(SHARED_PREF_BALANCE_USD, (float) sharedPrefBalanceUSD).apply();
                    this.textViewBalance.setText(StringUtils.GetCurrencyFormat(sharedPrefBalanceUSD));
                } else {
                    sharedPrefBalanceKHR += Double.parseDouble(MainActivity.payResponse.getAmount());
                    sharedPref.edit().putFloat(SHARED_PREF_BALANCE_KHR, (float) sharedPrefBalanceKHR).apply();
                    this.textViewBalance.setText(StringUtils.GetCurrencyFormat(sharedPrefBalanceKHR));
                }
            }

            try {
                // MARK: - Update Log Request
                Log.d("db-id -> ", "" + historyItemDto.Id);
                historyItemDto = demoDatabase.historyItemDao().loadRecord(MainActivity.payResponse.getRefNo());
                if (historyItemDto != null) {
                    historyItemDto.Json = json;
                    demoDatabase.historyItemDao().updateRecord(historyItemDto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, ResultActivity.class)
                    .putExtra(IPay88Constants.PAY_RESPONSE_DATA, MainActivity.payResponse);
            startActivity(intent);
        } else if (requestCode == IPay88Constants.PAY_REQUEST_CODE
                && resultCode == Activity.RESULT_CANCELED
                && data != null
                && MainActivity.errorMsg != null) {
            Toast.makeText(getApplicationContext(), MainActivity.errorMsg, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView050:
            case R.id.textView100:
            case R.id.textView125:
            case R.id.textView200:
            case R.id.textView500:
            case R.id.textView1000:
            case R.id.textView2000:
            case R.id.textView5000:
                this.selectedAmount((TextView) view);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_activity_main_history:
                intent = new Intent(this, HistoryActivity.class)
                        .putExtra("CURRENCY", sharedPrefCurrency);
                startActivity(intent);
                return true;
            /*case R.id.menu_activity_main_setting:
                intent = new Intent(this, SettingActivity.class)
                        .putExtra("CURRENCY", sharedPrefCurrency);
                startActivity(intent);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearSelectedAmount(boolean isUSD) {
        this.textView050.setBackgroundColor(Color.TRANSPARENT);
        this.textView100.setBackgroundColor(Color.TRANSPARENT);
        this.textView125.setBackgroundColor(Color.TRANSPARENT);
        this.textView200.setBackgroundColor(Color.TRANSPARENT);
        this.textView500.setBackgroundColor(Color.TRANSPARENT);
        this.textView1000.setBackgroundColor(Color.TRANSPARENT);
        this.textView2000.setBackgroundColor(Color.TRANSPARENT);
        this.textView5000.setBackgroundColor(Color.TRANSPARENT);

        this.textView050.setText(isUSD ? "0.50" : "2.00K");
        this.textView100.setText(isUSD ? "1.00" : "4.00K");
        this.textView125.setText(isUSD ? "1.25" : "5.00K");
        this.textView200.setText(isUSD ? "2.00" : "8.00K");
        this.textView500.setText(isUSD ? "5.00" : "20.00K");
        this.textView1000.setText(isUSD ? "10.00" : "40.00K");
        this.textView2000.setText(isUSD ? "20.00" : "80.00K");
        this.textView5000.setText(isUSD ? "50.00" : "200.00K");
    }

    private void selectedAmount(TextView textView) {
        clearSelectedAmount(this.sharedPrefCurrency.equals("USD"));
        String amountFormat = textView.getText().toString();
        String amountStr = StringUtils.GetAmount(amountFormat);
        this.selectedAmount = amountStr;
        this.btnCheckout.setText("Checkout (" + StringUtils.GetCurrencyFormat(Double.parseDouble(this.selectedAmount)) + " " + this.sharedPrefCurrency + ")");
        textView.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
        //this.textViewCurrentAccount.setText(this.selectedCurrency.equals("USD") ? "Current Balance in USD :" : "Current Balance in KHR :");
    }
}