package com.example.chatbot;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.regex.Pattern;

public class checkStudentAttendance extends AppCompatActivity {

    Button proceed;
    EditText sid;
    ProgressBar caPro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_student_attendance);

        sid = (EditText) findViewById(R.id.student_id_attendance_check);
        proceed = (Button) findViewById(R.id.check_std_attend);
        caPro = (ProgressBar) findViewById(R.id.caProg);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Pattern.matches("[0-9][0-9][A-Z][A-Z][A-Z][A-Z][0-9][0-9][0-9][0-9][0-9][0-9]", sid.getText().toString().trim())) {

                    sid.setText(null);
                    sid.setHint("Enter correct student id");
                    sid.setTextColor(Color.RED);
                    sid.setFocusable(true);
                }
                else {
                    proceed.setVisibility(View.GONE);
                    caPro.setVisibility(View.VISIBLE);
                    checkAttend();
                }
            }
        });
    }

    public void checkAttend(){

        Intent intentThis = getIntent();
        finish();
        startActivity(intentThis);
        Intent intent = new Intent(this, checkAttendance.class);
        intent.putExtra("student_id", sid.getText().toString().trim());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
