package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.math.BigDecimal;
import java.util.Locale;

public class JobComparisonDetail extends AppCompatActivity {
    // internal values for job selections
    private Job job1;
    private Job job2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_comparison_detail);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Job Comparison");

        // Get the jobs from the intent
        if (getIntent() != null) {
            try {
                job1 = (Job) getIntent().getSerializableExtra("job1");
                job2 = (Job) getIntent().getSerializableExtra("job2");

                if (job1 != null && job2 != null) {
                    displayJobComparison();
                } else {
                    Toast.makeText(this, "Error: Job data missing", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error loading job data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void displayJobComparison() {
        try {
            // Set job titles in table header
            TextView tvJob1Title = findViewById(R.id.job1Title);
            TextView tvJob2Title = findViewById(R.id.job2Title);
            tvJob1Title.setText(job1.getTitle());
            tvJob2Title.setText(job2.getTitle());

            // Company
            TextView tvJob1Company = findViewById(R.id.job1Company);
            TextView tvJob2Company = findViewById(R.id.job2Company);
            tvJob1Company.setText(job1.getCompany());
            tvJob2Company.setText(job2.getCompany());

            // Location
            TextView tvJob1Location = findViewById(R.id.job1Location);
            TextView tvJob2Location = findViewById(R.id.job2Location);
            tvJob1Location.setText(job1.getLocation());
            tvJob2Location.setText(job2.getLocation());
            
            // Salary
            TextView tvJob1Salary = findViewById(R.id.job1Salary);
            TextView tvJob2Salary = findViewById(R.id.job2Salary);
            BigDecimal j1AdjSalary = job1.getYearlySalary().divide(BigDecimal.valueOf(job1.getCostOfLiving()), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal j2AdjSalary = job2.getYearlySalary().divide(BigDecimal.valueOf(job2.getCostOfLiving()), 2, BigDecimal.ROUND_HALF_UP);

            tvJob1Salary.setText(String.format(Locale.US, "$%,.0f", j1AdjSalary));
            tvJob2Salary.setText(String.format(Locale.US, "$%,.0f", j2AdjSalary));

            // Bonus
            TextView tvJob1Bonus = findViewById(R.id.job1Bonus);
            TextView tvJob2Bonus = findViewById(R.id.job2Bonus);

            BigDecimal j1AdjBonus = job1.getYearlyBonus().divide(BigDecimal.valueOf(job1.getCostOfLiving()), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal j2AdjBonus = job2.getYearlyBonus().divide(BigDecimal.valueOf(job2.getCostOfLiving()), 2, BigDecimal.ROUND_HALF_UP);

            tvJob1Bonus.setText(String.format(Locale.US, "$%,.0f", j1AdjBonus));
            tvJob2Bonus.setText(String.format(Locale.US, "$%,.0f", j2AdjBonus));

            // tuition
            TextView tvJob1Tuition = findViewById(R.id.job1Tuition);
            TextView tvJob2Tuition = findViewById(R.id.job2Tuition);

            tvJob1Tuition.setText(String.format(Locale.US, "$%,.0f", job1.getTuitionAssistance()));
            tvJob2Tuition.setText(String.format(Locale.US, "$%,.0f", job2.getTuitionAssistance()));

            // health insurance
            TextView tvJob1Insurance = findViewById(R.id.job1Insurance);
            TextView tvJob2Insurance = findViewById(R.id.job2Insurance);

            tvJob1Insurance.setText(String.format(Locale.US, "$%,.0f", job1.getInsurance()));
            tvJob2Insurance.setText(String.format(Locale.US, "$%,.0f", job2.getInsurance()));

            // employee discount
            TextView tvJob1Discount = findViewById(R.id.job1Discount);
            TextView tvJob2Discount = findViewById(R.id.job2Discount);

            tvJob1Discount.setText(String.format(Locale.US, "$%,.0f", job1.getEmployeeDiscount()));
            tvJob2Discount.setText(String.format(Locale.US, "$%,.0f", job2.getEmployeeDiscount()));

            // adoption assistance
            TextView tvJob1Assist = findViewById(R.id.job1Assist);
            TextView tvJob2Assist = findViewById(R.id.job2Assist);

            tvJob1Assist.setText(String.format(Locale.US, "$%,.0f", job1.getAdoptionAssistance()));
            tvJob2Assist.setText(String.format(Locale.US, "$%,.0f", job2.getAdoptionAssistance()));

            // Job Scores
            TextView tvJob1Score = findViewById(R.id.job1Score);
            TextView tvJob2Score = findViewById(R.id.job2Score);
            tvJob1Score.setText(String.format(Locale.US, "%.1f", job1.getJobScore()));
            tvJob2Score.setText(String.format(Locale.US, "%.1f", job2.getJobScore()));

            // Display the winner
            TextView tvComparisonResult = findViewById(R.id.comparisonResult);
            if (job1.getJobScore() > job2.getJobScore()) {
                tvComparisonResult.setText(job1.getTitle() + " at " + job1.getCompany() + " has a higher score");
            } else if (job2.getJobScore() > job1.getJobScore()) {
                tvComparisonResult.setText(job2.getTitle() + " at " + job2.getCompany() + " has a higher score");
            } else {
                tvComparisonResult.setText("Both jobs have equal scores");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error displaying comparison: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}