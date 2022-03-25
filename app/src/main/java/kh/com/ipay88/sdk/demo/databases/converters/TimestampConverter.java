package kh.com.ipay88.sdk.demo.databases.converters;


import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kh.com.ipay88.sdk.constants.IPay88Constants;

public class TimestampConverter {
    static DateFormat df = new SimpleDateFormat(IPay88Constants.TIME_STAMP_FORMAT);

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}