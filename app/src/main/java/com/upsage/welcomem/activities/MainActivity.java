package com.upsage.welcomem.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.upsage.welcomem.R;
import com.upsage.welcomem.data.EmployeeData;
import com.upsage.welcomem.utils.SQLSingleton;
import com.upsage.welcomem.utils.ThemeUtil;

public class MainActivity extends AppCompatActivity {

    EmployeeData employee;
    SharedPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userPreferences = getSharedPreferences("user", 0);
        employee = new EmployeeData(userPreferences);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem name = menu.findItem(R.id.helloNameMenuItem);
        name.setTitle(getString(R.string.helloString) +' '+ employee.getName());
        return true;
    }

    @Override
    protected void onResume() {
        SQLSingleton.startConnection();
        super.onResume();
    }

    public void scanQRCode(View view){
        startActivity(new Intent(this, QRScannerActivity.class));
    }

    public void goPathway(View view){
        startActivity(new Intent(this, PathwayActivity.class));
    }

    public void goOrderHistory(View view){
        startActivity(new Intent(this, OrderHistoryActivity.class));
    }

    public void checkWorkHours(View view){
        startActivity(new Intent(this, WorkHoursActivity.class));
    }

    public void goSettings(View view){
        startActivity(new Intent(this, SettingsActivity.class));
        finish();
    }

    public void goLogout(View view){
        userPreferences.edit().clear().apply();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.helloNameMenuItem:
                checkWorkHours(null);
                break;
            case R.id.settingsMenuItem:
                goSettings(null);
                break;
            case R.id.logoutMenuItem:
                goLogout(null);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
