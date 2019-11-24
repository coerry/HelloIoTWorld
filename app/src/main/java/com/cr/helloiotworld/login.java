package com.cr.helloiotworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.cr.helloiotworld.data.LoginDataSource;
import com.cr.helloiotworld.data.LoginRepository;

import java.io.IOException;

public class login extends AppCompatActivity {
    EditText loginCreditCardInput, loginNameInput;
    DatabaseHandler db;

    LoginRepository lr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = DatabaseHandler.getInstance(this);

        loginCreditCardInput = findViewById(R.id.loginCreditCardInput);
        loginNameInput = findViewById(R.id.loginNameInput);
    }

    public void login(View view) throws IOException {
        String clientName = String.valueOf(loginNameInput.getText());
        String creditCard = String.valueOf(loginCreditCardInput.getText());

        try {
            lr = LoginRepository.getInstance(new LoginDataSource(clientName, creditCard));
        } catch (Exception e) {
            throw new IOException("Error logging in", e);
        }

        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
