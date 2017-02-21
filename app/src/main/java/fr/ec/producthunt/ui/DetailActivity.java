package fr.ec.producthunt.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ViewAnimator;
import fr.ec.producthunt.R;

public class DetailActivity extends AppCompatActivity {

  public static final String POST_URL_KEY = "post_url";
  public static final int PROGRESS_CHILD =0;
  public static final int WEBVIEW_CHILD =1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    WebView webView = (WebView) findViewById(R.id.webView);
    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);

    final ViewAnimator viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
    viewAnimator.setDisplayedChild(PROGRESS_CHILD);


    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    webView.setWebViewClient(new WebViewClient() {

      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
      }

      @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        viewAnimator.setDisplayedChild(PROGRESS_CHILD);

      }

      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        viewAnimator.setDisplayedChild(WEBVIEW_CHILD);
      }
    });
    String postUrl = obtainPostUrlFromIntent();
    webView.loadUrl(postUrl);

  }

  private String obtainPostUrlFromIntent() {

    Intent intent = getIntent();
    if(intent.getExtras().containsKey(POST_URL_KEY)) {
      return intent.getExtras().getString(POST_URL_KEY);
    }else {
      throw new IllegalStateException("Il faut passer l'url du post");
    }
  }
}
