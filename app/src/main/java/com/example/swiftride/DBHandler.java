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
    private static final int DB_VERSION = 12;

    // User table
    private static final String TABLE_USER = "user";
    private static final String USER_ID_COL = "user_id";
    private static final String USER_NAME_COL = "name";
    private static final String DOB_COL = "dob";
    private static final String USER_EMAIL_COL = "email";
    private static final String PROFILE_IMG_COL = "profile_img_path";
    private static final String PASSWORD_COL = "password";
    private static final String USER_TYPE_COL = "userType";


    //Register Bus Table
    private static final String TABLE_BUS = "bus";
    private static final String BUS_ID_COL = "bus_id";
    private static final String BUS_LICENSE_PLATE_COL = "license_no";
    private static final String ROUTE_NO_COL = "routeNo";
    private static final String ROUTE_START_COL = "startRoute";
    private static final String ROUTE_DESTINATION_COL = "destinationRoute";
    private static final String NO_SEATS_COL = "noSeats";
    private static final String TIME_SLOT_COL = "timeSlots";

    private static final String OWNER_ID_COL = "ownerId";
    private static final String DRIVER_ID_COL = "driverId";

    // Table name
    private static final String TABLE_RESERVATION = "reservation";

    // Column names
    private static final String RESERVATION_ID_COL = "reservation_id"; // Primary key for the reservation table
    private static final String RESERVATION_DATE_COL = "reservation_date"; // Date and time of the reservation
    private static final String BUS_ID_COL_Reservation = "bus_id"; // Foreign key referencing the bus table
    private static final String PASSENGER_ID_COL = "passenger_id"; // Foreign key referencing the user table for passengers
    private static final String DRIVER_ID_COL_Reservation = "driver_id"; // Foreign key referencing the user table for drivers
    private static final String OWNER_ID_COL_Reservation = "owner_id"; // Foreign key referencing the user table for drivers

    private static final String START_POINT_COL = "start_point"; // Starting point of the trip
    private static final String DESTINATION_POINT_COL = "destination_point"; // Destination point of the trip
    private static final String SEAT_NUMBER_COL = "seat_number"; // Reserved seat number
    private static final String TIME_COL = "time";


    // Notification table for managing swap seats
    private static final String TABLE_NOTIFICATION = "notification";

    // Column names
    private static final String NOTIFICATION_ID_COL = "notification_id"; // Primary key for the reservation table
    private static final String SENDER_ID_COL = "sender"; // Date and time of the reservation
    private static final String RECEIVER_ID_COL = "receiver"; // Foreign key referencing the bus table
    private static final String SEAT_1_COL = "seat1"; // Foreign key referencing the user table for passengers
    private static final String SEAT_2_COL = "seat2"; // Foreign key referencing the user table for drivers
    private static final String BUS_ID_NOTIFICATION = "busIdNotification"; // Foreign key referencing the user table for drivers

    private static final String STATUS_COL = "status"; // Starting point of the trip
    private static final String TIME_COL_NOTIFICATION = "timeNotification"; // Destination point of the trip





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

        String createBusTable = "CREATE TABLE " + TABLE_BUS + " ("
                + BUS_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " // Auto-increment for bus ID
                + BUS_LICENSE_PLATE_COL + " TEXT NOT NULL, " // Unique route number
                + ROUTE_NO_COL + " TEXT NOT NULL, " // Unique route number
                + ROUTE_START_COL + " TEXT, " // Start route as text
                + ROUTE_DESTINATION_COL + " TEXT, " // Destination route as text
                + NO_SEATS_COL + " INTEGER, " // Number of seats as integer
                + TIME_SLOT_COL + " TEXT, "
                + OWNER_ID_COL + " INTEGER, " // Owner ID as integer
                + DRIVER_ID_COL + " INTEGER, " // Driver ID as integer
                + "FOREIGN KEY (" + OWNER_ID_COL + ") REFERENCES " + TABLE_USER + "(" + USER_ID_COL + "), " // Foreign key for owner ID
                + "FOREIGN KEY (" + DRIVER_ID_COL + ") REFERENCES " + TABLE_USER + "(" + USER_ID_COL + ") " // Foreign key for driver ID
                + ")";

