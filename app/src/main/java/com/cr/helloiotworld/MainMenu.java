package com.cr.helloiotworld;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                db.choseDrink(position + 1);
            }
        });
    }

    public void btn_click(View view) {

        switch (view.getId()) {

            case R.id.drink_history_button:
                Intent intent = new Intent(this, OrderHistory.class);
                intent.putExtra("chosen", db.getAllDrinks("chosen"));
                startActivity(intent);
                break;
            case R.id.glass_status_button:
                Intent intent2 = new Intent(this, DrinkStatus.class);
                startActivity(intent2);
                break;
        }
    }
}
