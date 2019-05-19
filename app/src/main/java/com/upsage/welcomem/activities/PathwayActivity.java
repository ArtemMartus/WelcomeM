package com.upsage.welcomem.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.upsage.welcomem.R;
import com.upsage.welcomem.data.EmployeeData;
import com.upsage.welcomem.data.Pathways;
import com.upsage.welcomem.data.entries.PathwayEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;
import com.upsage.welcomem.utils.ThemeUtil;

public class PathwayActivity extends AppCompatActivity implements OnTaskCompleted {

    Pathways pathways;
    ListView listView;
    TextView titleTextView;

    //todo make drag and drop functionality

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathway);

        listView = findViewById(R.id.pathwaysListView);
        titleTextView = findViewById(R.id.pathwaysTitleTextView);
    }

    @Override
    public void onTaskCompleted(Object o) {
        if (pathways.ready()) {
            titleTextView.setText(R.string.pathwaysString);
            listView.setAdapter(pathways);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                PathwayEntry item = pathways.getItem(position);
                if (item != null) {
                    Intent intent = new Intent(PathwayActivity.this, ShowOrderActivity.class);
                    intent.putExtra("orderId", item.getId());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        SQLSingleton.startConnection();

        SharedPreferences userPreferences = getSharedPreferences("user", 0);
        EmployeeData employee = new EmployeeData(userPreferences);
        pathways = new Pathways(employee.getId(), this);
        pathways.test(this);
        titleTextView.setText(R.string.loadingString);
        listView.setAdapter(null);
        super.onResume();
    }
}
