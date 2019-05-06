package com.example.chatbot;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class studentWelcomePage extends AppCompatActivity {

    String studId;
    Button checkAttendance, chatbotBtn, logout, chngPass, feed;
    Boolean exit=false;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_welcome_page);
        Bundle bundle = getIntent().getExtras();
        studId = bundle.getString("student_id");

        sharedPreferences = getSharedPreferences( studentLogin.studentDetails, Context.MODE_PRIVATE);

        checkAttendance = (Button) findViewById(R.id.check_attendance);
        chatbotBtn = (Button) findViewById(R.id.chatbot_btn);
        logout = (Button) findViewById(R.id.logout_std);
        chngPass = (Button) findViewById(R.id.std_chngPassword);
        feed = (Button) findViewById(R.id.feed_btnS);

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

        checkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAttend();
            }
        });

        chatbotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoChatbot();
            }
        });
    }

    private void feedbackForm() {

        Intent intent = new Intent(this, feedback.class);
        intent.putExtra("id", studId);
        startActivity(intent);
    }

    public void checkAttend(){

        Intent intent = new Intent(this, checkAttendance.class);
        intent.putExtra("student_id", studId);
        startActivity(intent);
    }

    private void gotoChatbot(){

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user", "student");
        intent.putExtra("student_id", studId);
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

    public void chngPasswordActivity(){

        Intent intent = new Intent(this, changePassword.class);
        intent.putExtra("id", studId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (exit) {
            studentWelcomePage.this.finish();
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
