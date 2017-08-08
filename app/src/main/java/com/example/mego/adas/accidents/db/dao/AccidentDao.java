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


package com.example.mego.adas.accidents.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.mego.adas.accidents.db.entity.Accident;

import java.util.List;


@Dao
public interface AccidentDao {

    @Query("SELECT * FROM accidents")
    LiveData<List<Accident>> getAllAccidents();

    @Query("SELECT * FROM accidents WHERE accidentId = :accidentId")
    LiveData<Accident> getAccident(String accidentId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllAccidents(List<Accident> accidents);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAccident(Accident accident);

    @Delete
    void deleteAccidents(List<Accident> accidents);
}
