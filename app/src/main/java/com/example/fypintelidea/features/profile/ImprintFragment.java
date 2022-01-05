package com.example.fypintelidea.features.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.fypintelidea.R;

public class ImprintFragment extends Fragment {

    public ImprintFragment() {

    }

    public static ImprintFragment newInstance() {
        return new ImprintFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_imprint, container, false);
    }
}
