package com.example.mego.adas.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.mego.adas.db.dao.AccidentDao;
import com.example.mego.adas.db.entity.Accident;

/**
 * Created by ahmed on 7/22/2017.
 */

@Database(entities = {Accident.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSATNCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSATNCE == null) {
            INSATNCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "accidents.db").build();
        }
        return INSATNCE;
    }

    public abstract AccidentDao accidentDao();
}
