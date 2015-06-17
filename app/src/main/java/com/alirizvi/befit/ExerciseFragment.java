package com.alirizvi.befit;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alirizvi.befit.data.ActivityType;
import com.alirizvi.befit.data.ExerciseActivity;
import com.alirizvi.befit.data.MyDBHandler;

import java.util.Calendar;
import java.util.List;


public class ExerciseFragment extends Fragment {

    private Button mDailyBt;
    private Button mWeeklyBt;
    private Button mDatePicker;
    private Spinner mActivitiesSp;
    private RadioGroup mDurationGroup;
    private Button mNewExerciseBt;
    private Button mAddBt;
    private TextView mDateTv;

    private Calendar mSelectedDate = Calendar.getInstance();

    private MyDBHandler mDBHandler;
    private List<ActivityType> mActivitites;

    private DatePickerDialog.OnDateSetListener mDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet (DatePicker view, int year, int month, int day) {
            mSelectedDate = Calendar.getInstance();
            mSelectedDate.set(Calendar.YEAR, year);
            mSelectedDate.set(Calendar.MONTH, month);
            mSelectedDate.set(Calendar.DAY_OF_MONTH, day);
            mDateTv.setText(Util.DATE_FORMAT.format(mSelectedDate.getTime()));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBHandler = new MyDBHandler(getActivity());
    }

    private void updateActivitiesList() {
        mActivitites = mDBHandler.getActivityTypes();
        mActivitiesSp.setAdapter(new ArrayAdapter<ActivityType>(getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1, mActivitites));
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_activities, container, false);

        mDateTv = (TextView)layout.findViewById(R.id.date_text);
        mDateTv.setText(Util.DATE_FORMAT.format(mSelectedDate.getTime()));

        mDailyBt = (Button) layout.findViewById(R.id.daily_schedule);
        mDailyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateStr = Util.DATE_FORMAT.format(mSelectedDate.getTime());
                showActivitiesList(dateStr, true);
            }
        });
        mWeeklyBt = (Button)layout.findViewById(R.id.weekly_schedule);
        mWeeklyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateStr = Util.DATE_FORMAT.format(mSelectedDate.getTime());
                showActivitiesList(dateStr, false);
            }
        });

        mDatePicker = (Button) layout.findViewById(R.id.pickup_date);
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the current date as the default date in the picker
                final Calendar c = mSelectedDate;
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Create a new instance of DatePickerDialog and return it
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mDatePickerListener, year, month, day);
                datePickerDialog.show();
            }
        });

        mActivitiesSp = (Spinner) layout.findViewById(R.id.activities_list);
        updateActivitiesList();

        mDurationGroup = (RadioGroup) layout.findViewById(R.id.duration_radio_group);
        mNewExerciseBt = (Button) layout.findViewById(R.id.new_activity_bt);
        mNewExerciseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogLayout = inflater.inflate(R.layout.add_activity_layout, null);
                final EditText nameEt = (EditText) dialogLayout.findViewById(R.id.activity_type);
                final EditText caloriesEt = (EditText)dialogLayout.findViewById(R.id.calories_burned);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogLayout)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String type = nameEt.getText().toString();
                                int calories = Integer.parseInt(caloriesEt.getText().toString());
                                mDBHandler.insertNewActivityType(new ActivityType(type, calories));
                                dialogInterface.dismiss();
                                updateActivitiesList();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        mAddBt = (Button)layout.findViewById(R.id.add_activity_bt);
        mAddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActivitites.size() == 0) {
                    Toast.makeText(getActivity(), "Try adding an activity first", Toast.LENGTH_LONG).show();
                    return;
                }

                ActivityType activity = mActivitites.get(mActivitiesSp.getSelectedItemPosition());
                String type = activity.getName();
                int caloriesBurned = activity.getCaloriesBurned();

                int duration = getDuration();
                String date = Util.DATE_FORMAT.format(mSelectedDate.getTime());

                int caloriesBurnedTotal = (int) ((caloriesBurned / 10.0) * duration);

                mDBHandler.insertActivity(new ExerciseActivity("", type,date, duration, caloriesBurnedTotal));
                Toast.makeText(getActivity(), "Activity has been added!", Toast.LENGTH_LONG).show();
            }
        });

        return layout;
    }

    private void showActivitiesList(String date, boolean daily) {
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
        DialogFragment newFragment = ActivityList.newInstance(date, daily);
        newFragment.show(ft, "dialog");
    }

    private int getDuration() {
        int id = mDurationGroup.getCheckedRadioButtonId();
        if (id == R.id.min_30_radio)
            return 30;
        else if (id == R.id.min_45_radio)
            return 45;
        else if (id == R.id.min_60_radio)
            return 60;
        else if (id == R.id.min_75_radio)
            return 75;
        else if (id == R.id.min_90_radio)
            return 90;
        else
            return 90;
    }
}
