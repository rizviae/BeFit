package com.alirizvi.befit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// USED TO ADD DATABASES, and HANDLE SET, GET, INSERT, and DROP operations


public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "befit.db";

    public static final String TABLE_ACTIVITY_TYPES = "activity_types";
    public static final String COLUMN_ACTIVITY_TYPES_ID = "_id";
    public static final String COLUMN_ACTIVITY_TYPES_NAME = "name";
    public static final String COLUMN_ACTIVITY_TYPES_CALORIES = "calories";


    public static final String TABLE_ACTIVITIES = "activities";
    public static final String COLUMN_ACTIVITY_ID = "_id";
    public static final String COLUMN_ACTIVITYNAME = "activityname";
    public static final String COLUMN_ACTIVITYDATE = "activitydate";
    public static final String COLUMN_ACTIVITYSTARTTIME = "starttime";
    public static final String COLUMN_ACTIVITYTYPE = "activitytype";
    public static final String COLUMN_ACTIVITYDURATION = "activityduration";
    public static final String COLUMN_NUM_POSSIBLE_CALORIES = "numcalories";
    public static final String COLUMN_CALORIES_BURNT = "caloriesburnt";


    public static final String TABLE_DAILYMEALS = "dailymeals";
    public static final String COLUMN_MEAL_ID = "_id";
    public static final String COLUMN_MEALNAME = "mealname";
    public static final String COLUMN_MEALDATE = "activitydate";
    public static final String COLUMN_MEALTIME = "mealtime";
    public static final String COLUMN_MEALTYPE = "mealtype";
    public static final String COLUMN_SERVINGS = "mealservings";
    public static final String COLUMN_PRODUCER = "mealproducer";
    public static final String COLUMN_CALORIES_EATEN = "calorieseaten";
    public static final String COLUMN_ADDEDTOCALENDAR = "AddedToCalendar";
    public static final String COLUMN_DATE_ADDED = "dateadded";

    public static final String TABLE_PROFILE = "myprofile";
    public static final String COLUMN_PROFILE_ID = "_id";
    public static final String COLUMN_LAST = "lastname";
    public static final String COLUMN_FIRST = "firstname";
    public static final String COLUMN_DOB = "datofbirth";
    public static final String COLUMN_WEIGHT = "myweight";
    public static final String COLUMN_HEIGHT = "myheight";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_FAVORITEACTIVITY = "favoriteactivity";
    public static final String COLUMN_EMAIL = "email";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + TABLE_PROFILE + "(" +
                COLUMN_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LAST + " TEXT, " +
                COLUMN_FIRST + " TEXT, " +
                COLUMN_DOB + " DATE, " +
                COLUMN_WEIGHT + " INTEGER, " +
                COLUMN_HEIGHT + " REAL, " +
                COLUMN_GENDER + " INTEGER, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_FAVORITEACTIVITY + " TEXT " + ");";

        String query2 = "CREATE TABLE " + TABLE_ACTIVITIES + "(" +
                COLUMN_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ACTIVITYNAME + " TEXT, " +
                COLUMN_ACTIVITYDATE + " DATE, " +
                COLUMN_ACTIVITYSTARTTIME + " TIME, " +
                COLUMN_ACTIVITYTYPE + " TEXT, " +
                COLUMN_ACTIVITYDURATION + " INTEGER, " +
                COLUMN_NUM_POSSIBLE_CALORIES + " INTEGER, " +
                COLUMN_CALORIES_BURNT + " INTEGER, " +
                COLUMN_ADDEDTOCALENDAR + " BOOLEAN, " +
                COLUMN_DATE_ADDED + " DATE " + ");";

        String query3 = "CREATE TABLE " + TABLE_DAILYMEALS + "(" +
                COLUMN_MEAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MEALNAME + " TEXT, " +
                COLUMN_MEALDATE + " DATE, " +
                COLUMN_MEALTIME + " TIME, " +
                COLUMN_MEALTYPE + " TEXT, " +
                COLUMN_SERVINGS + " INTEGER, " +
                COLUMN_PRODUCER + " TEXT, " +
                COLUMN_NUM_POSSIBLE_CALORIES + " INTEGER, " +
                COLUMN_CALORIES_EATEN + " INTEGER, " +
                COLUMN_ADDEDTOCALENDAR + " BOOLEAN, " +
                COLUMN_DATE_ADDED + " DATE " + ");";


        String query4 = "CREATE TABLE " + TABLE_ACTIVITY_TYPES + " (" +
                COLUMN_ACTIVITY_TYPES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ACTIVITY_TYPES_NAME + " TEXT NOT NULL," +
                COLUMN_ACTIVITY_TYPES_CALORIES + " INTEGER NOT NULL);";

        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITIES);
        onCreate(db);
    }


    // how to add a new row to the database
       /* public void addActivity (Activity activity) {
            ContentValues values = new ContentValues();
            values.put(activ, Activity.getActivityName());
        SQLiteDatabase db = getWritableDatabase ();
        db.insert(TABLE_ACTIVITIES, null, values);

        // Toast confirmation
        db.close();
    }*/

    //how to delete an activity from db

    public void insertOrUpdateProfile(Profile profile) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.query(TABLE_PROFILE, null, null, null, null, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST, profile.getFirstName());
        values.put(COLUMN_LAST, profile.getLastName());
        values.put(COLUMN_DOB, profile.getDateOfBirth());
        values.put(COLUMN_EMAIL, profile.getEmail());
        values.put(COLUMN_GENDER, profile.getGender().ordinal());
        values.put(COLUMN_WEIGHT, profile.getWeight());
        values.put(COLUMN_HEIGHT, profile.getHeight());
        if (!c.moveToFirst()) {
            Log.d("DbHandler", "Inserting new Profile");
            db.insert(TABLE_PROFILE, null, values);
        } else {
            Log.d("DbHandler", "Updating my Profile " + profile.getHeight() );

            int idIndex = c.getColumnIndex(COLUMN_PROFILE_ID);
            String id = c.getString(idIndex);
            db.update(TABLE_PROFILE, values, COLUMN_PROFILE_ID + " = ?", new String[]{id});
        }
        db.close();
    }

    public Profile getProfile() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILE, null, null, null, null, null, null);

        Profile profile = null;

        if (cursor.moveToFirst()) {
            int firstNameIndex = cursor.getColumnIndex(COLUMN_FIRST);
            String firstName = cursor.getString(firstNameIndex);

            int lastNameIndex = cursor.getColumnIndex(COLUMN_LAST);
            String lastName = cursor.getString(lastNameIndex);

            int dobIndex = cursor.getColumnIndex(COLUMN_DOB);
            String dateOfBirth = cursor.getString(dobIndex);

            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            String email = cursor.getString(emailIndex);

            int genderIndex = cursor.getColumnIndex(COLUMN_GENDER);
            Profile.Gender gender = Profile.Gender.values()[cursor.getInt(genderIndex)];

            int weightIndex = cursor.getColumnIndex(COLUMN_WEIGHT);
            int weight = cursor.getInt(weightIndex);

            int targetWeightIndex = cursor.getColumnIndex(COLUMN_WEIGHT);
            int targetWeight = cursor.getInt(targetWeightIndex);

            int heightIndex = cursor.getColumnIndex(COLUMN_HEIGHT);
            float height = cursor.getFloat(heightIndex);


            /*String firstName, String lastName, String dateOfBirth, Gender gender, String email, int weight,
            int height, int targetWeight, int bmi*/
            profile = new Profile(firstName, lastName, dateOfBirth, gender, email, weight, height, targetWeight);
        }

        db.close();
        return profile;
    }

    public void deleteActivity(String ActivityName) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ACTIVITIES + " WHERE " + COLUMN_ACTIVITYNAME + "=\"" + ActivityName + "\" ;");
    }

    // Toast confirmation

    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ACTIVITIES + " WHERE 1 ";


        // Cursor points to a location in results

        Cursor c = db.rawQuery(query, null);

        // go to first row
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("ActivityName")) != null) {
                dbString += c.getString(c.getColumnIndex("ActivityName"));
                dbString += "\n";
            }
        }
        db.close();
        return dbString;
    }

    public void insertNewActivityType (ActivityType activity) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_TYPES_NAME, activity.getName());
        values.put(COLUMN_ACTIVITY_TYPES_CALORIES, activity.getCaloriesBurned());

        db.insert(TABLE_ACTIVITY_TYPES, null, values);
        db.close();
    }

    public List<ActivityType> getActivityTypes() {
        SQLiteDatabase db = getReadableDatabase();

        List<ActivityType> activities = new ArrayList<ActivityType>();
        Cursor cursor = db.query(TABLE_ACTIVITY_TYPES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int nameIndex = cursor.getColumnIndex(COLUMN_ACTIVITY_TYPES_NAME);
                String name = cursor.getString(nameIndex);

                int caloriesIndex = cursor.getColumnIndex(COLUMN_ACTIVITY_TYPES_CALORIES);
                int calories = cursor.getInt(caloriesIndex);

                Log.d("MyDBHandler", "Name of act: " + name);
                activities.add(new ActivityType(name, calories));
            } while (cursor.moveToNext());
        }

        db.close();
        return activities;
    }

    public void insertActivity(ExerciseActivity activity) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITYNAME, activity.getName());
        values.put(COLUMN_ACTIVITYDURATION, activity.getDuration());
        values.put(COLUMN_ACTIVITYDATE, activity.getDate());
        values.put(COLUMN_ACTIVITYTYPE, activity.getType());
        values.put(COLUMN_CALORIES_BURNT, activity.getCaloriesBurned());

        db.insert(TABLE_ACTIVITIES, null, values);
        db.close();
    }

    public List<ExerciseActivity> getActivities(){
        List<ExerciseActivity> activities = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_ACTIVITIES, null, null, null, null, null, null);

        if (!cursor.moveToFirst()) {
            do {
                int nameIndex = cursor.getColumnIndex(COLUMN_ACTIVITYNAME);
                String name = cursor.getString(nameIndex);

                int durationIndex = cursor.getColumnIndex(COLUMN_ACTIVITYDURATION);
                int duration = cursor.getInt(durationIndex);

                int dateIndex = cursor.getColumnIndex(COLUMN_ACTIVITYDATE);
                String date = cursor.getString(dateIndex);

                int typeIndex = cursor.getColumnIndex(COLUMN_ACTIVITYTYPE);
                String type = cursor.getString(typeIndex);

                int caloriesIndex = cursor.getColumnIndex(COLUMN_CALORIES_BURNT);
                int calories = cursor.getInt(caloriesIndex);

                activities.add(new ExerciseActivity(name, type, date, duration, calories));
            } while (cursor.moveToNext());
        }

        db.close();
        return activities;
    }

    public List<ExerciseActivity> getActivitiesForToday (String dateStr) {
        List<ExerciseActivity> activities = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_ACTIVITIES, null, COLUMN_ACTIVITYDATE + "= ?", new String[]{dateStr}, null, null, COLUMN_ACTIVITYDATE + " ASC ");


        if (cursor.moveToFirst()) {
            do {
                int nameIndex = cursor.getColumnIndex(COLUMN_ACTIVITYNAME);
                String name = cursor.getString(nameIndex);

                int durationIndex = cursor.getColumnIndex(COLUMN_ACTIVITYDURATION);
                int duration = cursor.getInt(durationIndex);

                int dateIndex = cursor.getColumnIndex(COLUMN_ACTIVITYDATE);
                String date = cursor.getString(dateIndex);

                int typeIndex = cursor.getColumnIndex(COLUMN_ACTIVITYTYPE);
                String type = cursor.getString(typeIndex);

                int caloriesIndex = cursor.getColumnIndex(COLUMN_CALORIES_BURNT);
                int calories = cursor.getInt(caloriesIndex);

                activities.add(new ExerciseActivity(name, type, date, duration, calories));
            } while (cursor.moveToNext());
        }
        db.close();
        return activities;
    }

    public List<ExerciseActivity> getActivitiesForThisWeek (String dateStr) {
        List<ExerciseActivity> activities = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        //date('2014-10-16', '-15 days');

        Cursor cursor = db.query(TABLE_ACTIVITIES, null, COLUMN_ACTIVITYDATE + " BETWEEN ? AND date( ?, '+7 days')",
                new String[]{dateStr, dateStr}, null, null, COLUMN_ACTIVITYDATE + " ASC ");


        if (cursor.moveToFirst()) {
            do {
                int nameIndex = cursor.getColumnIndex(COLUMN_ACTIVITYNAME);
                String name = cursor.getString(nameIndex);

                int durationIndex = cursor.getColumnIndex(COLUMN_ACTIVITYDURATION);
                int duration = cursor.getInt(durationIndex);

                int dateIndex = cursor.getColumnIndex(COLUMN_ACTIVITYDATE);
                String date = cursor.getString(dateIndex);

                int typeIndex = cursor.getColumnIndex(COLUMN_ACTIVITYTYPE);
                String type = cursor.getString(typeIndex);

                int caloriesIndex = cursor.getColumnIndex(COLUMN_CALORIES_BURNT);
                int calories = cursor.getInt(caloriesIndex);

                activities.add(new ExerciseActivity(name, type, date, duration, calories));
            } while (cursor.moveToNext());
        }
        db.close();
        return activities;
    }

    public void insertMeal(Meal meal) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_MEALNAME, meal.getName());
        values.put(COLUMN_MEALDATE, meal.getDate());
        values.put(COLUMN_MEALTYPE, meal.getType().ordinal());
        values.put(COLUMN_SERVINGS, meal.getServings());
        values.put(COLUMN_PRODUCER, meal.getProducer());
        values.put(COLUMN_CALORIES_EATEN, meal.getCalories());

        db.insert(TABLE_DAILYMEALS, null, values);

        Log.d("MyDBHandler", "Added meal " + meal.getName() );
        db.close();
    }

    public List<Meal> getMealsForToday(String date) {
        List<Meal> meals = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_DAILYMEALS, null, COLUMN_ACTIVITYDATE + "= ?", new String[]{date}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int mealNameIndex = cursor.getColumnIndex(COLUMN_MEALNAME);
                String mealName = cursor.getString(mealNameIndex);

                int mealDateIndex = cursor.getColumnIndex(COLUMN_MEALDATE);
                String mealDate = cursor.getString(mealDateIndex);

                int mealTypeIndex = cursor.getColumnIndex(COLUMN_MEALTYPE);
                int mealType = cursor.getInt(mealTypeIndex);

                int servingsIndex = cursor.getColumnIndex(COLUMN_SERVINGS);
                int servings = cursor.getInt(servingsIndex);

                int producerIndex = cursor.getColumnIndex(COLUMN_PRODUCER);
                String producer = cursor.getString(producerIndex);

                int caloriesIndex = cursor.getColumnIndex(COLUMN_CALORIES_EATEN);
                int calories = cursor.getInt(caloriesIndex);

                //String name, String producer, int servings, MealType type, String date
                meals.add(new Meal(mealName, producer, servings, Meal.MealType.values()[mealType], calories, mealDate));

            } while (cursor.moveToNext());
        }
        db.close();

        return meals;
    }

    public List<Meal> getMealsThisWeek(String date) {
        List<Meal> meals = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_DAILYMEALS, null, COLUMN_MEALDATE + " BETWEEN ? AND date( ?, '+7 days')",
                new String[]{date, date}, null, null, COLUMN_MEALDATE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int mealNameIndex = cursor.getColumnIndex(COLUMN_MEALNAME);
                String mealName = cursor.getString(mealNameIndex);

                int mealDateIndex = cursor.getColumnIndex(COLUMN_MEALDATE);
                String mealDate = cursor.getString(mealDateIndex);

                int mealTypeIndex = cursor.getColumnIndex(COLUMN_MEALTYPE);
                int mealType = cursor.getInt(mealTypeIndex);

                int servingsIndex = cursor.getColumnIndex(COLUMN_SERVINGS);
                int servings = cursor.getInt(servingsIndex);

                int producerIndex = cursor.getColumnIndex(COLUMN_PRODUCER);
                String producer = cursor.getString(producerIndex);

                int caloriesIndex = cursor.getColumnIndex(COLUMN_CALORIES_EATEN);
                int calories = cursor.getInt(caloriesIndex);

                //String name, String producer, int servings, MealType type, String date
                meals.add(new Meal(mealName, producer, servings, Meal.MealType.values()[mealType], calories, mealDate));

            } while (cursor.moveToNext());
        }
        db.close();

        return meals;
    }
}