package com.upsage.welcomem.data.entries;

import com.upsage.welcomem.interfaces.HasId;

import java.sql.Timestamp;

public class OrderInHistoryEntry implements HasId {
    private Integer id;
    private Integer clientId;
    private Timestamp deliveryDate;
    private Integer courier_id;
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

    public Timestamp getDeliveryDate() {
        return deliveryDate;
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

}
