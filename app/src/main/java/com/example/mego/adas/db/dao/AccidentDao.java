package com.example.mego.adas.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.mego.adas.db.entity.Accident;

import java.util.List;

/**
 * Created by ahmed on 7/22/2017.
 */

@Dao
public interface AccidentDao {

    @Query("SELECT * FROM accidents")
    LiveData<List<Accident>> getAllAccidents();

    @Query("select * from accidents where id = :accidentId")
    LiveData<Accident> getAccident(String accidentId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertAllAccidents(List<Accident> accidents);

    @Delete
    int[] deleteAccidents(List<Accident> accidents);
}
