package kh.com.ipay88.sdk.demo.databases;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HistoryItemDto {
    @PrimaryKey(autoGenerate = true)
    public int Id;
    public String RefNo;
    public long CreatedDateMilliseconds;
    public String Json;
    public double Amount;
    public double AmountSharedRef;
    public String Currency;
}
