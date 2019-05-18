package com.upsage.welcomem.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upsage.welcomem.R;
import com.upsage.welcomem.data.EmployeeData;
import com.upsage.welcomem.data.OrdersInHistory;
import com.upsage.welcomem.data.callback.SimpleItemTouchHelperCallback;
import com.upsage.welcomem.data.entries.OrderInHistoryEntry;
import com.upsage.welcomem.interfaces.OnItemClick;
import com.upsage.welcomem.interfaces.OnStartDragListener;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.ThemeUtil;

public class OrderHistoryActivity extends AppCompatActivity implements OnTaskCompleted, OnItemClick, OnStartDragListener {

    private OrdersInHistory ordersInHistory;
    private RecyclerView recyclerView;
    private TextView titleTextView;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        recyclerView = findViewById(R.id.ordersHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));

        titleTextView = findViewById(R.id.orderHistoryTitleTextView);
        titleTextView.setText(R.string.loadingString);

        SharedPreferences userPreferences = getSharedPreferences("user", 0);
        EmployeeData employee = new EmployeeData(userPreferences);
        ordersInHistory = new OrdersInHistory(employee.getId(), this, this);
        ordersInHistory.test(this);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(ordersInHistory);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onTaskCompleted(Object o) {
        if (ordersInHistory.ready()) {
            titleTextView.setText(R.string.orderHistoryString);
            recyclerView.setAdapter(ordersInHistory);
        } else {
            titleTextView.setText(R.string.loadingString);//never called
        }
    }

    @Override
    public void OnItemClick(View view, int position) {
        OrderInHistoryEntry item = ordersInHistory.getItem(position);
        if (item != null) {
            Intent intent = new Intent(OrderHistoryActivity.this, ShowOrderActivity.class);
            intent.putExtra("orderId", item.getId());
            startActivity(intent);
        }
    }
}
