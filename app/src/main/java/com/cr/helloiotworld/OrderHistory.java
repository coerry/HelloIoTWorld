package com.cr.helloiotworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class OrderHistory extends AppCompatActivity {
    private TextView payNotice;
    private int tagID;
    private ListView orderHistory;

    private DatabaseHandler db;
    private ArrayList<String> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHandler.getInstance(this);

        setContentView(R.layout.activity_order_history);
        payNotice = findViewById(R.id.payNotice);
        orderHistory = findViewById(R.id.drinkHistory);

        ArrayList<Map<String, String>> drinks = (ArrayList<Map<String, String>>) getIntent().getSerializableExtra("chosen");
        Float price = 0f;

        ArrayList<String> drinkList = new ArrayList<>();
        if (drinks != null)
            for (Map d : drinks) {
                drinkList.add(d.get("Name") + " - " + d.get("Price") + "€");
                price += Float.parseFloat(d.get("Price").toString());
            }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, drinkList);
        orderHistory.setAdapter(arrayAdapter);

        payNotice.setText(price.toString());

        if (tagID == 5) {
            orderHistory.setAdapter(null);
            db.buyAllDrinks();

            payNotice.setVisibility(View.VISIBLE);
        }
    }

    public void btn_click(View view) {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
