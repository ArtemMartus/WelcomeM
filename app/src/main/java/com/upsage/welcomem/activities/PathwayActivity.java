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
import com.upsage.welcomem.data.Pathways;
import com.upsage.welcomem.data.callback.SimpleItemDragAndDrop;
import com.upsage.welcomem.data.entries.PathwayEntry;
import com.upsage.welcomem.interfaces.OnItemClick;
import com.upsage.welcomem.interfaces.OnStartDragListener;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;
import com.upsage.welcomem.utils.ThemeUtil;

public class PathwayActivity extends AppCompatActivity implements OnTaskCompleted, OnItemClick, OnStartDragListener {

    private Pathways pathways;
    private ItemTouchHelper itemTouchHelper;
    private RecyclerView recyclerView;
    private TextView titleTextView;

    //todo make drag and drop functionality

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathway);

        recyclerView = findViewById(R.id.pathwaysRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        titleTextView = findViewById(R.id.pathwaysTitleTextView);
        titleTextView.setText(R.string.loadingString);


        SharedPreferences userPreferences = getSharedPreferences("user", 0);
        EmployeeData employee = new EmployeeData(userPreferences);
        pathways = new Pathways(employee.getId(), this, this);
        pathways.test(this);

        ItemTouchHelper.Callback callback = new SimpleItemDragAndDrop(pathways);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void OnItemClick(View view, int position) {
        PathwayEntry item = pathways.getItem(position);
        if (item != null) {
            Intent intent = new Intent(PathwayActivity.this, ShowOrderActivity.class);
            intent.putExtra("orderId", item.getId());
            startActivity(intent);
        }
    }

    @Override
    public void onTaskCompleted(Object o) {
        if (pathways.ready()) {
            titleTextView.setText(R.string.pathwaysString);
            recyclerView.setAdapter(pathways);
        } else
            titleTextView.setText(R.string.loadingString);
    }

    @Override
    protected void onResume() {
        SQLSingleton.startConnection();
        pathways.clear();
        pathways.test(this);
        if (pathways.ready())
            titleTextView.setText(R.string.pathwaysString);
        else
            titleTextView.setText(R.string.loadingString);
        super.onResume();
    }
}
