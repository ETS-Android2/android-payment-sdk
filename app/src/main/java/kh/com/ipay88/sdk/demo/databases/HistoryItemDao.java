package kh.com.ipay88.sdk.demo.databases;

/*
 * HistoryItemDao
 * Demo App
 *
 * Created by kunTola on 14/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HistoryItemDao {
    @Query("SELECT * FROM HistoryItemDto WHERE RefNo LIKE :RefNo LIMIT 1")
    HistoryItemDto loadRecord(String RefNo);
    @Query("SELECT * FROM HistoryItemDto ORDER BY Id DESC")
    List<HistoryItemDto> loadRecords();
    @Query("SELECT * FROM HistoryItemDto WHERE Currency LIKE :currency ORDER BY Id DESC")
    List<HistoryItemDto> loadRecords(String currency);
    @Insert
    void insertAll(HistoryItemDto... historyItems);
    @Update
    void updateRecord(HistoryItemDto historyItemDto);
    @Delete
    void deleteAll(HistoryItemDto... historyItems);
    @Query("DELETE FROM HistoryItemDto WHERE Currency LIKE :currency")
    void deleteAll(String currency);
}
