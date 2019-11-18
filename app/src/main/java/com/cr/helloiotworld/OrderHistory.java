package com.cr.helloiotworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class OrderHistory extends AppCompatActivity {

    TextView payNotice;
    int tagID;
    EditText orderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        payNotice = findViewById(R.id.payNotice);
        orderHistory = findViewById(R.id.drinkHistory);

        //TODO: show all ordered drinks in the orderHistory multiline text field with price.
        //TODO: IF ID #5 gets detected do the code in the if-clause

        if (tagID == 5) {

            //TODO: clear drink history
            payNotice.setVisibility(View.VISIBLE);
        }


    }

    public void btn_click(View view) {

        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}