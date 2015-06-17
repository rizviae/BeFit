package com.alirizvi.befit;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;



@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {

    Fragment profile,nutrition,exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        profile = new ProfileFragment();
        nutrition = new NutritionFragment();
        exercise = new ExerciseFragment();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        actionBar.addTab(
                actionBar.newTab()
                        .setText("Profile")
                        .setTabListener(new MyTabListener<ProfileFragment>(this, "profile", ProfileFragment.class)));

        actionBar.addTab(
                actionBar.newTab()
                        .setText("Activities")
                        .setTabListener(new MyTabListener<ExerciseFragment>(this, "activities", ExerciseFragment.class)));

        actionBar.addTab(
                actionBar.newTab()
                        .setText("Nutrition")
                        .setTabListener(new MyTabListener<FoodFragment>(this, "nutrition", FoodFragment.class)));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_befit_profile, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public  class MyTabListener<T extends Fragment>
            implements ActionBar.TabListener {

        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        public MyTabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(R.id.container, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.detach(mFragment);
        }

    }
}
