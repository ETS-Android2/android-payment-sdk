package kh.com.ipay88.sdk.demo.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class StringUtils {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static String GetAmountFormat(String amountStr) {
        double amount = Double.parseDouble(amountStr);
        if (amount/1000000000000d >= 1) {
            return df.format(amount/1000000000000d).concat("T");
        } else if (amount/1000000000 >= 1) {
            return df.format(amount/1000000000).concat("B");
        } else if (amount/1000000 >= 1) {
            return df.format(amount/1000000).concat("M");
        } else if (amount/1000 >= 1) {
            return df.format(amount/1000).concat("K");
        }
        return amountStr;
    }

    public static String GetAmount(String amountFormat) {
        if (amountFormat.contains("T")) {
            amountFormat = amountFormat.replace("T", "");
            double amount = Double.parseDouble(amountFormat) * 1000000000000d;
            return df.format(amount);
        } else if (amountFormat.contains("B")) {
            amountFormat = amountFormat.replace("B", "");
            double amount = Double.parseDouble(amountFormat) * 1000000000;
            return df.format(amount);
        } else if (amountFormat.contains("M")) {
            amountFormat = amountFormat.replace("M", "");
            double amount = Double.parseDouble(amountFormat) * 1000000;
            return df.format(amount);
        } else if (amountFormat.contains("K")) {
            amountFormat = amountFormat.replace("K", "");
            double amount = Double.parseDouble(amountFormat) * 1000;
            return df.format(amount);
        }
        return amountFormat;
    }

    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    public static String GetDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Phnom_Penh"));
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String GetCurrencyFormat(double amount) {
        return String.format("%,.2f", amount);
    }
}
