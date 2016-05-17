package com.example.daniel.officehours;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.*;

public class AddHours extends AppCompatActivity implements View.OnClickListener{
    EditText etClassCode;
    EditText etStartHours;
    EditText etStartMinutes;
    EditText etEndHours;
    EditText etEndMinutes;
    CheckBox rbRecurring;
    Spinner startSpinner;
    Spinner endSpinner;
    Spinner dayOfWeekSpinner;
    Button bCreateHours;
    UserLocalStore userLocalStore;
    String firstName;
    String lastName;
    int startHours;
    int startMinutes;
    int endHours;
    int endMinutes;
    String classCode;
    String dayOfWeek;
    String startAmpm;
    String endAmpm;
    String recurring;

    @Override
    public void onClick(View v) {
        User user = userLocalStore.getLoggedInUser();
        firstName = user.name;
        classCode = etClassCode.getText().toString().trim();

        startHours = Integer.parseInt(etStartHours.getText().toString().trim());
        startMinutes = Integer.parseInt(etStartMinutes.getText().toString().trim());
        startAmpm = startSpinner.getSelectedItem().toString().trim();
        dayOfWeek = dayOfWeekSpinner.getSelectedItem().toString().trim();

        if(rbRecurring.isChecked()) {
            recurring = "yes";
        } else {
            recurring = "no";
        }

        if (startAmpm.equals("AM") && startHours == 12) {
            startHours = 0;
        } else if (startAmpm.equals("PM") && startHours != 12) {
            startHours += 12;
        }


        endHours = Integer.parseInt(etEndHours.getText().toString().trim());
        endMinutes = Integer.parseInt(etEndMinutes.getText().toString().trim());
        endAmpm = endSpinner.getSelectedItem().toString();
        if (endAmpm.equals("AM") && endHours == 12) {
            endHours = 0;
        } else if (endAmpm.equals("PM") && endHours != 12) {
            endHours += 12;
        }

        switch(v.getId()){
            case R.id.bCreateHours:
                AddHoursTask task = new AddHoursTask();
                task.execute();
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hours);
        etClassCode = (EditText) findViewById(R.id.etCode);
        etStartHours = (EditText) findViewById(R.id.etStartHours);
        etStartMinutes = (EditText) findViewById(R.id.etStartMinutes);
        etEndHours = (EditText) findViewById(R.id.etEndHours);
        etEndMinutes = (EditText) findViewById(R.id.etStartMinutes);

        startSpinner = (Spinner) findViewById(R.id.spStartAMPM);
        endSpinner = (Spinner) findViewById(R.id.spEndAMPM);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.AM_PM, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        startSpinner.setAdapter(adapter);
        endSpinner.setAdapter(adapter);

        dayOfWeekSpinner = (Spinner) findViewById(R.id.spDayOfWeek);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.DayOfWeek, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayOfWeekSpinner.setAdapter(adapter2);

        rbRecurring = (CheckBox) findViewById(R.id.rbRecurring);

        bCreateHours = (Button) findViewById(R.id.bCreateHours);
        bCreateHours.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    /**ASYNC TASK**/

    class AddHoursTask extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        DBConnectionClass luc = new DBConnectionClass();
        String dbResponse = "";
        Gson gson = new Gson();

        @Override
        protected void onPreExecute() {
            Log.d("preExecute", "here");
            super.onPreExecute();
            loading = ProgressDialog.show(AddHours.this, "Adding Hours", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<String,String>();
            data.put("firstName", firstName);
            data.put("code", classCode);
            data.put("dayOfWeek", dayOfWeek);
            data.put("startHour", String.valueOf(startHours));
            data.put("startMinute", String.valueOf(startMinutes));
            data.put("endHour", String.valueOf(endHours));
            data.put("endMinute", String.valueOf(endMinutes));
            data.put("recurring", recurring);
            dbResponse = luc.sendPostRequest(URL.ADD_HOURS_URL,data);
            return dbResponse;
        }
        @Override
        protected void onPostExecute(String s) {
            if(dbResponse != null){
                Successful response = gson.fromJson(dbResponse, Successful.class);
                if(response.getSuccess()){
                    Toast.makeText(getApplicationContext(), "Hours Successfully Added", Toast.LENGTH_SHORT).show();
                    Log.d("reach", "in post execute");
                    Log.d("user name",userLocalStore.getLoggedInName());
                    ArrayList<String> classList = userLocalStore.getLoggedInUser().getClassList();
                    for(String course : classList){
                        Log.d("class",course);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error: Office Hours could not be created", Toast.LENGTH_SHORT).show();
                }
                User user = userLocalStore.getLoggedInUser();
                    switch(user.userType) {
                    case 2:
                        Intent in = new Intent(AddHours.this, TAMainActivity.class);
                        startActivity(in);
                        break;
                    case 3:
                        Intent in2 = new Intent(AddHours.this, ProfessorMainActivity.class);
                        startActivity(in2);
                        break;
                }


            }
            else{
                Log.d("bad","response was null, check connection");
            }
        }
    }
}
