package kh.com.ipay88.sdk.demo.databases;

import android.content.Context;

import androidx.room.Room;

public class IPay88DemoDatabaseHelper {
    private static final String DATABASE_NAME = "IPay88DemoDatabase";
    private static IPay88DemoDatabase demoDatabase;

    public static IPay88DemoDatabase getInstance(Context context) {
        demoDatabase = demoDatabase == null ? Room
                .databaseBuilder(context, IPay88DemoDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build() : demoDatabase;
        return demoDatabase;
    }
}
