package com.eraldguri.geophysicslab.navigation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.eraldguri.geophysicslab.R;

public class CoreLoggingFragment extends Fragment {

    private Spinner mRockTypesSpinner;
    private Spinner mMajorThickSpinner;
    private Spinner mMinorThickSpinner;
    private Spinner mMajorMinorSpinner;
    private Spinner mWeatherSpinner;
    private Spinner mSedimentaryStructureSpinner;
    private Spinner mNWSpinner;

    private ArrayAdapter<String> rockTypesAdapter;
    private ArrayAdapter<String> majorThickAdapter;
    private ArrayAdapter<String> minorThickAdapter;
    private ArrayAdapter<String> majorMinorAdapter;
    private ArrayAdapter<String> weatherAdapter;
    private ArrayAdapter<String> sedimentaryStructureAdapter;

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

        setupSpinners();
    }

    private void setupSpinners() {
        rockTypesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                requireContext().getResources().getStringArray(R.array.rockTypes));
        majorThickAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                requireContext().getResources().getStringArray(R.array.majorThicks));
        minorThickAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                requireContext().getResources().getStringArray(R.array.majorThicks));
        majorMinorAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                requireContext().getResources().getStringArray(R.array.major_minor));
        weatherAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                requireContext().getResources().getStringArray(R.array.weather));
        sedimentaryStructureAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                requireContext().getResources().getStringArray(R.array.sedimentary_structure));
    }
}