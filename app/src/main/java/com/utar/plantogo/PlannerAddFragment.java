package com.utar.plantogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.utar.plantogo.ui.planner.PlannerAddComponent;

public class PlannerAddFragment extends Fragment {

    public PlannerAddFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_planner, container, false);
        PlannerAddComponent plannerAddComponent = new PlannerAddComponent(requireContext());



        LinearLayout ll = view.findViewById(R.id.test2);
        ll.addView(plannerAddComponent);



        return  view;
    }


}
