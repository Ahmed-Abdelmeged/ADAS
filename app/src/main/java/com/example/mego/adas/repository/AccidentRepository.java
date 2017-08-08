package com.example.mego.adas.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.mego.adas.db.AppDatabase;
import com.example.mego.adas.db.entity.Accident;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by ahmed on 8/8/2017.
 */

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
