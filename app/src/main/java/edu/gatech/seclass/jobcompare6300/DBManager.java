package edu.gatech.seclass.jobcompare6300;

import java.math.BigDecimal;
import android.database.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertCurrentJob(String title, String company, String location, Integer cOl, BigDecimal salary,
            BigDecimal bonus, BigDecimal tuition, BigDecimal insurance, BigDecimal discount, BigDecimal adoption,
            double rank) {
        // delete current job if found
        database.delete(DBHelper.TABLE_CURRENT_JOB, null, null);

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, title);
        values.put(DBHelper.COLUMN_COMPANY, company);
        values.put(DBHelper.COLUMN_LOCATION, location);
        values.put(DBHelper.COLUMN_COST_OF_LIVING, cOl);
        values.put(DBHelper.COLUMN_SALARY, salary.toString());
        values.put(DBHelper.COLUMN_BONUS, bonus.toString());
        values.put(DBHelper.COLUMN_TUITION, tuition.toString());
        values.put(DBHelper.COLUMN_INSURANCE, insurance.toString());
        values.put(DBHelper.COLUMN_DISCOUNT, discount.toString());
        values.put(DBHelper.COLUMN_ADOPTOIN, adoption.toString());
        values.put(DBHelper.COLUMN_RANK, rank);
        return database.insert(DBHelper.TABLE_CURRENT_JOB, null, values);
    }

