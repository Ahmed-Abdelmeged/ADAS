/*
 * Copyright (c) 2017 Ahmed-Abdelmeged
 *
 * github: https://github.com/Ahmed-Abdelmeged
 * email: ahmed.abdelmeged.vm@gamil.com
 * Facebook: https://www.facebook.com/ven.rto
 * Twitter: https://twitter.com/A_K_Abd_Elmeged
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.mego.adas.app;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.mego.adas.accidents.db.dao.AccidentDao;
import com.example.mego.adas.accidents.db.entity.Accident;


@Database(entities = {Accident.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSATNCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSATNCE == null) {
            INSATNCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "adas.db").build();
        }
        return INSATNCE;
    }

    public abstract AccidentDao accidentDao();
}
