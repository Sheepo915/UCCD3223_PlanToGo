package com.utar.plantogo;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.utar.plantogo.internal.APIRequest;
import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

import java.util.Map;
import java.util.Objects;

/**
 * This application will be ran in a single activity architecture
 * Why?
 * <ul>
 *     <li>Modularity:
 *          <ul>
 *              <li>Easier to manage, test, and implement</li>
 *          </ul>
 *     </li>
 *     <li>
 *         Fragment Transaction:
 *         <ul>
 *             <li>Data transaction between fragment can be done by implemented methods of FragmentManager</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h3>Developer Guide</h3>
 * <p>MainActivity.java will only be used for fragment navigation management purpose.</p>
 */
public class MainActivity extends AppCompatActivity {

    private static final String SUPABASE_BASE_URL = BuildConfig.SUPABASE_BASE_URL;
    private static final String SUPABASE_KEY = BuildConfig.SUPABASE_API_KEY;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final Handler handler = new Handler(Looper.getMainLooper());
    public String User_Token;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private OnBackPressedCallback onBackPressedCallback;
    private Runnable updateBottomNavRunnable;
    private FragmentViewModel fragmentViewModel;
    private View profileHeaderContainer;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentViewModel = new ViewModelProvider(this).get(FragmentViewModel.class);

        // Initialize database instance
        AppDatabase.getInstance(this);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // Initialize Toolbar and BottomNavigationView
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        TextView loginText = findViewById(R.id.tv_profile_header_action_text);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        profileHeaderContainer = findViewById(R.id.cl_profile_header_container);

