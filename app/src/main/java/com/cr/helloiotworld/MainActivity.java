package com.cr.helloiotworld;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    EditText oldText, newText, editBrandId;
    TextView textView;
    DatabaseHandler db;
    private TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //oldText = findViewById(R.id.editTextOld);
        newText = findViewById(R.id.editTextNew);
        editBrandId = findViewById(R.id.editTextBrandId);
        textView = findViewById(R.id.textView);

        db = new DatabaseHandler(this);
        txt = findViewById(R.id.txt);
        parseXML();
    }

    private void parseXML() {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("data.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            processParsing(parser);

        } catch (XmlPullParserException e) {

        } catch (IOException e) {
        }
    }

    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException{
        ArrayList<Player> players = new ArrayList<>();
        int eventType = parser.getEventType();
        Player currentPlayer = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if ("player".equals(eltName)) {
                        currentPlayer = new Player();
                        players.add(currentPlayer);
                    } else if (currentPlayer != null) {
                        if ("name".equals(eltName)) {
                            currentPlayer.name = parser.nextText();
                        } else if ("age".equals(eltName)) {
                            currentPlayer.age = parser.nextText();
                        } else if ("position".equals(eltName)) {
                            currentPlayer.position = parser.nextText();
                        }
                    }
                    break;
            }

            eventType = parser.next();
        }

        printPlayers(players);
    }

    private void printPlayers(ArrayList<Player> players) {
        StringBuilder builder = new StringBuilder();

        for (Player player : players) {
            builder.append(player.name).append("\n").
                    append(player.age).append("\n").
                    append(player.position).append("\n\n");
        }

        txt.setText(builder.toString());
    }

    public void btn_click(View view) {
        switch (view.getId()) {
            case R.id.add_product_button:
                db.insertProduct(oldText.getText().toString(), Integer.parseInt(editBrandId.getText().toString()));
                break;
            case R.id.add_brand_button:
                db.insertBrand(oldText.getText().toString());
                break;
            case R.id.delete_product_button:
                db.deleteProduct(oldText.getText().toString());
                break;
            case R.id.delete_brand_button:
                db.deleteBrand(oldText.getText().toString());
                break;
            case R.id.update_product_button:
                db.updateProduct(oldText.getText().toString(),
                        newText.getText().toString(),
                        Integer.parseInt(editBrandId.getText().toString()));
                break;
            case R.id.update_brand_button:
                db.updateBrand(oldText.getText().toString(),
                        newText.getText().toString());
                break;
            case R.id.menu_button:
                Intent intent = new Intent(this, login.class);
                startActivity(intent);
                break;
            case R.id.read_button:
                textView.setText("");

                Pair<ArrayList<String>, ArrayList<String>> text = db.read();
                textView.append("Products:\n");
                for (String str :
                        text.first) {
                    textView.append(str + "\n");
                }

                textView.append("Brands:\n");
                for (String str :
                        text.second) {
                    textView.append(str + "\n");
                }

                break;
        }
    }
}
