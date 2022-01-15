package com.eraldguri.geophysicslab.navigation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.fragments.CompassFragment;

public class CoreLoggingFragment extends Fragment implements View.OnClickListener {

    private Spinner mRockTypesSpinner;
    private Spinner mMajorThickSpinner;
    private Spinner mMinorThickSpinner;
    private Spinner mMajorMinorSpinner;
    private Spinner mWeatherSpinner;
    private Spinner mSedimentaryStructureSpinner;
    private Spinner mNWSpinner;

    private ImageButton compassButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_core_logging, container, false);

        initViews(root);

        return root;
    }

    private void initViews(View view) {
        mRockTypesSpinner               = view.findViewById(R.id.rock_type_spinner);
        mMajorThickSpinner              = view.findViewById(R.id.major_thick_spinner);
        mMinorThickSpinner              = view.findViewById(R.id.minor_thick_spinner);
        mMajorMinorSpinner              = view.findViewById(R.id.major_minor_spinner);
        mWeatherSpinner                 = view.findViewById(R.id.weather_spinner);
        mSedimentaryStructureSpinner    = view.findViewById(R.id.sedimentary_structure_spinner);
        mNWSpinner                      = view.findViewById(R.id.n_w_spinner);
        compassButton                   = view.findViewById(R.id.compass_button);

        compassButton.setOnClickListener(this);

        setupSpinners();
    }

    private void compass() {
        Fragment compassFragment = new CompassFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_core_logging_container, compassFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupSpinners() {
        ArrayAdapter<String> rockTypesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                requireContext().getResources().getStringArray(R.array.rockTypes));
        ArrayAdapter<String> majorThickAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                requireContext().getResources().getStringArray(R.array.majorThicks));
        ArrayAdapter<String> minorThickAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                requireContext().getResources().getStringArray(R.array.majorThicks));
        ArrayAdapter<String> majorMinorAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                requireContext().getResources().getStringArray(R.array.major_minor));
        ArrayAdapter<String> weatherAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                requireContext().getResources().getStringArray(R.array.weather));
        ArrayAdapter<String> sedimentaryStructureAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                requireContext().getResources().getStringArray(R.array.sedimentary_structure));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.compass_button) {
            compass();
        }
    }
}