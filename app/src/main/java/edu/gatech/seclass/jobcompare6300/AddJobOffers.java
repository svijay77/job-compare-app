package edu.gatech.seclass.jobcompare6300;

import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import java.math.BigDecimal;

public class AddJobOffers extends AppCompatActivity {

    private User user;

    private TextInputEditText edJobTitle, edCompany, edLocation, edCostOfLiving, edYearlySalary,
            edYearlyBonus, edTuition, edInsurance, edDiscount, edAdoption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_job_offers);

        // Create a new User with context - don't rely on passed User object
        user = new User(this);

        setupUI();
        empty();

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Job Offer");
    }

    private void setupUI() {
        edJobTitle = findViewById(R.id.etJobTitle);
        edCompany = findViewById(R.id.etCompany);
        edLocation = findViewById(R.id.etLocation);
        edCostOfLiving = findViewById(R.id.etCostOfLiving);
        edYearlySalary = findViewById(R.id.etYearlySalary);
        edYearlyBonus = findViewById(R.id.etYearlyBonus);
        edTuition = findViewById(R.id.edTuition);
        // rob has to fix the xml here
        edInsurance = findViewById(R.id.edInsurance);
        edDiscount = findViewById(R.id.edDiscount);
        edAdoption = findViewById(R.id.edAdoption);

        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        btnSave.setOnClickListener(v -> saveJobOffer());
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }

    private void saveJobOffer() {
        if (!validateInputs()) {
            return;
        }

        Job job = new Job(
                edJobTitle.getText().toString(),
                edCompany.getText().toString(),
                edLocation.getText().toString(),
                Integer.parseInt(edCostOfLiving.getText().toString()),
                new BigDecimal(edYearlySalary.getText().toString()),
                new BigDecimal(edYearlyBonus.getText().toString()),
                new BigDecimal(edTuition.getText().toString()),
                // fix xml here
                new BigDecimal(edInsurance.getText().toString()),
                new BigDecimal(edDiscount.getText().toString()),
                new BigDecimal(edAdoption.getText().toString()));
        job.setIsCurrentJob(false);
        user.setJobOffer(job);
        Toast.makeText(this, "Successfully saved the job offer!", Toast.LENGTH_SHORT).show();
        nextOption();
    }

    private void nextOption() {
        new AlertDialog.Builder(this).setMessage("What you like to do next?")
                .setPositiveButton("Add another Job Offer", (dialog, which) -> {
                    empty();
                })
                .setNeutralButton("Return to main menu", (dialog, which) -> {
                    setResult(RESULT_OK);
                    finish();
                })

                .setNegativeButton("Compare the job offer with the current job details", (dialog, which) -> {
                    if (user.hasCurrentJob()) {
                        Intent intent = new Intent(this, CompareJobOffers.class);
                        startActivityForResult(intent, 0);
                    } else {
                        Toast.makeText(this, "Please add current job first", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void empty() {
        edJobTitle.setText("");
        edCompany.setText("");
        edLocation.setText("");
        edCostOfLiving.setText("");
        edYearlyBonus.setText("");
        edYearlySalary.setText("");
        edTuition.setText("");
        edInsurance.setText("");
        edDiscount.setText("");
        edAdoption.setText("");
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
        if (edJobTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edCompany.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edLocation.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edCostOfLiving.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edYearlySalary.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edYearlyBonus.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edTuition.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edInsurance.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edDiscount.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edAdoption.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "At least one field is left empty", Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            BigDecimal salary = new BigDecimal(edYearlySalary.getText().toString());
            BigDecimal tuition = new BigDecimal(edTuition.getText().toString());
            BigDecimal insuranceBase = new BigDecimal(edInsurance.getText().toString());
            BigDecimal discount = new BigDecimal(edDiscount.getText().toString());
            BigDecimal adoption = new BigDecimal(edAdoption.getText().toString());

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
