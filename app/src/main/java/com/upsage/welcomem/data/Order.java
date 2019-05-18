package com.upsage.welcomem.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.OrderRetrieveTask;

import java.sql.Timestamp;

public class Order implements OnTaskCompleted {

    private OnTaskCompleted receiver;
    private Integer id = -1;
    private Integer clientId = -1;
    private String productsId = "";
    private Double quantity = -1.0;
    private Double sum = -1.0;
    private String currency = "";
    private String deliveryAddress = "";
    private Timestamp deliveryDate;
    private Integer managerId = -1;

    public Order(Integer id) {
        this.id = id;
    }

    public Order(Integer id, Integer clientId, String productsId, Double quantity, Double sum,
                 String currency, String deliveryAddress, Timestamp deliveryDate, Integer managerId) {
        this.id = id;
        this.clientId = clientId;
        this.productsId = productsId;
        this.quantity = quantity;
        this.sum = sum;
        this.currency = currency;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDate = deliveryDate;
        this.managerId = managerId;
    }

    @Override
    public void onTaskCompleted(Object o) {
        if ((o instanceof Order)) {
            Order orderData = (Order) o;
            copy(orderData);
        }

        if (receiver != null)
            if (id != -1) {
                save();
                receiver.onTaskCompleted(o);

            } else
                receiver.onTaskCompleted(null);
    }

    public boolean load(Context context) {
        if (context == null) {
            Log.e("Order load()", "Can't load it because context is empty");
            return false;
        }
        if (id == -1) {
            Log.e("Order load()", "Can't load order with invalid id");
            return false;
        }
        /*
        SharedPreferences orderPreferences = context.getSharedPreferences("order_" + id, 0);

        clientId = orderPreferences.getInt("clientId",-1);
        managerId = orderPreferences.getInt("managerId",-1);
        sum = (double) orderPreferences.getFloat("sum", -1.0f);
        quantity = (double) orderPreferences.getFloat("quantity", -1.0f);
        productsId = orderPreferences.getString("productsId","");
        currency = orderPreferences.getString("currency","");
        deliveryAddress = orderPreferences.getString("deliveryAddress","");
        long time = orderPreferences.getLong("deliveryDate",-1L);
        if(time != -1L)
            deliveryDate = new Timestamp(time);

        Log.d("Order load()","Async load of order starting");
        if (orderPreferences.getInt("id", -1) == -1)
            test((OnTaskCompleted) context);
        else
            test(null);
*/
        return ready();
    }

    public boolean ready() {
        return clientId != -1 && managerId != -1;
    }

    void save() {
        if (receiver == null) {
            Log.e("Order save()", "Can't save it because receiver is empty so no context");
            return;
        }
        if (id == -1) {
            Log.e("Order save()", "Can't save order with invalid id");
            return;
        }
        Log.d("Order save()", "Saving order to order_"+id);
        Context context = (Context) receiver;
        SharedPreferences orderPreferences = context.getSharedPreferences("order_" + id, 0);

        orderPreferences.edit()
                .putInt("id", id)
                .putInt("clientId", clientId)
                .putInt("managerId", managerId)
                .putFloat("sum", sum.floatValue())
                .putFloat("quantity", quantity.floatValue())
                .putString("productsId", productsId)
                .putString("currency", currency)
                .putString("deliveryAddress", deliveryAddress)
                .apply();
        if (deliveryDate != null)
            orderPreferences.edit()
                    .putLong("deliveryDate", deliveryDate.getTime())
                    .apply();
    }

    void copy(Order o) {
        id = o.id;
        clientId = o.clientId;
        productsId = o.productsId;
        quantity = o.quantity;
        sum = o.sum;
        currency = o.currency;
        deliveryAddress = o.deliveryAddress;
        deliveryDate = o.deliveryDate;
        managerId = o.managerId;
    }

    public void test(OnTaskCompleted receiver) {
        this.receiver = receiver;
        OrderRetrieveTask task = new OrderRetrieveTask(this);
        task.execute(this);

    }

    public OnTaskCompleted getReceiver() {
        return receiver;
    }

    public void setReceiver(OnTaskCompleted receiver) {
        this.receiver = receiver;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getProductsId() {
        return productsId;
    }

    public void setProductsId(String productsId) {
        this.productsId = productsId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "receiver=" + receiver +
                ", id=" + id +
                ", clientId=" + clientId +
                ", productsId='" + productsId + '\'' +
                ", quantity=" + quantity +
                ", sum=" + sum +
                ", currency='" + currency + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", managerId=" + managerId +
                '}';
    }
}
