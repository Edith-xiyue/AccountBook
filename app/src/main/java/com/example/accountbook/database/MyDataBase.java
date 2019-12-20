/***
 * Copyright (c) 2019 ASKEY Computer Corp. and/or its affiliates. All rights reserved.
 * Created by Allen Luo on 2019/10/30
 * Description: [PT-48][Intranet Chat] [APP][Database] Create Database
 */
package com.example.accountbook.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.accountbook.database.dao.IncomeDao;
import com.example.accountbook.database.table.IncomeEntity;

@Database(entities = { IncomeEntity.class},version = 1,exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {
    private static final String DB_NAME = "chat.db";
    public MyDataBase(){}
    public static MyDataBase sInstance;
    public static void init(Context context){
        Log.d("MyDatabase", "initData: ");
        sInstance = Room.databaseBuilder(context, MyDataBase.class,DB_NAME).build();
    }

    public static MyDataBase getInstance(){
        if (sInstance == null){
            throw new NullPointerException("sInstance == null");
        }
        return sInstance;
    }

    public abstract IncomeDao getIncomeDao();

}
