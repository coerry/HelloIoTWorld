package com.cr.helloiotworld;

import androidx.appcompat.app.AppCompatActivity;

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
    private int tagID[] = {0,0,0,0,0};

    private TextView textView;
    protected TextView displayXmlContent;
    protected XmlPullParserFactory xmlPullParserFactory;
    protected XmlPullParser parser;
    private String xmlPath = "";
    private DeviceParse deviceParse;
    int is_on = 0;
    int i = 0;

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
///////////////
        deviceParse = new DeviceParse();
        xmlPath = deviceParse.getFullXmlPath();

        textView = (TextView) findViewById(R.id.t2);

        displayXmlContent = (TextView)findViewById(R.id.display_xml);
        final Button postButton = (Button)findViewById(R.id.b_send);

        try {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(false);
            parser = xmlPullParserFactory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_on == 0) {
                    is_on = 1;
                    postButton.setText("ON");
                    i = 1;
                }else {
                    is_on = 0;
                    postButton.setText("OFF");
                    i = 0;
                }
            }
        });

        Timer myTimer = new Timer(); // Создаем таймер
        final Handler uiHandler = new Handler();

        myTimer.schedule(new TimerTask() { // Определяем задачу
            @Override
            public void run() {
                final String result = xmlPath;
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String s = result + "/inventory/";
                        if (i != 0) {
                            BackgroundAsyncTask backgroundAsyncTask = new BackgroundAsyncTask();
                            backgroundAsyncTask.execute(s);

                            textView.setText(backgroundAsyncTask.getS());
                        }else{textView.setText(" ");}
                    }
                });
            };
        }, 0L, 60L * 20);
///////////////
        if (tagID[3] == 1){
            drinkOk.setVisibility(View.INVISIBLE);
            drinkPoisoned.setVisibility(View.VISIBLE);
            notice.setVisibility(View.VISIBLE);
        }else if(tagID[2] == 1){
            drinkOk.setVisibility(View.VISIBLE);
            drinkPoisoned.setVisibility(View.INVISIBLE);
            notice.setVisibility(View.INVISIBLE);
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

    private class BackgroundAsyncTask extends AsyncTask<String, Void, String>{

        private String S;

        @Override
        protected String doInBackground(String ...params) {
            URL url = null;

            String returnedResult = "";
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(20000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                parser.setInput(is, null);
                returnedResult = getLoadedXmlValues(parser);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return returnedResult;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            displayXmlContent.setText(s);
            S = s;
        }
        public String getS(){
            return S;
        }
        private String getLoadedXmlValues(XmlPullParser parser) throws XmlPullParserException, IOException {
            int eventType = parser.getEventType();
            String name = null;
            List<Entity> list = new ArrayList<>();
            Entity mEntity = new Entity();
            String totalString = "";
            int num_id = 0;
            tagID[0] = 0;tagID[1] = 0;tagID[2] = 0;tagID[3] = 0;tagID[4] = 0;
            while (eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG){
                    name = parser.getName();
                    if(name.equals("item")){
                        if (num_id != 0){
                            list.add(num_id-1, mEntity);
                        }
                        mEntity = new Entity();
                        mEntity.id = String.valueOf(num_id);
                        num_id++;
                    }
                    else if(name.equals("epc")){
                        mEntity.epc = parser.nextText();
                        if (mEntity.epc.equals("000000000000000000000001")){
                            mEntity.epc = "Julia HappyDance";
                            tagID[0] = 1;
                        }
                        if (mEntity.epc.equals("000000000000000000000002")){
                            mEntity.epc = "Tom Rocket";
                            tagID[1] = 1;
                        }
                        if (mEntity.epc.equals("000000000000000000000003")){
                            mEntity.epc = "Drink ";
                            tagID[2] = 1;
                        }
                        if (mEntity.epc.equals("000000000000000000000004")){
                            mEntity.epc = "Poison";
                            tagID[3] = 1;
                        }
                        if (mEntity.epc.equals("000000000000000000000005")){
                            mEntity.epc = "Credit Card";
                            tagID[4] = 1;
                        }
                    }
                }
                eventType = parser.next();
            }
            //if(num_id != 0) {
            //    list.add(num_id - 1, mEntity);
            //   for (int i = 0; i < num_id; i++) {
            //       totalString += list.get(i).id + ") Name: " + list.get(i).epc + "\n detected by " + list.get(i).deviceId + " in time: " + list.get(i).ts + "\n\n";
            //    }
            //}
            return totalString;
        }
        public class Entity{
            public String id;
            public String epc;
        }
    }
}
