package com.example.gestioncalculateur;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SecondActivity extends AppCompatActivity {

    TextView txtEmployeeName, txtEmployeeAge, txtEmployeeExperience, txtexp;
    Button btnExp, btnCalculateSalary;
    EditText editTextStartDate, editTextEndDate,edtMeanPaymentLast5Years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Getting the intent from 1st activity
        String fullName = getIntent().getStringExtra("fullName");
        String dateOfBirth = getIntent().getStringExtra("dateOfbirth");

        // Setting variables to view
        txtEmployeeAge = findViewById(R.id.textViewForAge);
        txtEmployeeName = findViewById(R.id.textViewforName);
        edtMeanPaymentLast5Years = findViewById(R.id.editTextJobFiveSalary);
        txtEmployeeExperience = findViewById(R.id.textViewForExp);
        txtexp = findViewById(R.id.textViewEXP);
        btnCalculateSalary = findViewById(R.id.buttonRetirement);
        btnExp = findViewById(R.id.buttonExp);
        editTextStartDate = findViewById(R.id.editTextJobEntreeDate);
        editTextEndDate = findViewById(R.id.editTextJobEndDate);

        // Setting the employee name
        txtEmployeeName.setText("Mr. " + fullName);
        txtEmployeeAge.setText(String.valueOf(calculateAge(dateOfBirth)));


        // Setting onClickListener for editTextStartDate to show calendar view
        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextStartDate);
            }
        });

        // Setting onClickListener for editTextEndDate to show calendar view
        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextEndDate);
            }
        });


        /************  Buttons Code Opearations     ***********/
        // Button click listener for calculating the difference between dates
        btnExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDateString = editTextStartDate.getText().toString();
                String endDateString = editTextEndDate.getText().toString();

                if (!startDateString.isEmpty() && !endDateString.isEmpty()) {
                    try {
                        // Parse start and end dates
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date startDate = dateFormat.parse(startDateString);
                        Date endDate = dateFormat.parse(endDateString);

                        // Calculate difference
                        long differenceMillis = endDate.getTime() - startDate.getTime();
                        int years = (int) (differenceMillis / (1000L * 60 * 60 * 24 * 365));
                        int months = (int) ((differenceMillis % (1000L * 60 * 60 * 24 * 365)) / (1000L * 60 * 60 * 24 * 30.44));
                        int days = (int) ((differenceMillis % (1000L * 60 * 60 * 24 * 365)) % (1000L * 60 * 60 * 24 * 30.44) / (1000L * 60 * 60 * 24));

                        // Display the difference
                        txtexp.setVisibility(View.VISIBLE);
                        txtEmployeeExperience.setVisibility(View.VISIBLE);
                        txtEmployeeExperience.setText(years + " years, " + months + " months, " + days + " days");

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SecondActivity.this, "Please select both start and end dates", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCalculateSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read the mean payment of the last 5 years from the EditText
                String meanPaymentText = edtMeanPaymentLast5Years.getText().toString();

                if (!meanPaymentText.isEmpty()) {
                    try {
                        double meanPaymentLast5Years = Double.parseDouble(meanPaymentText);
                        double retirementSalary = calculateRetirementSalary(meanPaymentLast5Years);
                        Intent intent = new Intent();
                        intent.putExtra("retirementSalary", retirementSalary);
                        setResult(RESULT_OK, intent);
                        //startActivity(intent);
                        finish();
                    } catch (NumberFormatException e) {
                        Toast.makeText(SecondActivity.this, "Invalid input for mean payment", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SecondActivity.this, "Please enter the mean payment of the last 5 years", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to show DatePickerDialog
    private void showDatePickerDialog(final EditText editText) {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(SecondActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display selected date in EditText
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        editText.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Show the dialog
        datePickerDialog.show();
    }

    /// calculating age
    private int calculateAge(String dobString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dob = dateFormat.parse(dobString);
            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(dob);

            Calendar now = Calendar.getInstance();
            int age = now.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    //////// calculating retirement
    private double calculateRetirementSalary(double meanPaymentLast5Years) {
        double retirementSalaryFactor = 0.8;
        return meanPaymentLast5Years * retirementSalaryFactor;
    }
}
