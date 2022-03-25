package kh.com.ipay88.sdk.demo.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {HistoryItemDto.class}, exportSchema = false)
public abstract class IPay88DemoDatabase extends RoomDatabase {
    public abstract HistoryItemDao historyItemDao();
}
