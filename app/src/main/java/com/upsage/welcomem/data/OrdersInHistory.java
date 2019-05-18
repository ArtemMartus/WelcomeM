package com.upsage.welcomem.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.upsage.welcomem.R;
import com.upsage.welcomem.data.entries.OrderInHistoryEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.OrderHistoryRetrieveTask;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrdersInHistory extends ArrayAdapter<OrderInHistoryEntry> implements OnTaskCompleted {
    private Integer employeeId;
    private OnTaskCompleted receiver;

    public OrdersInHistory(Integer employeeId, Context context) {
        super(context, R.layout.order_item_in_history_list);
        this.employeeId = employeeId;
    }

    @Override
    public void onTaskCompleted(Object o) {
        if ((o instanceof List)) {
            addAll((List<OrderInHistoryEntry>) o);
            notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), R.string.sqlErrorRetryString, Toast.LENGTH_SHORT).show();
        }

        if (receiver != null)
            receiver.onTaskCompleted(null);
    }

    public void test(OnTaskCompleted receiver) {
        this.receiver = receiver;
        OrderHistoryRetrieveTask task = new OrderHistoryRetrieveTask(this);
        task.execute(employeeId);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        OrderInHistoryEntry entry = getItem(position);
        if (entry != null && entry.ready()) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_item_in_history_list, parent, false);
            }
            TextView orderIdTV = convertView.findViewById(R.id.orderHistoryIdTextView);
            TextView clientNameTV = convertView.findViewById(R.id.orderHistoryClientNameTextView);
            TextView deliveryDateTV = convertView.findViewById(R.id.deliveryDateHistoryTextView);

            String str = getContext().getString(R.string.orderIdString) + entry.getId();
            orderIdTV.setText(str);
            clientNameTV.setText(entry.getClientName());
            deliveryDateTV.setText(new SimpleDateFormat("dd/MM/yyyy HH:ss").format(entry.getDeliveryDate()));

            return convertView;
        } else
            return super.getView(position, convertView, parent);
    }

    public boolean ready() {
        for (int i = 0; i < getCount(); ++i) {
            OrderInHistoryEntry item = getItem(i);
            if (item != null && !item.ready())
                return false;
        }
        return true;
    }
}
