package com.example.myapplication.ui.past_trips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

public class TripDetailDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final int ARG_IMAGE_RES_ID = 0;

    public static TripDetailDialogFragment newInstance(String title, String description, int imageResId) {
        TripDetailDialogFragment fragment = new TripDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(String.valueOf(ARG_IMAGE_RES_ID), imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        View view = inflater.inflate(R.layout.fragment_trip_detail, container, false);

        if (getArguments() != null) {
            String title = getArguments().getString(ARG_TITLE);
            String description = getArguments().getString(ARG_DESCRIPTION);
            int imageResId = getArguments().getInt(String.valueOf(ARG_IMAGE_RES_ID));

            TextView titleView = view.findViewById(R.id.detailTitle);
            TextView descriptionView = view.findViewById(R.id.detailDescription);
            ImageView imageView = view.findViewById(R.id.detailImageView);

            titleView.setText(title);
            descriptionView.setText(description);
            imageView.setImageResource(imageResId);
        }

        return view;    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the dialog size and position here
        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.WRAP_CONTENT; // Set width to wrap content
            params.height = WindowManager.LayoutParams.WRAP_CONTENT; // Set height to wrap content
            params.dimAmount = 0.5f; // Dim background for focus
            getDialog().getWindow().setAttributes(params);
        }
    }
}

