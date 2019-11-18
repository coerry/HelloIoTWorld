package com.cr.helloiotworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DrinkStatus extends AppCompatActivity {


    ImageView drinkOk;
    ImageView drinkPoisoned;
    TextView notice;
    int tagID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_status);
        //TODO:Connect with real tag detection
        //EXTRA TODO: IF THERE IS TIME! Show in field notice always the last user.

        drinkOk = findViewById(R.id.drinkOk);
        drinkPoisoned = findViewById(R.id.drinkPoisoned);
        notice = findViewById(R.id.textView6);


        if (tagID == 4){
            drinkOk.setVisibility(View.INVISIBLE);
            drinkPoisoned.setVisibility(View.VISIBLE);
            notice.setVisibility(View.VISIBLE);
        }else{
            drinkOk.setVisibility(View.VISIBLE);
            drinkPoisoned.setVisibility(View.INVISIBLE);
            notice.setVisibility(View.INVISIBLE);
        }

    }

    public void btn_click(View view) {

        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
