package com.example.saggu.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MQWebViewActivity extends AppCompatActivity {

    String TAG = "Web Activity";
    WebView mywebView;
    ProgressBar progressBar;
    Bundle webViewBundle;
    String sn, oldsn;

    String cActivity;
    String MQID1, MQPASS1, MQID2, MQPASS2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqweb_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MQ");
        mywebView = (WebView) findViewById(R.id.myWebView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // WebView m = new WebView(this);

        mywebView.getSettings().setJavaScriptEnabled(true);
        mywebView.getSettings().setDomStorageEnabled(true);
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
            mywebView.loadUrl("https://123.63.148.103:10443/remote/login?lang=en");
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
                Log.d(TAG, "onResume: if executed");
                String blank = "";
                mywebView.loadUrl("javascript:(function() { document.getElementById('ctl00_uxPgCPH_uxSearchCtrl_txtCustnbr').value = '" + blank + "'; ;})()");

                //ctl00_uxPgCPH_uxSearchCtrl_txtsearch_device_no
                mywebView.loadUrl("javascript:(function() { document.getElementById('ctl00_uxPgCPH_uxSearchCtrl_txtsearch_device_no').value = '" + sn + "'; ;})()");
                //aspnetForm
                mywebView.loadUrl("javascript:document.getElementById('aspnetForm').submit();");
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
            Log.d(TAG, "onOptionsItemSelected: " + sn);

            String blank = "";
            mywebView.loadUrl("javascript:(function() { document.getElementById('ctl00_uxPgCPH_uxSearchCtrl_txtCustnbr').value = '" + blank + "'; ;})()");
            //ctl00_uxPgCPH_uxSearchCtrl_txtsearch_device_no
            mywebView.loadUrl("javascript:(function() { document.getElementById('ctl00_uxPgCPH_uxSearchCtrl_txtsearch_device_no').value = '" + sn + "'; ;})()");
            //aspnetForm
            mywebView.loadUrl("javascript:document.getElementById('aspnetForm').submit();");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
       mywebView.saveState(outState);
    }*/

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
            progressBar.setVisibility(View.GONE);
            //  Toast.makeText(MainActivity.this, "onPageFinished called", Toast.LENGTH_SHORT).show();
            String currentURL = mywebView.getUrl();
            Log.d(TAG, "onPageFinished: " + url);


            if (currentURL.equals("https://123.63.148.103:10443/remote/login?lang=en")) {
                mywebView.loadUrl("javascript:(function() { document.getElementById('username').value = '" + MQID1 + "'; ;})()");
                mywebView.loadUrl("javascript:(function() { document.getElementById('credential').value = '" + MQPASS1 + "'; ;})()");
                // mywebView.loadUrl("javascript:(function() { document.getElementById('username').value = 'JLFW_1768'; ;})()");
                // mywebView.loadUrl("javascript:(function() { document.getElementById('credential').value = 'JLFW_1768@1'; ;})()");

            }
            if (currentURL.equals("https://123.63.148.103:10443/sslvpn/portal.html?lang=en")) {
                Log.d(TAG, "onPageFinished: URL 2 ");
                mywebView.loadUrl("javascript:document.getElementByTitle('https://168.167.0.65/fastway/login.aspx').click();");

            }
            if (currentURL.equals("https://123.63.148.103:10443/proxy/https/168.167.0.65/fastway/login.aspx")) {

                Log.d(TAG, "onPageFinished: URL 3");
                // mywebView.loadUrl("javascript:(function() { document.getElementById('ctl00_uxPgCPH_username').value = 'JLFW_1768'; ;})()");
                // mywebView.loadUrl("javascript:(function() { document.getElementById('ctl00_uxPgCPH_password').value = 'JASPALSHARMA1'; ;})()");

                mywebView.loadUrl("javascript:(function() { document.getElementById('ctl00_uxPgCPH_username').value = '" + MQID2 + "'; ;})()");
                mywebView.loadUrl("javascript:(function() { document.getElementById('ctl00_uxPgCPH_password').value = '" + MQPASS2 + "'; ;})()");


            }
            if (currentURL.equals("https://www.google.co.in/?gfe_rd=cr&ei=yqL8WK3FDeLs8AeTirewDQ")) {
                Log.d(TAG, "onPageFinished: google.com");
                mywebView.loadUrl("javascript:(function() { document.getElementById('lst-ib').value = '" + sn + "'; ;})()");
                //  mywebView.loadUrl("javascript:document.getElementById('tsf').submit();");


            }
            if (currentURL.equals("https://123.63.148.103:10443/proxy/https/168.167.0.65/fastway/OrderManagement/ServiceOrderEnhanced.aspx?MenuID=683&") && counter == 0) {
                mywebView.loadUrl("javascript:(function() { document.getElementById('ctl00_uxPgCPH_uxSearchCtrl_txtsearch_device_no').value = '" + sn + "'; ;})()");
                mywebView.loadUrl("javascript:document.getElementById('aspnetForm').submit();");
                counter = 1;


                // mywebView.loadUrl("javascript:document.getElementById('aspnetForm').submit();");


            }

            Log.d(TAG, "onPageFinished: " + counter);

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //  super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    }


}
