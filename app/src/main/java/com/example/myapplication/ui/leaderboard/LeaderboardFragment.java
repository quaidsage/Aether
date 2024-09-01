package com.example.myapplication.ui.leaderboard;

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
import com.example.myapplication.databinding.FragmentLeaderboardBinding;
import com.example.myapplication.ui.CardAdapter;
import com.example.myapplication.ui.CardFactory;
import com.example.myapplication.ui.LeaderboardAdapter;
import com.example.myapplication.ui.LeaderboardItem;
import com.example.myapplication.ui.Trip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LeaderboardFragment extends Fragment {

    private FragmentLeaderboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LeaderboardViewModel leaderboardViewModel =
                new ViewModelProvider(this).get(LeaderboardViewModel.class);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FirebaseFirestore.setLoggingEnabled(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        ProgressBar progressBar = binding.getRoot().findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        firestore.collection("users")
                .orderBy("totalEmission", Query.Direction.DESCENDING)
                .limit(10) // Limit to top 10 scores
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        QuerySnapshot querySnapshot = task.getResult();
                        int rank = 1;
                        if (querySnapshot != null) {
                            List<LeaderboardItem> leaderboard = new ArrayList<>();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String username = document.getString("username");
                                Long score = document.getLong("totalEmission");
                                LeaderboardItem item = new LeaderboardItem(username, score, rank);
                                leaderboard.add(item);
                                rank++;
                            }

                            RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(new LeaderboardAdapter(leaderboard));
                        }
                    } else {
                        // Handle the error
                        System.err.println("Error fetching leaderboard: " + task.getException().getMessage());
                    }
                });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}