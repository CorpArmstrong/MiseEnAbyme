package com.corparmstrong.miseenabyme;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Tantalus on 9/12/2015.
 */
public class ViewHomePageActivity extends Activity {

    public static final String HOMEPAGE_URL = "https://www.apostlemod.com/home-mise-en-abyme.php";

    private WebView webView;
    private WebView webViewHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_remote_access);

        webView = (WebView) findViewById(R.id.webView);
        webViewHidden = (WebView) findViewById(R.id.webViewHidden);
        webView.setWebViewClient(new HelloWebViewClient());

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        // By using this method together with the overridden method onReceivedSslError()
        // you will avoid the "WebView Blank Page" problem to appear. This might happen if you
        // use a "https" url!
        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(
                    WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                WebView childWebView = webViewHidden;
                childWebView.setVisibility(View.VISIBLE);
                //webView.setVisibility(View.GONE);
                childWebView.setWebViewClient(new HelloWebViewClient());
                childWebView.getSettings().setJavaScriptEnabled(true);
                childWebView.getSettings().setDomStorageEnabled(true);
                transport.setWebView(childWebView);
                resultMsg.sendToTarget();
                return true;
            }
        });

        URI uri = null;

        try {
            uri = new URI(HOMEPAGE_URL);
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
            Toast.makeText(ViewHomePageActivity.this,
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
