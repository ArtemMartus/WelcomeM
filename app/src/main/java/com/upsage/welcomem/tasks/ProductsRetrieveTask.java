package com.upsage.welcomem.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.upsage.welcomem.data.entries.ProductEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.utils.SQLSingleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ProductsRetrieveTask extends AsyncTask<Set<Integer>, Void, List<ProductEntry>> {

    private final static String TAG = "Products retriever task";
    private OnTaskCompleted receiver;

    public ProductsRetrieveTask(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    @Override
    protected List<ProductEntry> doInBackground(Set<Integer>... args) {
        List<ProductEntry> products = new LinkedList<>();
        if (args.length > 0 && args[0] != null) {
            Set<Integer> prodIds = args[0];
            try {
                Log.i(TAG, "connection successful");

                for (Integer id :
                        prodIds) {
                    PreparedStatement statement = SQLSingleton.prepareStatement
                            ("SELECT * from products where id =?");
                    Log.i(TAG, "created statement");
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.first()) {
                        ProductEntry entry = new ProductEntry(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("model"),
                                resultSet.getString("color"),
                                resultSet.getDouble("price"),
                                resultSet.getDouble("discount")
                        );
                        products.add(entry);
                    }
                    resultSet.close();
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return products;
    }

    @Override
    protected void onPostExecute(List<ProductEntry> prods) {
        if(receiver!=null){
            Log.i(TAG, "ProductEntry we got be like: " + prods);
            receiver.onTaskCompleted(prods);
        }
        super.onPostExecute(prods);
    }
}
