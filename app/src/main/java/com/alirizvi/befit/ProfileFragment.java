package com.alirizvi.befit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alirizvi.befit.data.MyDBHandler;
import com.alirizvi.befit.data.Profile;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


public class ProfileFragment extends Fragment {

    private MyDBHandler mDbHandler;
    private Profile mProfile;

    private EditText mFirstName;
    private EditText mLastName;
    private TextView mDateOfBirth;
    private EditText mAge;
    private RadioGroup mGender;
    private EditText mEmail;
    private EditText mWeight;
    private EditText mTargetWeight;
    private EditText mHeight;
    private TextView mBmi;
    private Button mSaveBt;

    private Calendar mSelectedDate = Calendar.getInstance();

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String s = editable.toString();
            computeAndSetAge(s);
        }
    };

    private DatePickerDialog.OnDateSetListener mDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet (DatePicker view, int year, int month, int day) {
            mSelectedDate = Calendar.getInstance();
            mSelectedDate.set(Calendar.YEAR, year);
            mSelectedDate.set(Calendar.MONTH, month);
            mSelectedDate.set(Calendar.DAY_OF_MONTH, day);
            mDateOfBirth.setText(Util.DATE_FORMAT.format(mSelectedDate.getTime()));
        }
    };

    private void computeAndSetAge(String dob) {
        if (dob.isEmpty()) {
            mAge.setText("Not available");
            return;
        }

        try {
            Date date = Util.DATE_FORMAT.parse(dob);

            Calendar dateOfBirth = Calendar.getInstance();
            dateOfBirth.setTime(date);

            Calendar currentDate = Calendar.getInstance();

            currentDate.add(Calendar.YEAR, -dateOfBirth.get(Calendar.YEAR));
            currentDate.add(Calendar.MONTH, -dateOfBirth.get(Calendar.MONTH));

            mAge.setText(String.valueOf(currentDate.get(Calendar.YEAR)));
        } catch (ParseException e) {
            e.printStackTrace();
            mAge.setText("Not available");
        }
    }

    private void computeBmi() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHandler = new MyDBHandler(getActivity());
        mProfile = mDbHandler.getProfile();

        Log.d("ProfileFragment", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("ProfileFragment", "onCreateView");
        View layout = inflater.inflate(R.layout.fragment_befit_profile, container, false);

        mSaveBt = (Button)layout.findViewById(R.id.save_bt);
        mSaveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        mFirstName = (EditText) layout.findViewById(R.id.firstname);
        mLastName = (EditText) layout.findViewById(R.id.lastname);
        mDateOfBirth = (TextView)layout.findViewById(R.id.dateofbirth);
        mDateOfBirth.addTextChangedListener(mTextWatcher);
        mDateOfBirth.setOnClickListener(new View.OnClickListener() {
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
        mAge = (EditText) layout.findViewById(R.id.myage);

        mGender = (RadioGroup) layout.findViewById(R.id.gender_radiogroup);
        mEmail = (EditText)layout.findViewById(R.id.myemail);
        mWeight = (EditText) layout.findViewById(R.id.myweight);
        mTargetWeight = (EditText)layout.findViewById(R.id.targetweight);
        mHeight = (EditText) layout.findViewById(R.id.height);
        mBmi = (TextView) layout.findViewById(R.id.bmi);

        if (mProfile != null) {
            mFirstName.setText(mProfile.getFirstName());
            mLastName.setText(mProfile.getLastName());
            mDateOfBirth.setText(mProfile.getDateOfBirth());
            computeAndSetAge(mDateOfBirth.getText().toString());

            mGender.check(mProfile.getGender() == Profile.Gender.Male ? R.id.radio_male : R.id.radio_female);
            mEmail.setText(mProfile.getEmail());
            mWeight.setText(String.valueOf(mProfile.getWeight()));
            mTargetWeight.setText(String.valueOf(mProfile.getTargetWeight()));
            mHeight.setText(String.valueOf(mProfile.getHeight()));
            mBmi.setText(String.valueOf(mProfile.getBmi()));
        }
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void save() {

        if (mProfile == null)
            mProfile = new Profile();

        mProfile.setFirstName(mFirstName.getText().toString());
        mProfile.setLastName(mLastName.getText().toString());
        mProfile.setDateOfBirth(mDateOfBirth.getText().toString());
        mProfile.setGender(mGender.getCheckedRadioButtonId() == R.id.radio_male ? Profile.Gender.Male : Profile.Gender.Female);
        mProfile.setEmail(mEmail.getText().toString());
        mProfile.setWeight(Integer.parseInt(mWeight.getText().toString().isEmpty() ? "0" : mWeight.getText().toString()));
        mProfile.setHeight(Float.parseFloat(mHeight.getText().toString().isEmpty() ? "0" : mHeight.getText().toString()));

        mDbHandler.insertOrUpdateProfile(mProfile);

        Toast.makeText(getActivity(), "Saved Profile", Toast.LENGTH_LONG).show();

    }
}
