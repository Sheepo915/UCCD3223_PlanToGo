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
import com.utar.plantogo.ui.planner.PlannerEditComponent;

public class PlannerEditFragment extends Fragment {

    public PlannerEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner_details, container, false);

        PlannerEditComponent plannerEditComponent = new PlannerEditComponent(requireContext());


        LinearLayout ll = view.findViewById(R.id.ll_nav_tab_container2);
//        LinearLayout ll2 = view.findViewById(R.id.ll_content_container2);
        ll.addView(plannerEditComponent);
//        ll2.addView(plannerEditComponent);



        return  view;
    }


}

