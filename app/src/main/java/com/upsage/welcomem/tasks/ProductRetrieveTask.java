package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.data.Product;
import com.upsage.welcomem.interfaces.OnTaskCompleted;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRetrieveTask extends AsyncTask<Product,Void,Product> {
    private final static String host = "jdbc:mysql://remotemysql.com:3306/XN5vmpIBV5";
    private final static String db_user = "XN5vmpIBV5";
    private final static String db_password = "3KW9meu7IL";
    private final static String TAG = "Product retriever task";
    private OnTaskCompleted receiver;

    public ProductRetrieveTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    @Override
    protected Product doInBackground(Product... args) {
        Product product = null;
        if (args.length > 0 && args[0] != null) {
            Product productData = args[0];
            try {
                Connection connection = DriverManager.getConnection(host, db_user, db_password);
                Log.i(TAG, "connection successful");
                PreparedStatement statement = connection.prepareStatement
                        ("SELECT * from products where id =?");
                Log.i(TAG, "created statement");
                statement.setInt(1, productData.getId());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.first()) {
                    product = new Product(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("model"),
                            resultSet.getString("color"),
                            resultSet.getDouble("price"),
                            resultSet.getDouble("discount")
                    );
                }
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return product;
    }

    @Override
    protected void onPostExecute(Product product) {
        if(receiver!=null){
            Log.i(TAG,"Product we got be like: "+product);
            receiver.onTaskCompleted(product);
        }
        super.onPostExecute(product);
    }
}
