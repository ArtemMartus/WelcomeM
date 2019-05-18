package com.upsage.welcomem.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.ProductRetrieveTask;

import java.text.DecimalFormat;

public class Product implements OnTaskCompleted {
    private OnTaskCompleted receiver;
    private Integer id;
    private String name;
    private String model;
    private String color;
    private Double price;
    private Double discount;
    private boolean isReady = false;

    public Product(Integer id) {
        this.id = id;
    }

    public Product(Integer id, String name, String model, String color, Double price, Double discount) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.color = color;
        this.price = price;
        this.discount = discount;
    }

    public boolean load(Context context) {
        if (context == null) {
            Log.e("Product load()", "Can't load it because context is empty");
            return false;
        }
        if (id == -1) {
            Log.e("Product load()", "Can't load order with invalid id");
            return false;
        }
        SharedPreferences orderPreferences = context.getSharedPreferences("product_" + id, 0);

        name = orderPreferences.getString("name", "");
        model = orderPreferences.getString("model", "");
        color = orderPreferences.getString("color", "");
        price = (double) orderPreferences.getFloat("price", -1.0f);
        discount = (double) orderPreferences.getFloat("discount", -1.0f);

        /*Log.d("Product load()", "Downloading product data asynchronously");

        if (orderPreferences.getInt("id", -1) == -1)
            test((OnTaskCompleted) context);
        else
            test(null);*/

        return ready();
    }

    public boolean ready() {
        return isReady && price > 0;
    }

    void save() {
        if (receiver == null) {
            Log.e("Product save()", "Can't save it because receiver is empty so no context");
            return;
        }
        if (id == -1) {
            Log.e("Product save()", "Can't save product with invalid id");
            return;
        }
        Log.d("Product save()", "Saving product to client" + id);
        Context context = (Context) receiver;
        SharedPreferences orderPreferences = context.getSharedPreferences("product_" + id, 0);

        orderPreferences.edit()
                .putInt("id", id)
                .putString("name", name)
                .putString("model", model)
                .putString("color", color)
                .putFloat("price", price.floatValue())
                .putFloat("discount", discount.floatValue())
                .apply();
    }

    public void test(OnTaskCompleted receiver) {
        this.receiver = receiver;
        ProductRetrieveTask task = new ProductRetrieveTask(this);
        task.execute(this);
    }

    void copy(Product p) {
        id = p.id;
        name = p.name;
        model = p.model;
        color = p.color;
        price = p.price;
        discount = p.discount;
    }

    public String asString() {
        DecimalFormat df = new DecimalFormat("#.00");
        String out = name + "/" + model + "/" + color + " = " + df.format(getFinalPrice());
        return out;
    }

    @Override
    public void onTaskCompleted(Object o) {
        isReady = true;
        if ((o instanceof Product)) {
            Product product = (Product) o;
            copy(product);
        }

        if (receiver != null)
            if (id != -1) {
                save();
                receiver.onTaskCompleted(o);

            } else
                receiver.onTaskCompleted(null);
    }

    public Double getFinalPrice() {
        if (discount > 0 && discount < 100)
            return (price * discount / 100);
        else
            return (price);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "receiver=" + receiver +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", isReady=" + isReady +
                '}';
    }


}
