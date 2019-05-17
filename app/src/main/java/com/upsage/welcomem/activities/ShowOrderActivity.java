package com.upsage.welcomem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.upsage.welcomem.OnTaskCompleted;
import com.upsage.welcomem.R;
import com.upsage.welcomem.data.Client;
import com.upsage.welcomem.data.EmployeeData;
import com.upsage.welcomem.data.Order;
import com.upsage.welcomem.data.Product;
import com.upsage.welcomem.utils.ThemeStyle;
import com.upsage.welcomem.utils.ThemeUtil;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ShowOrderActivity extends AppCompatActivity implements OnTaskCompleted {
    private final static String TAG = "Show Order Activity";

    TextView clientNameTextView;
    TextView clientTelephoneTextView;
    TextView productsTextView;
    TextView managerNameTextView;
    TextView managerTelephoneTextView;
    TextView clientBalanceTextView;
    TextView sumTextView;
    TextView orderPaidTextView;
    TextView orderAddressTextView;
    Button payOrderButton;

    Order order;
    Client client;
    EmployeeData manager;
    List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);

        clientNameTextView = findViewById(R.id.clientNameTextView);
        clientTelephoneTextView = findViewById(R.id.clientTelephoneTextView);
        productsTextView = findViewById(R.id.productsTextView);
        managerNameTextView = findViewById(R.id.managerNameTextView);
        managerTelephoneTextView = findViewById(R.id.managerTelephoneTextView);
        clientBalanceTextView = findViewById(R.id.clientBalanceTextView);
        sumTextView = findViewById(R.id.sumTextView);
        orderPaidTextView = findViewById(R.id.orderPaidTextView);
        orderAddressTextView = findViewById(R.id.orderAddressTextView);
        payOrderButton = findViewById(R.id.payOrderButton);

        payOrderButton.setOnClickListener(v -> {
            Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();
        });

        orderAddressTextView.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(order.getDeliveryAddress()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        });
        if (ThemeUtil.getStyle() == ThemeStyle.BlackNormal
                || ThemeUtil.getStyle() == ThemeStyle.BlackSmall
                || ThemeUtil.getStyle() == ThemeStyle.BlackLarge) {
            orderAddressTextView.setTextColor(Color.YELLOW);
        } else
            orderAddressTextView.setTextColor(Color.BLUE);

        int id = getIntent().getIntExtra("orderId", -1);
        if (id == -1) {
            Log.e(TAG, "No order id as input");
            finish();
            return;
        }
        order = new Order(id);
        if (!order.load(this))
            order.test(this);
        else {
            onTaskCompleted(order);
        }

    }

    @Override
    public void onTaskCompleted(Object o) {

        if (client == null) {
            client = new Client(order.getClientId());
            client.load(ShowOrderActivity.this);
        }

        if (manager == null) {
            manager = new EmployeeData(order.getManagerId());
            manager.load(ShowOrderActivity.this);
        }

        if (products == null) {
            products = new LinkedList<>();
            for (String part : order.getProductsId().split(" ")) {
                try {
                    Product product = new Product(Integer.parseInt(part));
                    product.load(ShowOrderActivity.this);
                    products.add(product);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Show Order Activity", "Some error in products string THIS_PART ain't integer-[" + part + "]");
                    continue;
                }
            }
        } else {
            for (Product p : products) {
                if (!p.ready())
                    return; // We must wait until all products are loaded from database or cache
            }
        }


        if (order.getDeliveryDate() != null) {
            payOrderButton.setEnabled(false);
            payOrderButton.setVisibility(View.INVISIBLE);
            orderPaidTextView.setVisibility(View.VISIBLE);
            Date date = new Date(order.getDeliveryDate().getTime());
            orderPaidTextView.setText(getString(R.string.deliveredString) + date.toString());
        }
        clientNameTextView.setText(getString(R.string.clientNameString) + client.toString());
        clientTelephoneTextView.setText(getString(R.string.clientTelephoneString) + client.getTelNumber());
        clientBalanceTextView.setText(getString(R.string.clientBalanceString) + client.getBalance());

        managerNameTextView.setText(getString(R.string.managerNameString) +
                manager.getName() + " " + manager.getSurname());
        managerTelephoneTextView.setText(getString(R.string.managerTelephoneString) +
                manager.getTelNumber());

        String productsInfo = "";
        Double sum = 0.0;
        for (Product p : products) {
            productsInfo += p.asString() + "\n";
            sum += p.getFinalPrice();
        }
        productsTextView.setText(productsInfo);

        {
            DecimalFormat df = new DecimalFormat("#.00");
            sumTextView.setText(getString(R.string.sumString) + df.format(sum) + order.getCurrency());
            //sumTextView.setText(getString(R.string.sumString) + order.getSum().toString() + order.getCurrency());
        }
        orderAddressTextView.setText(getString(R.string.addressString) + order.getDeliveryAddress());

        Toast.makeText(this, "Completed!", Toast.LENGTH_SHORT).show();
    }
}
