package com.utar.plantogo;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private OnBackPressedCallback onBackPressedCallback;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateBottomNavRunnable;
    private FragmentViewModel fragmentViewModel;
    private TextView LoginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Toolbar and BottomNavigationView
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        LoginText = findViewById(R.id.tv_profile_header_action_text);

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

        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
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

    private void showBottomDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}
