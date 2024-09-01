package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SignInActivity;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.ui.CardAdapter;
import com.example.myapplication.ui.CardFactory;
import com.example.myapplication.ui.CardItem;
import com.example.myapplication.ui.Trip;
import com.example.myapplication.ui.past_trips.TripDetailDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements CardAdapter.OnCardClickListener {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userRef = db.collection("users").document("aaa");

        ProgressBar progressBar = binding.getRoot().findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String username = document.getString("username");
                    Long totalEmission = document.getLong("totalEmission");
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    assert totalEmission != null;
                    String totalEmissionFormatted = formatter.format(totalEmission.doubleValue());

                    formatter = new DecimalFormat("#,###.0");
                    String totalDistanceFormatted = formatter.format(totalEmission.doubleValue() * 101.24);

                    TextView welcomeMsg = root.findViewById(R.id.textTitle);
                    welcomeMsg.setText("Welcome back, " + username);

                    TextView totalEmissionMsg = root.findViewById(R.id.textTotalEmission);
                    totalEmissionMsg.setText("Total Emissions Saved: " + totalEmissionFormatted + " kt CO2");

                    TextView distanceMsg = root.findViewById(R.id.textDistance);
                    distanceMsg.setText("Total Distance: " + totalDistanceFormatted + " km");

                } else {
                    TextView welcomeMsg = root.findViewById(R.id.text_home);
                    welcomeMsg.setText(" User");
                }
            } else {
                // ...
            }
        });

        userRef.collection("trips")
                .limit(2)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Trip> trips = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert each document to a Trip object
                            Trip trip = document.toObject(Trip.class);
                            trips.add(trip);
                        }

                        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(new CardAdapter(new CardFactory().createCardList(trips), this));

                    } else {
                        // ...
                    }
                });

        return root;
    }

    @Override
    public void onCardClick(CardItem cardItem) {
        TripDetailDialogFragment dialogFragment = TripDetailDialogFragment.newInstance(cardItem.getTitle(), cardItem.getDescription(), cardItem.getImageResId());
        dialogFragment.show(requireActivity().getSupportFragmentManager(), "TripDetailDialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}