package com.example.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class studentsignup extends AppCompatActivity {

    RequestQueue requestQueue;
    String std_id;
    Button studentSignUp;
    EditText s_sid,s_semail, s_sfname, s_slname, s_sphone, s_spass, s_spass2;
    ProgressBar ssProgressBar;
    TextView errMsg;
    public static String errorMsg="";
    public static String studentSignupurl = "http://botonline.co.in/chatbot/studentSignup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_studentsignup);
        studentSignUp = (Button) findViewById(R.id.btn_studentsignup);
        s_sid = (EditText) findViewById(R.id.signup_sid);
        s_semail = (EditText) findViewById(R.id.signup_semail);
        s_sfname = (EditText) findViewById(R.id.signup_sfname);
        s_slname = (EditText) findViewById(R.id.signup_slname);
        s_sphone = (EditText) findViewById(R.id.signup_sphone);
        s_spass = (EditText) findViewById(R.id.signup_spassword);
        s_spass2 = (EditText) findViewById(R.id.signup_spassword2);
        ssProgressBar = (ProgressBar) findViewById(R.id.ssProgressbar);
        errMsg = (TextView) findViewById(R.id.signup_serrorText);

        if (!errMsg.equals("")) {

            errMsg.setText(errorMsg);
            errMsg.setVisibility(View.VISIBLE);
        }

        studentSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestQueue = Volley.newRequestQueue(getApplicationContext());
                ssProgressBar.setVisibility(View.VISIBLE);
                studentSignUp.setVisibility(View.GONE);

                final String sid = s_sid.getText().toString().trim();
                final String semail = s_semail.getText().toString().trim().toLowerCase();
                final String sfname = s_sfname.getText().toString().trim();
                final String slname = s_slname.getText().toString().trim();
                final String sphone = s_sphone.getText().toString().trim();
                final String spass = s_spass.getText().toString().trim();
                final String spass2 = s_spass2.getText().toString().trim();

                if (sid.isEmpty() || semail.isEmpty() || sfname.isEmpty() || slname.isEmpty() || spass.isEmpty() || spass2.isEmpty()) {

                    errorMsg = "All fields are mandatory";
                    errMsg.setText(errorMsg);
                    errMsg.setVisibility(View.VISIBLE);
                    ssProgressBar.setVisibility(View.GONE);
                    studentSignUp.setVisibility(View.VISIBLE);

                } else {

                    if (!Pattern.matches("[0-9][0-9][A-Z][A-Z][A-Z][A-Z][0-9][0-9][0-9][0-9][0-9][0-9]", sid)) {

                        errorMsg = "Invalid id";
                        errMsg.setText(errorMsg);
                        errMsg.setVisibility(View.VISIBLE);
                        ssProgressBar.setVisibility(View.GONE);
                        studentSignUp.setVisibility(View.VISIBLE);
                    }
                    else if (!Pattern.matches("^[a-z0-9.]+(?:\\\\.[a-z0-9.]+)*@msruas.ac.in", semail)) {

                        errorMsg = "Invalid email-id";
                        errMsg.setText(errorMsg);
                        errMsg.setVisibility(View.VISIBLE);
                        ssProgressBar.setVisibility(View.GONE);
                        studentSignUp.setVisibility(View.VISIBLE);
                    }
                    else if((sphone.length() != 10 || !Pattern.matches("[0-9]*", sphone)) && !sphone.isEmpty()){

                        errorMsg = "Invalid phone number";
                        errMsg.setText(errorMsg);
                        errMsg.setVisibility(View.VISIBLE);
                        ssProgressBar.setVisibility(View.GONE);
                        studentSignUp.setVisibility(View.VISIBLE);
                    }
                    else if(spass.length() < 8){

                        errorMsg = "Password size >= 8";
                        errMsg.setText(errorMsg);
                        errMsg.setVisibility(View.VISIBLE);
                        ssProgressBar.setVisibility(View.GONE);
                        studentSignUp.setVisibility(View.VISIBLE);
                    }
                    else if(!spass.equals(spass2)){

                        errorMsg = "Password entered doesn't match";
                        errMsg.setText(errorMsg);
                        errMsg.setVisibility(View.VISIBLE);
                        ssProgressBar.setVisibility(View.GONE);
                        studentSignUp.setVisibility(View.VISIBLE);
                    }
                    else {

                        final StringRequest request = new StringRequest(Request.Method.POST, studentSignupurl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println(response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    errorMsg = jsonObject.getString("message");

                                    if (success.equals("1")) {

                                        Toast.makeText(studentsignup.this, "Registration successfull!", Toast.LENGTH_SHORT).show();
                                        std_id = s_sid.getText().toString().toUpperCase().trim();
                                        welcomeStudent();
                                    } else {

                                        Toast.makeText(studentsignup.this, "Registration error!, " + errorMsg, Toast.LENGTH_SHORT).show();
                                        if(errorMsg.toLowerCase().contains("duplicate")){

                                            errorMsg = "Already registered";
                                        }
                                        finish();
                                        startActivity(getIntent());
                                    }
                                } catch (JSONException e) {

                                    e.printStackTrace();
                                    Toast.makeText(studentsignup.this, "Registration Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                                    ssProgressBar.setVisibility(View.GONE);
                                    studentSignUp.setVisibility(View.VISIBLE);
                                }
                                requestQueue.stop();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                String message = "Registeration Error! ";
                                ssProgressBar.setVisibility(View.GONE);
                                studentSignUp.setVisibility(View.VISIBLE);
                                if (error instanceof TimeoutError) {
                                    message = message + "Timeout Error!";

                                } else if (error instanceof NoConnectionError) {
                                    message = message + "NoConnection Error!";

                                } else if (error instanceof AuthFailureError) {
                                    message = message + "Authentication Error!";
                                } else if (error instanceof ServerError) {
                                    message = message + "Server Side Error!";
                                } else if (error instanceof NetworkError) {
                                    message = message + "Network Error!";
                                } else if (error instanceof ParseError) {
                                    message = message + "Parse error";
                                }
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                requestQueue.stop();
                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parameters = new HashMap<>();
                                parameters.put("student_id", sid);
                                parameters.put("student_fname", sfname);
                                parameters.put("student_lname", slname);
                                parameters.put("student_email", semail);
                                parameters.put("student_phone", sphone);
                                parameters.put("student_password", spass);
                                return parameters;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Content-Type", "application/x-www-form-urlencoded");
                                return params;
                            }
                        };

                        requestQueue.add(request);
                    }
                }
            }
        });



    }
    public void welcomeStudent(){

        errorMsg="";
        Intent intent = new Intent(this, studentWelcomePage.class);
        Intent intentThis = getIntent();
        finish();
        startActivity(intentThis);
        intent.putExtra("student_id", std_id);
        startActivity(intent);
    }
}
