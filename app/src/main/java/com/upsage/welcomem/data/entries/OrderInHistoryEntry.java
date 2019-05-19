package com.upsage.welcomem.data.entries;

import com.upsage.welcomem.interfaces.HasId;

import java.sql.Timestamp;

public class OrderInHistoryEntry implements HasId {
    private Integer id = -1;
    private Integer clientId = -1;
    private Timestamp deliveryDate;
    private Integer courier_id = -1;
    private String clientName;

    public OrderInHistoryEntry(Integer id, Integer clientId, Timestamp deliveryDate, Integer courier_id, String clientName) {
        this.id = id;
        this.clientId = clientId;
        this.deliveryDate = deliveryDate;
        this.courier_id = courier_id;
        this.clientName = clientName;
    }

    public boolean ready() {
        return courier_id != -1 && clientId != -1;
    }

    @Override
    public String toString() {
        return "OrderInHistoryEntry{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", deliveryDate=" + deliveryDate +
                ", courier_id=" + courier_id +
                ", clientName='" + clientName + '\'' +
                '}';
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getCourier_id() {
        return courier_id;
    }

    public void setCourier_id(Integer courier_id) {
        this.courier_id = courier_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
