/***
 * Copyright (c) 2019 ASKEY Computer Corp. and/or its affiliates. All rights reserved.
 * Created by Allen Luo on 2019/10/30
 * Description: [PT-48][Intranet Chat] [APP][Database] Create Database
 */
package com.example.accountbook.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.accountbook.database.table.IncomeEntity;

import java.util.List;

@Dao
public interface IncomeDao {

    @Insert
    void insert(IncomeEntity entities);

    @Delete
    void delete(IncomeEntity entities);

    @Update
    void update(IncomeEntity... entities);

    @Query("SELECT * FROM income_info")
    IncomeEntity Query();

    @Query("SELECT * FROM income_info")
    List<IncomeEntity> getAllIncome();

    @Query("select * from income_info where income_time =:time")
    IncomeEntity getIncomeEntity (long time);

    @Query("select * from income_info where id =:id")
    IncomeEntity getIncomeEntity (int id);
}