package com.example.lordbramasta.praktikum;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface eventDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertEvent(dataEvent data);

    @Update
    int updateEvent(dataEvent data);

    @Query("SELECT * FROM tbEvent")
    dataEvent selectAllEvent();

//    @Query("SELECT nama_event from tbEvent")
//    dataEvent selectNamaEvent();

}
