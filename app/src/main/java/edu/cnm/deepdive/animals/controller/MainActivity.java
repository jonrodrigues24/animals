package edu.cnm.deepdive.animals.controller;

import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.cnm.deepdive.animals.BuildConfig;
import edu.cnm.deepdive.animals.R;
import edu.cnm.deepdive.animals.model.Animal;
import edu.cnm.deepdive.animals.service.WebServiceProxy;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  private WebView contentView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    contentView = findViewById(R.id.content_view);
    setupWebView();


  }

  private void setupWebView() {

    contentView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
      }
    });
    WebSettings settings = contentView.getSettings();
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setDisplayZoomControls(false);
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);
    new Retriever().start();
  }

  private class Retriever extends Thread {

    @Override
    public void run() {
      try {
        Response<List<Animal>> response = WebServiceProxy.getInstance().getAnimals(BuildConfig.API_KEY).execute();
        if(response.isSuccessful()) {

          Log.d(getClass().getName(), response.message());
        } else {

          Log.e(getClass().getName(), response.message());

        }
      } catch (IOException e) {
        Log.e(getClass().getName(), e.getMessage(), e);
      }

    }
  }

}