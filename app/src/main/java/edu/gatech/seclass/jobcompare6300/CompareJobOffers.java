package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CompareJobOffers extends AppCompatActivity implements JobAdapter.OnJobSelectionChangedListener {
    private RecyclerView rvJobs;
    private JobAdapter jobAdapter;
    private Button btnCompare;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_job_offers);

        user = new User(this);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Compare Job Offers");

        rvJobs = findViewById(R.id.rvJobs);
        btnCompare = findViewById(R.id.btnCompare);
        btnCompare.setEnabled(false); // Initially disabled until 2 jobs are selected

        rvJobs.setLayoutManager(new LinearLayoutManager(this));

//        List<Job> jobs = MockData.getMockJobOffers();
        List<Job> jobs = loadJobsFromDatabase();

        for (Job job : jobs) {
            job.setJobScore(calculateRank(job));
        }

        //List<Job> updatedJobs = jobs;
        //you have to insert the jobs into job calculator

        System.out.println("Jobs from data, size of jobs: " + jobs.size());

//        for (Job job : jobs) {
//            System.out.println("Job from MockData: " + job.getTitle() + " at " + job.getCompany());
//        }

        // used for testing the sort functionality
//        for (int i = 0; i < jobs.size(); i++) {
//            jobs.get(i).setJobScore(i + 1);
//        }

        // sort the jobs by rank before displaying
        Collections.sort(jobs, (j1, j2) -> {
            if (j1.getJobScore() > j2.getJobScore()) {
                return -1;
            } else if (j1.getJobScore() < j2.getJobScore()) {
                return 1;
            } else {
                return 0;
            }
        });
        jobAdapter = new JobAdapter(jobs, this);
        rvJobs.setAdapter(jobAdapter);

        btnCompare.setOnClickListener(v -> {
            List<Job> selectedJobs = jobAdapter.getSelectedJobs();
            if (selectedJobs.size() == 2) {
                Intent intent = new Intent(CompareJobOffers.this, JobComparisonDetail.class);
                intent.putExtra("job1", selectedJobs.get(0));
                intent.putExtra("job2", selectedJobs.get(1));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select exactly 2 jobs to compare",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // this shouldn't go here
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        // Clean up resources
//        if (user != null) {
//            try {
//                user.close();
//            } catch (Exception e) {
//                Log.e("CompareJobOffers", "Error closing user resources", e);
//            }
//        }
//    }

    private List<Job> loadJobsFromDatabase() {
        try {
            List<Job> allJobs = new ArrayList<>();

            if (user != null) {
                // add current job first if you can
                if (user.hasCurrentJob()) {
                    Job currentJob = user.getCurrentJob().get();
                    allJobs.add(currentJob); // Add current job at the beginning of the list
                    Log.d("loadJobsFromDatabase", "Added current job: " + currentJob.getTitle() + " at " + currentJob.getCompany());
                }

                // Then add all job offers
                ArrayList<Job> jobOffers = user.getJobOffers();
                if (jobOffers != null && !jobOffers.isEmpty()) {
                    allJobs.addAll(jobOffers);
                    Log.d("loadJobsFromDatabase", "Added " + jobOffers.size() + " job offers");
                }


                // Check if we got any jobs
                if (allJobs != null) {
                    Log.d("loadJobsFromDatabase", "Loaded " + allJobs.size() + " jobs from database");

                    if (allJobs.isEmpty()) {
                        Toast.makeText(this, "No job offers available to compare", Toast.LENGTH_LONG).show();
                    } else {
                        for (Job job : allJobs) {
                            Log.d("loadJobsFromDatabase", "Job from database: " + job.getTitle() + " at " + job.getCompany());
                        }
                    }

                    return allJobs;
                } else {
                    Log.e("loadJobsFromDatabase", "getJobOffers returned null");
                }
            } else {
                Log.e("loadJobsFromDatabase", "User object is null");
            }
        } catch (Exception e) {
            Log.e("loadJobsFromDatabase", "Error loading jobs: " + e.getMessage(), e);
        }

        // Return empty list as fallback
        Toast.makeText(this, "Could not load job offers", Toast.LENGTH_SHORT).show();
        return new ArrayList<>();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelectionChanged(List<Job> selectedJobs) {
        // Enable or disable compare button based on selection count
        btnCompare.setEnabled(selectedJobs.size() == 2);
    }
    private double calculateRank(Job job) {
        //List<Job> jobs = loadJobsFromDatabase();
        Settings Settings = user.getSettings();


        int weightSalary = (Settings != null)? Settings.getSalaryWeight() : 1;
        int weightBonus = (Settings != null)? Settings.getBonusWeight() : 1;
        int weightTuition = (Settings != null)? Settings.getTuitionWeight() : 1;
        int weightHealth = (Settings != null)? Settings.getHealthWeight() : 1;
        int weightDiscount = (Settings != null)? Settings.getEmployeeWeight() : 1;
        int weightAdoption = (Settings != null)? Settings.getAdoptionWeight() : 1;


        weightSalary = Math.max(0 , Math.min(9,weightSalary));
        weightBonus = Math.max(0 , Math.min(9,weightBonus));
        weightTuition = Math.max(0 , Math.min(9,weightTuition));
        weightHealth = Math.max(0 , Math.min(9,weightHealth));
        weightDiscount = Math.max(0 , Math.min(9,weightDiscount));
        weightAdoption = Math.max(0 , Math.min(9,weightAdoption));






        Integer total = weightSalary + weightBonus + weightTuition + weightHealth + weightDiscount + weightAdoption;
        if (total == 0) total = 6;

        BigDecimal weightAYS = BigDecimal.valueOf(weightSalary).divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal weightAYB = BigDecimal.valueOf(weightBonus).divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal weightTR = BigDecimal.valueOf(weightTuition).divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal weightHI = BigDecimal.valueOf(weightHealth).divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal weightEPSD = BigDecimal.valueOf(weightDiscount).divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal weightCAA = BigDecimal.valueOf(weightAdoption).divide(BigDecimal.valueOf(total), 2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal weightAYB = BigDecimal.valueOf(comparisonSettings.getBonusWeight() / total);
//        BigDecimal weightTR = BigDecimal.valueOf(comparisonSettings.getTuitionWeight() / total);
//        BigDecimal weightHI = BigDecimal.valueOf(comparisonSettings.getHealthWeight() / total);
//        BigDecimal weightEPSD = BigDecimal.valueOf(comparisonSettings.getEmployeeWeight() / total);
//        BigDecimal weightCAA = BigDecimal.valueOf(comparisonSettings.getAdoptionWeight() / total);

        // AYS = Yearly Salary Adjusted for cost of living
        BigDecimal ays = job.getYearlySalary().divide(BigDecimal.valueOf(job.getCostOfLiving()), 2, BigDecimal.ROUND_HALF_UP);
        // AYB = Yearly Bonus Adjusted for cost of living
        BigDecimal ayb = job.getYearlyBonus().divide(BigDecimal.valueOf(job.getCostOfLiving()), 2, BigDecimal.ROUND_HALF_UP);
        // TR = Tuition Reimbursement ($0 to $15,000 inclusive annually)
        // HI = Health Insurance ($0-$1,000 inclusive + 2% of AYS annually)
        // EPSD = Employee Product/Service Discount (dollar amount up to 18% of Yearly
        // Salary)
        // CAA = Child Adoption Assistance (expressed as a lump sum available over 5
        // years)
        BigDecimal caaPerYear = job.getAdoptionAssistance().divide(BigDecimal.valueOf(5), 5, BigDecimal.ROUND_HALF_UP);


//        BigDecimal weightedAYS = ays.multiply(weightAYS);
//        BigDecimal weightedAYB = ayb.multiply(weightAYB);
//        BigDecimal weightedTR = tuition.multiply(weightTR);
//        BigDecimal weightedHI = insurance.multiply(weightHI);
//        BigDecimal weightedEPSD = discount.multiply(weightEPSD);
//        BigDecimal weightedCAA = caaPerYear.multiply(weightCAA);
//
//        BigDecimal js = weightedAYS.add(weightedAYB).add(weightedTR).add(weightedHI).add(weightedEPSD).add(weightedCAA);


        BigDecimal js = BigDecimal.ZERO;
        js = js.add(ays.multiply(weightAYS));
        js = js.add(ayb.multiply(weightAYB));
        js = js.add(job.getTuitionAssistance().multiply(weightTR));
        js = js.add(job.getInsurance().multiply(weightHI));
        js = js.add(job.getEmployeeDiscount().multiply(weightEPSD));
        js = js.add(caaPerYear.multiply(weightCAA));

        return js.doubleValue();
    }
}