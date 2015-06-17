package com.alirizvi.befit.data;

public class ActivityType {
    private String mName;
    private int mCaloriesBurned; //Per 10 mins

    public ActivityType(String name, int caloriesBurned) {
        setName(name);
        setCaloriesBurned(caloriesBurned);
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }


    @Override
    public String toString() {
        return mName;
    }

    public int getCaloriesBurned() {
        return mCaloriesBurned;
    }

    public void setCaloriesBurned(int mCaloriesBurned) {
        this.mCaloriesBurned = mCaloriesBurned;
    }
}
