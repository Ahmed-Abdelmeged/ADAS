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



package com.example.mego.adas.accidents.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.mego.adas.accidents.db.AppDatabase;
import com.example.mego.adas.accidents.db.entity.Accident;

import java.util.List;

import io.reactivex.Completable;


public class AccidentRepository {
    private Context context;

    public AccidentRepository(Context context) {
        this.context = context;
    }

    public LiveData<List<Accident>> getAccidents() {
        return AppDatabase.getDatabase(context).accidentDao().getAllAccidents();
    }

    public Completable insertAccidents(List<Accident> accidents) {
        return Completable.fromAction(() ->
                AppDatabase.getDatabase(context).accidentDao().insertAllAccidents(accidents));
    }

    public Completable deleteAccidents(List<Accident> accidents) {
        return Completable.fromAction(() ->
                AppDatabase.getDatabase(context).accidentDao().deleteAccidents(accidents));
    }

    public LiveData<Accident> getAccident(String accidentId) {
        return AppDatabase.getDatabase(context).accidentDao().getAccident(accidentId);
    }
}