// Execute the SQL query to create the table
        db.execSQL(createBusTable);

        String createReservationTable = "CREATE TABLE " + TABLE_RESERVATION + " (" +
                RESERVATION_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + // Auto-increment for reservation ID
                RESERVATION_DATE_COL + " DATETIME NOT NULL, " + // Reservation date and time
                BUS_ID_COL_Reservation + " INTEGER NOT NULL, " + // Foreign key to Bus table
                PASSENGER_ID_COL + " INTEGER NOT NULL, " + // Foreign key to User table (Passenger role)
                DRIVER_ID_COL_Reservation + " INTEGER NOT NULL, " + // Foreign key to User table (Driver role)
                OWNER_ID_COL_Reservation + " INTEGER NOT NULL, " + // Foreign key to User table (Owner role)
                START_POINT_COL + " TEXT NOT NULL, " + // Starting point of the route
                DESTINATION_POINT_COL + " TEXT NOT NULL, " + // Destination point of the route
                SEAT_NUMBER_COL + " INTEGER NOT NULL, " + // Seat number being reserved
                TIME_COL + " TEXT NOT NULL, " + // Starting point of the route
                "FOREIGN KEY (" + BUS_ID_COL_Reservation + ") REFERENCES " + TABLE_BUS + "(" + BUS_ID_COL + "), " + // Foreign key for bus ID
                "FOREIGN KEY (" + PASSENGER_ID_COL + ") REFERENCES " + TABLE_USER + "(" + USER_ID_COL + "), " + // Foreign key for passenger ID
                "FOREIGN KEY (" + DRIVER_ID_COL_Reservation + ") REFERENCES " + TABLE_USER + "(" + USER_ID_COL + "), " + // Foreign key for driver ID
                "FOREIGN KEY (" + OWNER_ID_COL_Reservation + ") REFERENCES " + TABLE_USER + "(" + USER_ID_COL + "), " + // Foreign key for owner ID
                "UNIQUE (" + BUS_ID_COL_Reservation + ", " + SEAT_NUMBER_COL + ", " + RESERVATION_DATE_COL + ")" + // Prevent duplicate bookings
                ");";


// Execute the SQL query to create the table
        db.execSQL(createReservationTable);

        String createNotificationTable = "CREATE TABLE " + TABLE_NOTIFICATION + " (" +
                NOTIFICATION_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + // Auto-increment for notification ID
                SENDER_ID_COL + " INTEGER NOT NULL, " + // Sender ID
                RECEIVER_ID_COL + " INTEGER NOT NULL, " + // Receiver ID
                SEAT_1_COL + " INTEGER NOT NULL, " + // Seat of sender
                SEAT_2_COL + " INTEGER NOT NULL, " + // Seat of receiver
                BUS_ID_NOTIFICATION + " INTEGER NOT NULL, " + // Bus ID
                STATUS_COL + " TEXT NOT NULL, " + // Status of the request (pending, accepted, rejected)
                TIME_COL_NOTIFICATION + " DATETIME DEFAULT CURRENT_TIMESTAMP, " + // Timestamp of the notification
                "FOREIGN KEY (" + BUS_ID_NOTIFICATION + ") REFERENCES " + TABLE_BUS + "(" + BUS_ID_COL + "), " + // Foreign key for bus ID
                "FOREIGN KEY (" + SENDER_ID_COL + ") REFERENCES " + TABLE_USER + "(" + USER_ID_COL + "), " + // Foreign key for sender ID
                "FOREIGN KEY (" + RECEIVER_ID_COL + ") REFERENCES " + TABLE_USER + "(" + USER_ID_COL + ")" + // Foreign key for receiver ID
                ");";

