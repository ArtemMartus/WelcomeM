package com.upsage.welcomem.data;

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
import com.upsage.welcomem.data.entries.PathwayEntry;
import com.upsage.welcomem.interfaces.OnTaskCompleted;
import com.upsage.welcomem.tasks.PathwaysRetrieveTask;

import java.util.List;

public class Pathways extends ArrayAdapter<PathwayEntry> implements OnTaskCompleted {
    private Integer courierId;
    private OnTaskCompleted receiver;

    public Pathways(Integer courierId, Context context) {
        super(context, R.layout.pathway_item);
        this.courierId = courierId;
    }

    @Override
    public void onTaskCompleted(Object o) {
        if ((o instanceof List)) {
            addAll((List<PathwayEntry>) o);
            notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), R.string.sqlErrorRetryString, Toast.LENGTH_SHORT).show();
        }

        if (receiver != null)
            receiver.onTaskCompleted(null);
    }

    public void test(OnTaskCompleted receiver) {
        this.receiver = receiver;
        PathwaysRetrieveTask task = new PathwaysRetrieveTask(this);
        task.execute(courierId);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PathwayEntry entry = getItem(position);
        if (entry != null && entry.ready()) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.pathway_item, parent, false);
            }
            TextView orderIdTV = convertView.findViewById(R.id.pathOrderIdTextView);
            TextView addressTV = convertView.findViewById(R.id.pathAddressTextView);

            String str = getContext().getString(R.string.orderIdString) + entry.getId();
            orderIdTV.setText(str);
            addressTV.setText(entry.getAddress());

            return convertView;
        } else
            return super.getView(position, convertView, parent);
    }

    public boolean ready() {
        for (int i = 0; i < getCount(); ++i) {
            PathwayEntry item = getItem(i);
            if (item != null && !item.ready())
                return false;
        }
        return true;
    }
}
