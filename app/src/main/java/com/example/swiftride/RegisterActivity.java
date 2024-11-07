package com.example.swiftride;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;


import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class RegisterActivity extends Activity {


    private static final int REQUEST_IMAGE_PICK = 100;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 101;

    private ImageView profileImageView;
    private Uri selectedImageUri; // To store the selected image URI



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        profileImageView = findViewById(R.id.profileImageView);

    }

    public void selectProfileImage(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            // Permission already granted, open image picker
            openImagePicker();
        }
    }
    // Handle the permission request response
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Open the image picker intent
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Optionally, specify the type to images only
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    // Handle the result of the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null){
            selectedImageUri = data.getData();
            try {
                // Load the selected image into the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to save the selected image path to the database during registration
    private void saveProfileImagePath(String imagePath) {
        // Implement your database logic here
    }

    public void regUser(View view){
        EditText userNameEdt = findViewById(R.id.usernameRegister);
        String userName = userNameEdt.getText().toString();

        EditText dobEdt = findViewById(R.id.dobRegister);
        String userDob = dobEdt.getText().toString();

        EditText emailEdt = findViewById(R.id.emailRegister);
        String userEmail = emailEdt.getText().toString();

        EditText pwEdt = findViewById(R.id.registerPassword);
        String password = pwEdt.getText().toString().trim();

        EditText confirmPwEdt = findViewById(R.id.registerConfirmPassword);
        String confirmPassword = confirmPwEdt.getText().toString().trim();

        //Image path
        String imgPath = (selectedImageUri != null) ? selectedImageUri.toString() : ""; // Use selected image path if available

        if(userName.isEmpty() || userDob.isEmpty() || userEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Toast.makeText(this, "Please fill all text fields", Toast.LENGTH_SHORT).show();
        }

        if(!password.equals(confirmPassword)){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.regNewUser(userName, userDob, userEmail,  confirmPassword, imgPath);

        Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show();

        userNameEdt.setText("");
        dobEdt.setText("");
        emailEdt.setText("");
        pwEdt.setText("");
        confirmPwEdt.setText("");
        profileImageView.setImageResource(R.drawable.ic_default_profile);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
