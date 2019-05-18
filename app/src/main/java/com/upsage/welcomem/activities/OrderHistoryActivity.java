package com.upsage.welcomem.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.upsage.welcomem.R;
import com.upsage.welcomem.data.EmployeeData;
import com.upsage.welcomem.data.OrdersInHistory;
import com.upsage.welcomem.data.entries.OrderInHistoryEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.ThemeUtil;

public class OrderHistoryActivity extends AppCompatActivity implements OnTaskCompleted {

    OrdersInHistory ordersInHistory;
    ListView listView;
    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        listView = findViewById(R.id.ordersHistoryListView);
        titleTextView = findViewById(R.id.orderHistoryTitleTextView);
        titleTextView.setText(R.string.loadingString);

        SharedPreferences userPreferences = getSharedPreferences("user", 0);
        EmployeeData employee = new EmployeeData(userPreferences);
        ordersInHistory = new OrdersInHistory(employee.getId(), this);
        ordersInHistory.test(this);
    }

    @Override
    public void onTaskCompleted(Object o) {
        if (ordersInHistory.ready()) {
            titleTextView.setText(R.string.orderHistoryString);
            listView.setAdapter(ordersInHistory);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                OrderInHistoryEntry item = ordersInHistory.getItem(position);
                if (item != null) {
                    Intent intent = new Intent(OrderHistoryActivity.this, ShowOrderActivity.class);
                    intent.putExtra("orderId", item.getId());
                    startActivity(intent);
                }
            });
        } else {
            titleTextView.setText(R.string.loadingString);//never called
        }
    }
}
