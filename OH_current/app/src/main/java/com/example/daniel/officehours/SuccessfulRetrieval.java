//package com.example.daniel.officehours;
//
///**
// * Created by Daniel on 4/22/16.
// */
//public class SuccessfulRetrieval {
//
//    private Boolean success;
//    private String message;
//    private String firstName;
//    private String year;
//    private String month;
//    private String day;
//    private String starthour;
//    private String startminute;
//    private String endhour;
//    private String endminute;
//
//
//    public SuccessfulRetrieval(Boolean success, String message, String firstName, String year
//    , String month, String day, String starthour, String startminute, String endhour, String endminute){
//
//        this.success = success;
//        this.message = message;
//        this.firstName = firstName;
//        this.year = year;
//        this.month = month;
//        this.day = day;
//        this.starthour = starthour;
//        this.startminute = startminute;
//        this.endhour = endhour;
//        this.endminute = endminute;
//    }
//
//    public Boolean getSuccess(){
//        return success;
//    }
//
//    public String getFirstName(){
//        return this.firstName;
//    }
//    public String getYear(){
//        return this.year;
//    }
//    public String getDay(){
//        return this.day;
//    }
//    public String getStartHour(){
//        return this.starthour;
//    }
//    public String getStartMinute(){
//        return this.startminute;
//    }
//    public String getEndHour(){
//        return this.endhour;
//    }
//
//    public String getEndMinute(){
//        return this.endminute;
//    }
//
//}


package com.example.daniel.officehours;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Daniel on 4/22/16.
 */
//public class SuccessfulRetrieval {
//
//    private Boolean success;
//    private String message;
//    private ArrayList<String> data;
//
//    public SuccessfulRetrieval(Boolean success, String message){
//        this.success = success;
//        this.message = message;
//    }
//
//    public Boolean getSuccess(){
//        return success;
//    }
//
//    public String getMessage(){
//        return this.message;
//    }
//}


public class SuccessfulRetrieval {

    private String code;
    private String firstname;
    private String dayofweek;
    private String starthour;
    private String startminute;
    private String endhour;
    private String endminute;
    private String recurring;

    public SuccessfulRetrieval(String code, String firstName, String dayOfWeek, String startHour, String startMinute,
                               String endHour, String endMinute, String recurring){
        this.code = code;
        this.firstname = firstName;
        this.dayofweek = dayOfWeek;
        this.starthour = startHour;
        this.startminute = startMinute;
        this.endhour = endHour;
        this.endminute = endMinute;
        this.recurring = recurring;
    }

    public String getCode() {
        return code;
    }

    public String getFirstName(){
        return firstname;
    }

    public String getDayOfWeek(){
        return dayofweek;
    }

    public String getStartHour(){
        return starthour;
    }

    public String getStartMinute(){
        return startminute;
    }

    public String getEndHour(){
        return endhour;
    }

    public String getEndMinute(){
        return endminute;
    }

    public String getRecurring(){
        return recurring;
    }

//public class SuccessfulRetrieval {
//
//    private String[] data;
//
//    public SuccessfulRetrieval(String[] data){
//        this.data = data;
//    }
//
//    public String[] getData(){
//        return data;
//    }
//
}