//    public long updateCurrentJob(long id, String title, String company, String location, Integer cOl, BigDecimal salary,
//            BigDecimal bonus, BigDecimal tuition, BigDecimal insurance, BigDecimal discount,
//            BigDecimal adoption, double rank) {
//        // delete current job if found
//        database.delete(DBHelper.TABLE_CURRENT_JOB, null, null);
//
//        ContentValues values = new ContentValues();
//        values.put(DBHelper.COLUMN_TITLE, title);
//        values.put(DBHelper.COLUMN_COMPANY, company);
//        values.put(DBHelper.COLUMN_LOCATION, location);
//        values.put(DBHelper.COLUMN_COST_OF_LIVING, cOl);
//        values.put(DBHelper.COLUMN_SALARY, salary.toString());
//        values.put(DBHelper.COLUMN_BONUS, bonus.toString());
//        values.put(DBHelper.COLUMN_TUITION, tuition.toString());
//        values.put(DBHelper.COLUMN_INSURANCE, insurance.toString());
//        values.put(DBHelper.COLUMN_DISCOUNT, discount.toString());
//        values.put(DBHelper.COLUMN_ADOPTOIN, adoption.toString());
//        values.put(DBHelper.COLUMN_RANK, rank);
//
//        // Updates job with id given in request
//        int rowsUpdated = database.update(
//                DBHelper.TABLE_CURRENT_JOB,
//                values,
//                DBHelper.COLUMN_ID + " = ?",
//                new String[] { String.valueOf(id) });
//
//        return rowsUpdated;
//    }

    public long insertCurrentJobOffer(String title, String company, String location, Integer cOl, BigDecimal salary,
            BigDecimal bonus, BigDecimal tuition, BigDecimal insurance, BigDecimal discount, BigDecimal adoption,
            double rank) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, title);
        values.put(DBHelper.COLUMN_COMPANY, company);
        values.put(DBHelper.COLUMN_LOCATION, location);
        values.put(DBHelper.COLUMN_COST_OF_LIVING, cOl);
        values.put(DBHelper.COLUMN_SALARY, salary.toString());
        values.put(DBHelper.COLUMN_BONUS, bonus.toString());
        values.put(DBHelper.COLUMN_TUITION, tuition.toString());
        values.put(DBHelper.COLUMN_INSURANCE, insurance.toString());
        values.put(DBHelper.COLUMN_DISCOUNT, discount.toString());
        values.put(DBHelper.COLUMN_ADOPTOIN, adoption.toString());
        values.put(DBHelper.COLUMN_RANK, rank);
        return database.insert(DBHelper.TABLE_JOB_OFFERS, null, values);
    }

    public long insertCurrentSettings(int salaryWeight, int bonusWeight, int reimbursementWeight,
            int insuranceWeight, int empDiscountWeight, int adoptionWeight) {
        // First delete any existing settings
        database.delete(DBHelper.TABLE_SETTINGS, null, null);

        // Then insert new settings
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SALARY_WEIGHT, salaryWeight);
        values.put(DBHelper.COLUMN_BONUS_WEIGHT, bonusWeight);
        values.put(DBHelper.COLUMN_REIMBURSEMENT_WEIGHT, reimbursementWeight);
        values.put(DBHelper.COLUMN_INSURANCE_WEIGHT, insuranceWeight);
        values.put(DBHelper.COLUMN_EMP_DISCOUNT_WEIGHT, empDiscountWeight);
        values.put(DBHelper.COLUMN_ADOPTION_WEIGHT, adoptionWeight);

        return database.insert(DBHelper.TABLE_SETTINGS, null, values);
    }

    public long updateCurrentSettings(long id, int salaryWeight, int bonusWeight, int reimbursementWeight,
            int insuranceWeight, int empDiscountWeight, int adoptionWeight) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SALARY_WEIGHT, salaryWeight);
        values.put(DBHelper.COLUMN_BONUS_WEIGHT, bonusWeight);
        values.put(DBHelper.COLUMN_REIMBURSEMENT_WEIGHT, reimbursementWeight);
        values.put(DBHelper.COLUMN_INSURANCE_WEIGHT, insuranceWeight);
        values.put(DBHelper.COLUMN_EMP_DISCOUNT_WEIGHT, empDiscountWeight);
        values.put(DBHelper.COLUMN_ADOPTION_WEIGHT, adoptionWeight);

        // Update the settings with the specified ID
        int rowsUpdated = database.update(
                DBHelper.TABLE_SETTINGS,
                values,
                DBHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) });

        return rowsUpdated;
    }

    public Cursor getCurrentSettings() {
        return database.query(DBHelper.TABLE_SETTINGS, null, null, null, null, null, null);
    }

    public Cursor getCurrentJob() {
        return database.query(DBHelper.TABLE_CURRENT_JOB, null, null, null, null, null, null);
    }

    public Cursor getCurrentJobOffers() {
        try {
            Log.d("DBManager", "Getting job offers from table: " + DBHelper.TABLE_JOB_OFFERS);
            Cursor cursor = database.query(
                    DBHelper.TABLE_JOB_OFFERS,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null) {
                Log.d("DBManager", "Found " + cursor.getCount() + " job offers");
            } else {
                Log.e("DBManager", "Cursor is null when querying job offers");
            }

            return cursor;
        } catch (Exception e) {
            Log.e("DBManager", "Error getting job offers: " + e.getMessage(), e);
            return null;
        }
    }

    public boolean saveSettings(int salaryWeight, int bonusWeight, int tuitionWeight,
                                int insuranceWeight, int discountWeight, int adoptionWeight) {
        try {
            Log.d("DBManager", "Saving settings to database");

            // First delete settings already saved, so you can overwrite
            database.delete(DBHelper.TABLE_SETTINGS, null, null);

            // Next create new values to insert
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_SALARY_WEIGHT, salaryWeight);
            values.put(DBHelper.COLUMN_BONUS_WEIGHT, bonusWeight);
            values.put(DBHelper.COLUMN_REIMBURSEMENT_WEIGHT, tuitionWeight);
            values.put(DBHelper.COLUMN_INSURANCE_WEIGHT, insuranceWeight);
            values.put(DBHelper.COLUMN_EMP_DISCOUNT_WEIGHT, discountWeight);
            values.put(DBHelper.COLUMN_ADOPTION_WEIGHT, adoptionWeight);

            long result = database.insert(DBHelper.TABLE_SETTINGS, null, values);
            Log.d("DBManager", "Settings saved with result: " + result);

            return result != -1; // Return true if succeeeded
        } catch (Exception e) {
            Log.e("DBManager", "Error saving settings: " + e.getMessage(), e);
            return false;
        }
    }

    public Cursor getSettings() {
        try {
            Log.d("DBManager", "Getting settings from database");
            Cursor cursor = database.query(
                    DBHelper.TABLE_SETTINGS,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null) {
                Log.d("DBManager", "Found " + cursor.getCount() + " settings rows");
            } else {
                Log.e("DBManager", "Cursor is null when querying settings");
            }

            return cursor;
        } catch (Exception e) {
            Log.e("DBManager", "Error getting settings: " + e.getMessage(), e);
            return null;
        }
    }
}
