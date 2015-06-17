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

import com.alirizvi.befit.data.Meal;
import com.alirizvi.befit.data.MyDBHandler;

import java.util.List;

public class FoodList extends DialogFragment {
    public static final String DATE_ARG = "date";
    public static final String DAILY_ARG = "mDaily";
    public static FoodList newInstance(String date, boolean daily) {
        FoodList f = new FoodList();
        Bundle args = new Bundle();
        args.putString(DATE_ARG, date);
        args.putBoolean(DAILY_ARG, daily);

        f.setArguments(args);
        return f;
    }

    private ListView mListView;
    private List<Meal> mMeals;

    private boolean mDaily;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String date = getArguments().getString(DATE_ARG);
        mDaily = getArguments().getBoolean(DAILY_ARG);

        MyDBHandler dbHandler = new MyDBHandler(getActivity());
        mMeals = mDaily ? dbHandler.getMealsForToday(date) :
                dbHandler.getMealsThisWeek(date);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_list, container, false);

        mListView = (ListView)layout.findViewById(R.id.listview);
        if (mDaily) {
            mListView.setAdapter(new ArrayAdapter<Meal>(getActivity(), android.R.layout.simple_list_item_1,
                    android.R.id.text1, mMeals));
        } else {
            layout.findViewById(R.id.tv_date).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.tv_name).setVisibility(View.VISIBLE);
            mListView.setAdapter(new WeeklyAdapter());
        }

        return layout;
    }

    private class WeeklyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMeals.size();
        }

        @Override
        public com.alirizvi.befit.data.Meal getItem(int i) {
            return mMeals.get(i);
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

            Meal meal = getItem(i);

            TextView name = (TextView)view.findViewById(R.id.name);
            TextView date = (TextView)view.findViewById(R.id.date);

            name.setText(meal.getName());
            date.setText(meal.getDate());

            return view;
        }
    }
}