// Execute the SQL query to create the table
        db.execSQL(createNotificationTable);


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

    public long regBus(Context context, String licenseNo, String routeNO, String routeStart, String routeDestination, int noSeats, String timeSlots, int  ownerId, int driverId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BUS_LICENSE_PLATE_COL, licenseNo);
        values.put(ROUTE_NO_COL, routeNO);
        values.put(ROUTE_START_COL, routeStart);
        values.put(ROUTE_DESTINATION_COL, routeDestination);
        values.put(NO_SEATS_COL, noSeats);
        values.put(TIME_SLOT_COL, timeSlots);
        values.put(OWNER_ID_COL, ownerId);
        values.put(DRIVER_ID_COL, driverId);


        // Attempt to insert and return the result
        long result = db.insert(TABLE_BUS, null, values);
        db.close();

        // Show success message only if successful
        if (result != -1) {
            Toast.makeText(context, "User registered successfully!", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    public long reserveSeat(Context context, String reserveDate, int busId, int passengerId, int driverId, int ownerId, String start, String destination, int seatNo, String time) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get writable database
        ContentValues values = new ContentValues(); // Prepare content values for insertion

        // Insert reservation data into ContentValues
        values.put(RESERVATION_DATE_COL, reserveDate); // Reservation date
        values.put(BUS_ID_COL_Reservation, busId); // Bus ID
        values.put(PASSENGER_ID_COL, passengerId); // Passenger ID
        values.put(DRIVER_ID_COL_Reservation, driverId); // Driver ID
        values.put(OWNER_ID_COL_Reservation, ownerId); // Owner ID
        values.put(START_POINT_COL, start); // Start point
        values.put(DESTINATION_POINT_COL, destination); // Destination point
        values.put(SEAT_NUMBER_COL, seatNo); // Seat number
        values.put(TIME_COL, time); // Seat number


        // Attempt to insert and return the result
        long result = db.insert(TABLE_RESERVATION, null, values);

        // Close the database connection
        db.close();

        // Show success message only if the insertion is successful
        if (result != -1) {
            Toast.makeText(context, "Reservation made successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Reservation failed", Toast.LENGTH_SHORT).show();
        }

        return result; // Return the row ID of the inserted reservation (or -1 if failed)
    }

    public void createNotification(int senderId, int receiverId, int seat1, int seat2, int busId, String status, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Add values for each column in the notification table
        values.put(SENDER_ID_COL, senderId);       // Sender ID
        values.put(RECEIVER_ID_COL, receiverId);   // Receiver ID
        values.put(SEAT_1_COL, seat1);             // Seat 1
        values.put(SEAT_2_COL, seat2);             // Seat 2
        values.put(BUS_ID_NOTIFICATION, busId);    // Bus ID
        values.put(STATUS_COL, status);            // Status (pending, accepted, rejected)
        values.put(TIME_COL_NOTIFICATION, time);

        // Insert the record into the notification table
        db.insert(TABLE_NOTIFICATION, null, values);
        Log.e("DBHandler", "Notification data entered successfully");
        db.close(); // Close the database connection
    }




    // Returning bus details to display them in card view in frontend
    public List<Bus> getAllBuses(String cityName) {
        List<Bus> busList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_BUS + " WHERE " + ROUTE_START_COL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cityName});

        if (cursor.moveToFirst()) {
            do {
                Bus bus = new Bus(
                        cursor.getInt(cursor.getColumnIndexOrThrow(BUS_ID_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(BUS_LICENSE_PLATE_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ROUTE_NO_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ROUTE_START_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ROUTE_DESTINATION_COL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(NO_SEATS_COL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(OWNER_ID_COL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DRIVER_ID_COL))
                );
                busList.add(bus);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return busList;
    }

    public List<String> getDistinctCities() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> cities = new ArrayList<>();
        String query = "SELECT DISTINCT " + ROUTE_START_COL + " FROM " + TABLE_BUS + " UNION SELECT DISTINCT " + ROUTE_DESTINATION_COL + " FROM " + TABLE_BUS;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                cities.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cities;
    }

    public List<String> getDistinctTimeSlots() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> timeSlots = new ArrayList<>();
        String query = "SELECT DISTINCT " + TIME_SLOT_COL + " FROM " + TABLE_BUS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                timeSlots.add(cursor.getString(0)); // Add the time slot to the list
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return timeSlots; // Return the list of distinct time slots
    }




    //Get Start point of the bus for reservation
    public List<String> getStartPoint() {
        List<String> startPoint = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to select USER_ID_COL where USER_TYPE_COL is "Bus Driver"
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + ROUTE_START_COL + " FROM " + TABLE_BUS, null);

        // Check if the cursor contains the expected column
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Verify if USER_ID_COL exists in the cursor's column names
                int columnIndex = cursor.getColumnIndex(ROUTE_START_COL);
                if (columnIndex != -1) {
                    // Retrieve the NIC value from USER_ID_COL
                    String busStart = cursor.getString(columnIndex);
                    startPoint.add(busStart);
                } else {
                    Log.e("DBHandler", "Column '" + ROUTE_START_COL + "' not found in cursor.");
                }
            } while (cursor.moveToNext());
        } else {
            Log.e("DBHandler", "Cursor is empty or could not move to first row.");
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return startPoint;
    }

    // retreiving the destination for spinners in reservation window

    public List<String> getDestination() {
        List<String> startPoint = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to select USER_ID_COL where USER_TYPE_COL is "Bus Driver"
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + ROUTE_DESTINATION_COL + " FROM " + TABLE_BUS, null);

        // Check if the cursor contains the expected column
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Verify if USER_ID_COL exists in the cursor's column names
                int columnIndex = cursor.getColumnIndex(ROUTE_DESTINATION_COL);
                if (columnIndex != -1) {
                    // Retrieve the NIC value from USER_ID_COL
                    String busStart = cursor.getString(columnIndex);
                    startPoint.add(busStart);
                } else {
                    Log.e("DBHandler", "Column '" + ROUTE_DESTINATION_COL + "' not found in cursor.");
                }
            } while (cursor.moveToNext());
        } else {
            Log.e("DBHandler", "Cursor is empty or could not move to first row.");
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return startPoint;
    }

    public int getNoSeats(String startPoint, String destinationPoint, String timeSlot) {
        SQLiteDatabase db = this.getReadableDatabase();
        int noSeats = 0;

        // Query to retrieve the number of seats based on the selected start point, destination, and time slot
        String query = "SELECT " + NO_SEATS_COL + " FROM " + TABLE_BUS + " WHERE "
                + ROUTE_START_COL + " = ? AND "
                + ROUTE_DESTINATION_COL + " = ? AND "
                + TIME_SLOT_COL + " = ?";

        // Execute the query with the provided parameters
        Cursor cursor = db.rawQuery(query, new String[]{startPoint, destinationPoint, timeSlot});

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            noSeats = cursor.getInt(cursor.getColumnIndexOrThrow(NO_SEATS_COL));
        } else {
            Log.e("DBHandler", "No results found for the provided parameters.");
        }

        // Close the cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return noSeats;
    }

    public int getDriverId(String startPoint, String destinationPoint, String timeSlot) {
        SQLiteDatabase db = this.getReadableDatabase();
        int driverId = 0;

        // Query to retrieve the driver ID based on the selected start point, destination, and time slot
        String query = "SELECT " + DRIVER_ID_COL + " FROM " + TABLE_BUS + " WHERE "
                + ROUTE_START_COL + " = ? AND "
                + ROUTE_DESTINATION_COL + " = ? AND "
                + TIME_SLOT_COL + " = ?";

        // Execute the query with the provided parameters
        Cursor cursor = db.rawQuery(query, new String[]{startPoint, destinationPoint, timeSlot});

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            driverId = cursor.getInt(cursor.getColumnIndexOrThrow(DRIVER_ID_COL));
            Log.e("DBHandler", "Driver ID:" + driverId);

        } else {
            Log.e("DBHandler", "No results found for the provided parameters.");
        }

        // Close the cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return driverId;
    }

    public int getOwnerId(String startPoint, String destinationPoint, String timeSlot) {
        SQLiteDatabase db = this.getReadableDatabase();
        int ownerId = 0;

        // Query to retrieve the driver ID based on the selected start point, destination, and time slot
        String query = "SELECT " + OWNER_ID_COL + " FROM " + TABLE_BUS + " WHERE "
                + ROUTE_START_COL + " = ? AND "
                + ROUTE_DESTINATION_COL + " = ? AND "
                + TIME_SLOT_COL + " = ?";

        // Execute the query with the provided parameters
        Cursor cursor = db.rawQuery(query, new String[]{startPoint, destinationPoint, timeSlot});

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            ownerId = cursor.getInt(cursor.getColumnIndexOrThrow(OWNER_ID_COL));
            Log.e("DBHandler", "Owner ID:" + ownerId);

        } else {
            Log.e("DBHandler", "No results found for the provided parameters.");
        }

        // Close the cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return ownerId;
    }

    public int getBusId(String startPoint, String destinationPoint, String timeSlot) {
        SQLiteDatabase db = this.getReadableDatabase();
        int busId = 0;

        // Query to retrieve the driver ID based on the selected start point, destination, and time slot
        String query = "SELECT " + BUS_ID_COL + " FROM " + TABLE_BUS + " WHERE "
                + ROUTE_START_COL + " = ? AND "
                + ROUTE_DESTINATION_COL + " = ? AND "
                + TIME_SLOT_COL + " = ?";

        // Execute the query with the provided parameters
        Cursor cursor = db.rawQuery(query, new String[]{startPoint, destinationPoint, timeSlot});

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            busId = cursor.getInt(cursor.getColumnIndexOrThrow(BUS_ID_COL));
            Log.e("DBHandler", "Bus ID:" + busId);

        } else {
            Log.e("DBHandler", "No results found for the provided parameters.");
        }

        // Close the cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return busId;
    }


    public int getUserNic(String loginMail) {
        SQLiteDatabase db = this.getReadableDatabase();
        int nic = 0;

        // Query to retrieve the user ID (NIC) based on the email
        String query = "SELECT " + USER_ID_COL + " FROM " + TABLE_USER + " WHERE "
                + USER_EMAIL_COL + " = ?"; // Corrected missing closing quote

        // Execute the query with the provided parameter
        Cursor cursor = db.rawQuery(query, new String[]{loginMail});

        // Check if the cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            nic = cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID_COL));
            Log.e("DBHandler", "Userid: " + nic);

        } else {
            Log.e("DBHandler", "No results found for the provided parameters.");
        }

        // Close the cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return nic;
    }






    // Get owner email
    public int getOwnerIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int ownerId = -1; // Default value if not found

        // Execute query to find the user ID based on email
        Cursor cursor = db.rawQuery(
                "SELECT " + USER_ID_COL + " FROM " + TABLE_USER + " WHERE " + USER_EMAIL_COL + " = ?",
                new String[]{email}
        );

        // Check if the USER_ID_COL exists and cursor has results
        int columnIndex = cursor.getColumnIndex(USER_ID_COL);
        if (columnIndex != -1 && cursor.moveToFirst()) {
            ownerId = cursor.getInt(columnIndex);
        } else {
            Log.e("DBHandler", "Column '" + USER_ID_COL + "' not found or no results for email: " + email);
        }

        cursor.close();
        db.close();
        return ownerId;
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

    public List<Integer> getBookedSeats(String start, String destination, String timeSlot, String date) {
        List<Integer> bookedSeats = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Updated query to avoid conflicts with full timestamp in RESERVATION_DATE_COL
        String query = "SELECT " + SEAT_NUMBER_COL +
                " FROM " + TABLE_RESERVATION +
                " WHERE " + START_POINT_COL + " = ? AND " +
                DESTINATION_POINT_COL + " = ? AND " +
                TIME_COL + " = ? AND DATE(" + RESERVATION_DATE_COL + ") = ?";

        // Debug logs to verify parameters
        Log.d("DBHandler", "Query: " + query);
        Log.d("DBHandler", "Params: start=" + start + ", destination=" + destination +
                ", timeSlot=" + timeSlot + ", date=" + date);

        Cursor cursor = db.rawQuery(query, new String[]{start, destination, timeSlot, date});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int seatNumber = cursor.getInt(0);
                    bookedSeats.add(seatNumber); // Add seat number to the list
                    Log.d("DBHandler", "Booked Seat: " + seatNumber);
                } while (cursor.moveToNext());
            }
            cursor.close(); // Always close the cursor
        } else {
            Log.e("DBHandler", "Cursor is null! Query might have failed.");
        }

        db.close(); // Always close the database connection

        return bookedSeats;
    }

    // Get a list of bookings made by the user
    public List<String> getBookingsByUser(int userNic) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> bookings = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_RESERVATION + " WHERE " + PASSENGER_ID_COL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userNic)});

        if (cursor != null) {
            // Ensure the column indices are valid
            int seatNumberColIndex = cursor.getColumnIndex(SEAT_NUMBER_COL);
            int startPointColIndex = cursor.getColumnIndex(START_POINT_COL);
            int destinationColIndex = cursor.getColumnIndex(DESTINATION_POINT_COL);
            int timeSlotColIndex = cursor.getColumnIndex(TIME_COL);

            // Check if any of the column indices are invalid (-1)
            if (seatNumberColIndex == -1 || startPointColIndex == -1 || destinationColIndex == -1 || timeSlotColIndex == -1) {
                Log.e("DBHandler", "One or more column names are incorrect.");
            } else {
                // Proceed with extracting the data
                while (cursor.moveToNext()) {
                    int seatNumber = cursor.getInt(seatNumberColIndex);
                    String startPoint = cursor.getString(startPointColIndex);
                    String destination = cursor.getString(destinationColIndex);
                    String timeSlot = cursor.getString(timeSlotColIndex);
                    bookings.add("Seat " + seatNumber + ", From " + startPoint + " to " + destination + ", Time: " + timeSlot);
                }
            }
            cursor.close();
        }

        db.close();
        return bookings;
    }

    public boolean isSeatBookedByUser(int seatNo, int userNic) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_RESERVATION +
                " WHERE " + SEAT_NUMBER_COL + " = ? AND " + PASSENGER_ID_COL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(seatNo), String.valueOf(userNic)});
        boolean isBooked = false;
        if (cursor.moveToFirst()) {
            isBooked = cursor.getInt(0) > 0; // Check if count > 0
        }
        cursor.close();
        db.close();
        return isBooked;
    }

    public void cancelSeatBooking(int seatNo, int userNic) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = SEAT_NUMBER_COL + " = ? AND " + PASSENGER_ID_COL + " = ?";
        String[] whereArgs = {String.valueOf(seatNo), String.valueOf(userNic)};
        db.delete(TABLE_RESERVATION, whereClause, whereArgs);
        db.close();
        Log.e("DBHandler", "booking cancelled record deleted");
    }

    public int getPassengerId(int seatId, String startPoint, String destinationPoint, String timeSlot) {
        SQLiteDatabase db = this.getReadableDatabase();
        int passengerId = -1; // Default value indicating no result found

        // Query to retrieve the passenger ID based on seat ID, start point, destination, and time slot
        String query = "SELECT " + PASSENGER_ID_COL + " FROM " + TABLE_RESERVATION + " WHERE "
                + SEAT_NUMBER_COL + " = ? AND "
                + START_POINT_COL + " = ? AND "
                + DESTINATION_POINT_COL + " = ? AND "
                + TIME_COL + " = ?";

        Cursor cursor = null;
        try {
            // Execute the query with the provided parameters
            cursor = db.rawQuery(query, new String[]{String.valueOf(seatId), startPoint, destinationPoint, timeSlot});

            // Fetch the result if available
            if (cursor != null && cursor.moveToFirst()) {
                passengerId = cursor.getInt(cursor.getColumnIndexOrThrow(PASSENGER_ID_COL));
                Log.e("DBHandler", "Passenger ID: " + passengerId);
            } else {
                Log.e("DBHandler", "No results found for the provided parameters.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBHandler", "Error while retrieving passenger ID: " + e.getMessage());
        } finally {
            // Close the cursor and database connection
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return passengerId;
    }


    public void swapSeats(Context context, int seatId1, int seatId2, int userNic1, int userNic2, int busId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Start a transaction to ensure data consistency
            db.beginTransaction();

            // Update seat for user 1
            String updateSeat1 = "UPDATE " + TABLE_RESERVATION + " SET " + SEAT_NUMBER_COL + " = ? WHERE "
                    + PASSENGER_ID_COL + " = ? AND " + BUS_ID_COL_Reservation + " = ?";
            db.execSQL(updateSeat1, new Object[]{seatId2, userNic1, busId});

            // Update seat for user 2
            String updateSeat2 = "UPDATE " + TABLE_RESERVATION + " SET " + SEAT_NUMBER_COL + " = ? WHERE "
                    + PASSENGER_ID_COL + " = ? AND " + BUS_ID_COL_Reservation + " = ?";
            db.execSQL(updateSeat2, new Object[]{seatId1, userNic2, busId});

            // Commit the transaction
            db.setTransactionSuccessful();
            Toast.makeText(context, "Seats swapped successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to swap seats. Please try again.", Toast.LENGTH_SHORT).show();
        } finally {
            // End the transaction
            db.endTransaction();
        }
    }






    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS);  // Add this line
        onCreate(db);
    }

}
