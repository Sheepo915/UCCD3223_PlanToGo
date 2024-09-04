package com.utar.plantogo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.utar.plantogo.internal.APIRequest;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

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

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private OnBackPressedCallback onBackPressedCallback;
    private Runnable updateBottomNavRunnable;
    private FragmentViewModel fragmentViewModel;

    private static final String SUPABASE_URL = BuildConfig.SUPABASE_BASE_URL;
    private static final String SUPABASE_KEY = BuildConfig.SUPABASE_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Toolbar and BottomNavigationView
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        TextView loginText = findViewById(R.id.tv_profile_header_action_text);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        View profileHeaderContainer = findViewById(R.id.cl_profile_header_container);

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

            if (currentFragment instanceof AttractionFragment) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                bottomNavigationView.setVisibility(View.GONE);
                profileHeaderContainer.setVisibility(View.GONE);
            } else if (currentFragment instanceof SearchFragment) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                bottomNavigationView.setVisibility(View.GONE);
                profileHeaderContainer.setVisibility(View.GONE);
            } else if (currentFragment instanceof HomeFragment) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                bottomNavigationView.setVisibility(View.VISIBLE);
                profileHeaderContainer.setVisibility(View.VISIBLE);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                bottomNavigationView.setVisibility(View.VISIBLE);
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
            toolbar.setNavigationOnClickListener(v -> {
                if (isRoot) {
                    return;
                }
                if (onBackPressedCallback.isEnabled()) {
                    onBackPressedCallback.handleOnBackPressed();
                }
            });
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
        View bottomSheetView = getLayoutInflater().inflate(R.layout.registerbottomsheet, null);

        // Initialize the BottomSheetDialog
        BottomSheetDialog RegisBottomSheetDialog = new BottomSheetDialog(this);
        RegisBottomSheetDialog.setContentView(bottomSheetView);

        TextInputEditText UserName = bottomSheetView.findViewById(R.id.UserName);
        TextInputEditText UserPass = bottomSheetView.findViewById(R.id.UserPassword);
        TextInputEditText UserConPass = bottomSheetView.findViewById(R.id.UserConfirmPassword);
        TextView Login = bottomSheetView.findViewById(R.id.LoginBtn);
        Button RegisBtn =findViewById(R.id.RegisterBtn);

        Login.setOnClickListener(v -> {
            // Show the second bottom sheet for login
            showLoginBottomSheet();
            RegisBottomSheetDialog.dismiss(); // Dismiss the first bottom sheet
        });

        RegisBtn.setOnClickListener(v -> {
            // Handle registration logic here
            String name = UserName.getText().toString();
            String password = UserPass.getText().toString();
            String Conpassword = UserConPass.getText().toString();

            if(password.equals(Conpassword)){
                handleRegister(name, password);
                RegisBottomSheetDialog.dismiss(); // Dismiss the first bottom sheet
            }
            else{
                Toast.makeText(this, "Password is not the same. " , Toast.LENGTH_SHORT).show();
            }

        });

        RegisBottomSheetDialog.show();

    }

    private void showLoginBottomSheet() {
        // Inflate the second bottom sheet layout
        View loginSheetView = getLayoutInflater().inflate(R.layout.bottomsheetlayout, null);

        // Initialize the BottomSheetDialog for login
        BottomSheetDialog loginBottomSheetDialog = new BottomSheetDialog(this);
        loginBottomSheetDialog.setContentView(loginSheetView);

        // Get references to the inputs and button
        TextInputEditText UserName = loginSheetView.findViewById(R.id.LoginName);
        TextInputEditText LoginPassword = loginSheetView.findViewById(R.id.LoginPassword);
        Button LoginBtn = loginSheetView.findViewById(R.id.LoginBtn);
        TextView RegisBTN = loginSheetView.findViewById(R.id.RegisterBtn);

        // Set onClick listener for the confirm login button
        LoginBtn.setOnClickListener(v -> {
            // Handle login confirmation logic here
            String username = UserName.getText().toString();
            String loginPassword = LoginPassword.getText().toString();
            handleLogin(username, loginPassword);
            loginBottomSheetDialog.dismiss(); // Dismiss the login bottom sheet
        });

        RegisBTN.setOnClickListener(v -> {
            // Show the second bottom sheet for login
            showRegisDialog();
            loginBottomSheetDialog.dismiss(); // Dismiss the first bottom sheet
        });

        // Show the second BottomSheetDialog for login
        loginBottomSheetDialog.show();
    }

    private void handleLogin(String username, String password) {
        // Perform login actions (e.g., validate credentials against Supabase or local storage)
        // Placeholder: Show a toast for now
        Toast.makeText(this, "Logging in with: " + username, Toast.LENGTH_SHORT).show();

        // Navigate to the main activity or perform desired action on successful login
    }

    private void handleRegister(String name, String password) {
        // Perform registration actions (e.g., save credentials to Supabase)
        try {
            String jsonBody = String.format("{\"name\": \"%s\", \"password\": \"%s\"}", name, password);
            APIRequest apiRequest = new APIRequest("https://bcmbrswetxlzoujdpcsw.supabase.co");
            apiRequest.addHeader("Content-Type", "application/json");
            apiRequest.addHeader("apikey", SUPABASE_KEY);
            apiRequest.addHeader("Authorization", "Bearer " + SUPABASE_KEY);
            apiRequest.setRequestMethod(APIRequest.REQUEST_METHOD.POST);
            apiRequest.setRequestBody(jsonBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Placeholder: Show a toast for now
        Toast.makeText(this, "Registering with: " + name, Toast.LENGTH_SHORT).show();

        // Navigate to the main activity or perform desired action on successful registration
    }
}
