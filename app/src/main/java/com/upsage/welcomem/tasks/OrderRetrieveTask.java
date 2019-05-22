package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.data.Order;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRetrieveTask extends AsyncTask<Order,Void,Order> {
    private final static String TAG = "Order retriever task";
    private OnTaskCompleted receiver;

    public OrderRetrieveTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    @Override
    protected Order doInBackground(Order... args) {
        Order order = null;
        if (args.length > 0 && args[0] != null) {

            Integer orderData = args[0].getId();

            try {
                Log.i(TAG, "connection successful");
                PreparedStatement statement = SQLSingleton.prepareStatement
                        ("SELECT * from orders where id =?");
                Log.i(TAG, "created statement");
                statement.setInt(1, orderData);
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
                            resultSet.getInt("manager_id"),
                            resultSet.getInt("courier_id")
                    );
                }
                resultSet.close();
                statement.close();
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
