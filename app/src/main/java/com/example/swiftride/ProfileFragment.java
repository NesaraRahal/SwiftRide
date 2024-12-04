package com.example.swiftride;

import static com.example.swiftride.DBHandler.DOB_COL;
import static com.example.swiftride.DBHandler.PROFILE_IMG_COL;
import static com.example.swiftride.DBHandler.USER_EMAIL_COL;
import static com.example.swiftride.DBHandler.USER_NAME_COL;
import static com.example.swiftride.MainActivity.loginMail;

import android.Manifest;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.InputStream;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;

    private ImageView profileImageView;
    private TextView userNameTextView, userEmailTextView, userDobTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImageView = view.findViewById(R.id.profile_image);
        userNameTextView = view.findViewById(R.id.user_name);
        userEmailTextView = view.findViewById(R.id.user_email);
        userDobTextView = view.findViewById(R.id.user_dob);

        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            loadUserProfile();
        }

        return view;
    }

    private void loadUserProfile() {
        DBHandler dbHandler = new DBHandler(getContext());

        // Assume nicUser is fetched from the session or passed as an argument
        int nicUser = dbHandler.getUserNic(loginMail); // Assuming loginMail is the email and nicUser is fetched from there

        // Get user profile data from the database
        Cursor cursor = dbHandler.getUserProfile(getContext(), nicUser);

        if (cursor != null && cursor.moveToFirst()) {
            // Get the indices of the columns
            int userNameIndex = cursor.getColumnIndex(USER_NAME_COL);
            int userEmailIndex = cursor.getColumnIndex(USER_EMAIL_COL);
            int userDobIndex = cursor.getColumnIndex(DOB_COL);
            int profileImgPathIndex = cursor.getColumnIndex(PROFILE_IMG_COL);

            // Retrieve the user details, with default values if columns are not found
            String userName = (userNameIndex != -1) ? cursor.getString(userNameIndex) : "N/A";
            String userEmail = (userEmailIndex != -1) ? cursor.getString(userEmailIndex) : "N/A";
            String userDob = (userDobIndex != -1) ? cursor.getString(userDobIndex) : "N/A";
            String profileImgPath = (profileImgPathIndex != -1) ? cursor.getString(profileImgPathIndex) : "";

            // Set the retrieved values to the TextViews
            userNameTextView.setText(userName);
            userEmailTextView.setText(userEmail);
            userDobTextView.setText(userDob);

            // Set profile image if available
            if (profileImgPath != null && !profileImgPath.isEmpty()) {
                Bitmap profileImage = getProfileImage(getContext(), profileImgPath);
                if (profileImage != null) {
                    profileImageView.setImageBitmap(profileImage);
                } else {
                    // Default image if profile image is null or cannot be loaded
                    profileImageView.setImageResource(R.drawable.user);
                }
            } else {
                // Default image if no profile image path is found
                profileImageView.setImageResource(R.drawable.user);
            }
        } else {
            // Handle case when no user data is found
            userNameTextView.setText("N/A");
            userEmailTextView.setText("N/A");
            userDobTextView.setText("N/A");
            profileImageView.setImageResource(R.drawable.user);
        }
    }

    private Bitmap getProfileImage(Context context, String profileImgPath) {
        if (profileImgPath.startsWith("content://")) {
            try {
                // Create a URI from the content path
                Uri imageUri = Uri.parse(profileImgPath);

                // Use ContentResolver to get an InputStream for the image
                InputStream imageStream = context.getContentResolver().openInputStream(imageUri);

                // Decode the InputStream into a Bitmap
                return BitmapFactory.decodeStream(imageStream);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            // If the path is a regular file path, decode it directly
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
