/***
 * Copyright (c) 2019 ASKEY Computer Corp. and/or its affiliates. All rights reserved.
 * Created by Allen Luo on 2019/10/30
 * Description: [PT-48][Intranet Chat] [APP][Database] Create Database
 */
package com.example.accountbook.database.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "income_info")
public class IncomeEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "income_type")
    private int incomeType;
    @ColumnInfo(name = "income_money")
    private String incomeMoney;
    @ColumnInfo(name = "income_time")
    private long incomeTime;
    @ColumnInfo(name = "income_remark")
    private String incomeRemark;

    public Integer getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(int incomeType) {
        this.incomeType = incomeType;
    }

    public String getIncomeMoney() {
        return incomeMoney;
    }

    public void setIncomeMoney(String incomeMoney) {
        this.incomeMoney = incomeMoney;
    }

    public String getIncomeRemark() {
        return incomeRemark;
    }

    public void setIncomeRemark(String incomeRemark) {
        this.incomeRemark = incomeRemark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getIncomeTime() {
        return incomeTime;
    }

    public void setIncomeTime(long incomeTime) {
        this.incomeTime = incomeTime;
    }

    @Override
    public String toString() {
        return "IncomeEntity{" +
                "id=" + id +
                ", incomeType='" + incomeType + '\'' +
                ", incomeMoney='" + incomeMoney + '\'' +
                ", incomeTime='" + incomeTime + '\'' +
                ", incomeRemark='" + incomeRemark + '\'' +
                '}';
    }
}
