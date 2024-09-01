package com.example.myapplication.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private List<LeaderboardItem> items;

    public LeaderboardAdapter(List<LeaderboardItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_card, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        private TextView usernameTextView;
        private TextView totalEmissionTextView;
        private ImageView rankImageView;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.textUsername);
            totalEmissionTextView = itemView.findViewById(R.id.textTotalEmission);
            rankImageView = itemView.findViewById(R.id.rankImageView);
        }

        public void bind(LeaderboardItem item) {
            usernameTextView.setText(item.getUsername());

            Long totalEmission = item.getTotalEmission();
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            String totalEmissionFormatted = formatter.format(totalEmission.doubleValue());
            String description = "Total Emissions Saved: " + totalEmissionFormatted + " kt CO2";
            totalEmissionTextView.setText(description);

            switch (item.getRank()) {
                case 1:
                    rankImageView.setImageResource(R.mipmap.first_place);
                    break;
                case 2:
                    rankImageView.setImageResource(R.mipmap.second_place);
                    break;
                case 3:
                    rankImageView.setImageResource(R.mipmap.third_place);
                    break;
                default:
                    break;
            }
        }
    }
}
