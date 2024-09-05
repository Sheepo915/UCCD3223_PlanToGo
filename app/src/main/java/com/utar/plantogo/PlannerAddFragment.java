package com.utar.plantogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.utar.plantogo.ui.planner.PlannerAddComponent;

public class PlannerAddFragment extends Fragment {

    public PlannerAddFragment() {
    }

    private EditText titleInput, destinationInput, startDateInput, endDateInput, notesInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_planner, container, false);
        PlannerAddComponent plannerAddComponent = new PlannerAddComponent(requireContext());

        titleInput = plannerAddComponent.findViewById(R.id.editTextText);
        destinationInput = plannerAddComponent.findViewById(R.id.editTextText2);
        startDateInput = plannerAddComponent.findViewById(R.id.editTextDate); // This is only for one date, you can create another input for the end date.
        notesInput = plannerAddComponent.findViewById(R.id.editTextText5);

        Button startPlanningButton = plannerAddComponent.findViewById(R.id.button);
        startPlanningButton.setOnClickListener(v -> savePlan());

        LinearLayout ll = view.findViewById(R.id.test2);
        ll.addView(plannerAddComponent);


        return view;
    }

    private void savePlan() {
        String title = titleInput.getText().toString();
        String destination = destinationInput.getText().toString();
        String startDate = startDateInput.getText().toString();  // You may want to use a DatePicker here
        String endDate = "2023-12-31"; // Placeholder, use a similar EditText or DatePicker for end date
        String notes = notesInput.getText().toString();

        if (title.isEmpty() || destination.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(requireContext(), "Plan Saved", Toast.LENGTH_SHORT).show();


    }
}
