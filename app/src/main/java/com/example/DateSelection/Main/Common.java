package com.example.DateSelection.Main;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// common reusable functions defined here
public class Common {

    // control number of successful attempts needed for a trial
    private static final int TotalSuccessfulAttemptsNeededPerTrial = 2;

    public static int getTotalSuccessfulAttemptsNeededPerTrial() {
        return TotalSuccessfulAttemptsNeededPerTrial;
    }

    //return all days
    public static String[] GetDaysList() {
        String[] days = new String[31];
        for (int i = 0; i <= days.length - 1; i++) {
            days[i] = (i < 9 ? "0" : "") + (i + 1) + "";
        }
        return days;
    }

    //get day index from day value
    public static int GetDayIndex(String dayStr) {
        String[] days = GetDaysList();
        for (int i = 0; i <= days.length - 1; i++) {
            if (days[i].equals(dayStr))
                return i;
        }
        return -1;
    }

    //get day string from day index
    public static String GetDayStr(int dayIndex) {
        String[] days = GetDaysList();
        return days[dayIndex];
    }

    //return all months
    public static String[] GetMonthList() {
        return new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    }


    //return all years
    public static String[] GetYearsList() {
        String[] years = new String[146];
        for (int i = 0; i <= years.length - 1; i++) {
            years[i] = 1900 + i + "";
        }
        return years;
    }


    //get year index from year value
    public static int GetYearIndex(String yearStr) {
        String[] years = GetYearsList();
        for (int i = 0; i <= years.length - 1; i++) {
            if (years[i].equals(yearStr))
                return i;
        }
        return -1;
    }

    //get year string from year index
    public static String GetYearStr(int yearIndex) {
        String[] years = GetYearsList();
        return years[yearIndex];
    }

    //validate date
    public static String ValidateDate(int day, String month, int year) {
        if (day > 28 && month.equals("Feb") && year % 4 != 0) {
            return "day cannot be > 28 for a non-leap year";
        } else if (day > 29 && month.equals("Feb") && year % 4 == 0) {
            return "day cannot be > 29 for a leap year";
        } else if (day == 31 && (month.equals("Apr") || month.equals("Jun") || month.equals("Sep") || month.equals("Nov"))) {
            return "day cannot be 31 for " + month;
        } else
            return "Valid";
    }

    // called to randomly generate date from previous three decades
    public static String GenerateRandomSmallDate(String dateStr) {
        //String[] dates = new String[]{"12-Jun-1981" ,"25-Jul-1984" , "02-Oct-1983","19-Aug-1987",
        //"21-Aug-1984" ,"28-Jul-1982" , "29-May-1983","03-Sep-1982",
        //"15-Jul-1984" ,"11-May-1981"};
        String[] dates = new String[]{"12-Apr-2021", "12-Apr-2021", "02-Oct-1983", "19-Aug-1987",
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

    // called to randomly generate date from previous five decades
    public static String GenerateRandomMediumDate(String dateStr) {
       /* String[] dates = new String[]{"11-Jul-1966", "27-Jun-1965", "05-Aug-1967", "23-Oct-1963",
                "19-Jul-1962", "28-Aug-1965", "29-Sep-1962", "03-May-1963",
                "15-Sep-1961", "11-Jul-1964"};*/
        String[] dates = new String[]{"12-Apr-2021", "12-Apr-2021", "02-Oct-1983", "19-Aug-1987",
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

    // called to randomly generate date from previous seven decades
    public static String GenerateRandomLargeDate(String dateStr) {
       /* String[] dates = new String[]{"29-May-1942", "15-Jul-1944", "28-Sept-1942", "03-Jul-1947",
                "21-Jun-1945", "19-Nov-1941", "11-Aug-1943", "14-Sep-1944",
                "07-May-1948", "26-Apr-1946"};*/
        String[] dates = new String[]{"12-Apr-2021", "12-Apr-2021", "02-Oct-1983", "19-Aug-1987",
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
