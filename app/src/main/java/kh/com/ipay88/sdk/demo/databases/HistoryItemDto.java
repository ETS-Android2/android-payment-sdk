package kh.com.ipay88.sdk.demo.databases;

/*
 * HistoryItemDto
 * Demo App
 *
 * Created by kunTola on 14/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

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
