package edu.gatech.seclass.jobcompare6300;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ManageSettings extends AppCompatActivity {
    private EditText salaryInput;
    private EditText bonusInput;
    private EditText tuitionInput;
    private EditText healthInput;
    private EditText employeeInput;
    private EditText adoptionInput;
    private Settings comparisonSettings;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_settings);

        // Sarv, this should give this class access to DB actions like saving settings
        user = new User(this);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Comparison Settings");

        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
        loadExistingSettings();
    }
    private void loadExistingSettings() {
        salaryInput = (EditText) findViewById(R.id.salaryInputID);
        bonusInput = (EditText) findViewById(R.id.bonusInputID);
        tuitionInput = (EditText) findViewById(R.id.tuitionInputID);
        healthInput = (EditText) findViewById(R.id.healthInputID);
        employeeInput = (EditText) findViewById(R.id.employeeInputID);
        adoptionInput = (EditText) findViewById(R.id.adoptionInputID);

        salaryInput.setText(Integer.toString(user.getSettings().getSalaryWeight()));
        bonusInput.setText(Integer.toString(user.getSettings().getBonusWeight()));
        tuitionInput.setText(Integer.toString(user.getSettings().getTuitionWeight()));
        healthInput.setText(Integer.toString(user.getSettings().getHealthWeight()));
        employeeInput.setText(Integer.toString(user.getSettings().getEmployeeWeight()));
        adoptionInput.setText(Integer.toString(user.getSettings().getAdoptionWeight()));
    }

    // Set up toolbar back button action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Settings loadSettingsFromDatabase() {
        comparisonSettings = user.getSettings();

        if (comparisonSettings == null) {
            // If no settings exist in DB, create new default settings
            comparisonSettings = new Settings();
            return comparisonSettings;
        }

        // Populate EditText fields with existing values
        salaryInput.setText(String.valueOf(comparisonSettings.getSalaryWeight()));
        bonusInput.setText(String.valueOf(comparisonSettings.getBonusWeight()));
        tuitionInput.setText(String.valueOf(comparisonSettings.getTuitionWeight()));
        healthInput.setText(String.valueOf(comparisonSettings.getHealthWeight()));
        employeeInput.setText(String.valueOf(comparisonSettings.getEmployeeWeight()));
        adoptionInput.setText(String.valueOf(comparisonSettings.getAdoptionWeight()));

        return comparisonSettings;
    }
    public void handleEditSettings(View view) {
        //create new settings to store any user input
        Log.d("ManageSettings", "entered EditSettings func");
        Settings newSettings = user.getSettings();

        if (!TextUtils.isEmpty(salaryInput.getText().toString())) {
            newSettings.setSalaryWeight(Integer.parseInt(salaryInput.getText().toString()));
        }
        if (!TextUtils.isEmpty(bonusInput.getText().toString())) {
            newSettings.setBonusWeight(Integer.parseInt(bonusInput.getText().toString()));
        }
        if (!TextUtils.isEmpty(tuitionInput.getText().toString())) {
            newSettings.setTuitionWeight(Integer.parseInt(tuitionInput.getText().toString()));
        }
        if (!TextUtils.isEmpty(healthInput.getText().toString())) {
            newSettings.setHealthWeight(Integer.parseInt(healthInput.getText().toString()));
        }
        if (!TextUtils.isEmpty(employeeInput.getText().toString())) {
            newSettings.setEmployeeWeight(Integer.parseInt(employeeInput.getText().toString()));
        }
        if (!TextUtils.isEmpty(adoptionInput.getText().toString())) {
            newSettings.setAdoptionWeight(Integer.parseInt(adoptionInput.getText().toString()));
        }

        //save new settings into user
        user.saveSettings(newSettings);
        Log.d("ManageSettings", "exiting EditSettings func");
        Toast.makeText(this, "Successfully saved comparison settings!", Toast.LENGTH_SHORT).show();
        finish();

    }
}
