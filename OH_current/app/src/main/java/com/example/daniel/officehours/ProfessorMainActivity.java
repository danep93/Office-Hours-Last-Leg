package com.example.daniel.officehours;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class ProfessorMainActivity extends AppCompatActivity implements View.OnClickListener, WeekView.EventClickListener, WeekView.EventLongPressListener {

    EditText editTextName;
    TextView textViewName;
    EditText editTextPassword;
    EditText editTextEmail;
    Button bLogout;
    Button bAddHours;
    UserLocalStore userLocalStore;
    Toolbar toolbar;
    ImageButton bPlus;
    SuccessfulRetrieval[] response;

    WeekView mWeekView;
    WeekView.EventClickListener mEventClickListener;
    //    MonthLoader.MonthChangeListener mMonthChangeListener;
    WeekView.EventLongPressListener mEventLongPressListener;
    ArrayList<WeekViewEvent> mEvents;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.bPlus:
                startActivity(new Intent(this, ClassCreator.class));
                break;
            case R.id.bPAddHours:
                startActivity(new Intent(this, AddHours.class));
                break;
        }
    }


    @Override
    protected void onStart() {
        //when this opens we need to authenticate user to make sure he's logged in
        super.onStart();
        if (authenticate()) {
            displayUserDetails();
        } else {
            startActivity(new Intent(this, Login.class));
        }
    }

    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();
        editTextName.setText(user.name);
        //editTextName.setText(user.name);
    }

    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_activity_main);



        editTextName = (EditText) findViewById(R.id.etName);
        //textViewName = (TextView)findViewById(R.id.tvName);
        //textViewName.setText(UserLocalStore.SP_NAME);
        bLogout = (Button) findViewById(R.id.bLogout);
        bAddHours = (Button) findViewById(R.id.bPAddHours);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bPlus = (ImageButton) findViewById(R.id.bPlus);
        userLocalStore = new UserLocalStore(this);

        ProfessorMainTask task = new ProfessorMainTask();
        task.execute();

        bPlus.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        bAddHours.setOnClickListener(this);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(mEventClickListener);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(mMonthChangeListener);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(mEventLongPressListener);

        mEvents = new ArrayList<WeekViewEvent>();

    }

    public List<WeekViewEvent> getEvents(int newYear, int newMonth) {
        ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
//        WeekViewEvent event1 = new WeekViewEvent(0, "why three", 2016, 05, 16, 1, 00, 2016, 05, 16, 2, 00);
//        events.add(event1);
//        WeekViewEvent event2 = new WeekViewEvent(1, "Event 2", 2016, 05, 15, 9, 00, 2016, 05, 15, 10, 00);
//        events.add(event2);
//        WeekViewEvent event3 = new WeekViewEvent(2, "Event 3", 2016, 05, 14, 6, 00, 2016, 05, 14, 7, 00);
//        events.add(event3);
        int currentDayOfWeek = Calendar.DAY_OF_WEEK;        //Sunday starts at 0
        int difference;

        for (int i = 0; i < response.length; i++) {
            String code = response[i].getCode();
            String name = response[i].getFirstName();
            String recurring = response[i].getRecurring();
            int dayOfWeek = dayOfWeekToInt(response[i].getDayOfWeek());
            difference = dayOfWeek - currentDayOfWeek;
            if (difference < 0) {   //If the day of the week already passed, push it to next week.
                difference += 7;
            }
            int year = Calendar.YEAR;
            int month = Calendar.MONTH;
            int day = Calendar.DAY_OF_MONTH + difference;
            int startHour = Integer.parseInt(response[i].getStartHour());
            int startMinute = Integer.parseInt(response[i].getStartMinute());
            int endHour = Integer.parseInt(response[i].getEndHour());
            int endMinute = Integer.parseInt(response[i].getEndMinute());

            Calendar mycal = new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
            int max = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

            while (day < max) {
                WeekViewEvent event = new WeekViewEvent(0, code + " " + name, year, month, day,
                        startHour, startMinute, year, month, day, endHour, endMinute);
                events.add(event);
                if (recurring.equals("yes")) {
                    day = max;
                } else {
                    day += 7;   //Next week
                }
            }

        }
        return events;
    }

    public int dayOfWeekToInt(String day) {
        if (day.equals("Monday")) {         //Sunday starts at 0
            return 1;
        } else if (day.equals("Tuesday")) {
            return 2;
        } else if (day.equals("Wednesday")) {
            return 3;
        } else if (day.equals("Thursday")) {
            return 4;
        } else {
            return 5;
        }
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            // Populate the week view with some events.
            List<WeekViewEvent> events = getEvents(newYear, newMonth);
            return events;
        }
    };

//    @Override
//    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
//        // Populate the week view with some events.
//        List<WeekViewEvent> events = getEvents(newYear, newMonth);
//        return events;
//    }


    class ProfessorMainTask extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        DBConnectionClass luc = new DBConnectionClass();
        String dbResponse = "";
        Gson gson = new Gson();
        User user;

        @Override
        protected void onPreExecute() {
            Log.d("preExecute", "here");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<String> classCodes = userLocalStore.getLoggedInUser().getClassList();
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("CMSC136", "CMSC136");
//            for (String classCode : classCodes) {
//                System.out.println("Classes " + classCode);
//                data.put(classCode, classCode);
//            }

            dbResponse = luc.sendPostRequest(URL.RETRIEVE_HOURS_URL, data);
            return dbResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            if (dbResponse != null) {
                response = gson.fromJson(dbResponse, SuccessfulRetrieval[].class);
            } else {
                Log.d("bad", "response was null, check connection");
            }
        }
    }
}