package edu.cnm.deepdive.animals.controller;

import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.cnm.deepdive.animals.BuildConfig;
import edu.cnm.deepdive.animals.R;
import edu.cnm.deepdive.animals.model.Animal;
import edu.cnm.deepdive.animals.service.WebServiceProxy;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import retrofit2.Response;


//the controller
public class MainActivity extends AppCompatActivity {

  private WebView contentView;
  private Spinner animalSelector;
  private ArrayAdapter<Animal> adapter;

  @Override

  //immediately displays app is open // ask about this
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    contentView = findViewById(R.id.content_view);
    animalSelector = findViewById(R.id.animal_selector);
    animalSelector.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        Animal animal = (Animal) adapterView.getItemAtPosition(position);
        contentView.loadUrl(animal.getImageUrl());

      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });
    setupWebView();


  }
//web view settings to fit device
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
// runs the app
  private class Retriever extends Thread {

    @Override
    public void run() {
      try {
        Response<List<Animal>> response = WebServiceProxy.getInstance().getAnimals(BuildConfig.API_KEY).execute();
        if(response.isSuccessful()) {
          List<Animal> animals = response.body();


          adapter = new ArrayAdapter<>(MainActivity.this
              , R.layout.item_animal_spinner, animals);
          adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
          runOnUiThread(() -> {

            animalSelector.setAdapter(adapter);
          });

        } else {

          Log.e(getClass().getName(), response.message());

        }
      } catch (IOException e) {
        Log.e(getClass().getName(), e.getMessage(), e);
      }

    }
  }

}