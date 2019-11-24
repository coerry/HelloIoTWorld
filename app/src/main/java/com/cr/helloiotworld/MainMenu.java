package com.cr.helloiotworld;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class MainMenu extends AppCompatActivity {
    private boolean connectedAntenna = false;
    private ListView listView;
    private TextView nameGreetings;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHandler.getInstance(this);
        setContentView(R.layout.activity_main_menu);
        // EXTRA TODO: Greet User by name instead of "Main Menu"

        nameGreetings = findViewById(R.id.nameGreetings);
        listView = findViewById(R.id.listView);

        ArrayList<Map<String, String>> drinks = db.getAllDrinks("-");

        ArrayList<String> drinkList = new ArrayList<>();
        for (Map d : drinks) {
            drinkList.add(d.get("Name") + " - " + d.get("Price") + "€");
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, drinkList);
        listView.setAdapter(arrayAdapter);

        // EXTRA TODO: Greeting a customer!

//        nameGreetings.setText("Hey! " + );
    }

    public void btn_click(View view) {
        Button glassStatus = findViewById(R.id.connectGlass);

        switch (view.getId()) {
            case R.id.connectGlass:
                if (connectedAntenna) {
                    //TODO:Send Start query to Antenna
                    glassStatus.setText("not connected");
                    glassStatus.setBackgroundColor(Color.parseColor("#FFE91E63"));
                    connectedAntenna = false;
                } else {
                    //TODO:Send Stop query to Antenna
                    glassStatus.setText("Connected");
                    glassStatus.setBackgroundColor(Color.parseColor("#FF4CAF50"));
                    connectedAntenna = true;
                }
                break;
            case R.id.orderDrink_button:
                //TODO: add the selected Drink to the Ordered list
                break;
            case R.id.drink_history_button:
                Intent intent = new Intent(this, OrderHistory.class);
                startActivity(intent);
                break;
            case R.id.glass_status_button:
                Intent intent2 = new Intent(this, DrinkStatus.class);
                startActivity(intent2);
                break;
        }
    }
}
