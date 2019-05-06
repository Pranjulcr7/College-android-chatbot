package com.example.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;


public class FullscreenActivity extends AppCompatActivity{

    TextToSpeech t1;
    Button stud, fact, gues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        stud = findViewById(R.id.btn_student);
        fact = findViewById(R.id.btn_faculty);
        gues = findViewById(R.id.btn_guest);
        stud.clearAnimation();
        fact.clearAnimation();
        gues.clearAnimation();

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        String toSpeak = "please choose your identity";
        Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

        stud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(FullscreenActivity.this, R.anim.bounce);
                stud.startAnimation(anim);
                studentLogin();
                String toSpeak = "welcome student";
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        fact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(FullscreenActivity.this, R.anim.bounce);
                fact.startAnimation(anim);
                facultyLogin();
                String toSpeak = "welcome faculty";
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        gues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(FullscreenActivity.this, R.anim.bounce);
                gues.startAnimation(anim);
                guestLogin();
                String toSpeak = "welcome guest";
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    public void studentLogin(){

        Intent intent = new Intent(this, studentLogin.class);
        startActivity(intent);
    }

    public void guestLogin(){

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user", "guest");
        startActivity(intent);
    }

    public void facultyLogin(){

        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
