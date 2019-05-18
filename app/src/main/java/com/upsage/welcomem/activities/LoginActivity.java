package com.upsage.welcomem.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upsage.welcomem.R;
import com.upsage.welcomem.data.EmployeeData;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.ThemeUtil;


public class LoginActivity extends AppCompatActivity implements OnTaskCompleted {

    EditText loginEditText;
    EditText passwordEditText;
    CheckBox rememberCheckBox;
    SharedPreferences userPreferences;
    Button signInButton;

    EmployeeData employeeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rememberCheckBox = findViewById(R.id.rememberCheckBox);
        signInButton = findViewById(R.id.signInButton);

        userPreferences = getSharedPreferences("user", 0);
        employeeData = new EmployeeData(userPreferences);
        if (employeeData.isNotEmpty() && employeeData.getId() != -1) {
            goMain();
        }

        loginEditText.setText(employeeData.getLogin());
        rememberCheckBox.setChecked(employeeData.getRemember());
    }

    private void goMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();
    }

    public void signInCallback(View view) {
        employeeData.setLogin(loginEditText.getText().toString());
        employeeData.setPassword(passwordEditText.getText().toString());
        employeeData.setRemember(rememberCheckBox.isChecked());

        if (employeeData.isNotEmpty()) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            signInButton.setEnabled(false);
            employeeData.test(this);
        } else
            Toast.makeText(this, "Неправильно введено данні", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTaskCompleted(Object o) {
        if(o != null) {
            employeeData.save(userPreferences);
            goMain();
        }
        else {
            Toast.makeText(LoginActivity.this, "Неправильно введено данні", Toast.LENGTH_SHORT).show();
            signInButton.setEnabled(true);
        }
    }
}
