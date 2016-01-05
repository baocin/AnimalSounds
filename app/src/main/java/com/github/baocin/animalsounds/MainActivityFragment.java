package com.github.baocin.animalsounds;

import android.graphics.Color;
import android.media.MediaPlayer;
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
import java.util.HashMap;
import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MainActivityFragment";
    private static final int LEFT_ANIMAL_ID = 0;
    private static final int RIGHT_ANIMAL_ID = 1;
    public HashMap<String, Integer> animalImages = new HashMap<>();
    public HashMap<String, Integer> animalAudio = new HashMap<>();
    private static MediaPlayer mp = new MediaPlayer();

    public int correctAnimalID = 0;
    public ArrayList<String> pickedAnimals = new ArrayList<>();

    public MainActivityFragment() {
        animalImages.put("cat", R.drawable.cat_photo);
        animalImages.put("cow", R.drawable.cow_photo);
        animalImages.put("donkey", R.drawable.donkey_photo);
        animalImages.put("frog", R.drawable.frog_photo);
        animalImages.put("horse", R.drawable.horse_photo);
        animalImages.put("pig", R.drawable.pig_photo);
        animalImages.put("sheep", R.drawable.sheep_photo);

        animalAudio.put("cat", R.raw.cat);
        animalAudio.put("cow", R.raw.cow);
        animalAudio.put("donkey", R.raw.donkey);
        animalAudio.put("frog", R.raw.frog);
        animalAudio.put("horse", R.raw.horse);
        animalAudio.put("pig", R.raw.pig);
        animalAudio.put("sheep", R.raw.sheep);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        setupAnimals(v);

        return v;
    }
    public void resetAnimals(View v){
        //Stop audio (if playing)
        mp.stop();

        //Clear background colors
        ImageView leftAnimal = (ImageView) v.findViewById(R.id.leftImage);
        ImageView rightAnimal = (ImageView) v.findViewById(R.id.rightImage);

        leftAnimal.setBackgroundColor(Color.TRANSPARENT);
        rightAnimal.setBackgroundColor(Color.TRANSPARENT);

        TextView leftText = (TextView) v.findViewById(R.id.leftText);
        TextView rightText = (TextView) v.findViewById(R.id.rightText);

        leftText.setVisibility(View.INVISIBLE);
        rightText.setVisibility(View.INVISIBLE);

        leftText.setBackgroundColor(Color.TRANSPARENT);
        rightText.setBackgroundColor(Color.TRANSPARENT);

        leftText.setAllCaps(false);
        rightText.setAllCaps(false);
    }

    public void setupAnimals(View v){
        resetAnimals(v);

        ImageView leftAnimal = (ImageView) v.findViewById(R.id.leftImage);
        ImageView rightAnimal = (ImageView) v.findViewById(R.id.rightImage);
        leftAnimal.setOnClickListener(this);
        rightAnimal.setOnClickListener(this);

        pickedAnimals = pickNItems(animalImages.keySet(), 2);
        leftAnimal.setImageResource(animalImages.get(pickedAnimals.get(LEFT_ANIMAL_ID)));
        rightAnimal.setImageResource(animalImages.get(pickedAnimals.get(RIGHT_ANIMAL_ID)));

        TextView leftText = (TextView) v.findViewById(R.id.leftText);
        TextView rightText = (TextView) v.findViewById(R.id.rightText);
        leftText.setText(pickedAnimals.get(LEFT_ANIMAL_ID));
        rightText.setText(pickedAnimals.get(RIGHT_ANIMAL_ID));



        TextView questionText = (TextView) v.findViewById(R.id.questionText);
//        questionText.setText("Choose between the " + pickedAnimals.get(LEFT_ANIMAL_ID) + " and the " + pickedAnimals.get(RIGHT_ANIMAL_ID));
        questionText.setText("Choose the animal that makes this sound!");

        //pick the correct answer
        correctAnimalID = randomRange(LEFT_ANIMAL_ID, RIGHT_ANIMAL_ID); //pick either 0 and 1

        //Play the correct animal sound
        mp = MediaPlayer.create(this.getActivity(), animalAudio.get(pickedAnimals.get(correctAnimalID)));
        mp.start();
    }

    public void onClick(View v) {
        TextView leftText = (TextView) getActivity().findViewById(R.id.leftText);
        TextView rightText = (TextView) getActivity().findViewById(R.id.rightText);
        ImageView leftAnimal = (ImageView) getActivity().findViewById(R.id.leftImage);
        ImageView rightAnimal = (ImageView) getActivity().findViewById(R.id.rightImage);

        //Bold the correct animal name
        if (correctAnimalID == LEFT_ANIMAL_ID){
            leftText.setAllCaps(true);
            leftAnimal.setBackgroundColor(getResources().getColor(R.color.green));
            leftText.setBackgroundColor(getResources().getColor(R.color.green));
            rightAnimal.setBackgroundColor(getResources().getColor(R.color.red));
            rightText.setBackgroundColor(getResources().getColor(R.color.red));
        }else {
            rightText.setAllCaps(true);
            leftAnimal.setBackgroundColor(getResources().getColor(R.color.red));
            leftText.setBackgroundColor(getResources().getColor(R.color.red));
            rightAnimal.setBackgroundColor(getResources().getColor(R.color.green));
            rightText.setBackgroundColor(getResources().getColor(R.color.green));
        }

        if (v.getId() == R.id.leftImage){
            if (correctAnimalID == LEFT_ANIMAL_ID){
                //CORRECT
                Toast.makeText(v.getContext(), pickedAnimals.get(LEFT_ANIMAL_ID) + " is CORRECT!", Toast.LENGTH_SHORT).show();
                setupAnimals(getView());
            }else {
                Toast.makeText(v.getContext(), pickedAnimals.get(LEFT_ANIMAL_ID) + " is incorrect, TRY AGAIN?", Toast.LENGTH_SHORT).show();
            }
            leftText.setVisibility(View.VISIBLE);
            rightText.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.rightImage){
            if (correctAnimalID == RIGHT_ANIMAL_ID){
                //CORRECT
                Toast.makeText(v.getContext(), pickedAnimals.get(RIGHT_ANIMAL_ID) + " is CORRECT!", Toast.LENGTH_SHORT).show();
                setupAnimals(getView());
            }else {
                Toast.makeText(v.getContext(), pickedAnimals.get(RIGHT_ANIMAL_ID) + " is incorrect, TRY AGAIN?", Toast.LENGTH_SHORT).show();
            }
            leftText.setVisibility(View.VISIBLE);
            rightText.setVisibility(View.VISIBLE);
        }
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
