package com.example.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class studentClass{

    String id, fname, lname, attendance;

    public studentClass(String id, String fname, String lname) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.attendance="";
    }
}

public class attendanceList extends AppCompatActivity {

    String lecId, classId, cCode, cName, facId;
    RequestQueue requestQueue;
    String getListUrl ="http://botonline.co.in/chatbot/getList.php";
    String takeAttendUrl = "http://botonline.co.in/chatbot/attendance.php";
    TextView idText, fnameText, lnameText;
    Button presentBtn, absentBtn;
    ProgressBar pb;
    ArrayList<studentClass> studArray = new ArrayList<>();
    int flag=0, len=0, lock=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        Bundle bundle = getIntent().getExtras();
        lecId = bundle.getString("lecture_id");
        classId = bundle.getString("class_id");
        cCode = bundle.getString("course_code");
        cName = bundle.getString("course_name");
        facId = bundle.getString("faculty_id");

        idText = (TextView) findViewById(R.id.stdId);
        fnameText = (TextView) findViewById(R.id.stdFname);
        lnameText = (TextView) findViewById(R.id.stdLname);
        presentBtn = (Button) findViewById(R.id.presentBtn);
        absentBtn = (Button) findViewById(R.id.absentBtn);
        pb = (ProgressBar) findViewById(R.id.listProg);

        pb.setVisibility(View.VISIBLE);
        presentBtn.setVisibility(View.GONE);
        absentBtn.setVisibility(View.GONE);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray students = jsonObject.getJSONArray("students");

                            len = students.length();
                            for (int i=0; i<len; i++){

                                JSONObject stud = students.getJSONObject(i);

                                String id = stud.getString("id");
                                String fname = stud.getString("fname");
                                String lname = stud.getString("lname");
                                studentClass obj = new studentClass(id,fname,lname);
                                studArray.add(obj);
                            }
                            take();

                        }catch (JSONException e){

                            e.printStackTrace();
                            finish();
                            startActivity(getIntent());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String message="Attendance Error!  ";
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
                Map<String, String> parameters = new HashMap<>();
                parameters.put("class_id", classId);
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

    public void take(){

        pb.setVisibility(View.GONE);
        presentBtn.setVisibility(View.VISIBLE);
        absentBtn.setVisibility(View.VISIBLE);
        disp();
        presentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                studArray.get(flag).attendance="1";
                setAttend(flag,"1");
                if(flag < len ){

                    flag = flag + 1;
                }
                else{

                    Toast.makeText(getApplicationContext(),"Attendance done!", Toast.LENGTH_SHORT).show();
                    welcomeF();
                }
            }
        });

        absentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                studArray.get(flag).attendance="0";
                setAttend(flag,"0");
                if(flag < len){

                    flag = flag + 1;
                }
                else{

                    Toast.makeText(getApplicationContext(),"Attendance done!", Toast.LENGTH_SHORT).show();
                    welcomeF();
                }
            }
        });
    }

    public void setAttend(final int count, final String at){

        pb.setVisibility(View.VISIBLE);
        presentBtn.setVisibility(View.GONE);
        absentBtn.setVisibility(View.GONE);

        StringRequest request = new StringRequest(Request.Method.POST, takeAttendUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.get("message").equals("1")) {

                                pb.setVisibility(View.GONE);
                                presentBtn.setVisibility(View.VISIBLE);
                                absentBtn.setVisibility(View.VISIBLE);
                                if(flag<len) {
                                    disp();
                                }
                                else {
                                    welcomeF();
                                }
                            }
                        }catch (JSONException e){

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String message="Attendance Error!  ";
                        pb.setVisibility(View.VISIBLE);
                        presentBtn.setVisibility(View.GONE);
                        absentBtn.setVisibility(View.GONE);
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
                        welcomeF();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("lecture_id", lecId);
                parameters.put("student_id", studArray.get(count).id);
                parameters.put("attendance", at);
                parameters.put("course_code", cCode);
                parameters.put("course_name", cName);
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

    public void disp(){

        idText.setText("Student id : "+studArray.get(flag).id);
        fnameText.setText("First name : "+studArray.get(flag).fname);
        lnameText.setText("Last  name : "+studArray.get(flag).lname);
    }

    public void welcomeF(){

        finish();
        Intent intent = new Intent(this, facultyWelcomePage.class);
        intent.putExtra("faculty_id", facId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
