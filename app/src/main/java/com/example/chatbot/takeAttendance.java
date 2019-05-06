package com.example.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class takeAttendance extends AppCompatActivity {

    String facId;
    Spinner sec, prg, dept, batch, courseCode, courseName;
    Button proceed;
    ProgressBar getList;
    RequestQueue requestQueue;
    String getListUrl = "http://botonline.co.in/chatbot/getLectureId.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        Bundle bundle = getIntent().getExtras();
        facId = bundle.getString("faculty_id");
        getList = (ProgressBar) findViewById(R.id.getListProgressbar);

        sec = (Spinner) findViewById(R.id.section_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Section, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sec.setAdapter(adapter);

        prg = (Spinner) findViewById(R.id.program_spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.Program, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prg.setAdapter(adapter1);

        dept = (Spinner) findViewById(R.id.dept_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.Department, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dept.setAdapter(adapter2);

        batch = (Spinner) findViewById(R.id.batch_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.Batch, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batch.setAdapter(adapter3);

        courseCode = (Spinner) findViewById(R.id.course_code_spinner);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.CourseCodeArray, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseCode.setAdapter(adapter4);

        courseName = (Spinner) findViewById(R.id.course_name_spinner);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.CourseNameArray, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseName.setAdapter(adapter5);

        courseName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        courseCode.setSelection(0);
                        break;
                    case 1:
                        courseCode.setSelection(1);
                        break;
                    case 2:
                        courseCode.setSelection(2);
                        break;
                    case 3:
                        courseCode.setSelection(3);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        courseCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        courseName.setSelection(0);
                        break;
                    case 1:
                        courseName.setSelection(1);
                        break;
                    case 2:
                        courseName.setSelection(2);
                        break;
                    case 3:
                        courseName.setSelection(3);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });


        proceed = (Button) findViewById(R.id.procced_attendance);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proceed.setVisibility(View.GONE);
                getList.setVisibility(View.VISIBLE);
                final String section = sec.getSelectedItem().toString().trim();
                final String program = prg.getSelectedItem().toString().trim();
                final String department = dept.getSelectedItem().toString().trim();
                final String batchs = batch.getSelectedItem().toString().trim();
                final String cCode = courseCode.getSelectedItem().toString().trim();
                final String cName = courseName.getSelectedItem().toString().trim();
                requestQueue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, getListUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println(response);
                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    String error = jsonObject.getString("error");

                                    if(success.equals("1")){

                                        String lectureId = jsonObject.getString("lecture_id");
                                        String classId = jsonObject.getString("class_id");
                                        String courseCode = jsonObject.getString("course_code");
                                        String courseName = jsonObject.getString("course_name");
                                        takeAttend(lectureId,classId,courseCode,courseName);

                                    }
                                    else {

                                        if(error.trim().toLowerCase().contains("class_id")){

                                            error= "No such class found!";
                                        }
                                        Toast.makeText(takeAttendance.this, error, Toast.LENGTH_LONG).show();
                                        finish();
                                        startActivity(getIntent());
                                    }


                                }catch (JSONException e){

                                    e.printStackTrace();
                                    Toast.makeText(takeAttendance.this, e.toString(), Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(getIntent());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                String message="Attendance Error! ";
                                getList.setVisibility(View.GONE);
                                proceed.setVisibility(View.VISIBLE);
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
                        parameters.put("sec",section);
                        parameters.put("dept",department);
                        parameters.put("prog",program);
                        parameters.put("batch",batchs);
                        parameters.put("code",cCode);
                        parameters.put("name",cName);
                        parameters.put("id", facId);
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

    public void takeAttend(String lecId, String classId, String cCode, String cName){

        Intent intent = new Intent(this, attendanceList.class);
        intent.putExtra("lecture_id", lecId);
        intent.putExtra("class_id", classId);
        intent.putExtra("course_code", cCode);
        intent.putExtra("course_name", cName);
        intent.putExtra("faculty_id", facId);
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


}
