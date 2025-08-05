package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Optional;

// This application relies on fragments and appCompatActivities
// to handle different "screens"
public class MainMenuFragment extends Fragment {
    private User user;
    private static final int REQUEST_CODE_ADD_CURRENT_JOB = 1;
    private static final int REQUEST_CODE_ADD_JOB_OFFER = 2;
    private static final int REQUEST_CODE_COMPARE_JOBS = 3;
    private static final int REQUEST_CODE_EDIT_SETTINGS = 4;

    private TextView txtCurrentJobStatus;
    private TextView txtJobOffersStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_fragment, container, false);

        // Initialize the User with context for database access
        user = new User(requireContext());

        // Find our TextViews
//         txtCurrentJobStatus = view.findViewById(R.id.txtCurrentJobStatus);
//         txtJobOffersStatus = view.findViewById(R.id.txtJobOffersStatus);

        Button btnForm1 = view.findViewById(R.id.addEditCurrentJob);
        Button btnForm2 = view.findViewById(R.id.addJobOffers);
        Button btnForm3 = view.findViewById(R.id.compareEditJobOffers);
        Button btnForm4 = view.findViewById(R.id.editSettings);

        btnForm1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddCurrentJob.class);
            intent.putExtra("user", user);
            startActivityForResult(intent, REQUEST_CODE_ADD_CURRENT_JOB);
        });
        btnForm2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddJobOffers.class);
            intent.putExtra("user", user);
            startActivityForResult(intent, REQUEST_CODE_ADD_JOB_OFFER);
        });
        btnForm3.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CompareJobOffers.class);
            intent.putExtra("user", user);
            startActivityForResult(intent, REQUEST_CODE_COMPARE_JOBS);
        });
        btnForm4.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ManageSettings.class);
            intent.putExtra("user", user);
            startActivityForResult(intent, REQUEST_CODE_EDIT_SETTINGS);
        });

        // disables compare button if < 2 jobs total (current job + # job offers)
        int currentJobExists = user.hasCurrentJob() ? 1 : 0;
        if (user.getJobOffers().size() + currentJobExists < 2) {
            btnForm3.setEnabled(false);
        } else {
            btnForm3.setEnabled(true);
        }

        // btnForm2.setOnClickListener(v -> startActivity(new Intent(getActivity(),
        // AddJobOffers.class)));
        // btnForm3.setOnClickListener(v -> startActivity(new Intent(getActivity(),
        // CompareJobOffers.class)));
        // btnForm4.setOnClickListener(v -> startActivity(new Intent(getActivity(),
        // ManageSettings.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

//        Log.i("MainMenuFragment", "onResume RAN");

        if (user != null) {
            user.close();
        }
        
        user = new User(requireContext());

        updateJobInformation();
        updateButtonStates();
    }

    private void updateJobInformation() {
        try {
            updateCurrentJobStatus();

            updateJobOffersStatus();
        } catch (Exception e) {
            Log.e("tag", "Error updating job information", e);
        }
    }

    private void updateCurrentJobStatus() {
        try {
            if (user.hasCurrentJob()) {
                Optional<Job> currentJobOptional = user.getCurrentJob();

                if (currentJobOptional.isPresent()) {
                    Job currentJob = currentJobOptional.get();
                    String jobInfo = String.format("%s at %s\nSalary: %s, Location: %s",
                            currentJob.getTitle(),
                            currentJob.getCompany(),
                            currentJob.getYearlySalary().toString(),
                            currentJob.getLocation());
                    txtCurrentJobStatus.setText(jobInfo);
                } else {
                    txtCurrentJobStatus.setText("No details available");
                }
            } else {
                txtCurrentJobStatus.setText("No current job found");
            }
        } catch (Exception e) {
            Log.e("tag", "Error updating current job status", e);
            txtCurrentJobStatus.setText("Error loading current job");
        }
    }

    private void updateJobOffersStatus() {
        try {
            ArrayList<Job> jobOffers = user.getJobOffers();

            Log.d("tag", "Job offers length: " + jobOffers.size());

            if (jobOffers != null && !jobOffers.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append(jobOffers.size()).append(" job offer(s) found\n");

                txtJobOffersStatus.setText(sb.toString());
            } else {
                txtJobOffersStatus.setText("No job offers found");
            }
        } catch (Exception e) {
            Log.e("tag", "Error updating job offers status", e);
            txtJobOffersStatus.setText("Error loading job offers");
        }
    }

    private void updateButtonStates() {
        try {
            Button compareButton = getView().findViewById(R.id.compareEditJobOffers);
            Button currentJobButton = getView().findViewById(R.id.addEditCurrentJob);
            
            if (compareButton != null) {
                boolean hasJobOffers = user.getJobOffers() != null && !user.getJobOffers().isEmpty();
                compareButton.setEnabled(hasJobOffers);
            }
            
            if (currentJobButton != null) {
                String buttonText = user.hasCurrentJob() ? "Edit Current Job" : "Add Current Job";
                currentJobButton.setText(buttonText);
            }
            
        } catch (Exception e) {
            Log.e("MainMenuFragment", "Error updating button states", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_ADD_CURRENT_JOB:
                    user = (User) data.getSerializableExtra("user");
                    break;
                case REQUEST_CODE_ADD_JOB_OFFER:
                    user = (User) data.getSerializableExtra("user");
                    break;
                case REQUEST_CODE_COMPARE_JOBS:
                    user = (User) data.getSerializableExtra("user");
                    break;
                case REQUEST_CODE_EDIT_SETTINGS:
                    user = (User) data.getSerializableExtra("user");
                    break;
            }
        }
    }

}