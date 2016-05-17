package com.example.daniel.officehours;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.List;

public class TAMainActivity extends AppCompatActivity implements View.OnClickListener, WeekView.EventClickListener, WeekView.EventLongPressListener{

    EditText editTextName;
    TextView textViewName;
    EditText editTextPassword;
    EditText editTextEmail;
    Button bLogout;
    Button bAddHours;
    Button bAddClass;
    UserLocalStore userLocalStore;
    Toolbar toolbar;

    WeekView mWeekView;
    WeekView.EventClickListener mEventClickListener;
    //    MonthLoader.MonthChangeListener mMonthChangeListener;
    WeekView.EventLongPressListener mEventLongPressListener;
    ArrayList<WeekViewEvent> mEvents;


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.bAddClass:
                Intent in = new Intent(this, TAClassAdder.class);
                in.putExtra("taEmail", userLocalStore.getLoggedInUser().email);
                startActivity(in);
                break;
            case R.id.bTAAddHours:
                startActivity(new Intent(this, AddHours.class));
                break;
        }
    }


    @Override
    protected void onStart() {
        //when this opens we need to authenticate user to make sure he's logged in
        super.onStart();
        if(authenticate()){
            displayUserDetails();
        }
        else{
            startActivity(new Intent(this, Login.class));
        }
    }

    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();
        editTextName.setText(user.name);
        //editTextName.setText(user.name);
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ta_activity_main);

        editTextName = (EditText) findViewById(R.id.etName);
        //textViewName = (TextView)findViewById(R.id.tvName);
        //textViewName.setText(UserLocalStore.SP_NAME);
        bLogout = (Button) findViewById(R.id.bLogout);
        bAddHours = (Button) findViewById(R.id.bTAAddHours);
        bAddClass = (Button) findViewById(R.id.bAddClass);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bLogout.setOnClickListener(this);
        bAddHours.setOnClickListener(this);
        bAddClass.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);

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
        WeekViewEvent event1 = new WeekViewEvent(0, "why three", 2016, 05, 16, 1, 00, 2016, 05, 16, 2, 00);
        events.add(event1);
        WeekViewEvent event2 = new WeekViewEvent(1, "Event 2", 2016, 05, 15, 9, 00, 2016, 05, 15, 10, 00);
        events.add(event2);
        WeekViewEvent event3 = new WeekViewEvent(2, "Event 3", 2016, 05, 14, 6, 00, 2016, 05, 14, 7, 00);
        events.add(event3);
        return events;
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
}
