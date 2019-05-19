package com.upsage.welcomem.data.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upsage.welcomem.R;

public class PathwayEntryViewHolder extends RecyclerView.ViewHolder {

    private TextView pathOrderIdTextView;
    private TextView pathAddressTextView;

    public PathwayEntryViewHolder(@NonNull View itemView) {
        super(itemView);
        pathOrderIdTextView = itemView.findViewById(R.id.pathOrderIdTextView);
        pathAddressTextView = itemView.findViewById(R.id.pathAddressTextView);
    }

    public void setData(String orderId, String deliveryAddress) {
        pathOrderIdTextView.setText(orderId);
        pathAddressTextView.setText(deliveryAddress);

    }


}
