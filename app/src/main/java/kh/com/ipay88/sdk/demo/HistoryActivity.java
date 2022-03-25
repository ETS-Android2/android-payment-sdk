package kh.com.ipay88.sdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kh.com.ipay88.sdk.constants.IPay88Constants;
import kh.com.ipay88.sdk.demo.adapter.HistoryRecyclerViewAdapter;
import kh.com.ipay88.sdk.demo.databases.HistoryItemDto;
import kh.com.ipay88.sdk.demo.databases.IPay88DemoDatabase;
import kh.com.ipay88.sdk.demo.databases.IPay88DemoDatabaseHelper;
import kh.com.ipay88.sdk.models.IPay88PayResponse;

public class HistoryActivity extends AppCompatActivity implements HistoryRecyclerViewAdapter.ItemClickListener {
    private HistoryRecyclerViewAdapter adapter;
    private IPay88DemoDatabase demoDatabase;
    private List<HistoryItemDto> historyItemDtoList;
    private HistoryItemDto historyItemDto;
    private ImageView imageViewEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        //overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        getSupportActionBar().setTitle("Payment History");

        initData();
        initView();
    }

    private void initData() {
        // MARK: - Load History from Room
        try {
            String currency = "USD";
            Intent intent = this.getIntent();
            if (intent != null && intent.getStringExtra("CURRENCY") != null) {
                currency = intent.getStringExtra("CURRENCY");
            }
            demoDatabase = IPay88DemoDatabaseHelper.getInstance(getApplicationContext());
            historyItemDtoList = demoDatabase.historyItemDao().loadRecords(currency);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerviewHistory);
        this.imageViewEmpty = findViewById(R.id.imageViewEmpty);

        historyItemDtoList = historyItemDtoList == null ? new ArrayList<>() : historyItemDtoList;
        this.imageViewEmpty.setVisibility(historyItemDtoList.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(historyItemDtoList.isEmpty() ? View.GONE : View.VISIBLE);

        if (!historyItemDtoList.isEmpty()) {
            this.imageViewEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new HistoryRecyclerViewAdapter(this, historyItemDtoList);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    public void onItemClick(View view, int position) {
        this.historyItemDto = adapter.getItem(position);
        IPay88PayResponse payResponse = IPay88PayResponse.GenerateObjectFromJSONData(this.historyItemDto.Json);
        Intent intent = new Intent(this, ResultActivity.class)
                .putExtra(IPay88Constants.PAY_RESPONSE_DATA, payResponse);
        startActivity(intent);
    }
}