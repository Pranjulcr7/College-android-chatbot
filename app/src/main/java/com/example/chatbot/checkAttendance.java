package com.example.chatbot;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class studentA{

    String attend, course, name;
    int count, total;

    public studentA(String attend, String course, String name) {
        this.attend = attend;
        this.course = course;
        this.name = name;
        this.count =0;
        this.total=0;
    }
}

public class checkAttendance extends AppCompatActivity {

    String studId;
    int flag=-1;
    String checkAttUrl = "http://botonline.co.in/chatbot/checkAttendance.php";
    RequestQueue requestQueue;
    ArrayList<studentA> studArr = new ArrayList<>();
    LinearLayout layout;
    Button refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);
        Bundle bundle = getIntent().getExtras();
        studId = bundle.getString("student_id");
        layout = (LinearLayout) findViewById(R.id.attendance);
        refresh = (Button) findViewById(R.id.refresh_btnA);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAttend();
            }
        });

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, checkAttUrl,
                new Response.Listener<String>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray students = jsonObject.getJSONArray("students");

                            String attendance, course="null";

                            for(int i=0; i<students.length(); i++){

                                JSONObject stud = students.getJSONObject(i);
                                String stat = stud.getString("attendance");
                                String code = stud.getString("course").toUpperCase().trim();
                                String name = stud.getString("name").toUpperCase().trim();

                                if(course.equals(code)){

                                    if(stat.equals("1")){

                                        studArr.get(flag).count++;
                                    }
                                    studArr.get(flag).total++;
                                }
                                else{

                                    flag++;
                                    studentA obj = new studentA( stat, code, name);
                                    if(stat.equals("1")){

                                        obj.count++;
                                    }
                                    obj.total++;
                                    course = code;
                                    studArr.add(obj);
                                }
                            }

                            if(studArr.size()>0){

                                for (int i=0; i<=flag; i++){

                                    TextView t1 = new TextView( checkAttendance.this);

                                    float per = (float) (studArr.get(i).count*100)/studArr.get(i).total;
                                    t1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    String text = "\nCourse :  "+ studArr.get(i).name
                                    +"\n\nCourse code :  " + studArr.get(i).course + "\n\nClass attended :  "+studArr.get(i).count
                                            +"\n\nTotal classes :  "+studArr.get(i).total+"\n\nPercentage :  "+
                                            roundTwoDecimals(per)+" %\n";

                                    t1.setText(text);
                                    t1.setPadding(30, 30, 20, 20);
                                    t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                                    t1.setTextColor(R.color.black_overlay);
                                    if(per >= 80){
                                        t1.setBackgroundResource(R.drawable.att_text1);
                                    }else{
                                        t1.setBackgroundResource(R.drawable.att_text2);
                                    }
                                    layout.addView(t1);
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

                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<>();
                parameters.put("student_id", studId);
                return parameters;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    public double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        return Double.valueOf(twoDForm.format(d));
    }

    private void checkAttend(){

        Intent intent = new Intent(this, checkAttendance.class);
        intent.putExtra("student_id", studId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
