package com.example.fypintelidea.features.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.example.fypintelidea.R;

public class DataSecurityFragment extends Fragment {

    public DataSecurityFragment() {
    }

    public static DataSecurityFragment newInstance() {
        return new DataSecurityFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_security, container, false);
    }
}
