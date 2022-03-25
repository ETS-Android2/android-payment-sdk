package kh.com.ipay88.sdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import kh.com.ipay88.sdk.constants.IPay88Constants;
import kh.com.ipay88.sdk.models.IPay88PayResponse;

public class ResultActivity extends AppCompatActivity {

    private ImageView imageViewStatus;
    private TextView textViewStatus;
    private EditText editTextDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        getSupportActionBar().setTitle("Payment Result");

        initView();
        initData();
    }

    private void initView() {
        this.imageViewStatus = findViewById(R.id.imageViewStatus);
        this.textViewStatus = findViewById(R.id.textViewStatus);
        this.editTextDetails = findViewById(R.id.editTextDetails);
        this.editTextDetails.setKeyListener(null);
    }

    private void initData() {
        // MARK: - Get Data from previous screen
        Intent intent = this.getIntent();
        if (intent != null && intent.getSerializableExtra(IPay88Constants.PAY_RESPONSE_DATA) != null) {

            IPay88PayResponse payResponse = (IPay88PayResponse) intent.getSerializableExtra(IPay88Constants.PAY_RESPONSE_DATA);
            String jsonOb = IPay88PayResponse.GenerateJSONData(payResponse, true);

            this.imageViewStatus.setImageResource(payResponse.getStatus().equals("1") ? R.drawable.ic_success : R.drawable.ic_failed);
            this.textViewStatus.setText(payResponse.getStatus().equals("1") ? "SUCCESS" : "FAILED");
            this.editTextDetails.setText(jsonOb);

        } else {
            this.imageViewStatus.setImageResource(R.drawable.ic_exclaimation);
            this.textViewStatus.setText("NO RESPONSE");
            this.editTextDetails.setText(null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}