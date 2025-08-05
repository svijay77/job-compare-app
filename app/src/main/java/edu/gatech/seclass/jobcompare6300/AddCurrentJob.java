package edu.gatech.seclass.jobcompare6300;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;
import java.math.BigDecimal;

public class AddCurrentJob extends AppCompatActivity {
    private User user;
    private TextView txtJobLabel;
    private TextInputEditText editJobTitle, editCompany, editLocation, editCostOfLiving, editSalary,
            editBonus, editTuition, editInsurance, editDiscount, editAdoption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_current_job);

        user = new User(this);

        setupUI();
        loadExisting();

        loadAndDisplayCurrentJob();

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add/Edit Current Job");
    }

    private void setupUI() {
        txtJobLabel = findViewById(R.id.textJobTitle);
        editJobTitle = findViewById(R.id.editJobTitle);
        editCompany = findViewById(R.id.editCompany);
        editLocation = findViewById(R.id.editLocation);
        editCostOfLiving = findViewById(R.id.editCostOfLiving);
        editSalary = findViewById(R.id.editSalary);
        editBonus = findViewById(R.id.editBonus);
        editTuition = findViewById(R.id.editTuition);
        // rob has to fix the xml here
        editInsurance = findViewById(R.id.editInsurance);
        editDiscount = findViewById(R.id.editDiscount);
        editAdoption = findViewById(R.id.editAdoption);

        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        btnSave.setOnClickListener(v -> saveCurrentJob());
        btnCancel.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("user", user);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

    }

    private void loadExisting() {
        Job currentJob = null;
        try {
            currentJob = user.getCurrentJob().get();
        } catch (Exception e) {
            Log.e("AddCurrentJob", "Error getting current job: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // TODO am suspicious of this, revisit the logic
        if (currentJob.getTitle() != "") {
            editJobTitle.setText(currentJob.getTitle());
            editCompany.setText(currentJob.getCompany());
            editLocation.setText(currentJob.getLocation());
            editCostOfLiving.setText(String.valueOf(currentJob.getCostOfLiving()));
            editSalary.setText(currentJob.getYearlySalary().toString());
            editBonus.setText(currentJob.getYearlyBonus().toString());
            BigDecimal twoPercent = currentJob.getYearlySalary().multiply(BigDecimal.valueOf(0.02));
            editInsurance.setText(currentJob.getInsurance().subtract(twoPercent).toString());
            editTuition.setText(currentJob.getTuitionAssistance().toString());
            editDiscount.setText(currentJob.getEmployeeDiscount().toString());
            editAdoption.setText(currentJob.getAdoptionAssistance().toString());
        }

    }

    private void loadAndDisplayCurrentJob() {
        Log.i("AddCurrentJob", "Created all tables successfully");
        // Get current job from User (which loads from database)
        Job currentJob = null;
        try {
            currentJob = user.getCurrentJob().get();
        } catch (Exception e) {
            Log.e("AddCurrentJob", "Error getting current job: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (currentJob != null && currentJob.getTitle() != null && !currentJob.getTitle().isEmpty()) {
            txtJobLabel.setText(currentJob.getTitle());

        } else {
            // No job found
            txtJobLabel.setText("No current job found");
        }
    }

    private void saveCurrentJob() {
        if (!validateInputs()) {
            return;
        }
        try {
            Job job = new Job(
                    editJobTitle.getText().toString(),
                    editCompany.getText().toString(),
                    editLocation.getText().toString(),
                    Integer.parseInt(editCostOfLiving.getText().toString()),
                    new BigDecimal(editSalary.getText().toString()),
                    new BigDecimal(editBonus.getText().toString()),
                    new BigDecimal(editTuition.getText().toString()),
                    // fix xml here
                    new BigDecimal(editInsurance.getText().toString()),
                    new BigDecimal(editDiscount.getText().toString()),
                    new BigDecimal(editAdoption.getText().toString()));
            job.setIsCurrentJob(true);
            user.setCurrentJob(job);
            Toast.makeText(this, "Successfully saved the current job!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "The number is not valid, try again please", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Set up toolbar back button action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateInputs() {
        if (editJobTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editCompany.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editLocation.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editCostOfLiving.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editSalary.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editBonus.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editTuition.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editInsurance.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editDiscount.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editAdoption.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            BigDecimal salary = new BigDecimal(editSalary.getText().toString());
            BigDecimal tuition = new BigDecimal(editTuition.getText().toString());
            BigDecimal insuranceBase = new BigDecimal(editInsurance.getText().toString());
            BigDecimal discount = new BigDecimal(editDiscount.getText().toString());
            BigDecimal adoption = new BigDecimal(editAdoption.getText().toString());

            if (tuition.compareTo(BigDecimal.ZERO) < 0 || tuition.compareTo(BigDecimal.valueOf(15000)) > 0) {
                Toast.makeText(this, "Tuition Reimbursement is out of range", Toast.LENGTH_LONG).show();
                return false;

            }

            if (insuranceBase.compareTo(BigDecimal.ZERO) < 0 || insuranceBase.compareTo(BigDecimal.valueOf(1000)) > 0) {
                Toast.makeText(this, "Health insurance is out of range", Toast.LENGTH_LONG).show();
                return false;

            }

            BigDecimal maxDiscount = salary.multiply(BigDecimal.valueOf(0.18));
            if (discount.compareTo(BigDecimal.ZERO) < 0 || discount.compareTo(maxDiscount) > 0) {
                Toast.makeText(this, "Employee discount is out of range", Toast.LENGTH_LONG).show();
                return false;

            }

            if (adoption.compareTo(BigDecimal.ZERO) < 0 || adoption.compareTo(BigDecimal.valueOf(30000)) > 0) {
                Toast.makeText(this, "Child adoption assistance is out of range", Toast.LENGTH_LONG).show();
                return false;

            }
            return true;

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Try again please", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}