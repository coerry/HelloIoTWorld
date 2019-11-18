package com.cr.helloiotworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    boolean connectedAntenna=false;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // EXTRA TODO: Greet User by name instead of "Main Menu"

        listView= findViewById(R.id.listView);

        ArrayList<String> drinkList = new ArrayList<>();

        drinkList.add("Wine");
        drinkList.add("Beer");
        drinkList.add("Vodka Red Bull");
        drinkList.add("Gin Tonic");
        drinkList.add("Cola");
        drinkList.add("Orange Juice");
        drinkList.add("Soda");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, drinkList);
        listView.setAdapter(arrayAdapter);

    }

    public void btn_click(View view) {

        Button glassStatus = findViewById(R.id.connectGlass);

        switch (view.getId()) {
            case R.id.connectGlass:
                    if (connectedAntenna){
                        //TODO:Send Start query to Antenna
                        glassStatus.setText("not connected");
                        glassStatus.setBackgroundColor(Color.parseColor("#FFE91E63"));
                        connectedAntenna = false;
                    }else {
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
