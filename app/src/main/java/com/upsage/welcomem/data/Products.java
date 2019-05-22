package com.upsage.welcomem.data;

import android.util.Log;

import com.upsage.welcomem.data.entries.ProductEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.ProductsRetrieveTask;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Products implements OnTaskCompleted {
    private static final String TAG = "Products log";
    private OnTaskCompleted receiver;
    private Set<Integer> productIds;
    private List<ProductEntry> products;

    public Products(String productIds) {
        this.productIds = new HashSet<>();
        for (String part : productIds.split(" ")) {
            try {
                this.productIds.add(Integer.parseInt(part));
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Some error in productIds string THIS_PART ain't integer-[" + part + "]");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void test(OnTaskCompleted receiver) {
        if (productIds != null && productIds.size() > 0) {
            this.receiver = receiver;
            ProductsRetrieveTask task = new ProductsRetrieveTask(this);
            task.execute(productIds);
        } else {
            Log.e(TAG, "productIds set has no items => no need in task");
        }
    }

    public boolean ready() {
        return products != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onTaskCompleted(Object o) {
        if ((o instanceof List)) {
            products = new LinkedList<>();
            products.addAll((List<ProductEntry>) o);
        }

        if (receiver != null)
            receiver.onTaskCompleted(o);
    }

    public Double getPrice() {
        if (!ready())
            return 0.0;
        Double sum = 0.0;
        for (ProductEntry p : products) {
            sum += p.getFinalPrice();
        }
        return sum;
    }

    public String getProductsInfo() {
        if (!ready())
            return "";
        StringBuilder productsInfo = new StringBuilder();
        for (ProductEntry p : products) {
            productsInfo.append(p.asString()).append("\n");
        }
        return productsInfo.toString();
    }
}
