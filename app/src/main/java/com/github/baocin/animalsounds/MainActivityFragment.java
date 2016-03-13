package com.github.baocin.animalsounds;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
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
import java.util.Locale;
import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MainActivityFragment";
    private static final int LEFT_ANIMAL_ID = 0;
    private static final int RIGHT_ANIMAL_ID = 1;
    public HashMap<String, Integer> animalImages = new HashMap<>();
    private AudioThread audioThread;
    private Handler mainHandler;
    public int correctAnimalID = 0;
    public ArrayList<String> pickedAnimals = new ArrayList<>();
    public static final int MUSIC_DONE = 1;

    public MainActivityFragment() {
        //Data!
        animalImages.put("cat", R.drawable.cat_photo);
        animalImages.put("cow", R.drawable.cow_photo);
        animalImages.put("donkey", R.drawable.donkey_photo);
        animalImages.put("frog", R.drawable.frog_photo);
        animalImages.put("horse", R.drawable.horse_photo);
        animalImages.put("pig", R.drawable.pig_photo);
        animalImages.put("sheep", R.drawable.sheep_photo);
        animalImages.put("fox", R.drawable.fox_photo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mainHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch(msg.what){
                    case MUSIC_DONE:
                        Log.d(TAG, "music done");
                        //Generate new question
                        setupAnimals(getView());
                        break;
                }
                return false;
            }
        });

        //Start the background audio thread  (to prevent blocking ui)
        audioThread = new AudioThread(getContext(), mainHandler);
        audioThread.start();



        setupAnimals(v);

        return v;
    }

    public void resetAnimals(View v){

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
        Log.d(TAG, "Picked " + pickedAnimals);

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

        //Ask the question
        sendMessage(AudioThread.SPEAK_STATUS, "Tap the image of a " + pickedAnimals.get(correctAnimalID) + ".");

    }

    public void onClick(View v) {
        TextView leftText = (TextView) getActivity().findViewById(R.id.leftText);
        TextView rightText = (TextView) getActivity().findViewById(R.id.rightText);
        ImageView leftAnimal = (ImageView) getActivity().findViewById(R.id.leftImage);
        ImageView rightAnimal = (ImageView) getActivity().findViewById(R.id.rightImage);

        //Stop audio!
        sendMessage(AudioThread.STOP_STATUS, "");

        Log.d(TAG, "Testing: " + correctAnimalID + " vs " + LEFT_ANIMAL_ID + " or " + RIGHT_ANIMAL_ID + " " + v.getId());

        //Set correct choice to all caps
        if (correctAnimalID == LEFT_ANIMAL_ID){
            leftText.setAllCaps(true);
        }else {
            rightText.setAllCaps(true);
        }

        //Set the correct animal text view to all caps, and color the animal image backgrounds
        if ((correctAnimalID == LEFT_ANIMAL_ID && v.getId() == R.id.leftImage) || (correctAnimalID == RIGHT_ANIMAL_ID && v.getId() == R.id.rightImage)) {
            sendMessage(AudioThread.SPEAK_STATUS, "Correct!");
            sendMessage(AudioThread.PLAY_STATUS, pickedAnimals.get(correctAnimalID));

//            setupAnimals(getView());
        }else{
            if (v.getId() == R.id.leftImage){
                sendMessage(AudioThread.PLAY_STATUS, pickedAnimals.get(LEFT_ANIMAL_ID));
            }else{
                sendMessage(AudioThread.PLAY_STATUS, pickedAnimals.get(RIGHT_ANIMAL_ID));
            }
        }

    }

    public void sendMessage(int command, String data){
        if (audioThread.handler != null) {
            Bundle bundle = new Bundle();
            bundle.putString("data", data);
            Log.d(TAG, command + "  " + data);
            Message message = new Message();
            message.what = command;
            message.setData(bundle);
            audioThread.handler.sendMessage(message);
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
//        audioThread.handler.getLooper().quit();
//        //Close the Text to Speech Library
//        if(audioThread.ttobj != null) {
//            audioThread.ttobj.stop();
//            audioThread.ttobj.shutdown();
//            Log.d(TAG, "Text to Speach Destroyed");
//        }
        super.onDestroy();
    }

}
