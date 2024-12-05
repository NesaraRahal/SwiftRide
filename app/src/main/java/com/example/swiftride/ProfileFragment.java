package com.example.swiftride;

import static com.example.swiftride.DBHandler.DOB_COL;
import static com.example.swiftride.DBHandler.PASSWORD_COL;
import static com.example.swiftride.DBHandler.PROFILE_IMG_COL;
import static com.example.swiftride.DBHandler.USER_EMAIL_COL;
import static com.example.swiftride.DBHandler.USER_NAME_COL;
import static com.example.swiftride.MainActivity.loginMail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.InputStream;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;

    private ImageView profileImageView;
    private EditText userNameEditText, userEmailEditText, userDobEditText, passwordEditText;
    private Button editButton, saveButton;

    private boolean isEditing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        profileImageView = view.findViewById(R.id.profile_image);
        userNameEditText = view.findViewById(R.id.user_name);
        userEmailEditText = view.findViewById(R.id.user_email);
        userDobEditText = view.findViewById(R.id.user_dob);
        passwordEditText = view.findViewById(R.id.edit_password);
        editButton = view.findViewById(R.id.btn_edit);
        saveButton = view.findViewById(R.id.btn_save);

        // Set up button listeners
        editButton.setOnClickListener(v -> toggleEditMode());
        saveButton.setOnClickListener(v -> saveUserProfile());

        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            loadUserProfile();
        }

        return view;
    }

    private void toggleEditMode() {
        isEditing = !isEditing;

        // Toggle edit mode for all EditTexts
        userNameEditText.setFocusableInTouchMode(isEditing);
        userEmailEditText.setFocusableInTouchMode(isEditing);
        userDobEditText.setFocusableInTouchMode(isEditing);

        // Show or hide password field
        passwordEditText.setVisibility(isEditing ? View.VISIBLE : View.GONE);

        // Update button text
        editButton.setText(isEditing ? "Cancel" : "Edit");
        saveButton.setVisibility(isEditing ? View.VISIBLE : View.GONE);
    }

    private void loadUserProfile() {
        DBHandler dbHandler = new DBHandler(getContext());

        // Assume nicUser is fetched from the session or passed as an argument
        int nicUser = dbHandler.getUserNic(loginMail);

        // Get user profile data from the database
        Cursor cursor = dbHandler.getUserProfile(getContext(), nicUser);

        if (cursor != null && cursor.moveToFirst()) {
            // Get the indices of the columns
            int userNameIndex = cursor.getColumnIndex(USER_NAME_COL);
            int userEmailIndex = cursor.getColumnIndex(USER_EMAIL_COL);
            int userDobIndex = cursor.getColumnIndex(DOB_COL);
            int profileImgPathIndex = cursor.getColumnIndex(PROFILE_IMG_COL);

            // Retrieve the user details
            String userName = cursor.getString(userNameIndex);
            String userEmail = cursor.getString(userEmailIndex);
            String userDob = cursor.getString(userDobIndex);
            String profileImgPath = cursor.getString(profileImgPathIndex);

            // Set values in the EditTexts
            userNameEditText.setText(userName);
            userEmailEditText.setText(userEmail);
            userDobEditText.setText(userDob);

            // Set profile image
            if (profileImgPath != null && !profileImgPath.isEmpty()) {
                Bitmap profileImage = getProfileImage(getContext(), profileImgPath);
                if (profileImage != null) {
                    profileImageView.setImageBitmap(profileImage);
                } else {
                    profileImageView.setImageResource(R.drawable.user);
                }
            } else {
                profileImageView.setImageResource(R.drawable.user);
            }

            // Disable editing by default
            toggleEditMode();
        } else {
            Toast.makeText(getContext(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserProfile() {
        String userName = userNameEditText.getText().toString();
        String userEmail = userEmailEditText.getText().toString();
        String userDob = userDobEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        DBHandler dbHandler = new DBHandler(getContext());

        int id = dbHandler.getUserNic(loginMail);
        // Save user profile to the database
        boolean success = dbHandler.updateUserProfile(userName, userEmail, userDob, password, id);
        if (success) {
            Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            toggleEditMode();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(getContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getProfileImage(Context context, String profileImgPath) {
        if (profileImgPath.startsWith("content://")) {
            try {
                Uri imageUri = Uri.parse(profileImgPath);
                InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
                return BitmapFactory.decodeStream(imageStream);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return BitmapFactory.decodeFile(profileImgPath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadUserProfile();
            } else {
                Toast.makeText(getContext(), "Permission denied, cannot load profile image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
