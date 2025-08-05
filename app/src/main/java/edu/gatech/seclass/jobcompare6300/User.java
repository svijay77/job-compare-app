package edu.gatech.seclass.jobcompare6300;

import java.math.BigDecimal;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.io.Serializable;
import java.util.Optional;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Job currentJob;
    private ArrayList<Job> jobOffers;
    private Settings settings;

    private transient DBManager dbManager;
    private transient Context context;

    public User() {
        currentJob = new Job();
        jobOffers = new ArrayList<>();
    }

    public User(Context context) {
        currentJob = new Job();
        jobOffers = new ArrayList<Job>();
        settings = new Settings();
        this.context = context;

        // Initialize database manager
        if (context != null) {
            try {
                dbManager = new DBManager(context);
                dbManager.open();

                // Load each type of data in separate try blocks
                try {
                    loadCurrentJobFromDatabase();
                } catch (Exception e) {
                    Log.e("User", "Error loading current job: " + e.getMessage(), e);
                }

                try {
                    loadJobOffersFromDatabase();
                } catch (Exception e) {
                    Log.e("User", "Error loading job offers: " + e.getMessage(), e);
                }

                try {
                    loadSettingsFromDatabase();
                } catch (Exception e) {
                    Log.e("User", "Error loading settings: " + e.getMessage(), e);
                    // Already have default settings from initialization
                }
            } catch (Exception e) {
                Log.e("User", "Error initializing database: " + e.getMessage(), e);
            }
        } else {
            Log.e("User", "Context is null, cannot initialize database");
        }
    }

    public void close() {
        if (dbManager != null) {
            try {
                dbManager.close();
            } catch (Exception e) {
                Log.e("User", "Error closing database: " + e.getMessage(), e);
            }
            dbManager = null;
        }
    }

    private Job loadCurrentJobFromDatabase() {
        try {
            Cursor cursor = dbManager.getCurrentJob();
            if (cursor != null && cursor.moveToFirst()) {
                currentJob = cursorToJob(cursor);
                currentJob.setIsCurrentJob(true);
                cursor.close();

                return currentJob;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Job emptyJob = new Job();
        return emptyJob;
    }

    // Load job offers from db
    private void loadJobOffersFromDatabase() {
        try {
            Log.d("User", "Loading job offers from database...");
            Cursor cursor = dbManager.getCurrentJobOffers();

            if (cursor != null) {
                Log.d("User", "Got cursor with " + cursor.getCount() + " rows");
                jobOffers.clear(); // Clear existing offers

                while (cursor.moveToNext()) {
                    Job job = cursorToJob(cursor);
                    jobOffers.add(job);
                    Log.d("User", "Loaded job offer: " + job.getTitle() + " at " + job.getCompany());
                }

                Log.d("User", "Loaded " + jobOffers.size() + " job offers from database");
                cursor.close();
            } else {
                Log.e("User", "Cursor is null when loading job offers");
            }
        } catch (Exception e) {
            Log.e("User", "Error loading job offers: " + e.getMessage(), e);
        }
    }

    public boolean hasCurrentJob() {
        // Try to load the current job if it wasn't loaded yet
        if (currentJob == null && dbManager != null) {
            try {
                loadCurrentJobFromDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // checks if there is a job and it has a title, title check may be superfluou
        return currentJob != null &&
                currentJob.getTitle() != null &&
                !currentJob.getTitle().isEmpty();
    }

    // Helper method to convert cursor to Job
    private Job cursorToJob(Cursor cursor) {
        Job job = new Job();

        try {
            // Get columnss
            int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
            int titleIndex = cursor.getColumnIndex(DBHelper.COLUMN_TITLE);
            int companyIndex = cursor.getColumnIndex(DBHelper.COLUMN_COMPANY);
            int locationIndex = cursor.getColumnIndex(DBHelper.COLUMN_LOCATION);
            int colIndex = cursor.getColumnIndex(DBHelper.COLUMN_COST_OF_LIVING);
            int salaryIndex = cursor.getColumnIndex(DBHelper.COLUMN_SALARY);
            int bonusIndex = cursor.getColumnIndex(DBHelper.COLUMN_BONUS);
            int tuitionIndex = cursor.getColumnIndex(DBHelper.COLUMN_TUITION);
            int insuranceIndex = cursor.getColumnIndex(DBHelper.COLUMN_INSURANCE);
            int discountIndex = cursor.getColumnIndex(DBHelper.COLUMN_DISCOUNT);
            int adoptionIndex = cursor.getColumnIndex(DBHelper.COLUMN_ADOPTOIN);
            int rankIndex = cursor.getColumnIndex(DBHelper.COLUMN_RANK);

            // Create a new Job with required props
            String title = titleIndex >= 0 ? cursor.getString(titleIndex) : "";
            String company = companyIndex >= 0 ? cursor.getString(companyIndex) : "";
            String location = locationIndex >= 0 ? cursor.getString(locationIndex) : "";
            Integer col = colIndex >= 0 ? cursor.getInt(colIndex) : 0;

            // Handle BigDecimal fields
            BigDecimal salary = BigDecimal.ZERO;
            if (salaryIndex >= 0 && !cursor.isNull(salaryIndex)) {
                try {
                    salary = new BigDecimal(cursor.getString(salaryIndex));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            BigDecimal bonus = BigDecimal.ZERO;
            if (bonusIndex >= 0 && !cursor.isNull(bonusIndex)) {
                try {
                    bonus = new BigDecimal(cursor.getString(bonusIndex));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            BigDecimal tuition = BigDecimal.ZERO;
            if (tuitionIndex >= 0 && !cursor.isNull(tuitionIndex)) {
                try {
                    tuition = new BigDecimal(cursor.getString(tuitionIndex));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            BigDecimal insurance = BigDecimal.ZERO;
            if (insuranceIndex >= 0 && !cursor.isNull(insuranceIndex)) {
                try {
                    insurance = new BigDecimal(cursor.getString(insuranceIndex));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            BigDecimal discount = BigDecimal.ZERO;
            if (discountIndex >= 0 && !cursor.isNull(discountIndex)) {
                try {
                    discount = new BigDecimal(cursor.getString(discountIndex));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            BigDecimal adoption = BigDecimal.ZERO;
            if (adoptionIndex >= 0 && !cursor.isNull(adoptionIndex)) {
                try {
                    adoption = new BigDecimal(cursor.getString(adoptionIndex));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Create job
            job = new Job(title, company, location, col, salary, bonus, tuition, insurance, discount, adoption);

            // Set ID and rank
            if (idIndex >= 0) {
                job.setId(cursor.getLong(idIndex));
            }

            if (rankIndex >= 0) {
                job.setJobScore(cursor.getDouble(rankIndex));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return job;
    }

    public User(Job _job) {
        currentJob = _job;
        jobOffers = new ArrayList<>();
    }

    public void setCurrentJob(Job _job) {
        // currentJob = _job;

        currentJob = _job;

        // Save to database if dbManager is available
        if (dbManager != null && _job != null) {
            try {
                long result = dbManager.insertCurrentJob(
                        _job.getTitle(),
                        _job.getCompany(),
                        _job.getLocation(),
                        _job.getCostOfLiving(),
                        _job.getYearlySalary(),
                        _job.getYearlyBonus(),
                        _job.getTuitionAssistance(),
                        _job.getInsurance(),
                        _job.getEmployeeDiscount(),
                        _job.getAdoptionAssistance(),
                        _job.getJobScore());

                if (result > 0) {
                    _job.setId(result); // Update the job with its database ID
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setJobOffer(Job _job) {
        // Add to job offers instead of setting as current job
        jobOffers.add(_job);
        Log.d("User", "Added job offer: " + _job.getTitle() + " to collection (size now: " + jobOffers.size() + ")");

        // Save to database if dbManager is available
        if (dbManager != null && _job != null) {
            try {
                Log.d("User", "Saving job offer to database: " + _job.getTitle());
                long result = dbManager.insertCurrentJobOffer(
                        _job.getTitle(),
                        _job.getCompany(),
                        _job.getLocation(),
                        _job.getCostOfLiving(),
                        _job.getYearlySalary(),
                        _job.getYearlyBonus(),
                        _job.getTuitionAssistance(),
                        _job.getInsurance(),
                        _job.getEmployeeDiscount(),
                        _job.getAdoptionAssistance(),
                        _job.getJobScore());

                if (result > 0) {
                    _job.setId(result); // Update the job with its database ID
                    Log.d("User", "Successfully saved job offer with ID: " + result);
                } else {
                    Log.e("User", "Failed to save job offer to database, result: " + result);
                }
            } catch (Exception e) {
                Log.e("User", "Error saving job offer: " + e.getMessage(), e);
            }
        } else {
            Log.e("User", "Cannot save job offer: dbManager is " + (dbManager == null ? "null" : "not null") +
                    ", job is " + (_job == null ? "null" : "not null"));
        }
    }

    public Optional<Job> getCurrentJob() {
        // return currentJob;

        try {
            Cursor cursor = dbManager.getCurrentJob();
            if (cursor != null && cursor.moveToFirst()) {
                currentJob = cursorToJob(cursor);
                currentJob.setIsCurrentJob(true);
                cursor.close();

                return Optional.of(currentJob);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    };

    public void addJobOffer(Job _jobOffer) {

        jobOffers.add(_jobOffer);
    }

    public ArrayList<Job> getJobOffers() {
        if ((jobOffers == null || jobOffers.isEmpty()) && dbManager != null) {
            try {
                loadJobOffersFromDatabase();
            } catch (Exception e) {
                Log.e("User", "Error loading job offers: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Return the job offers list (might be empty if there are none job offers)
        if (jobOffers == null) {
            jobOffers = new ArrayList<>();
        }
        return jobOffers;
    }

    public Settings getSettings() {
        if (settings == null) {
            settings = new Settings(); // Return default settings if null

            // Try to load from database
            if (dbManager != null) {
                try {
                    loadSettingsFromDatabase();
                } catch (Exception e) {
                    Log.e("User", "Error loading settings in getSettings(): " + e.getMessage(), e);
                }
            }
        }
        return settings;
    }

    public void saveSettings(Settings _settings) {
        Log.d("User_saveSettings", "entered save settings func");
        this.settings = _settings;

        // Save to database
        if (dbManager != null && _settings != null) {
            Log.d("User_saveSettings", "i pass this if condition");
            try {
                Log.d("User_saveSettings", "Saving settings to database");
                boolean result = dbManager.saveSettings(
                        _settings.getSalaryWeight(),
                        _settings.getBonusWeight(),
                        _settings.getTuitionWeight(),
                        _settings.getHealthWeight(),
                        _settings.getEmployeeWeight(),
                        _settings.getAdoptionWeight()
                );

                if (result) {
                    Log.d("User_saveSettings", "Settings saved successfully");
                } else {
                    Log.e("User_saveSettings", "Failed to save settings");
                }
            } catch (Exception e) {
                Log.e("User_saveSettings", "Error saving settings: " + e.getMessage(), e);
            }
        } else {
            Log.e("User_saveSettings", "Cannot save settings: dbManager is " +
                    (dbManager == null ? "null" : "not null"));
        }
    }

    private void loadSettingsFromDatabase() {
        try {
            Log.d("User", "Loading settings from database");

            // Create default settings before attempting to loadfrom Database
            settings = new Settings();

            if (dbManager == null) {
                Log.e("User", "DBManager is null, cannot load settings");
                return;
            }

            Cursor cursor = dbManager.getSettings();

            if (cursor != null) {
                Log.d("User", "Got settings cursor with " + cursor.getCount() + " rows");

                if (cursor.moveToFirst()) {
                    try {
                        settings = cursorToSettings(cursor);
                        Log.d("User", "Settings loaded successfully from database");
                    } catch (Exception e) {
                        Log.e("User", "Error parsing settings from cursor: " + e.getMessage(), e);
                        // Already created default settings above
                    }
                } else {
                    Log.d("User", "No settings found in database, using defaults");
                    // Already created default settings above

                    // Save default settings to DB
                    saveSettings(settings);
                }
                cursor.close();
            } else {
                Log.e("User", "Settings cursor is null");
                // Already created default settings above
            }
        } catch (Exception e) {
            Log.e("User", "Error loading settings: " + e.getMessage(), e);
            // Ensure settings is not null
            if (settings == null) {
                settings = new Settings();
            }
        }
    }

    private Settings cursorToSettings(Cursor cursor) {
        Settings settings = new Settings();

        try {
            Log.d("User", "Converting cursor to settings object");

            // Add column name debugging
            String[] columnNames = cursor.getColumnNames();
            Log.d("User", "Available columns: " + String.join(", ", columnNames));

            int salaryIndex = cursor.getColumnIndex(DBHelper.COLUMN_SALARY_WEIGHT);
            Log.d("User", "COLUMN_SALARY_WEIGHT index: " + salaryIndex);
            if (salaryIndex >= 0 && !cursor.isNull(salaryIndex)) {
                int value = cursor.getInt(salaryIndex);
                Log.d("User", "Setting salary weight: " + value);
                settings.setSalaryWeight(value);
            }

            int bonusIndex = cursor.getColumnIndex(DBHelper.COLUMN_BONUS_WEIGHT);
            Log.d("User", "COLUMN_BONUS_WEIGHT index: " + bonusIndex);
            if (bonusIndex >= 0 && !cursor.isNull(bonusIndex)) {
                int value = cursor.getInt(bonusIndex);
                Log.d("User", "Setting bonus weight: " + value);
                settings.setBonusWeight(value);
            }

            // CRITICAL FIX: Verify column name matches exactly what's in the database
            int tuitionIndex = cursor.getColumnIndex(DBHelper.COLUMN_REIMBURSEMENT_WEIGHT);
            Log.d("User", "COLUMN_REIMBURSEMENT_WEIGHT index: " + tuitionIndex);
            if (tuitionIndex >= 0 && !cursor.isNull(tuitionIndex)) {
                int value = cursor.getInt(tuitionIndex);
                Log.d("User", "Setting tuition weight: " + value);
                settings.setTuitionWeight(value);
            }

            int insuranceIndex = cursor.getColumnIndex(DBHelper.COLUMN_INSURANCE_WEIGHT);
            Log.d("User", "COLUMN_INSURANCE_WEIGHT index: " + insuranceIndex);
            if (insuranceIndex >= 0 && !cursor.isNull(insuranceIndex)) {
                int value = cursor.getInt(insuranceIndex);
                Log.d("User", "Setting health weight: " + value);
                settings.setHealthWeight(value);
            }

            int discountIndex = cursor.getColumnIndex(DBHelper.COLUMN_EMP_DISCOUNT_WEIGHT);
            Log.d("User", "COLUMN_EMP_DISCOUNT_WEIGHT index: " + discountIndex);
            if (discountIndex >= 0 && !cursor.isNull(discountIndex)) {
                int value = cursor.getInt(discountIndex);
                Log.d("User", "Setting employee weight: " + value);
                settings.setEmployeeWeight(value);
            }

            int adoptionIndex = cursor.getColumnIndex(DBHelper.COLUMN_ADOPTION_WEIGHT);
            Log.d("User", "COLUMN_ADOPTION_WEIGHT index: " + adoptionIndex);
            if (adoptionIndex >= 0 && !cursor.isNull(adoptionIndex)) {
                int value = cursor.getInt(adoptionIndex);
                Log.d("User", "Setting adoption weight: " + value);
                settings.setAdoptionWeight(value);
            }

            Log.d("User", "Settings successfully converted from cursor");
        } catch (Exception e) {
            Log.e("User", "Error converting cursor to settings: " + e.getMessage(), e);
        }

        return settings;
    }
}
