package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.data.Order;
import com.upsage.welcomem.interfaces.OnTaskCompleted;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRetrieveTask extends AsyncTask<Order,Void,Order> {
    private final static String host = "jdbc:mysql://remotemysql.com:3306/XN5vmpIBV5";
    private final static String db_user = "XN5vmpIBV5";
    private final static String db_password = "3KW9meu7IL";
    private final static String TAG = "Order retriever task";
    private OnTaskCompleted receiver;

    public OrderRetrieveTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    @Override
    protected Order doInBackground(Order... args) {
        Order order = null;
        if (args.length > 0 && args[0] != null) {
            Order orderData = args[0];
            try {
                Connection connection = DriverManager.getConnection(host, db_user, db_password);
                Log.i(TAG, "connection successful");
                PreparedStatement statement = connection.prepareStatement
                        ("SELECT * from orders where id =?");
                Log.i(TAG, "created statement");
                statement.setInt(1, orderData.getId());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.first()) {
                    order = new Order(
                            resultSet.getInt("id"),
                            resultSet.getInt("client_id"),
                            resultSet.getString("products_id"),
                            resultSet.getDouble("quantity"),
                            resultSet.getDouble("sum"),
                            resultSet.getString("currency"),
                            resultSet.getString("delivery_address"),
                            resultSet.getTimestamp("delivery_date"),

                            resultSet.getInt("manager_id")
                    );
                }
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return order;
    }

    @Override
    protected void onPostExecute(Order orderData) {
        if(receiver!=null){
            Log.i(TAG,"Order we got be like: "+orderData);
            receiver.onTaskCompleted(orderData);
        }
        super.onPostExecute(orderData);
    }
}
