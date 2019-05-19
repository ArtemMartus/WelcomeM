package com.upsage.welcomem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upsage.welcomem.activities.MainActivity;
import com.upsage.welcomem.data.EmployeeData;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;
import com.upsage.welcomem.utils.ThemeUtil;


public class LoginActivity extends AppCompatActivity implements OnTaskCompleted {

    EditText loginEditText;
    EditText passwordEditText;
    CheckBox rememberCheckBox;
    SharedPreferences userPreferences;
    Button signInButton;

    EmployeeData employeeData;

    // В пакете activities лежат классы всех классов
    // В пакете data лежат классы модельной части данных, callback классы помошники для RecyclerView
    //      В подпапке entries лежат POJO классы элементов таблиц из БД
    //      В подпапке viewholders лежат вспомогательные классы для работы RecyclerView
    // В пакете interfaces лежат интерфейсы использованные в проекте
    // В пакете tasks лежат асинхронные таски получения и записи данных из БД
    // В пакете utils лежат вспомогательные классы

    // таски все работают одинаково - получают statement из вспомогательного класса,
    //      отправляют SQL запрос, используют полученные данные

    // классы из пакета data абстрагируют активити от работы с интернетом и БД

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // В этом активити в этой функции у нас начало всей программы
        // С помощью ThemeUtil делаем динамические темы
        // Зажав Ctrl кликом мыши по классу или методу вас перенесет к его реализации
        ThemeUtil.onCreateSetTheme(this);
        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rememberCheckBox = findViewById(R.id.rememberCheckBox);
        signInButton = findViewById(R.id.signInButton);

        userPreferences = getSharedPreferences("user", 0);
        employeeData = new EmployeeData(userPreferences);

        if (employeeData.isNotEmpty() && employeeData.getId() != -1) {
            goMain();// Если есть записаны данные о курьере - минуем экран логина и включаем главный экран
        }

        loginEditText.setText(employeeData.getLogin());
        rememberCheckBox.setChecked(employeeData.getRemember());
    }

    private void goMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();
    }

    // В классах активити стоят SQLSingleton.startConnection() такие инструкции для подключению
    //      к БД если вдруг отключились
    @Override
    protected void onResume() {
        SQLSingleton.startConnection();
        super.onResume();
    }

    // Чтобы посмотреть откуда функция вызывается - зажмите Ctrl + левый клик по имени функции
    public void signInCallback(View view) {
        employeeData.setLogin(loginEditText.getText().toString());
        employeeData.setPassword(passwordEditText.getText().toString());
        employeeData.setRemember(rememberCheckBox.isChecked());

        if (employeeData.isNotEmpty()) {
            Toast.makeText(this, R.string.loadingString, Toast.LENGTH_SHORT).show();
            signInButton.setEnabled(false);
            employeeData.test(this);
        } else
            Toast.makeText(this, R.string.incorrectInputString, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTaskCompleted(Object o) {
        if(o != null) {
            employeeData.save(userPreferences);
            goMain();
        }
        else {
            Toast.makeText(LoginActivity.this, R.string.incorrectInputString, Toast.LENGTH_SHORT).show();
            signInButton.setEnabled(true);
        }
    }
}
