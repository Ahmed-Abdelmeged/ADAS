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



package com.example.mego.adas.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.mego.adas.db.entity.Accident;
import com.example.mego.adas.repository.AccidentRepository;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;



public class AccidentViewModel extends AndroidViewModel {

    private AccidentRepository repository;

    public AccidentViewModel(Application application) {
        super(application);
        repository = new AccidentRepository(application);
    }

    void addAccidents(List<Accident> accidents) {
        repository.insertAccidents(accidents)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Timber.e("Accidents inserted successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("Error inserting accidents");
                    }
                });
    }

    void deleteAccidents(List<Accident> accidents) {
        repository.deleteAccidents(accidents)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Timber.e("Accidents deleted successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("Error deleting accidents");
                    }
                });
    }

    LiveData<List<Accident>> getAccidents() {
        return repository.getAccidents();
    }

    LiveData<Accident> getAccident(String accidentId) {
        return repository.getAccident(accidentId);
    }
}
