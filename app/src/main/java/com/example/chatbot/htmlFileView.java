package com.example.chatbot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class htmlFileView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_file_view);
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");

        WebView web;
        web = (WebView) findViewById(R.id.webViewHtml);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
