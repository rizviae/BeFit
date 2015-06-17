package com.alirizvi.befit;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.alirizvi.befit.data.Meal;
import com.alirizvi.befit.data.MyDBHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FoodFragment extends Fragment {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private Button mDailyBt;
    private Button mWeeklyBt;

    private List<Meal> mBreakfast = new ArrayList<>();
    private List<Meal> mLunch = new ArrayList<>();
    private List<Meal> mDinner = new ArrayList<>();

    private Spinner mBreakfastMeals;
    private Spinner mLunchMeals;
    private Spinner mDinnerMeals;
    private Button mAddToCalendarBt;
    private Button mDatePicker;
    private Button mNewMeal;

    private TextView mTotalCalories;

    private TextView mBreakfastCalories;
    private TextView mLunchCalories;
    private TextView mDinnerCalories;

    private Calendar mSelectedDate = Calendar.getInstance();

    private MyDBHandler mDBHandler;

    private DatePickerDialog.OnDateSetListener mDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet (DatePicker view, int year, int month, int day) {
            mSelectedDate = Calendar.getInstance();
            mSelectedDate.set(Calendar.YEAR, year);
            mSelectedDate.set(Calendar.MONTH, month);
            mSelectedDate.set(Calendar.DAY_OF_MONTH, day);
        }
    };

    private AdapterView.OnItemSelectedListener mFoodSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            setTextForCaloriesTv(adapterView);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private void setTextForCaloriesTv(AdapterView<?> sp) {
        int selectedPos = sp.getSelectedItemPosition();

        int id = sp.getId();

        switch (id) {
            case R.id.breakfast_menu:
                if (mBreakfast.size() != 0) {
                    Meal m = mBreakfast.get(selectedPos);
                    mBreakfastCalories.setText("Calories: " + String.valueOf(m.getCalories()));
                } else {
                    mBreakfastCalories.setText("");
                }
                break;
            case R.id.lunch_menu:
                if (mLunch.size() != 0) {
                    Meal m = mLunch.get(selectedPos);
                    mLunchCalories.setText("Calories: " + String.valueOf(m.getCalories()));
                } else {
                    mLunchCalories.setText("");
                }
                break;
            case R.id.dinner_menu:
                if (mDinner.size() != 0) {
                    Meal m = mDinner.get(selectedPos);
                    mDinnerCalories.setText("Calories: " + String.valueOf(m.getCalories()));
                } else {
                    mDinnerCalories.setText("");
                }
                break;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDBHandler = new MyDBHandler(getActivity());
    }

    private void refreshTotalCalories() {
        int totalCalories = 0;

        for (Meal meal : mBreakfast)
            totalCalories += meal.getCalories();

        for (Meal meal : mLunch)
            totalCalories += meal.getCalories();

        for (Meal meal : mDinner)
            totalCalories += meal.getCalories();

        mTotalCalories.setText("Total Calories: " + totalCalories);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_meals, container, false);

        mNewMeal = (Button)layout.findViewById(R.id.new_meal_bt);
        mNewMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupLayout = inflater.inflate(R.layout.new_meal_layout, null);
                final EditText mealNameEt = (EditText) popupLayout.findViewById(R.id.meal_name);
                final EditText mealProducerEt = (EditText) popupLayout.findViewById(R.id.meal_producer);
                final EditText mealServingsEt = (EditText) popupLayout.findViewById(R.id.servings);
                final EditText caloriesEt = (EditText) popupLayout.findViewById(R.id.calories);
                final RadioGroup typeRadioGroup= (RadioGroup) popupLayout.findViewById(R.id.type_radio);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(popupLayout);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mealName = mealNameEt.getText().toString();
                        String mealProducer = mealProducerEt.getText().toString();
                        int calories = Integer.parseInt(caloriesEt.getText().toString());
                        int mealServings = Integer.parseInt(mealServingsEt.getText().toString());

                        Meal.MealType type = Meal.MealType.Breakfast;
                        switch (typeRadioGroup.getCheckedRadioButtonId()) {
                            case R.id.breakfast_radio:
                                type = Meal.MealType.Breakfast;
                                mBreakfast.add(new Meal(mealName, mealProducer, mealServings, type, calories, DATE_FORMAT.format(mSelectedDate.getTime())));
                                mBreakfastMeals.setAdapter(new ArrayAdapter<Meal>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, mBreakfast));
                                break;
                            case R.id.lunch_radio:
                                type = Meal.MealType.Lunch;
                                mLunch.add(new Meal(mealName, mealProducer, mealServings, type, calories, DATE_FORMAT.format(mSelectedDate.getTime())));
                                mLunchMeals.setAdapter(new ArrayAdapter<Meal>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, mLunch));
                                break;
                            case R.id.dinner_radio:
                                type = Meal.MealType.Dinner;
                                mDinner.add(new Meal(mealName, mealProducer, mealServings, type, calories, DATE_FORMAT.format(mSelectedDate.getTime())));
                                mDinnerMeals.setAdapter(new ArrayAdapter<Meal>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, mDinner));
                                break;
                            default:
                                break;
                        }
                        refreshTotalCalories();
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();


            }
        });

        mBreakfastMeals = (Spinner)layout.findViewById(R.id.breakfast_menu);
        mBreakfastMeals.setOnItemSelectedListener(mFoodSelectedListener);
        mBreakfastMeals.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1,
                new String[]{"Add a meal"}));
        mBreakfastCalories = (TextView) layout.findViewById(R.id.calories_breakfast);

        mLunchMeals = (Spinner)layout.findViewById(R.id.lunch_menu);
        mLunchMeals.setOnItemSelectedListener(mFoodSelectedListener);
        mLunchMeals.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1,
                new String[]{"Add a meal"}));
        mLunchCalories = (TextView) layout.findViewById(R.id.calories_lunch);

        mDinnerMeals = (Spinner)layout.findViewById(R.id.dinner_menu);
        mDinnerMeals.setOnItemSelectedListener(mFoodSelectedListener);
        mDinnerMeals.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1,
                new String[]{"Add a meal"}));
        mDinnerCalories = (TextView) layout.findViewById(R.id.calories_dinner);

        mTotalCalories = (TextView)layout.findViewById(R.id.total_calories);
        refreshTotalCalories();

        mDailyBt = (Button) layout.findViewById(R.id.daily_schedule);
        mDailyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateStr = DATE_FORMAT.format(mSelectedDate.getTime());
                showMealsList(dateStr, true);
            }
        });
        mWeeklyBt = (Button)layout.findViewById(R.id.weekly_schedule);
        mWeeklyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateStr = DATE_FORMAT.format(mSelectedDate.getTime());
                showMealsList(dateStr, false);
            }
        });

        mDatePicker = (Button) layout.findViewById(R.id.pickup_date);
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the current date as the default date in the picker
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Create a new instance of DatePickerDialog and return it
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mDatePickerListener, year, month, day);
                datePickerDialog.show();
            }
        });

        mAddToCalendarBt = (Button)layout.findViewById(R.id.add_to_calendar_bt);
        mAddToCalendarBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Meal meal : mBreakfast)
                    mDBHandler.insertMeal(meal);

                for (Meal meal : mLunch)
                    mDBHandler.insertMeal(meal);

                for (Meal meal : mDinner)
                    mDBHandler.insertMeal(meal);
            }
        });

        return layout;
    }

    private void showMealsList(String date, boolean daily) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = FoodList.newInstance(date, daily);
        newFragment.show(ft, "dialog");
    }
}
