package com.alirizvi.befit.data;

public class Meal {
    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public int getCalories() {
        return mCalories;
    }

    public void setCalories(int mCalories) {
        this.mCalories = mCalories;
    }

    public enum MealType {
        Breakfast, Lunch, Dinner
    }

    private String mName;
    private String mProducer;
    private int mServings;
    private MealType mType;
    private String mDate;
    private int mCalories;

    @Override
    public String toString() {
        return mName;
    }

    public Meal(String name, String producer, int servings, MealType type, int calories, String date) {
        setName(name);
        setProducer(producer);
        setServings(servings);
        setType(type);
        setDate(date);
        setCalories(calories);
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getProducer() {
        return mProducer;
    }

    public void setProducer(String mProducer) {
        this.mProducer = mProducer;
    }

    public int getServings() {
        return mServings;
    }

    public void setServings(int mServings) {
        this.mServings = mServings;
    }

    public MealType getType() {
        return mType;
    }

    public void setType(MealType mType) {
        this.mType = mType;
    }
}
