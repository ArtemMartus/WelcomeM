package com.upsage.welcomem.data;

import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.OrderRetrieveTask;

import java.sql.Timestamp;

public class Order implements OnTaskCompleted {

    private OnTaskCompleted receiver;
    private Integer id;
    private Integer clientId = -1;
    private String productsId = "";
    private Double quantity = -1.0;
    private Double sum = -1.0;
    private String currency = "";
    private String deliveryAddress = "";
    private Timestamp deliveryDate;
    private Integer managerId = -1;
    private Integer courierId = -1;

    public Order(Integer id) {
        this.id = id;
    }

    public Order(Integer id, Integer clientId, String productsId, Double quantity, Double sum,
                 String currency, String deliveryAddress, Timestamp deliveryDate, Integer managerId,
                 Integer courierId) {
        this.id = id;
        this.clientId = clientId;
        this.productsId = productsId;
        this.quantity = quantity;
        this.sum = sum;
        this.currency = currency;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDate = deliveryDate;
        this.managerId = managerId;
        this.courierId = courierId;
    }

    @Override
    public void onTaskCompleted(Object o) {
        if ((o instanceof Order)) {
            Order orderData = (Order) o;
            copy(orderData);
        }

        if (receiver != null)
            if (id != -1) {
                receiver.onTaskCompleted(o);

            } else
                receiver.onTaskCompleted(null);
    }

    public boolean ready() {
        return clientId != -1 && managerId != -1;
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
        courierId = o.courierId;
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

    public Integer getCourierId() {
        return courierId;
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

    public String getCurrency() {
        return currency;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public Integer getManagerId() {
        return managerId;
    }

    @Override
    public String toString() {
        return "Order{" +
                ", id=" + id +
                ", clientId=" + clientId +
                ", productsId='" + productsId + '\'' +
                ", quantity=" + quantity +
                ", sum=" + sum +
                ", currency='" + currency + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", managerId=" + managerId +
                ", courierId=" + courierId +
                '}';
    }
}
