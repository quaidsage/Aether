package com.example.myapplication.ui;

import android.widget.ImageView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class CardFactory {
    int iteration = 0;

    public CardItem createCard(String type, int emission) {
        iteration++;
        String title = "Trip #" + iteration;
        String description;
        String transport;
        switch (type.toLowerCase()) {
            case "walk":
                transport = "walking";
                break;
            case "drive":
                transport = "driving";
                break;
            case "bike":
                transport = "biking";
                break;
            case "swim":
                transport = "swimming";
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
        int imgID;

        if (emission > 0) {
            description = "You prevented " + emission + " kt of carbon emissions by " + transport + "!";
            imgID = R.mipmap.ic_launcher;
        } else {
            description = "You contributed " + Math.abs(emission) + " kt of carbon emissions by " + transport + ".";
            imgID = R.mipmap.dark_icon;
        }
        return new CardItem(title,description, imgID);
    }

    public List<CardItem> createCardList(List<Trip> trips) {
        List<CardItem> cards = new ArrayList<>();
        for (Trip trip : trips) {
            cards.add(createCard(trip.getType(), trip.getEmission()));
        }

        Collections.reverse(cards);

        return cards;
    }
}
