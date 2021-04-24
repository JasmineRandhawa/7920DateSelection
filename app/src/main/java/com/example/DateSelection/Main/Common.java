package com.example.DateSelection.Main;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// common reusable functions defined here
public class Common {

    // control number of successful attempts needed for a trial
    private static final int TotalSuccessfulAttemptsNeededPerTrial = 10;

    public static int getTotalSuccessfulAttemptsNeededPerTrial() {
        return TotalSuccessfulAttemptsNeededPerTrial;
    }

    // called to randomly generate date from previous  two year
    public static String GenerateRandomSmallDate(String dateStr) {
        String[] dates = new String[]{"12-Jun-2020", "25-Jul-2019", "02-Oct-2020", "19-Aug-2019",
                "21-Aug-2020", "28-Jul-2019", "29-May-2020", "03-Sep-2019",
                "15-Jul-2019", "11-May-2020"};
        if (dateStr.equals("")) {
            return dates[0];
        } else {
            int index = 0;
            for (int i = 0; i <= dates.length - 1; i++) {
                if (dates[i].equals(dateStr)) {
                    index = i;
                    break;
                }
            }
            return dates[index + 1];
        }
    }

    public static int GetYearDiff(String dateToBeSelected)
    {
        int yearDiff = 0;
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        String[] dateParts = new String[3];
        if(!dateToBeSelected.equals(""))
            dateParts = dateToBeSelected.split("-");
        if(dateParts!=null && dateParts.length>2)
        {
            int year = Integer.parseInt(dateParts[2]);
            yearDiff = yearNow - year;
        }
        return yearDiff;
    }

    public static int GetMonthDiff(String dateToBeSelected, int yearDiff)
    {
        int monthDiff = 0;
        Calendar cal = Calendar.getInstance();
        int monthNow = cal.get(Calendar.MONTH) + 1;
        String[] dateParts = new String[3];
        if(!dateToBeSelected.equals(""))
            dateParts = dateToBeSelected.split("-");
        if(dateParts!=null && dateParts.length>1)
        {
            int month = Integer.parseInt(Common.GetMonthIndex(dateParts[1]));
            monthDiff = monthNow - month;
        }
        return yearDiff * 12 +monthDiff ;
    }

    // called to randomly generate date from previous three years
    public static String GenerateRandomMediumDate(String dateStr) {
        String[] dates = new String[]{"11-Jul-2017", "27-Jun-2018", "02-Oct-2018", "19-Aug-2017",
                "21-Aug-2018", "28-Jul-2017", "29-May-2018", "03-Sep-2017",
                "15-Jul-2018", "11-May-2017"};
        if (dateStr.equals("")) {
            return dates[0];
        } else {
            int index = 0;
            for (int i = 0; i <= dates.length - 1; i++) {
                if (dates[i].equals(dateStr)) {
                    index = i;
                    break;
                }
            }
            return dates[index + 1];
        }
    }

    // called to randomly generate date from previous four year
    public static String GenerateRandomLargeDate(String dateStr) {
        String[] dates = new String[]{"29-May-2015", "15-Jul-2016", "02-Oct-2015", "19-Aug-2016",
                "21-Aug-2016", "28-Jul-2015", "29-May-2015", "03-Sep-2016",
                "15-Jul-2015", "11-May-2016"};
        if (dateStr.equals("")) {
            return dates[0];
        } else {
            int index = 0;
            for (int i = 0; i <= dates.length - 1; i++) {
                if (dates[i].equals(dateStr)) {
                    index = i;
                    break;
                }
            }
            return dates[index + 1];
        }
    }

    //get month string from month number
    public static String GetMonthName(int month)
    {
        String monthString;
        switch (month) {
            case 1:  monthString = "Jan";
                break;
            case 2:  monthString = "Feb";
                break;
            case 3:  monthString = "Mar";
                break;
            case 4:  monthString = "Apr";
                break;
            case 5:  monthString = "May";
                break;
            case 6:  monthString = "Jun";
                break;
            case 7:  monthString = "Jul";
                break;
            case 8:  monthString = "Aug";
                break;
            case 9:  monthString = "Sep";
                break;
            case 10: monthString = "Oct";
                break;
            case 11: monthString = "Nov";
                break;
            case 12: monthString = "Dec";
                break;
            default: monthString = "Invalid month";
                break;
        }
        return monthString;
    }

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

        int dayOfMonth = 0;
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

    public static String GetDayOFWeek(String dateStr) {

        Date date = GetDateStr(dateStr);
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            dayOfWeek = dayOfWeek - 1;
            if (dayOfWeek == -1) {
                dayOfWeek = 6;
            }
            String dayOfWeekStr = GetDayName(dayOfWeek);
            if (!dayOfWeekStr.equals(""))
                dayOfWeekStr = dayOfWeekStr.substring(0, 3);
            return dayOfWeekStr;
        } else
            return "";
    }
}
