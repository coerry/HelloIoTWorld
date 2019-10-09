package com.cr.helloiotworld;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText oldText, newText, editBrandId;
    TextView textView;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oldText = findViewById(R.id.editTextOld);
        newText = findViewById(R.id.editTextNew);
        editBrandId = findViewById(R.id.editTextBrandId);
        textView = findViewById(R.id.textView);

        db = new DatabaseHandler(this);
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
