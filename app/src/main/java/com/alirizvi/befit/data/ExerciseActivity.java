package com.alirizvi.befit.data;

import java.util.Date;

public class ExerciseActivity {
    private String mName;
    private String mType;
    private String mDate;
    private int mDuration;
    private int mCaloriesBurned;

    public ExerciseActivity(String name, String type, String date, int duration, int caloriesBurned) {
        setName(name);
        setType(type);
        setDate(date);
        setDuration(duration);
        setCaloriesBurned(caloriesBurned);
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    @Override
    public String toString() {
        return mType + " on " + mDate;
    }

    public int getCaloriesBurned() {
        return mCaloriesBurned;
    }

    public void setCaloriesBurned(int mCaloriesBurned) {
        this.mCaloriesBurned = mCaloriesBurned;
    }
}
