package com.github.baocin.animalsounds;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MainActivityFragment";
    public HashMap<String, Integer> animals = new HashMap<>();
    public int correctAnimal;

    public MainActivityFragment() {
        animals.put("cat", R.drawable.cat_photo);
        animals.put("cow", R.drawable.cow_photo);
        animals.put("donkey", R.drawable.donkey_photo);
        animals.put("frog", R.drawable.frog_photo);
        animals.put("horse", R.drawable.horse_photo);
        animals.put("pig", R.drawable.pig_photo);
        animals.put("sheep", R.drawable.sheep_photo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        ImageView leftAnimal = (ImageView) v.findViewById(R.id.leftImage);
        ImageView rightAnimal = (ImageView) v.findViewById(R.id.rightImage);
        leftAnimal.setOnClickListener(this);

        ArrayList<String> pickedAnimals = pickNItems(animals.keySet(), 2);
        leftAnimal.setImageResource(animals.get(pickedAnimals.get(0)));
        rightAnimal.setImageResource(animals.get(pickedAnimals.get(1)));

        TextView leftText = (TextView) v.findViewById(R.id.leftText);
        TextView rightText = (TextView) v.findViewById(R.id.rightText);
        leftText.setText(pickedAnimals.get(0));
        rightText.setText(pickedAnimals.get(1));

        leftText.setVisibility(View.INVISIBLE);
        rightText.setVisibility(View.INVISIBLE);

        TextView questionText = (TextView) v.findViewById(R.id.questionText);
        questionText.setText("Choose between the " + pickedAnimals.get(0) + " and the " + pickedAnimals.get(1));

        return v;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.leftImage){
            Toast.makeText(v.getContext(), "L", Toast.LENGTH_LONG).show();
        }
            Toast.makeText(v.getContext(),
                    "T" + v.getId(),
                    Toast.LENGTH_LONG).show();

    }

    public void playAnimalSound(){

    }

    public int randomRange(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public <T> T pickRandom(Collection<T> collection) {
        ArrayList<T> array = new ArrayList<>(collection);
        T item = array.get(randomRange(0, array.size() - 1));
        return item;
    }

    public <T> ArrayList<T> pickNItems(Collection<T> collection, int n){
        if (n > collection.size()) return null;
        ArrayList<T> rawCollection = new ArrayList<>(collection);
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < n; i++){
            int randomIndex = randomRange(0, rawCollection.size() - 1);
            result.add(rawCollection.get(randomIndex));
            rawCollection.remove(randomIndex);
        }
        return result;
    }




}
