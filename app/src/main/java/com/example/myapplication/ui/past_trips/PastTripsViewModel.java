package com.example.myapplication.ui.past_trips;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PastTripsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PastTripsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}