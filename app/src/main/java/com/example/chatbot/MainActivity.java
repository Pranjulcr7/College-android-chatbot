package com.example.chatbot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static com.example.chatbot.R.drawable.*;

public class MainActivity extends AppCompatActivity implements AIListener {
    private static final int REQUEST_MICROPHONE = 0;
    AIService aiService;
    TextView t;
    TextToSpeech t1;
    LinearLayout layout;
    ScrollView chatScroll;
    ImageView button;
    String user, facId="", studId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        user = bundle.getString("user");
        if(user.equals("faculty") ){

            facId = bundle.getString("faculty_id");
        }
        else if(user.equals("student") ){

            studId = bundle.getString("student_id");
        }
        else {
            facId = null;
            studId = null;
        }
        layout = (LinearLayout)findViewById(R.id.chatbox);
        chatScroll = (ScrollView)findViewById(R.id.chatScroll);
        button = (ImageView)findViewById(R.id.button);

        final AIConfiguration config = new AIConfiguration("4dd449b62b8347acb45b6bda5ed22878",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        final AIDataService aiDataService = new AIDataService(config);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

    }

    public void buttonClicked(View view) throws AIServiceException {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);

        }
        else {
            Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blink_anim);
            button.startAnimation(anim);
            t1.stop();
            button.setBackgroundResource(support);
            aiService.startListening();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResult(AIResponse result) {

        button.clearAnimation();
        Log.d("anu",result.toString());
        Result result1 = result.getResult();
        String resultString = result1.getFulfillment().getSpeech().trim();

        if(resultString.contains("http")){

            if( user.equals("guest") && resultString.contains("assignment")) {

                resultString = "Unauthorised user";
            }
            else{

                webView(resultString);
                resultString = "Loading";
            }
        }
        else if(resultString.contains("bye-bye!")){

            finish();
        }
        else if(resultString.contains("attendancexxx") ){

            if(user.equals("faculty")) {

                resultString = "Loading";
                attendance();
            }
            else{

                resultString = "Unauthorised user";
            }
        }
        else if(resultString.contains("attendanceyyy")){

            if(user.equals("student")) {

                resultString = "Loading";
                Checkattendance();
            }
            else {

                resultString = "Unauthorised user";
            }
        }
        else if(resultString.contains("attendancezzz")){

            if(user.equals("faculty")) {

                resultString = "Loading";
                CheckStudattendance();
            }
            else {

                resultString = "Unauthorised user";
            }
        }
            //t.setText("\nQUERY : "+result1.getResolvedQuery()+"\nACTION: "+ result1.getFulfillment().getSpeech()+"\n"+t.getText());
            TextView img1 = new TextView(MainActivity.this);
            TextView img2 = new TextView(MainActivity.this);

            LinearLayout l1 = new LinearLayout(MainActivity.this);
            LinearLayout l2 = new LinearLayout(MainActivity.this);

            img1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            img2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            l1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            l2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            l1.setOrientation(LinearLayout.HORIZONTAL);
            l2.setOrientation(LinearLayout.HORIZONTAL);

            l1.setHorizontalGravity(Gravity.LEFT);
            l2.setHorizontalGravity(Gravity.RIGHT);

            l2.setPadding(0, 12, 100, 12);
            l2.setPadding(100, 12, 0, 12);

            img1.setText(result1.getResolvedQuery());
            img2.setText(resultString);

            img1.setBackgroundResource(lefttext);
            img2.setBackgroundResource(righttext);


            img1.setPadding(20, 20, 20, 20);
            img2.setPadding(20, 20, 20, 20);

            layout.setClipToOutline(true);

            img1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
            img2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

            l1.addView(img1);
            l2.addView(img2);

            layout.addView(l1);
            layout.addView(l2);

            chatScroll.post(new Runnable() {
                @Override
                public void run() {

                    chatScroll.fullScroll(View.FOCUS_DOWN);
                }
            });

            String toSpeak = resultString;
//        Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            button.setBackgroundResource(mic);
    }

    private void Checkattendance() {

        Intent intent = new Intent(this, checkAttendance.class);
        intent.putExtra("student_id", studId);
        finish();
        startActivity(intent);
    }

    private void CheckStudattendance() {

        Intent intent = new Intent(this, checkStudentAttendance.class);
        finish();
        startActivity(intent);
    }

    public void webView(String url){

        Intent intent = new Intent(this, htmlFileView.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public void attendance(){

        Intent intent = new Intent(this, takeAttendance.class);
        intent.putExtra("faculty_id", facId);
        finish();
        startActivity(intent);
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
