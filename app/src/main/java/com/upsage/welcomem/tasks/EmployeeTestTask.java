package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.OnTaskCompleted;
import com.upsage.welcomem.data.EmployeeData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeTestTask extends AsyncTask<EmployeeData, Void, EmployeeData> {
    private final static String host = "jdbc:mysql://remotemysql.com:3306/XN5vmpIBV5";
    private final static String db_user = "XN5vmpIBV5";
    private final static String db_password = "3KW9meu7IL";
    private final OnTaskCompleted receiver;

    public EmployeeTestTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    private String TAG = "Employee Test Credential Task";

    @Override
    protected EmployeeData doInBackground(EmployeeData... args) {
        EmployeeData employee = null;
        if (args.length > 0 && args[0] != null) {
            EmployeeData credential = args[0];
            try {
                Connection connection = DriverManager.getConnection(host, db_user, db_password);
                Log.i(TAG, "connection successful");

                PreparedStatement statement;
                if(credential.isNotEmpty()) {
                    statement = connection.prepareStatement
                            ("SELECT * from employees where login =? and password =?");
                    Log.i(TAG, "created statement for login and password");
                    statement.setString(1, credential.getLogin());
                    statement.setString(2, credential.getPassword());
                } else {
                    statement = connection.prepareStatement
                            ("SELECT * from employees where id =?");
                    Log.i(TAG, "created statement for id");
                    statement.setInt(1, credential.getId());
                }
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.first()) {
                    employee = new EmployeeData(
                            resultSet.getString("login"),
                            resultSet.getString("password"),
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
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return employee;
    }

    @Override
    protected void onPostExecute(EmployeeData employeeData) {
        if(receiver!=null){
            Log.i(TAG,"EmployeeData be like: "+employeeData);
            receiver.onTaskCompleted(employeeData);
        }
        super.onPostExecute(employeeData);
    }
}
