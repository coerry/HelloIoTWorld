package com.cr.helloiotworld;

import androidx.appcompat.app.AppCompatActivity;
import main.java.com.cr.helloiotworld.ParserClass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DrinkStatus extends AppCompatActivity {


    ImageView drinkOk;
    ImageView drinkPoisoned;
    TextView notice;
    private Integer tagID[] = {0, 0, 0, 0, 0}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_status);
        //TODO:Connect with real tag detection
        //EXTRA TODO: IF THERE IS TIME! Show in field notice always the last user.

        //Comment: I added the tag id #3 with the same code than the else clause, just to make sure
        // we can use the tag id #3 to set back the status to Drink OK.

        drinkOk = findViewById(R.id.drinkOk);
        drinkPoisoned = findViewById(R.id.drinkPoisoned);
        notice = findViewById(R.id.textView6);

        ParserClass parserClass = new ParserClass();

        tagID = parserClass.getTags();

        if (tagID[3] == 1) {
            drinkOk.setVisibility(View.INVISIBLE);
            drinkPoisoned.setVisibility(View.VISIBLE);
            notice.setVisibility(View.VISIBLE);
        } else if (tagID[2] == 1) {
            drinkOk.setVisibility(View.VISIBLE);
            drinkPoisoned.setVisibility(View.INVISIBLE);
            notice.setVisibility(View.INVISIBLE);
        } else {
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
