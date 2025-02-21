package com.example.proyectoad.IncidenciasAdd;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IncidenciaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public IncidenciaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}