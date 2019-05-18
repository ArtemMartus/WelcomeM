package com.upsage.welcomem.data.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upsage.welcomem.R;

public class OrderInHistoryViewHolder extends RecyclerView.ViewHolder {

    private TextView orderIdTV;
    private TextView clientNameTV;
    private TextView deliveryDateTV;

    public OrderInHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        orderIdTV = itemView.findViewById(R.id.orderHistoryIdTextView);
        clientNameTV = itemView.findViewById(R.id.orderHistoryClientNameTextView);
        deliveryDateTV = itemView.findViewById(R.id.deliveryDateHistoryTextView);
    }

    public void setData(String orderId, String clientName, String deliveryDate) {
        orderIdTV.setText(orderId);
        clientNameTV.setText(clientName);
        deliveryDateTV.setText(deliveryDate);
    }


}
