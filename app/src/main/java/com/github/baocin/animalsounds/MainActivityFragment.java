package com.github.baocin.animalsounds;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MainActivityFragment";
    private static final int LEFT_ANIMAL_ID = 0;
    private static final int RIGHT_ANIMAL_ID = 1;
    private TextToSpeech ttobj;
    public HashMap<String, Integer> animalImages = new HashMap<>();
    public HashMap<String, Integer> animalAudio = new HashMap<>();
    private static MediaPlayer mp = new MediaPlayer();
    public int correctAnimalID = 0;
    public ArrayList<String> pickedAnimals = new ArrayList<>();
    private int rightClickCount = 0;
    private int leftClickCount = 0;

    public MainActivityFragment() {
        //Data!
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

        ttobj=new TextToSpeech(this.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttobj.setLanguage(Locale.US);

            }
        }
        );

        setupAnimals(v);

        return v;
    }

    public void resetAnimals(View v){
        leftClickCount = 0;
        rightClickCount = 0;

        //Stop audio (if playing)
        mp.stop();

        //Stop text to speach
        ttobj.stop();

        //get layout elements
        ImageView leftAnimal = (ImageView) v.findViewById(R.id.leftImage);
        ImageView rightAnimal = (ImageView) v.findViewById(R.id.rightImage);
        TextView leftText = (TextView) v.findViewById(R.id.leftText);
        TextView rightText = (TextView) v.findViewById(R.id.rightText);

        //Clear background colors
        leftAnimal.setBackgroundColor(Color.TRANSPARENT);
        rightAnimal.setBackgroundColor(Color.TRANSPARENT);


        //force animal name text to visible
        leftText.setVisibility(View.VISIBLE);
        rightText.setVisibility(View.VISIBLE);

        //Set animal text and image backgrounds to transparent(nothing)
        leftText.setBackgroundColor(Color.TRANSPARENT);
        rightText.setBackgroundColor(Color.TRANSPARENT);
        leftAnimal.setBackgroundColor(Color.TRANSPARENT);
        rightAnimal.setBackgroundColor(Color.TRANSPARENT);

        //all animal names are lowercase by default
        leftText.setAllCaps(false);
        rightText.setAllCaps(false);
    }

    public void setupAnimals(View v){
        //reset to sane defaults
        resetAnimals(v);

        //Get layout elements - images and text views
        ImageView leftAnimal = (ImageView) v.findViewById(R.id.leftImage);
        ImageView rightAnimal = (ImageView) v.findViewById(R.id.rightImage);
        TextView leftText = (TextView) v.findViewById(R.id.leftText);
        TextView rightText = (TextView) v.findViewById(R.id.rightText);
        TextView questionText = (TextView) v.findViewById(R.id.questionText);

        //randomize next images
        pickedAnimals = pickNItems(animalImages.keySet(), 2);

        //pick the correct answer
        correctAnimalID = randomRange(LEFT_ANIMAL_ID, RIGHT_ANIMAL_ID); //pick either 0 and 1

        //Set on click handlers for both images
        leftAnimal.setOnClickListener(this);
        rightAnimal.setOnClickListener(this);

        //set animal images
        leftAnimal.setImageResource(animalImages.get(pickedAnimals.get(LEFT_ANIMAL_ID)));
        rightAnimal.setImageResource(animalImages.get(pickedAnimals.get(RIGHT_ANIMAL_ID)));

        //set animal name text
        leftText.setText(pickedAnimals.get(LEFT_ANIMAL_ID));
        rightText.setText(pickedAnimals.get(RIGHT_ANIMAL_ID));

        questionText.setText("Tap the image of a " + pickedAnimals.get(correctAnimalID) + ".");

        //Stop audio!
        mp.stop();

        //Say the question
        ttobj.speak(questionText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

    }

    public void onClick(View v) {
        TextView leftText = (TextView) getActivity().findViewById(R.id.leftText);
        TextView rightText = (TextView) getActivity().findViewById(R.id.rightText);
        ImageView leftAnimal = (ImageView) getActivity().findViewById(R.id.leftImage);
        ImageView rightAnimal = (ImageView) getActivity().findViewById(R.id.rightImage);

        //Stop audio!
        mp.stop();

        //Set the correct animal text view to all caps, and color the animal image backgrounds
        if (correctAnimalID == LEFT_ANIMAL_ID){
            leftText.setAllCaps(true);
            leftAnimal.setBackgroundColor(getResources().getColor(R.color.green));
            leftText.setBackgroundColor(getResources().getColor(R.color.green));
            rightAnimal.setBackgroundColor(getResources().getColor(R.color.red));
            rightText.setBackgroundColor(getResources().getColor(R.color.red));
//            leftAnimal.setBackgroundResource(R.drawable.green_circle);
//            rightAnimal.setBackgroundResource(R.drawable.red_circle);
        }else {
            rightText.setAllCaps(true);
            leftAnimal.setBackgroundColor(getResources().getColor(R.color.red));
            leftText.setBackgroundColor(getResources().getColor(R.color.red));
            rightAnimal.setBackgroundColor(getResources().getColor(R.color.green));
            rightText.setBackgroundColor(getResources().getColor(R.color.green));
        }

        if (v.getId() == R.id.leftImage){
            leftClickCount++;

            //Some Animation!
//            Animation imageShakeAnim = (Animation) AnimationUtils.loadAnimation(getContext(), R.anim.image_shake);
//            leftAnimal.startAnimation(imageShakeAnim);

            if (correctAnimalID == LEFT_ANIMAL_ID){
                //CORRECT
                Toast.makeText(v.getContext(), pickedAnimals.get(LEFT_ANIMAL_ID) + " is CORRECT!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(v.getContext(), pickedAnimals.get(LEFT_ANIMAL_ID) + " is incorrect, TRY AGAIN?", Toast.LENGTH_SHORT).show();
            }
//            leftText.setVisibility(View.VISIBLE);
//            rightText.setVisibility(View.VISIBLE);
            mp = MediaPlayer.create(this.getActivity(), animalAudio.get(pickedAnimals.get(LEFT_ANIMAL_ID)));
            mp.start();
        }

        if (v.getId() == R.id.rightImage){
            rightClickCount++;

            //Some Animation!
//            imageShakeAnim.setTarget(R.id.rightImage);
//            imageShakeAnim.start();

            if (correctAnimalID == RIGHT_ANIMAL_ID){
                //CORRECT
                Toast.makeText(v.getContext(), pickedAnimals.get(RIGHT_ANIMAL_ID) + " is CORRECT!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(v.getContext(), pickedAnimals.get(RIGHT_ANIMAL_ID) + " is incorrect, TRY AGAIN?", Toast.LENGTH_SHORT).show();
            }
//            leftText.setVisibility(View.VISIBLE);
//            rightText.setVisibility(View.VISIBLE);
            mp = MediaPlayer.create(this.getActivity(), animalAudio.get(pickedAnimals.get(RIGHT_ANIMAL_ID)));
            mp.start();
        }

        if ((leftClickCount + rightClickCount) > 2){
            Toast.makeText(v.getContext(), "Click Count: " + leftClickCount + "/" + rightClickCount, Toast.LENGTH_SHORT).show();

            //Play the correct animal sound
//            mp = MediaPlayer.create(this.getActivity(), animalAudio.get(pickedAnimals.get(correctAnimalID)));
//            mp.start();

            Log.d(TAG, "Milliseconds left: " + (mp.getDuration() - mp.getCurrentPosition()));
            //Wait until audio is finished
            try {
                Log.d(TAG, "Sleeping for a second, generating next question.");
                Thread.sleep((mp.getDuration() - mp.getCurrentPosition()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Generate new question
            setupAnimals(getView());
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


    @Override
    public void onDestroy() {
        //Close the Text to Speech Library
        if(ttobj != null) {
            ttobj.stop();
            ttobj.shutdown();
            Log.d(TAG, "Text to Speach Destroyed");
        }
        super.onDestroy();
    }

}
