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

    // called to randomly generate date from previous  decades
    public static String GenerateRandomSmallDate(String dateStr) {
        String[] dates = new String[]{"12-Jun-2001", "25-Jul-2004", "02-Oct-2003", "19-Aug-2007",
                "21-Aug-2004", "28-Jul-2002", "29-May-2003", "03-Sep-2002",
                "15-Jul-2004", "11-May-2001"};
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

    // called to randomly generate date from previous two decades
    public static String GenerateRandomMediumDate(String dateStr) {
        String[] dates = new String[]{"11-Jul-1996", "27-Jun-1995", "02-Oct-1993", "19-Aug-1997",
                "21-Aug-1994", "28-Jul-1992", "29-May-1993", "03-Sep-1992",
                "15-Jul-1994", "11-May-1991"};
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

    // called to randomly generate date from previous three decades
    public static String GenerateRandomLargeDate(String dateStr) {
        String[] dates = new String[]{"29-May-1982", "15-Jul-1984", "02-Oct-1983", "19-Aug-1987",
                "21-Aug-1984", "28-Jul-1982", "29-May-1983", "03-Sep-1982",
                "15-Jul-1984", "11-May-1981"};
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
