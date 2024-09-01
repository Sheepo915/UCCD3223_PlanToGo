package com.utar.plantogo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.utar.plantogo.example.nearbysearch.NearbySearchExample;
import com.utar.plantogo.ui.ProfileHeader;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Create an instance of NearbySearchExample
        NearbySearchExample example = new NearbySearchExample(getContext());
        Future<Map<String, Object>> futureResponse = null;
        futureResponse = example.loadExampleResponse();

        // Add TextView to the view hierarchy
        FrameLayout rootView = view.findViewById(R.id.home_fragment_container);
        TextView textView = new TextView(view.getContext());
        rootView.addView(textView);

        // Initially set the TextView to show "Loading..."
        textView.setText("Loading...");

        // Update the TextView once the future is done
        Future<Map<String, Object>> finalFutureResponse = futureResponse;
        example.executorService.submit(() -> {
            try {
                // Get the response map, this blocks until the future is done
                Map<String, Object> responseMap = finalFutureResponse.get();

                // Update the TextView on the UI thread
                requireActivity().runOnUiThread(() -> {
                    textView.setText(responseMap.toString());
                });

            } catch (Exception e) {
                // Handle any errors here, also on the UI thread
                requireActivity().runOnUiThread(() -> {
                    textView.setText(e.toString());
                });
            }
        });

        return view;
    }
}