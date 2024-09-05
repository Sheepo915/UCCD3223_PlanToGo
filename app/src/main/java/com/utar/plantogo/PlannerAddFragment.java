package com.utar.plantogo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.internal.db.model.PlannedTrips;

import java.util.Calendar;

public class PlannerAddFragment extends Fragment {

    private EditText titleInput, destinationInput, startDateInput, endDateInput, notesInput;
    private Calendar calendar;

    public PlannerAddFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        calendar = Calendar.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_planner, container, false);

        titleInput = view.findViewById(R.id.ti_trip_name);
        destinationInput = view.findViewById(R.id.ti_location);
        startDateInput = view.findViewById(R.id.ti_start_date);
        endDateInput = view.findViewById(R.id.ti_end_date);
        notesInput = view.findViewById(R.id.ti_notes);

        // Set DatePicker for start date
        startDateInput.setOnClickListener(v -> showDatePickerDialog(startDateInput));

        // Set DatePicker for end date
        endDateInput.setOnClickListener(v -> showDatePickerDialog(endDateInput));

        Button startPlanningButton = view.findViewById(R.id.btn_add_trip);
        startPlanningButton.setOnClickListener(v -> savePlan());

        return view;
    }

    private void showDatePickerDialog(final EditText dateInput) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, yearSelected, monthSelected, dayOfMonthSelected) -> {
            String selectedDate = dayOfMonthSelected + "/" + (monthSelected + 1) + "/" + yearSelected;
            dateInput.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void savePlan() {
        String title = titleInput.getText().toString();
        String destination = destinationInput.getText().toString();
        String startDate = startDateInput.getText().toString();
        String endDate = endDateInput.getText().toString();
        String notes = notesInput.getText().toString();

        if (title.isEmpty() || destination.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase db = AppDatabase.getInstance(requireContext());
        new Thread(() -> {
            PlannedTrips plannedTrips = new PlannedTrips(title, destination, startDate, endDate, notes);

            db.plannedTripsDao().insertPlannedTrip(plannedTrips);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Plan Saved", Toast.LENGTH_SHORT).show();
            });
        }).start();

    }
}
