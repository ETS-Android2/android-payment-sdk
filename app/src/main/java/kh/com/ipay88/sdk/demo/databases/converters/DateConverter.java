package kh.com.ipay88.sdk.demo.databases.converters;

/*
 * DateConverter
 * Demo App
 *
 * Created by kunTola on 14/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}