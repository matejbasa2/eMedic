package com.example.emedic.Doctor;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.emedic.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PacientiFragment extends Fragment {

    private TextView textView;

    public PacientiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pacienti_fragment, container, false);
        textView = view.findViewById(R.id.fragment_text_display);
        textView.setText(getArguments().getString("message"));
        return view;
    }

}
