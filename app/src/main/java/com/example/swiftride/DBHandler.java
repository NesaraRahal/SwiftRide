package com.example.swiftride;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "swiftridedb";

    // below int is our database version
    private static final int DB_VERSION = 4;

    // User table
    private static final String TABLE_USER = "user";
    private static final String USER_ID_COL = "user_id";
    private static final String USER_NAME_COL = "name";
    private static final String DOB_COL = "dob";
    private static final String USER_EMAIL_COL = "email";
    private static final String PROFILE_IMG_COL = "profile_img_path";
    private static final String PASSWORD_COL = "password";



    // Owner table
    private static final String TABLE_OWNER = "owner";
    private static final String OWNER_ID_COL = "owner_id";
    private static final String OWNER_NIC_COL = "nic"; // foreign key referencing user_id

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
                + USER_EMAIL_COL + " TEXT,"
                + PROFILE_IMG_COL + " TEXT,"
                + PASSWORD_COL + " TEXT)";

        // method to execute above sql query
        db.execSQL(createUserTable);

        String createOwnerTable = "CREATE TABLE " + TABLE_OWNER + " ("
                + OWNER_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OWNER_NIC_COL + " INTEGER, "
                + "FOREIGN KEY(" + OWNER_NIC_COL + ") REFERENCES " + TABLE_USER + "(" + USER_ID_COL + "))";
        db.execSQL(createOwnerTable);
    }


    // this method is use to add new course to our sqlite database.
    public void regNewUser(String nicUser,String userName, String userDOB, String userEmail, String password, String profileImgPath ) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(USER_ID_COL, nicUser);
        values.put(USER_NAME_COL, userName);
        values.put(DOB_COL, userDOB);
        values.put(USER_EMAIL_COL, userEmail);
        values.put(PROFILE_IMG_COL, profileImgPath);
        values.put(PASSWORD_COL, password);



        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_USER, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }
    public boolean isValid(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + USER_EMAIL_COL + " = ? AND " + PASSWORD_COL + " = ?", new String[]{email, password});

        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        return userExists;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OWNER);
        onCreate(db);
    }


}
