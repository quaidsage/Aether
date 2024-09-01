package com.example.myapplication.ui.past_trips;

import android.os.Bundle;
import android.util.Log;
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

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentPastTripsBinding;
import com.example.myapplication.ui.CardAdapter;
import com.example.myapplication.ui.CardFactory;
import com.example.myapplication.ui.CardItem;
import com.example.myapplication.ui.Trip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class PastTripsFragment extends Fragment implements CardAdapter.OnCardClickListener {

    private FragmentPastTripsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PastTripsViewModel pastTripsViewModel =
                new ViewModelProvider(this).get(PastTripsViewModel.class);

        binding = FragmentPastTripsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPastTrips;
        pastTripsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        FirebaseFirestore.setLoggingEnabled(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        String userId = "aaa";
        CollectionReference tripsRef = firestore.collection("users").document(userId).collection("trips");

        ProgressBar progressBar = binding.getRoot().findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        AtomicInteger totalEmission = new AtomicInteger();

        tripsRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        List<Trip> trips = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Trip trip = document.toObject(Trip.class);
                            totalEmission.addAndGet(trip.getEmission());

                            Log.d("Firestore", "Trip: " + trip.getType() + ", Emission: " + trip.getEmission());
                            trips.add(trip);
                        }

                        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(new CardAdapter(new CardFactory().createCardList(trips), this));

                        DocumentReference userRef = firestore.collection("users").document(userId);
                        userRef.update("totalEmission", totalEmission.get());

                    } else {
                        Log.d("Firestore", "Error getting documents: ", task.getException());
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