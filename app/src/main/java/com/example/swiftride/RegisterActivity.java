package com.example.swiftride;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import android.widget.Toast;
import android.Manifest;


import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.Calendar;

public class RegisterActivity extends ComponentActivity {


    private static final int REQUEST_IMAGE_PICK = 100;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 101;

    private ImageView profileImageView;
    private Uri selectedImageUri; // To store the selected image URI

    public static String userEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_page);
        profileImageView = findViewById(R.id.profileImageView);

        // Find the button and EditText in the layout
        Button dobButton = findViewById(R.id.btn_dob);  // Button to trigger the DatePickerDialog
        final EditText dobEditText = findViewById(R.id.dobRegister);  // EditText to display the selected date

        // Set an OnClickListener for the Date of Birth button
        dobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create the DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegisterActivity.this,  // Context for the dialog
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            // Format the selected date and display it in the EditText
                            //String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                            String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                            dobEditText.setText(selectedDate);  // Set the selected date to the EditText
                        },
                        year, month, day  // Set the initial date to the current date
                );

                // Show the DatePickerDialog
                datePickerDialog.show();
         }
        });

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

        EditText userNICEdt = findViewById(R.id.userNICRegister);
        String userNic = userNICEdt.getText().toString();

        EditText dobEdt = findViewById(R.id.dobRegister);
        String userDob = dobEdt.getText().toString();

        EditText emailEdt = findViewById(R.id.emailRegister);
        userEmail = emailEdt.getText().toString();

        EditText pwEdt = findViewById(R.id.registerPassword);
        String password = pwEdt.getText().toString().trim();

        EditText confirmPwEdt = findViewById(R.id.registerConfirmPassword);
        String confirmPassword = confirmPwEdt.getText().toString().trim();

        Spinner userTypeSpinner = findViewById(R.id.userType);
        String selectedUserType = userTypeSpinner.getSelectedItem().toString();
        Log.d("Registration", "Selected User Type: " + selectedUserType);
        // Image path
        String imgPath = (selectedImageUri != null) ? selectedImageUri.toString() : "";


        // Validate fields
        if (userName.isEmpty() || userNic.isEmpty() || userDob.isEmpty() || userEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || selectedUserType.isEmpty()) {
            Toast.makeText(this, "Please fill all text fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register the user
        DBHandler dbHandler = new DBHandler(this);
        long result = dbHandler.regNewUser(this, userNic, userName, userDob, userEmail, password, imgPath, selectedUserType);

        if (result == -1) {
            Toast.makeText(this, "Registration failed. Try a different NIC or email.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reset fields after successful registration
        userNameEdt.setText("");
        dobEdt.setText("");
        userNICEdt.setText("");
        emailEdt.setText("");
        pwEdt.setText("");
        confirmPwEdt.setText("");
        profileImageView.setImageResource(R.drawable.ic_default_profile);
        selectedImageUri = null;  // Reset the image URI

        // Navigate to the main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
