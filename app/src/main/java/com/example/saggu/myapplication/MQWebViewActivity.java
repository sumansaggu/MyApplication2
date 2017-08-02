package com.example.saggu.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MQWebViewActivity extends AppCompatActivity {

    static final String loginPage = "http://bss.myfastway.in:9003/oapservice/";
    static final String redirectLoginPage = "http://bss.myfastway.in:9003/oapservice/index.php?r=login/index&redirect=true";
    static final String afterLoginPage = "http://bss.myfastway.in:9003/oapservice/index.php?r=accountmanag/get_csr_info&account_no=";
    String TAG = "Web Activity";
    WebView mywebView;
    ProgressBar progressBar;
    Bundle webViewBundle;
    String sn, oldsn;

    String cActivity;
    String MQID1, MQPASS1, MQID2, MQPASS2;
    int scale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqweb_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MQ");
        mywebView = (WebView) findViewById(R.id.myWebView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // WebView m = new WebView(this);
        // mywebView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
        mywebView.getSettings().setJavaScriptEnabled(true);
        mywebView.getSettings().setDomStorageEnabled(true);
        mywebView.getSettings().setBuiltInZoomControls(true);
        mywebView.getSettings().setSupportZoom(true);


        mywebView.setWebViewClient(new myWebViewClient());
        Bundle bundle = getIntent().getExtras();
        Log.d(TAG, "onCreate: webactivity");
        loadShardPref();
        if (bundle == null) {
            return;
        }
        if (bundle != null) {
            sn = bundle.getString("SN");
            oldsn = sn;
            Log.d(TAG, "onCreate bundle: " + sn);
            // Log.d(TAG, "onResume:  first " + sn+"  second "+ oldsn);
        }

        if (savedInstanceState != null) {
            mywebView.restoreState(savedInstanceState);
            //   Log.d(TAG, "onCreate: sis not null");
        } else {
            mywebView.loadUrl(loginPage);
            //   mywebView.loadUrl("https://www.google.co.in/?gfe_rd=cr&ei=yqL8WK3FDeLs8AeTirewDQ");

        }

    }

    public void loadShardPref() {
        SharedPreferences sharedPreferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        MQID1 = sharedPreferences.getString("MQID1", "N/A");
        MQPASS1 = sharedPreferences.getString("MQPASS1", "N/A");
        MQID2 = sharedPreferences.getString("MQID2", "N/A");
        MQPASS2 = sharedPreferences.getString("MQPASS2", "N/A");
        Log.d(TAG, "ld password: " + MQID1 + " " + MQPASS1 + " " + MQID2 + " " + MQPASS2);


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
        if (intent != null) {
            setIntent(intent);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        if (bundle != null) {
            sn = bundle.getString("SN");
            cActivity = bundle.getString("CALLINGACTIVITY");
            if (!oldsn.equals(sn)) {
                myWebViewClient webViewClient = new myWebViewClient();
                Log.d(TAG, "onResume: if executed");
                mywebView.loadUrl("javascript:(function() { var x = document.getElementsByClassName('inner_custom');" +
                        "var l = x.length;" +
                        "console.log(l);" +
                        "x[0].value = 'serialno'})()");
                mywebView.loadUrl("javascript:(function() { var x = document.getElementsByClassName('nav-search-input');" +
                        "var l = x.length;" +
                        "console.log(l);" +
                        "x[0].value = '" + sn + "'})()");
                if (webViewClient.counter == 0)
                    mywebView.loadUrl("javascript:(function() { var x = document.getElementsByClassName('btn btn-sm btn-danger btn-round');" +
                            "var l = x.length;" +
                            "console.log('button');" +
                            "console.log(l);" +
                            "x[0].click()})()");
                webViewClient.counter++;
            }
            Log.d(TAG, "onResume:  new " + sn + "  old " + oldsn);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myWebViewClient myweb = new myWebViewClient();
        myweb.counter = 0;
        Log.d(TAG, "onBackPressed: ");
        Intent intent = new Intent(this, ViewAll.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown: ");
        oldsn = sn; // swap the old an new sn
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (cActivity.equals("VIEWALL")) {
                Intent intent = new Intent(this, ViewAll.class);
                startActivity(intent);
            }
            if (cActivity.equals("STBRECORD")) {
                Intent intent = new Intent(this, STBRecord.class);
                startActivity(intent);

            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mq_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.paste) {
          mywebView.reload();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    private class myWebViewClient extends WebViewClient {
        public int counter = 0;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "onPageStarted: ");
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.setInitialScale(80);

            progressBar.setVisibility(View.GONE);
            //  Toast.makeText(MainActivity.this, "onPageFinished called", Toast.LENGTH_SHORT).show();
            String currentURL = mywebView.getUrl();
            Log.d(TAG, "onPageFinished: " + url);


            if (currentURL.equals(loginPage)) {
                mywebView.loadUrl("javascript:(function() { var x = document.getElementsByClassName('form-control');" +
                        "var l = x.length;" +
                        "console.log(l);" +
                        "x[0].value = '" + MQID1 + "'})()");

                mywebView.loadUrl("javascript:(function() { document.getElementById('upass').value = '" + MQPASS1 + "'; ;})()");
                mywebView.loadUrl("javascript:(function() { document.getElementById('submitbutton').click() ; ;})()");
            }
            if (currentURL.equals(redirectLoginPage)) {
                mywebView.loadUrl("javascript:(function() { document.getElementById('username').value = '" + MQID1 + "'; ;})()");
                mywebView.loadUrl("javascript:(function() { document.getElementById('upass').value = '" + MQPASS1 + "'; ;})()");
            }
            if (currentURL.equals(afterLoginPage + MQID1) && counter == 0) {
                // setting option selected
                Log.d(TAG, "options");
                mywebView.loadUrl("javascript:(function() { var x = document.getElementsByClassName('inner_custom');" +
                        "var l = x.length;" +
                        "console.log(l);" +
                        "x[0].value = 'serialno'})()");
                //setting serial no
                Log.d(TAG, "input search");
                mywebView.loadUrl("javascript:(function() { var x = document.getElementsByClassName('nav-search-input');" +
                        "var l = x.length;" +
                        "console.log(l);" +
                        "x[0].value = '" + sn + "'})()");
                mywebView.loadUrl("javascript:(function() { var x = document.getElementsByClassName('btn btn-sm btn-danger btn-round');" +
                        "var l = x.length;" +
                        "console.log('button');" +
                        "console.log(l);" +
                        "x[0].click()})()");
                counter++;
            } else {
                // Toast.makeText(MQWebViewActivity.this, ""+currentURL, Toast.LENGTH_SHORT).show();
                mywebView.loadUrl("javascript:(function() { var x = document.getElementsByClassName('inner_custom');" +
                        "var l = x.length;" +
                        "console.log(l);" +
                        "x[0].value = 'serialno'})()");
                mywebView.loadUrl("javascript:(function() { var x = document.getElementsByClassName('nav-search-input');" +
                        "var l = x.length;" +
                        "console.log(l);" +
                        "x[0].value = '" + sn + "'})()");
            }
            Log.d(TAG, "onPageFinished and counter is: " + counter);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //  super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    }


}
