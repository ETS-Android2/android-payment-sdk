package kh.com.ipay88.sdk.demo.databases;

/*
 * IPay88DemoDatabase
 * Demo App
 *
 * Created by kunTola on 11/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {HistoryItemDto.class}, exportSchema = false)
public abstract class IPay88DemoDatabase extends RoomDatabase {
    public abstract HistoryItemDao historyItemDao();
}
