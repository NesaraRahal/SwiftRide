package com.example.swiftride;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "swiftridedb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // User table
    private static final String TABLE_USER = "user";
    private static final String USER_ID_COL = "user_id";
    private static final String USER_NAME_COL = "name";
    private static final String DOB_COL = "dob";
    private static final String USER_EMAIL_COL = "email";
    private static final String PROFILE_IMG_COL = "profile_img_path";
    private static final String USER_TYPE_ID_COL = "user_type";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUserTable = "CREATE TABLE " + TABLE_USER + " ("
                + USER_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_NAME_COL + " TEXT,"
                + DOB_COL + " TEXT,"
                + USER_EMAIL_COL + " TEXT,"
                + PROFILE_IMG_COL + " TEXT,"
                + USER_TYPE_ID_COL + " TEXT )";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(createUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
