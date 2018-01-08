package com.example.octavian.hipperchat;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


public class Main2Activity extends AppCompatActivity {
    String username, readurl, writeurl;
    Handler handler;
    int millis = 1000;
    RequestQueue queue;
    JSONArray jsonArray;
    TextView textView;
    EditText inputField;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        queue = Volley.newRequestQueue(this);
        textView = findViewById(R.id.textView2);
        inputField = findViewById(R.id.editText3);
        cal = Calendar.getInstance(TimeZone.getDefault());

        Intent intent = getIntent();
        Bundle bin = intent.getExtras();
        username = (String) bin.get("username");
        readurl = "https://webtechlecture.appspot.com/chat/posting/list";
        writeurl = "https://webtechlecture.appspot.com/chat/posting/new";

        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, readurl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonArray = new JSONArray(response);
                            textView.setText("");
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                cal.setTimeInMillis(obj.getInt("time"));
                                String elem = "[" + String.format("%02d", (cal.get(Calendar.HOUR_OF_DAY))) + ":" + String.format("%02d", cal.get(Calendar.MINUTE)) + ", " + obj.get("userid").toString() + "] "+ obj.get("text").toString() + "\n";
                                textView.setText(textView.getText() + elem);
                            }
                        } catch (JSONException e) {
                            Log.e("ERR", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERR", error.toString());
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

                handler.postDelayed(this, millis);
            }
        }, millis);

        ((Button) findViewById(R.id.sendButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = URLEncoder.encode(username);
                String text = URLEncoder.encode(inputField.getText().toString());
                String url = writeurl + "?userid=" + userid + "&text=" + text;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.e("Error.Response", error.toString());
                            }
                        }
                );
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
    }

}
