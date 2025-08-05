package edu.gatech.seclass.jobcompare6300;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<Job> jobs;
    private List<Job> selectedJobs = new ArrayList<>();
    private final int MAX_SELECTED = 2;
    private OnJobSelectionChangedListener listener;

    public interface OnJobSelectionChangedListener {
        void onSelectionChanged(List<Job> selectedJobs);
    }

    public JobAdapter(List<Job> jobs, OnJobSelectionChangedListener listener) {
        this.jobs = jobs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_card, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobs.get(position);
        holder.bind(job);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public List<Job> getSelectedJobs() {
        return selectedJobs;
    }



    class JobViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewJobTitle, textViewCompany, textViewLocation, textViewCostOfLiving;
        private TextView textViewSalary, textViewBonus, textViewTuitionAssistance, textViewInsurance;
        private TextView textViewEmployeeDiscount, textViewAdoptionAssistance, textViewJobScore;
        private CheckBox cbSelectJob;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewJobTitle = itemView.findViewById(R.id.jobTitle);
            textViewCompany = itemView.findViewById(R.id.company);
            textViewLocation = itemView.findViewById(R.id.location);
            textViewCostOfLiving = itemView.findViewById(R.id.costOfLiving);
            textViewSalary = itemView.findViewById(R.id.salary);
            textViewBonus = itemView.findViewById(R.id.bonus);
            textViewTuitionAssistance = itemView.findViewById(R.id.tuitionAssistance);
            textViewInsurance = itemView.findViewById(R.id.insurance);
            textViewEmployeeDiscount = itemView.findViewById(R.id.employeeDiscount);
            textViewAdoptionAssistance = itemView.findViewById(R.id.adoptionAssistance);
            textViewJobScore = itemView.findViewById(R.id.jobScore);
            cbSelectJob = itemView.findViewById(R.id.cbSelectJob);
        }

        void bind(Job job) {
            // fill out job title
            String jobTitle = job.getTitle();
            if (jobTitle != null && !jobTitle.isEmpty()) {
                textViewJobTitle.setText(jobTitle);
                textViewJobTitle.setVisibility(View.VISIBLE);
            } else {
                textViewJobTitle.setText("No Job Title Available");
                textViewJobTitle.setVisibility(View.VISIBLE);
            }

            textViewCompany.setText(job.getCompany());
            textViewLocation.setText(job.getLocation());
            textViewCostOfLiving.setText("Cost of Living: " + job.getCostOfLiving());

            try {
                Object salaryObj = job.getYearlySalary();

                if (salaryObj instanceof java.math.BigDecimal) {
                    java.math.BigDecimal salaryBD = (java.math.BigDecimal) salaryObj;
                    java.text.NumberFormat formatter = java.text.NumberFormat.getCurrencyInstance();
                    textViewSalary.setText(formatter.format(salaryBD.doubleValue()));
                } else {
                    textViewSalary.setText("$0");
                }
            } catch (Exception e) {
                System.out.println("Error formatting salary: " + e.getMessage());
                textViewSalary.setText("$0");
            }

            // Handle the additional fields
            textViewTuitionAssistance.setText(job.getTuitionAssistance().toString());
            textViewInsurance.setText(job.getInsurance().toString());
            textViewEmployeeDiscount.setText(job.getEmployeeDiscount().toString());
            textViewAdoptionAssistance.setText(job.getAdoptionAssistance().toString());

            // Job score
            textViewJobScore.setText(String.format(Locale.US, "%.1f", job.getJobScore()));

            // Highlight current job if applicable
            if (job.isCurrentJob()) {
                textViewJobTitle.setTextColor(Color.parseColor("#2196F3"));  // Blue color
                textViewCompany.setText(textViewCompany.getText() + " (Current)");
            } else {
                textViewJobTitle.setTextColor(Color.BLACK);
            }

            cbSelectJob.setChecked(selectedJobs.contains(job));
            cbSelectJob.setOnClickListener(v -> {
                if (cbSelectJob.isChecked()) {
                    if (selectedJobs.size() >= MAX_SELECTED) {
                        cbSelectJob.setChecked(false);
                    } else {
                        selectedJobs.add(job);
                    }
                } else {
                    selectedJobs.remove(job);
                }
                listener.onSelectionChanged(selectedJobs);
            });
        }
    }
}