package com.utar.plantogo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.utar.plantogo.internal.APIRequest;

public class EditAccountFragment extends Fragment {
    public EditAccountFragment() {}

    private String Name,Number;
    private static final String SUPABASE_BASE_URL = BuildConfig.SUPABASE_BASE_URL;
    private static final String SUPABASE_KEY = BuildConfig.SUPABASE_API_KEY;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);
        ImageView backImageView = view.findViewById(R.id.Account_Back);

        Button saveAcc = view.findViewById(R.id.Account_Save);
        Name = String.valueOf(view.findViewById(R.id.Display_Name));
        Number = String.valueOf(view.findViewById(R.id.Phone_Num));

        saveAcc.setOnClickListener(v ->{
            String UserName = Name;
            String Phone = Number;
            try {
                handleSave(UserName,Phone);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        // Set OnClickListener for back navigation
        backImageView.setOnClickListener(v -> {
            // Go back to the previous fragment
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void handleSave(String UserName, String Phone) throws Exception {
        try{
            APIRequest apiRequest = new APIRequest(SUPABASE_BASE_URL + "/auth/v1/user");

            // Set the request method to POST
            apiRequest.setRequestMethod(APIRequest.REQUEST_METHOD.POST);

            String token = getToken(requireContext());

            // Construct the JSON body
            String jsonBody = String.format("{\"phone\": \"" + Phone + "\", \"data\": {\"displayName\": \" + "+ UserName +"\"}}");
            apiRequest.setRequestBody(jsonBody);

            // Add required headers
            apiRequest.addHeader("Content-Type", "application/json");
            apiRequest.addHeader("apikey", SUPABASE_KEY);
            apiRequest.addHeader("Authorization", "Bearer " + token);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error initializing APIRequest: " + e.getMessage());
        }
    }

    private String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", null); // Returns null if no token found
    }
}
