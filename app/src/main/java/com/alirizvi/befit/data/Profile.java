package com.alirizvi.befit.data;

import android.util.Log;

import java.util.Date;

public class Profile {

    private String mFirstName;
    private String mLastName;
    private String mDateOfBirth;
    private Gender mGender;
    private String mEmail;
    private int mWeight;
    private float mHeight;
    private int mTargetWeight;
    private int mBmi;

    public Profile() {
        setFirstName("");
        setFirstName("");
        setLastName("");
        setDateOfBirth("");
        setGender(Gender.Male);
        setEmail("");
        setWeight(0);
        setHeight(0);
        setTargetWeight(0);

        //int bmi = (int)Math.floor(weight / Math.pow(height, 2));
        setBmi(0);
    }

    public Profile(String firstName, String lastName, String dateOfBirth, Gender gender, String email, int weight,
                   float height, int targetWeight) {
        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
        setGender(gender);
        setEmail(email);
        setWeight(weight);
        setHeight(height);
        setTargetWeight(targetWeight);

        int bmi;
        if (mWeight != 0 && mHeight != 0) {
            Log.d("Profile", "Weight: " + mWeight + " and Height: "+ mHeight);
            bmi = calcBmi();
        } else
            bmi = 0;

        setBmi(bmi);
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getDateOfBirth() {
        return mDateOfBirth;
    }

    public void setDateOfBirth(String mDateOfBirth) {
        this.mDateOfBirth = mDateOfBirth;
    }

    public Gender getGender() {
        return mGender;
    }

    public void setGender(Gender mGender) {
        this.mGender = mGender;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int mWeight) {

        this.mWeight = mWeight;
    }

    private int calcBmi() {
        return (int) Math.floor(mWeight / Math.pow(mHeight, 2));
    }

    public int getTargetWeight() {
        return mTargetWeight;
    }

    public void setTargetWeight(int mTargetWeight) {
        this.mTargetWeight = mTargetWeight;
    }

    public int getBmi() {
        return mBmi;
    }

    private void setBmi(int mBmi) {
        this.mBmi = mBmi;
    }

    public float getHeight() {
        return mHeight;
    }

    public void setHeight(float mHeight) {
        this.mHeight = mHeight;
    }

    public enum Gender {
        Male, Female
    }
}
