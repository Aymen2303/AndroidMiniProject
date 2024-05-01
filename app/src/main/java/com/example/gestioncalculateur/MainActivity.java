package com.example.gestioncalculateur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtPhone, edtDateBirth;
    Button btnGonext, btnSendInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permission to send SMS
        if (checkSelfPermission(android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.SEND_SMS}, 1);
        }

        /********   Setting to layouts    *********/
        edtName = findViewById(R.id.editTextFullName);
        edtPhone =  findViewById(R.id.editTextPhoneNumber);
        edtDateBirth =  findViewById(R.id.editTextBirth);
        btnGonext =  findViewById(R.id.buttonNext);
        btnSendInfos =  findViewById(R.id.buttonSms);


        //double retirementSalary = getIntent().getDoubleExtra("retirementSalary", 0.0);
        double retirementSalary = 3265.32;

        /*********    Using buttons  ********/
        btnGonext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isItEmpty()){
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra("fullName", edtName.getText().toString());
                    intent.putExtra("dateOfbirth", edtDateBirth.getText().toString());
                    startActivity(intent);
                }
                else Toast.makeText(MainActivity.this, "Please Fill in all the fields", Toast.LENGTH_LONG).show();
            }
        });

        btnSendInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edtName.getText().toString();
                String phoneNumber = edtPhone.getText().toString();

                if (!name.isEmpty() && retirementSalary != 0.0) {
                    String message = "Dear Mr. " + name + ", born on " + edtDateBirth.getText().toString() +
                            ", your retirement salary is: " + retirementSalary;

                    // Use SmsManager to send the SMS message
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);

                    Toast.makeText(MainActivity.this, "SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Please fill in all the fields and calculate the retirement salary first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*****  showing date picker dialog when clicking on age****/
        edtDateBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


    }
    /************   End of OnCreate Method  **********/

    ///void to make fields empty after usages
    public void setToNull(){
        edtName.setText("");
        edtPhone.setText("");
        edtDateBirth.setText("");
    }

    //// boolean to check if fields are empty
    public boolean isItEmpty(){
        return !edtName.getText().toString().isEmpty() &&
                !edtDateBirth.getText().toString().isEmpty() &&
                !edtPhone.getText().toString().isEmpty() ;
    }

    ////void to get the edit text as a calender view
    private void showDatePickerDialog() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display selected date in EditText
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        edtDateBirth.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Show the dialog
        datePickerDialog.show();
    }

}
