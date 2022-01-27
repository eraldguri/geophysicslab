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

public class CoreLoggingFragment extends Fragment  {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_core_logging, container, false);


        return root;
    }

}