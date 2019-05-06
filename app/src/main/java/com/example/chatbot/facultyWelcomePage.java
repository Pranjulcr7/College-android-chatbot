package com.example.chatbot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class facultyWelcomePage extends AppCompatActivity {

    String facId;
    Button takeAttendance, checkStdAt, chatbotBtn, logout, chngPass, feed;
    Boolean exit=false;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_welcome_page);
        Bundle bundle = getIntent().getExtras();
        facId = bundle.getString("faculty_id");

        sharedPreferences = getSharedPreferences( loginActivity.facultyDetails, Context.MODE_PRIVATE);

        takeAttendance = (Button) findViewById(R.id.take_attendance);
        checkStdAt = (Button) findViewById(R.id.check_stdAttendance);
        logout = (Button) findViewById(R.id.logout_fac);
        chatbotBtn = (Button) findViewById(R.id.chatbot_btnF);
        chngPass = (Button) findViewById(R.id.fac_chngPassword);
        feed = (Button) findViewById(R.id.feed_btnF);

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackForm();
            }
        });

        chngPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chngPasswordActivity();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        takeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendance();
            }
        });

        checkStdAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkA();
            }
        });

        chatbotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatBot();
            }
        });
    }

    public void attendance(){

        Intent intent = new Intent(this, takeAttendance.class);
        intent.putExtra("faculty_id", facId);
        startActivity(intent);
    }

    public void checkA(){

        Intent intent = new Intent(this, checkStudentAttendance.class);
        startActivity(intent);
    }

    private void feedbackForm() {

        Intent intent = new Intent(this, feedback.class);
        intent.putExtra("id", facId);
        startActivity(intent);
    }

    public void chatBot(){

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user", "faculty");
        intent.putExtra("faculty_id", facId);
        startActivity(intent);
    }

    public void chngPasswordActivity(){

        Intent intent = new Intent(this, changePassword.class);
        intent.putExtra("id", facId);
        startActivity(intent);
    }

    public void logout(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        finish();
        Intent intent = new Intent(this, FullscreenActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (exit) {
            facultyWelcomePage.this.finish();
            logout();
        }
        else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

}


