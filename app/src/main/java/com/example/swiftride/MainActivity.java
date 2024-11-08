package com.example.swiftride;

import static android.opengl.ETC1.isValid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void registerPage(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void logUser(View view){
        EditText emailLoginEdt = findViewById(R.id.emailInput);
        String loginMail = emailLoginEdt.getText().toString().trim();

        EditText passwordLoginEdt = findViewById(R.id.passwordInput);
        String loginPassword = passwordLoginEdt.getText().toString().trim();

        if (loginMail.isEmpty() || loginPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if(checkCredentials(loginMail, loginPassword)){

            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkCredentials(String loginMail, String loginPassword) {
            DBHandler loginCredentials = new DBHandler(this);
             if(loginCredentials.isValid(loginMail, loginPassword)){
                 return true;
             }
        return false;
    }

}