        // Set BottomNavigationView listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                navigateToFragment(new HomeFragment(), true, true, true);
                if (profileHeaderContainer != null) {
                    profileHeaderContainer.setVisibility(View.VISIBLE);
                }
                return true;
            } else if (itemId == R.id.nav_planner) {
                navigateToFragment(new PlannerFragment(), true, true, false);
                if (profileHeaderContainer != null) {
                    profileHeaderContainer.setVisibility(View.GONE);
                }
                return true;
            } else if (itemId == R.id.nav_setting) {
                navigateToFragment(new SettingFragment(), true, true, false);
                if (profileHeaderContainer != null) {
                    profileHeaderContainer.setVisibility(View.GONE);
                }
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.root_fragment_container);

            if (isRootPage(currentFragment)) {
                if (currentFragment instanceof HomeFragment) {
                    profileHeaderContainer.setVisibility(View.VISIBLE);
                } else {
                    profileHeaderContainer.setVisibility(View.GONE);
                }
                bottomNavigationView.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            } else {
                profileHeaderContainer.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.GONE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisDialog();
            }
        });

        // Register the OnBackPressedCallback for handling Back Button
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    updateBottomNavigationView();
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
        toolbar.setNavigationOnClickListener(v -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                updateBottomNavigationView();
            } else {
                finish();
            }
        });
    }

    private boolean isRootPage(Fragment fragment) {
        return fragment instanceof HomeFragment || fragment instanceof PlannerFragment || fragment instanceof SettingFragment;
    }

    /**
     * This is a wrapper for navigation between fragments
     *
     * @param fragment      Fragment that will be navigated to
     * @param showBottomNav Configuration for enabling Bottom Navigation Bar
     * @param showActionBar Configuration for enabling Top App Bar
     */
    public void navigateToFragment(Fragment fragment, boolean showBottomNav, boolean showActionBar, boolean isRoot) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.root_fragment_container);

        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            // If the current fragment is the same as the new one, no need to navigate
            return;
        }
        transaction.replace(R.id.root_fragment_container, fragment);

        if (isRoot) {
            // Clear the back stack if this is the root fragment
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            // Add the transaction to the back stack
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
            transaction.addToBackStack(null);
        }

        transaction.commit();

        if (showBottomNav) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }

        if (showActionBar) {
            toolbar.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(!isRoot);
        } else {
            toolbar.setVisibility(View.GONE);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
            Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        }

        debounceUpdateBottomNavigationView(); // Ensure this is called after navigation
    }

    private void updateBottomNavigationView() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.root_fragment_container);

        if (currentFragment instanceof HomeFragment) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        } else if (currentFragment instanceof PlannerFragment) {
            bottomNavigationView.setSelectedItemId(R.id.nav_planner);
        } else if (currentFragment instanceof SettingFragment) {
            bottomNavigationView.setSelectedItemId(R.id.nav_setting);
        }
    }

    private void debounceUpdateBottomNavigationView() {
        if (updateBottomNavRunnable != null) {
            handler.removeCallbacks(updateBottomNavRunnable);
        }

        updateBottomNavRunnable = this::updateBottomNavigationView;
        handler.postDelayed(updateBottomNavRunnable, 100); // Delay in milliseconds
    }

    private void showRegisDialog() {
        // Inflate the first bottom sheet layout
        View bottomSheetView = getLayoutInflater().inflate(R.layout.layout_register_bottom_sheet, null);

        // Initialize the BottomSheetDialog
        BottomSheetDialog registerBottomSheetDialog = new BottomSheetDialog(this);
        registerBottomSheetDialog.setContentView(bottomSheetView);
        registerBottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        TextInputEditText userEmail = bottomSheetView.findViewById(R.id.ti_user_email);
        TextInputEditText userPassword = bottomSheetView.findViewById(R.id.ti_user_password);
        TextInputEditText userConfirmPassword = bottomSheetView.findViewById(R.id.ti_user_confirm_password);
        TextView loginButton = bottomSheetView.findViewById(R.id.btn_login);
        Button registerButton = bottomSheetView.findViewById(R.id.btn_register);

        loginButton.setOnClickListener(v -> {
            // Show the second bottom sheet for login
            showLoginBottomSheet();
            registerBottomSheetDialog.dismiss(); // Dismiss the first bottom sheet
        });

        registerButton.setOnClickListener(v -> {
            // Handle registration logic here
            String email = Objects.requireNonNull(userEmail.getText()).toString();
            String password = Objects.requireNonNull(userPassword.getText()).toString();
            String confirmPassword = Objects.requireNonNull(userConfirmPassword.getText()).toString();

            if (password.equals(confirmPassword)) {
                handleRegister(email, password);
                registerBottomSheetDialog.dismiss(); // Dismiss the first bottom sheet
            } else {
                Toast.makeText(this, "Password is not the same. ", Toast.LENGTH_SHORT).show();
            }

        });

        registerBottomSheetDialog.show();

    }

    private void showLoginBottomSheet() {
        // Inflate the second bottom sheet layout
        View loginSheetView = getLayoutInflater().inflate(R.layout.layout_login_bottom_sheet, null);

        // Initialize the BottomSheetDialog for login
        BottomSheetDialog loginBottomSheetDialog = new BottomSheetDialog(this);
        loginBottomSheetDialog.setContentView(loginSheetView);
        loginBottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        // Get references to the inputs and button
        TextInputEditText loginEmail = loginSheetView.findViewById(R.id.ti_login_email);
        TextInputEditText loginPassword = loginSheetView.findViewById(R.id.ti_login_password);
        Button loginButton = loginSheetView.findViewById(R.id.btn_login);
        TextView registerButton = loginSheetView.findViewById(R.id.btn_register);

        // Set onClick listener for the confirm login button
        loginButton.setOnClickListener(v -> {
            // Handle login confirmation logic here
            String email = Objects.requireNonNull(loginEmail.getText()).toString();
            String password = Objects.requireNonNull(loginPassword.getText()).toString();
            handleLogin(email, password);
            loginBottomSheetDialog.dismiss(); // Dismiss the login bottom sheet
        });

        registerButton.setOnClickListener(v -> {
            // Show the second bottom sheet for login
            showRegisDialog();
            loginBottomSheetDialog.dismiss(); // Dismiss the first bottom sheet
        });

        // Show the second BottomSheetDialog for login
        loginBottomSheetDialog.show();
    }

    private void handleLogin(String email, String password) {
        try {
            // Construct the APIRequest object with the Supabase token URL
            APIRequest apiRequest = new APIRequest(SUPABASE_BASE_URL + "/auth/v1/token?grant_type=password");

            // Set the request method to POST
            apiRequest.setRequestMethod(APIRequest.REQUEST_METHOD.POST);

            // Construct the JSON body for login
            String jsonBody = String.format("{\"email\": \"%s\", \"password\": \"%s\", \"grant_type\": \"password\"}", email, password);
            apiRequest.setRequestBody(jsonBody);

            // Add required headers
            apiRequest.addHeader("Content-Type", "application/json");
            apiRequest.addHeader("apikey", SUPABASE_KEY);


            // Make the request and handle the response
            apiRequest.makeRequest(new APIRequest.ResponseCallback() {
                @Override
                public void onSuccess(Map<String, Object> responseMap) {
                    String accessToken = (String) responseMap.get("access_token");

                    if (accessToken != null) {
                        // Successfully retrieved the token, you can now use it for authenticated requests
                        Log.d("LoginUser", "User logged in successfully. Access Token: " + accessToken);
                        saveToken(getApplicationContext(), accessToken);
                    } else {
                        Log.e("LoginUser", "Login successful but token not found in response.");
                    }
                }

                @Override
                public void onError(Exception e) {
                    // Handle errors
                    e.printStackTrace();
                    System.out.println("Login failed: " + e.getMessage());
                    // Show error message
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error initializing APIRequest: " + e.getMessage());
        }
    }

    private void handleRegister(String email, String password) {
        try {
            APIRequest apiRequest = new APIRequest(SUPABASE_BASE_URL + "/auth/v1/signup");
            apiRequest.setRequestMethod(APIRequest.REQUEST_METHOD.POST);

            String jsonBody = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);
            apiRequest.setRequestBody(jsonBody);

            apiRequest.addHeader("Content-Type", "application/json");
            apiRequest.addHeader("apikey", SUPABASE_KEY);

            apiRequest.makeRequest(new APIRequest.ResponseCallback() {
                @Override
                public void onSuccess(Map<String, Object> responseMap) {
                    // Handle successful registration
                    Log.d("SupabaseAuth", "Registration successful: " + responseMap.toString());
                    showLoginBottomSheet();
                }

                @Override
                public void onError(Exception e) {
                    // Handle errors
                    e.printStackTrace();
                    Log.e("SupabaseAuth", "Registration failed: " + e.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error initializing APIRequest: " + e.getMessage());
        }
    }

    private void saveToken(Context context, String token) {
        try {
            // Always use a valid context, for example, from an Activity or Fragment.
            SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("auth_token", token);
            editor.apply();
            Log.d("TokenWatcher", "Token:" + token);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any specific errors such as context-related issues.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Log.d("Location", "Permission denied");
            }
        }
    }

    private void getLastLocation() {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Get the last known location
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations, this can be null.
                if (location != null) {
                    // Get latitude and longitude
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Log the latitude and longitude
                    if (fragmentViewModel != null || latitude != 0.0 || longitude != 0.0) {
                        fragmentViewModel.setLatitude((float) latitude);
                        fragmentViewModel.setLongitude((float) longitude);
                    } else {
                        fragmentViewModel.setLatitude((float) 4.326199);
                        fragmentViewModel.setLongitude((float) 101.141494);
                        Log.d("Location", "ViewModel is not initialized");
                    }
                } else {
                    Log.d("Location", "Location is null");
                }
            }
        });
    }
}
