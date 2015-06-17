package com.alirizvi.befit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alirizvi.befit.data.ExerciseActivity;
import com.alirizvi.befit.data.MyDBHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityList extends DialogFragment {
    public static final String DATE_ARG = "date";
    public static final String DAILY_ARG = "mDaily";

    public static final SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("EEEE", Locale.US);

    public static ActivityList newInstance(String date, boolean daily) {
        ActivityList f = new ActivityList();
        Bundle args = new Bundle();
        args.putString(DATE_ARG, date);
        args.putBoolean(DAILY_ARG, daily);

        f.setArguments(args);
        return f;
    }

    private boolean mDaily;
    private ListView mListView;
    private List<ExerciseActivity> mActivities;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String date = getArguments().getString(DATE_ARG);
        mDaily = getArguments().getBoolean(DAILY_ARG);

        MyDBHandler dbHandler = new MyDBHandler(getActivity());
        mActivities = mDaily ? dbHandler.getActivitiesForToday(date) :
                dbHandler.getActivitiesForThisWeek(date);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_list, container, false);

        mListView = (ListView) layout.findViewById(R.id.listview);
        if (mDaily) {
            mListView.setAdapter(new ArrayAdapter<ExerciseActivity>(getActivity(), android.R.layout.simple_list_item_1,
                    android.R.id.text1, mActivities));
        } else {
            layout.findViewById(R.id.tv_date).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.tv_name).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.tv_kcals).setVisibility(View.VISIBLE);
            mListView.setAdapter(new WeeklyAdapter());
        }
        return layout;
    }

    private class WeeklyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mActivities.size();
        }

        @Override
        public ExerciseActivity getItem(int i) {
            return mActivities.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                view = inflater.inflate(R.layout.weekly_item, viewGroup, false);
            }

            ExerciseActivity activity = getItem(i);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView date = (TextView) view.findViewById(R.id.date);
            TextView calories = (TextView) view.findViewById(R.id.calories);

            try {
                Date d = Util.DATE_FORMAT.parse(activity.getDate());
                date.setText(OUTPUT_DATE_FORMAT.format(d));
            } catch (ParseException e) {
                e.printStackTrace();
                date.setText(activity.getDate());
            }

            name.setText(activity.getType());
            calories.setText(activity.getCaloriesBurned() + "kcal");

            return view;
        }
    }
}
