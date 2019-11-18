package com.cr.helloiotworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Todo: Store the entered name and the credit card in the database
        //TODO: tag ID for person #1, for Credit card #5
        //TODO: Bad person "Tom Rocket" needs to be preregisterd with id tag #2
    }

    public void btn_click(View view) {

        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
