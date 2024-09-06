package com.utar.plantogo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.utar.plantogo.internal.APIRequest;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Switch NightSwitch, NotisSwitch;
    private ImageView Help, Terms, LogOut;
    private AppCompatButton Edit;
    private static final String SUPABASE_BASE_URL = BuildConfig.SUPABASE_BASE_URL;
    private static final String SUPABASE_KEY = BuildConfig.SUPABASE_API_KEY;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        NightSwitch = view.findViewById(R.id.S_Night);
        NotisSwitch = view.findViewById(R.id.S_Notis);

        Help = view.findViewById(R.id.IV_Help);
        Terms = view.findViewById(R.id.IV_Term);
        Edit = view.findViewById(R.id.EditAccBTN);
        LogOut = view.findViewById(R.id.IV_LogOut);


        Help.setOnClickListener(v -> navigateToFragment(new HelpFragment()));
        Terms.setOnClickListener(v -> navigateToFragment(new TermsAndConditionsFragment()));
        Edit.setOnClickListener(v -> navigateToFragment(new EditAccountFragment()));
        LogOut.setOnClickListener(v ->logoutUser());


        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Set the initial states based on SharedPreferences
        NightSwitch.setChecked(sharedPreferences.getBoolean("NightMode", false));
        NotisSwitch.setChecked(sharedPreferences.getBoolean("Notifications", false));

        // Set listener for Night Mode switch
        NightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Enable Night Mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("NightMode", true);
                    Toast.makeText(getActivity(), "Night Mode Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    // Disable Night Mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("NightMode", false);
                    Toast.makeText(getActivity(), "Night Mode Disabled", Toast.LENGTH_SHORT).show();
                }
                editor.apply();
            }
        });

        // Set listener for Notifications switch
        NotisSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Enable Notifications;
                    editor.putBoolean("Notifications", true);
                    Toast.makeText(getActivity(), "Notifications Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    // Disable Notifications
                    editor.putBoolean("Notifications", false);
                    Toast.makeText(getActivity(), "Notifications Disabled", Toast.LENGTH_SHORT).show();
                }
                editor.apply();
            }
        });

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.root_fragment_container, fragment); // Replace with your container ID
        fragmentTransaction.addToBackStack(null); // Add this transaction to the back stack
        fragmentTransaction.commit();
    }


    private void logoutUser() {
        try {

            String token = getToken(getContext());

            APIRequest apiRequest = new APIRequest(SUPABASE_BASE_URL + "/auth/v1/logout");
            apiRequest.setRequestMethod(APIRequest.REQUEST_METHOD.POST);
            apiRequest.addHeader("Content-Type", "application/json");
            apiRequest.addHeader("apikey", SUPABASE_KEY);
            apiRequest.addHeader("Authorization", token);

            apiRequest.makeRequest(new APIRequest.ResponseCallback() {
                @Override
                public void onSuccess(Map<String, Object> responseMap) {
                    // Handle success (e.g., navigate to login screen)
                    Log.d("LogoutUser", "User logged out successfully.");
                    clearToken(getContext()); // Clear token from SharedPreferences
                }

                @Override
                public void onError(Exception e) {
                    // Handle errors
                    Log.e("LogoutUser", "Failed to log out: " + e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void clearToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("auth_token");
        editor.apply();
    }

    private String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", null); // Returns null if no token found
    }

}