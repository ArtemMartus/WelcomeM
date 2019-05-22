package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.data.EmployeeData;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeTestTask extends AsyncTask<EmployeeData, Void, EmployeeData> {
    private final OnTaskCompleted receiver;

    public EmployeeTestTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    private String TAG = "Employee Test Credential Task";

    @Override
    protected EmployeeData doInBackground(EmployeeData... args) {
        EmployeeData employee = null;
        if (args.length > 0 && args[0] != null) {

            EmployeeData credential = args[0]; // получаем данные сотрудника из аргумента
            try {

                PreparedStatement statement;
                if (credential.isNotEmpty()) {// если в данных есть логин и пароль - формируем запрос с использованием этих данных

                    statement = SQLSingleton.prepareStatement
                            ("SELECT * from employees where login =? and password =?");

                    statement.setString(1, credential.getLogin());
                    statement.setString(2, credential.getPassword());
                } else {// если только id - запрос другой
                    statement = SQLSingleton.prepareStatement
                            ("SELECT * from employees where id =?");

                    statement.setInt(1, credential.getId());
                }
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.first()) {
                    String login;
                    String password;
                    if (credential.isNotEmpty()) { // если нам изначально не поступали логин и пароль - оставим поля пустыми
                        login = resultSet.getString("login");
                        password = resultSet.getString("password");
                    } else {
                        login = "";
                        password = "";
                    }

                    employee = new EmployeeData(
                            login,
                            password,
                            credential.getRemember(),
                            resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getInt("id"),
                            resultSet.getString("address"),
                            resultSet.getString("email"),
                            resultSet.getString("telephone")
                    );
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return employee;
    }

    @Override
    protected void onPostExecute(EmployeeData employeeData) {
        if (receiver != null) {
            Log.i(TAG, "EmployeeData be like: " + employeeData);
            receiver.onTaskCompleted(employeeData);
        }
        super.onPostExecute(employeeData);
    }
}
