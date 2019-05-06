package com.example.chatbot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class changePassword extends AppCompatActivity {

    Button chngPass;
    ProgressBar cProg;
    EditText prev, new1, new2;
    TextView errMsg;
    public static String errorMsg="";
    RequestQueue requestQueue;
    String id, chngPassUrl = "http://botonline.co.in/chatbot/chngPass.php";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_change_password);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id").trim();

        if(id.length() == 12){

            sharedPreferences = getSharedPreferences( studentLogin.studentDetails, Context.MODE_PRIVATE);
        }
        else{

            sharedPreferences = getSharedPreferences( loginActivity.facultyDetails, Context.MODE_PRIVATE);
        }

        chngPass = (Button) findViewById(R.id.btn_chngP);
        cProg = (ProgressBar) findViewById(R.id.chngPProgressbar);
        prev = (EditText) findViewById(R.id.prevPassword);
        new1 = (EditText) findViewById(R.id.newPassword1);
        new2 = (EditText) findViewById(R.id.newPassword2);
        errMsg = (TextView) findViewById(R.id.chngPassTextError) ;

        if(!errMsg.equals("")){

            errMsg.setText(errorMsg);
            errMsg.setVisibility(View.VISIBLE);
        }

        chngPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cProg.setVisibility(View.VISIBLE);
                chngPass.setVisibility(View.GONE);
                final String prevP = prev.getText().toString().trim();
                final String newP1 = new1.getText().toString().trim();
                String newP2 = new2.getText().toString().trim();

                if(prevP.isEmpty() || newP1.isEmpty() || newP2.isEmpty()){

                    errorMsg = "All fields are mandatory !";
                    finish();
                    startActivity(getIntent());
                }
                else if(newP1.length() < 8){

                    errorMsg = "Password size should be >= 8";
                    finish();
                    startActivity(getIntent());
                }
                else if(!newP1.equals(newP2)){

                    errorMsg = "Passwords entered doesn't match !";
                    finish();
                    startActivity(getIntent());
                }
                else{

                    requestQueue = Volley.newRequestQueue(getApplicationContext());

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, chngPassUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String success = jsonObject.getString("success");
                                        errorMsg = jsonObject.getString("message");

                                        if(success.equals("1")){

                                            Toast.makeText(changePassword.this, "Password changed !", Toast.LENGTH_SHORT).show();
                                            finish();
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.clear();
                                            editor.commit();
                                            errorMsg="";
                                            startActivity(getIntent());
                                        }
                                        else {

                                            Toast.makeText(changePassword.this, "Error !, "+errorMsg, Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(changePassword.this, "Registration Error! "+e.toString(), Toast.LENGTH_SHORT).show();
                                        cProg.setVisibility(View.GONE);
                                        chngPass.setVisibility(View.VISIBLE);
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    String message="Password Change Error ! ";
                                    cProg.setVisibility(View.GONE);
                                    chngPass.setVisibility(View.VISIBLE);
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
                            }){

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parameters = new HashMap<>();
                            parameters.put("id", id.trim());
                            parameters.put("prev",prevP);
                            parameters.put("new",newP1);
                            return parameters;
                        }

                        @Override
                        public Map<String,String> getHeaders() throws AuthFailureError{

                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Content-Type","application/x-www-form-urlencoded");
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);
                }
            }
        });

    }
}
