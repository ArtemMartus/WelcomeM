package com.upsage.welcomem.utils;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLSingleton {
    private final static String host = "jdbc:mysql://remotemysql.com:3306/XN5vmpIBV5";
    private final static String db_user = "XN5vmpIBV5";
    private final static String db_password = "3KW9meu7IL";
    private final static String TAG = "SQLSingleton";
    private static SQLSingleton mInstance;
    private static Connection connection;


    private SQLSingleton() {
        Log.d(TAG, "Initializing sql database stuff");
        try {
            connection = DriverManager.getConnection(host, db_user, db_password);
        } catch (SQLException e) {
            e.printStackTrace();
            connection = null;
        }
    }

    private static void initInstance() throws SQLException {
        if (mInstance == null || (connection != null && connection.isClosed())) {
            synchronized (SQLSingleton.class) {
                if (mInstance == null)
                    mInstance = new SQLSingleton();
            }
        }
    }

    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        initInstance();
        return connection.prepareStatement(sql);
    }

    @Override
    protected void finalize() throws Throwable {
        connection.close();
        super.finalize();
    }
}
