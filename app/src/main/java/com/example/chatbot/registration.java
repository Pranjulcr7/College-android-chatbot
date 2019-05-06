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

public class registration extends AppCompatActivity {

    RequestQueue requestQueue;
    Button fSignUp;
    EditText fid, femail, ffname, flname, fphone, fpass, fpass2;
    ProgressBar fsProgressBar;
    TextView errMsg;
    public static String errorMsg="";
    public static String facultySignupurl = "http://botonline.co.in/chatbot/facultySignup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_registration);
        fSignUp = (Button) findViewById(R.id.btn_sfsignup);
        fid = (EditText) findViewById(R.id.signup_fid);
        femail = (EditText) findViewById(R.id.signup_femail);
        ffname = (EditText) findViewById(R.id.signup_ffname);
        flname = (EditText) findViewById(R.id.signup_flname);
        fphone = (EditText) findViewById(R.id.signup_fphone);
        fpass = (EditText) findViewById(R.id.signup_fpassword);
        fpass2 = (EditText) findViewById(R.id.signup_fpassword2);
        fsProgressBar = (ProgressBar) findViewById(R.id.fsProgressbar);
        errMsg = (TextView) findViewById(R.id.signup_ferrorText) ;

        if(!errMsg.equals("")){

            errMsg.setText(errorMsg);
            errMsg.setVisibility(View.VISIBLE);
        }

        fSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestQueue = Volley.newRequestQueue(getApplicationContext());
                fsProgressBar.setVisibility(View.VISIBLE);
                fSignUp.setVisibility(View.GONE);

                final String sid = fid.getText().toString().trim();
                final String semail = femail.getText().toString().trim();
                final String sfname = ffname.getText().toString().trim();
                final String slname = flname.getText().toString().trim();
                final String sphone = fphone.getText().toString().trim();
                final String spass = fpass.getText().toString().trim();
                final String spass2 = fpass2.getText().toString().trim();

                if (sid.isEmpty() || semail.isEmpty() || sfname.isEmpty() || slname.isEmpty() || sphone.isEmpty() || spass.isEmpty() || spass2.isEmpty()) {

                    errorMsg = "All fields are mandatory";
                    errMsg.setText(errorMsg);
                    errMsg.setVisibility(View.VISIBLE);
                    fsProgressBar.setVisibility(View.GONE);
                    fSignUp.setVisibility(View.VISIBLE);

                } else {

                    if (!Pattern.matches("[0-9][0-9][A-Z][A-Z][A-Z][A-Z][0-9][0-9][0-9]", sid)) {

                        errorMsg = "Invalid id";
                        errMsg.setText(errorMsg);
                        errMsg.setVisibility(View.VISIBLE);
                        fsProgressBar.setVisibility(View.GONE);
                        fSignUp.setVisibility(View.VISIBLE);
                    }
                    else if (!Pattern.matches("^[a-z0-9.]+(?:\\\\.[a-zA-Z0-9.]+)*@msruas.ac.in", semail)) {

                        errorMsg = "Invalid email-id";
                        errMsg.setText(errorMsg);
                        errMsg.setVisibility(View.VISIBLE);
                        fsProgressBar.setVisibility(View.GONE);
                        fSignUp.setVisibility(View.VISIBLE);
                    }
                    else if((sphone.length() != 10 || !Pattern.matches("[0-9]*", sphone)) && !sphone.isEmpty()){

                        errorMsg = "Invalid phone number";
                        errMsg.setText(errorMsg);
                        errMsg.setVisibility(View.VISIBLE);
                        fsProgressBar.setVisibility(View.GONE);
                        fSignUp.setVisibility(View.VISIBLE);
                    }
                    else if(spass.length() < 8){

                        errorMsg = "Password size >= 8";
                        errMsg.setText(errorMsg);
                        errMsg.setVisibility(View.VISIBLE);
                        fsProgressBar.setVisibility(View.GONE);
                        fSignUp.setVisibility(View.VISIBLE);
                    }
                    else if(!spass.equals(spass2)){

                        errorMsg = "Password entered doesn't match";
                        errMsg.setText(errorMsg);
                        errMsg.setVisibility(View.VISIBLE);
                        fsProgressBar.setVisibility(View.GONE);
                        fSignUp.setVisibility(View.VISIBLE);
                    }
                    else {

                        final StringRequest request = new StringRequest(Request.Method.POST, facultySignupurl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println(response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    errorMsg = jsonObject.getString("message");

                                    if(success.equals("1")){

                                        Toast.makeText(registration.this, "Registration successfull!", Toast.LENGTH_SHORT).show();
                                        errorMsg=null;
                                        welcomeFaculty();
                                    }
                                    else {

                                        Toast.makeText(registration.this, "Registration error!, "+errorMsg, Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(getIntent());
                                    }
                                }catch (JSONException e){

                                    e.printStackTrace();
                                    Toast.makeText(registration.this, "Registration Error! "+e.toString(), Toast.LENGTH_SHORT).show();
                                    fsProgressBar.setVisibility(View.GONE);
                                    fSignUp.setVisibility(View.VISIBLE);
                                }
                                requestQueue.stop();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                String message="Registeration Error! ";
                                fsProgressBar.setVisibility(View.GONE);
                                fSignUp.setVisibility(View.VISIBLE);
                                if (error instanceof TimeoutError) {
                                    message = message + "Timeout Error!";

                                }else if (error instanceof NoConnectionError) {
                                    message = message + "NoConnection Error!";

                                }else if (error instanceof AuthFailureError) {
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
                                parameters.put("fac_id", sid);
                                parameters.put("fac_fname", sfname);
                                parameters.put("fac_lname", slname);
                                parameters.put("fac_email", semail);
                                parameters.put("fac_phone", sphone);
                                parameters.put("fac_password", spass);
                                return parameters;
                            }

                            @Override
                            public Map<String,String> getHeaders() throws AuthFailureError{

                                Map<String,String> params = new HashMap<String, String>();
                                params.put("Content-Type","application/x-www-form-urlencoded");
                                return params;
                            }
                        };

                        requestQueue.add(request);
                    }
                }

            }

        });
    }

    public void welcomeFaculty(){

        errorMsg="";
        Intent intent = new Intent(this, facultyWelcomePage.class);
        Intent intentThis = getIntent();
        finish();
        startActivity(intentThis);
        startActivity(intent);
    }

    public void signInPage(View view){

        finish();
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
    }

    public void goBack(View view){

        finish();
        Intent intent = new Intent(this, FullscreenActivity.class);
        startActivity(intent);
    }
}
