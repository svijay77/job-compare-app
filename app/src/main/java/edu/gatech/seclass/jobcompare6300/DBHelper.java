package edu.gatech.seclass.jobcompare6300;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 2;

    // Tables for jobs
    public static final String TABLE_CURRENT_JOB = "current_job";
    public static final String TABLE_JOB_OFFERS = "job_offers";

    // Values for jobs
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_COMPANY = "company";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_COST_OF_LIVING = "cost_of_living";
    public static final String COLUMN_SALARY = "salary";
    public static final String COLUMN_BONUS = "bonus";
    public static final String COLUMN_TUITION = "tuition";
    public static final String COLUMN_INSURANCE = "insurance";
    public static final String COLUMN_DISCOUNT = "discount";
    public static final String COLUMN_ADOPTOIN = "adoption";
    public static final String COLUMN_RANK = "rank";

    // Tables for settings

    public static final String TABLE_SETTINGS = "settings";
    //
    // Values for settings
    public static final String COLUMN_SALARY_WEIGHT = "salary_weight";
    public static final String COLUMN_BONUS_WEIGHT = "bonus_weight";
    public static final String COLUMN_REIMBURSEMENT_WEIGHT = "reimbursement_weight";
    public static final String COLUMN_INSURANCE_WEIGHT = "insurance_weight";
    public static final String COLUMN_EMP_DISCOUNT_WEIGHT = "emp_discount_weight";
    public static final String COLUMN_ADOPTION_WEIGHT = "adoption_weight";

    private static final String TABLE_CURRENT_JOB_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_CURRENT_JOB + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_COMPANY + " TEXT, " +
            COLUMN_LOCATION + " TEXT, " +
            COLUMN_COST_OF_LIVING + " INTEGER, " +
            COLUMN_SALARY + " TEXT, " +
            COLUMN_BONUS + " TEXT, " +
            COLUMN_TUITION + " TEXT, " +
            COLUMN_INSURANCE + " TEXT, " +
            COLUMN_DISCOUNT + " TEXT, " +
            COLUMN_ADOPTOIN + " TEXT, " +
            COLUMN_RANK + " REAL" +
            ");";
    private static final String TABLE_JOB_OFFERS_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_JOB_OFFERS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_COMPANY + " TEXT, " +
            COLUMN_LOCATION + " TEXT, " +
            COLUMN_COST_OF_LIVING + " INTEGER, " +
            COLUMN_SALARY + " TEXT, " +
            COLUMN_BONUS + " TEXT, " +
            COLUMN_TUITION + " TEXT, " +
            COLUMN_INSURANCE + " TEXT, " +
            COLUMN_DISCOUNT + " TEXT, " +
            COLUMN_ADOPTOIN + " TEXT, " +
            COLUMN_RANK + " REAL" +
            ");";

    private static final String TABLE_SETTINGS_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTINGS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SALARY_WEIGHT + " INTEGER, " +
            COLUMN_BONUS_WEIGHT + " INTEGER, " +
            COLUMN_REIMBURSEMENT_WEIGHT + " INTEGER, " +
            COLUMN_INSURANCE_WEIGHT + " INTEGER, " +
            COLUMN_EMP_DISCOUNT_WEIGHT + " INTEGER, " +
            COLUMN_ADOPTION_WEIGHT + " INTEGER" +
            ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TABLE_CURRENT_JOB_CREATE);
            db.execSQL(TABLE_JOB_OFFERS_CREATE);
            db.execSQL(TABLE_SETTINGS_CREATE);
            Log.i("DBHelper", "Created all tables successfully");
        } catch (Exception e) {
            Log.e("DBHelper", "Error creating tables: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.i("DBHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT_JOB);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB_OFFERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
            onCreate(db);
        } catch (Exception e) {
            Log.e("DBHelper", "Error upgrading database: " + e.getMessage());
        }
    }
}
