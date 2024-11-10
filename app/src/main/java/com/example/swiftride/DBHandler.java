package com.example.swiftride;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "swiftridedb";

    // below int is our database version
    private static final int DB_VERSION = 5;

    // User table
    private static final String TABLE_USER = "user";
    private static final String USER_ID_COL = "user_id";
    private static final String USER_NAME_COL = "name";
    private static final String DOB_COL = "dob";
    private static final String USER_EMAIL_COL = "email";
    private static final String PROFILE_IMG_COL = "profile_img_path";
    private static final String PASSWORD_COL = "password";

    private static final String USER_TYPE_COL = "userType";






    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUserTable = "CREATE TABLE " + TABLE_USER + " ("
                + USER_ID_COL + " INTEGER PRIMARY KEY NOT NULL, "
                + USER_NAME_COL + " TEXT,"
                + DOB_COL + " DATE,"
                + USER_TYPE_COL + " TEXT,"
                + USER_EMAIL_COL + " TEXT,"
                + PROFILE_IMG_COL + " TEXT,"
                + PASSWORD_COL + " TEXT)";

        // method to execute above sql query
        db.execSQL(createUserTable);


    }


    // this method is use to add new course to our sqlite database.
    public long regNewUser(Context context, String nicUser, String userName, String userDOB, String userEmail, String password, String profileImgPath, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_ID_COL, nicUser);
        values.put(USER_NAME_COL, userName);
        values.put(DOB_COL, userDOB);
        values.put(USER_EMAIL_COL, userEmail);
        values.put(PROFILE_IMG_COL, profileImgPath);
        values.put(PASSWORD_COL, password);
        values.put(USER_TYPE_COL, userType);


        // Attempt to insert and return the result
        long result = db.insert(TABLE_USER, null, values);
        db.close();

        // Show success message only if successful
        if (result != -1) {
            Toast.makeText(context, "User registered successfully!", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    public boolean isValid(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USER + " WHERE " + USER_EMAIL_COL + " = ? AND " + PASSWORD_COL + " = ?",
                new String[]{email, password}
        );

        boolean userExists = cursor.getCount() > 0;

        if (userExists) {
            Log.d("DBHandler", "User found with email: " + email);
        } else {
            Log.d("DBHandler", "No matching user found with email: " + email);
        }

        cursor.close();
        return userExists;
    }


    public String userType(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + USER_TYPE_COL + " FROM " + TABLE_USER + " WHERE " + USER_EMAIL_COL + " = ?", new String[]{email});

        String userType = null;
        if (cursor.moveToFirst()) {
            userType = cursor.getString(0); // Retrieve the user type from the first column
        }
        cursor.close();
        return userType;
    }

    public List<String> getDriverNic() {
        List<String> driverNic = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to select USER_ID_COL where USER_TYPE_COL is "Bus Driver"
        Cursor cursor = db.rawQuery("SELECT " + USER_ID_COL + " FROM " + TABLE_USER + " WHERE " + USER_TYPE_COL + " = ?", new String[]{"Bus Driver"});

        // Check if the cursor contains the expected column
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Verify if USER_ID_COL exists in the cursor's column names
                int columnIndex = cursor.getColumnIndex(USER_ID_COL);
                if (columnIndex != -1) {
                    // Retrieve the NIC value from USER_ID_COL
                    String nicValue = cursor.getString(columnIndex);
                    driverNic.add(nicValue);
                } else {
                    Log.e("DBHandler", "Column '" + USER_ID_COL + "' not found in cursor.");
                }
            } while (cursor.moveToNext());
        } else {
            Log.e("DBHandler", "Cursor is empty or could not move to first row.");
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return driverNic;
    }






    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }


}
