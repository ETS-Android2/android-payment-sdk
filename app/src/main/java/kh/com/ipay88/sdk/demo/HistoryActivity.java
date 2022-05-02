package kh.com.ipay88.sdk.demo;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import kh.com.ipay88.sdk.demo.utils.SharedPrefHelper;
import kh.com.ipay88.sdk.models.IPay88PayResponse;

public class HistoryActivity extends AppCompatActivity implements HistoryRecyclerViewAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private HistoryRecyclerViewAdapter adapter;
    private IPay88DemoDatabase demoDatabase;
    private List<HistoryItemDto> historyItemDtoList;
    private HistoryItemDto historyItemDto;
    private ImageView imageViewEmpty;
    private TextView tvEmpty;

    private SharedPreferences sharedPref;
    private String sharedPrefCurrency;

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
        // MARK: - Get SharedPreference
        sharedPref = SharedPrefHelper.getInstance(getApplicationContext());
        sharedPrefCurrency = sharedPref.getString(SharedPrefHelper.SHARED_PREF_KEYS_CURRENCY, "USD");

        // MARK: - Load History from Room
        try {
            demoDatabase = IPay88DemoDatabaseHelper.getInstance(getApplicationContext());
            historyItemDtoList = demoDatabase.historyItemDao().loadRecords(sharedPrefCurrency);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        // set up the RecyclerView
        this.recyclerView = findViewById(R.id.recyclerviewHistory);
        this.imageViewEmpty = findViewById(R.id.imageViewEmpty);
        this.tvEmpty = findViewById(R.id.tvEmpty);

        historyItemDtoList = historyItemDtoList == null ? new ArrayList<>() : historyItemDtoList;
        this.imageViewEmpty.setVisibility(historyItemDtoList.isEmpty() ? View.VISIBLE : View.GONE);
        this.tvEmpty.setVisibility(historyItemDtoList.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(historyItemDtoList.isEmpty() ? View.GONE : View.VISIBLE);

        if (!historyItemDtoList.isEmpty()) {
            this.imageViewEmpty.setVisibility(View.GONE);
            this.tvEmpty.setVisibility(View.GONE);
            this.recyclerView.setVisibility(View.VISIBLE);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            this.adapter = new HistoryRecyclerViewAdapter(this, historyItemDtoList);
            this.adapter.setClickListener(this);
            this.recyclerView.setAdapter(adapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(this.historyItemDtoList.isEmpty()) {
            menu.getItem(0).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_history_clear:
                new AlertDialog.Builder(HistoryActivity.this)
                        .setTitle("Please confirm")
                        .setMessage("Do you want to delete all records for " + sharedPrefCurrency + " currency?")
                        .setPositiveButton(android.R.string.ok,
                                (dialog, which) -> {
                                    this.demoDatabase.historyItemDao().deleteAll(sharedPrefCurrency);
                                    this.historyItemDtoList = demoDatabase.historyItemDao().loadRecords(sharedPrefCurrency);
                                    this.imageViewEmpty.setVisibility(historyItemDtoList.isEmpty() ? View.VISIBLE : View.GONE);
                                    this.tvEmpty.setVisibility(historyItemDtoList.isEmpty() ? View.VISIBLE : View.GONE);
                                    this.recyclerView.setVisibility(historyItemDtoList.isEmpty() ? View.GONE : View.VISIBLE);
                                    this.adapter.notifyDataSetChanged();
                                    invalidateOptionsMenu();
                                })
                        .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel())
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}