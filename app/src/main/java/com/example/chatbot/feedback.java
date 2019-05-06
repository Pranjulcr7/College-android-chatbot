package com.example.chatbot;

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

public class feedback extends AppCompatActivity {

    EditText msg;
    Button submit;
    RequestQueue requestQueue;
    ProgressBar prog;
    TextView errMsg;
    public static String errorMsg="";
    String id, submitFeedUrl = "http://botonline.co.in/chatbot/feedback.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_feedback);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id").trim();
        msg = (EditText) findViewById(R.id.feed_msg);
        submit = (Button) findViewById(R.id.btn_feed);
        errMsg = (TextView) findViewById(R.id.Error_text);
        prog = (ProgressBar) findViewById(R.id.feedProg);

        if(!errMsg.equals("")){

            errMsg.setText(errorMsg);
            errMsg.setVisibility(View.VISIBLE);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prog.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                requestQueue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, submitFeedUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    errorMsg = jsonObject.getString("message");
                                    if(success.equals("1")){

                                        Toast.makeText(feedback.this, "Feedback Submitted !", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(getIntent());
                                    }
                                    else {

                                        Toast.makeText(feedback.this, "Error !, "+errorMsg, Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(getIntent());
                                    }
                                }catch (JSONException e){

                                    e.printStackTrace();
                                    Toast.makeText(feedback.this, "Registration Error! "+e.toString(), Toast.LENGTH_SHORT).show();
                                    prog.setVisibility(View.GONE);
                                    submit.setVisibility(View.VISIBLE);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                String message="Feedback Error ! ";
                                prog.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);
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
                        parameters.put("msg", msg.getText().toString().trim());
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
        });
    }

    @Override
    public void onBackPressed() {
        errorMsg="";
        finish();
        super.onBackPressed();
    }
}
