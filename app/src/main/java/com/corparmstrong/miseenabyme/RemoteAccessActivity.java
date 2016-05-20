package com.corparmstrong.miseenabyme;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by dmitriyfedykovich on 29.08.15.
 */
public class RemoteAccessActivity extends Activity {

    public static final String REMOTE_ACCESS_URL = "https://www.apostlemod.com/MiseEnAbyme";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_remote_access);

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new HelloWebViewClient());

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(true);

        URI uri = null;

        try {
            uri = new URI(REMOTE_ACCESS_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (uri != null) webView.loadUrl(uri.toString());
        else webView.loadUrl("http://www.google.com");
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            Toast.makeText(RemoteAccessActivity.this,
                    "Your Internet Connection May not be active Or " + description,
                    Toast.LENGTH_LONG).show();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }
    }
}
