package com.example.DateSelection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*common functions used for date selection*/
public class DateClass {

    public static Date GetDateStr(String dateString) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {

            Date date = dateFormat.parse(dateString);
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static int GetDayOfMonth() {

       int dayOfMonth =0;
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat dtf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String[] parts = dtf.format(c).split("-");
        dayOfMonth = Integer.parseInt(parts[0]);
        return dayOfMonth;
    }

    public static String GetDayName(int dayIndex) {
        String dayName = "";
        switch (dayIndex) {
            case 0:
                dayName = "Sunday";
                break;
            case 1:
                dayName = "Monday";
                break;
            case 2:
                dayName = "Tuesday";
                break;
            case 3:
                dayName = "Wednesday";
                break;
            case 4:
                dayName = "Thursday";
                break;
            case 5:
                dayName = "Friday";
                break;
            case 6:
                dayName = "Saturday";
                break;
        }
        return dayName;
    }

    public static String GetMonthIndex(String month) {
        String monthIndex = "";
        switch (month) {
            case "Jan":
                monthIndex = "01";
                break;
            case "Feb":
                monthIndex = "02";
                break;
            case "Mar":
                monthIndex = "03";
                break;
            case "Apr":
                monthIndex = "04";
                break;
            case "May":
                monthIndex = "05";
                break;
            case "Jun":
                monthIndex = "06";
                break;
            case "Jul":
                monthIndex = "07";
                break;
            case "Aug":
                monthIndex = "08";
                break;
            case "Sep":
                monthIndex = "09";
                break;
            case "Oct":
                monthIndex = "10";
                break;

            case "Nov":
                monthIndex = "11";
                break;
            case "Dec":
                monthIndex = "12";
                break;
        }
        return monthIndex;
    }

    public static String GetMonthName(int monthIndex) {
        String monthName = "";
        switch (monthIndex) {
            case 1:
                monthName = "Jan";
                break;
            case 2:
                monthName = "Feb";
                break;
            case 3:
                monthName = "Mar";
                break;
            case 4:
                monthName = "Apr";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "Jun";
                break;
            case 7:
                monthName = "Jul";
                break;
            case 8:
                monthName = "Aug";
                break;
            case 9:
                monthName = "Sep";
                break;
            case 10:
                monthName = "Oct";
                break;

            case 11:
                monthName = "Nov";
                break;
            case 12:
                monthName = "Dec";
                break;
        }
        return monthName;
    }


    public static String GetDayOFWeek(String dateStr) {

        Date date = GetDateStr(dateStr);
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            dayOfWeek = dayOfWeek-1;
            if(dayOfWeek == -1)
            {
                dayOfWeek = 6;
            }
            String dayOfWeekStr = DateClass.GetDayName(dayOfWeek);
            if (!dayOfWeekStr.equals(""))
                dayOfWeekStr = dayOfWeekStr.substring(0, 3);
            return dayOfWeekStr;
        } else
            return "";
    }
}
