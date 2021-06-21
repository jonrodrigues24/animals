package edu.cnm.deepdive.animals.controller;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.animals.BuildConfig;
import edu.cnm.deepdive.animals.R;
import edu.cnm.deepdive.animals.model.Animal;
import edu.cnm.deepdive.animals.service.WebServiceProxy;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;


//the controller
public class MainActivity extends AppCompatActivity {

  private ImageView image;
  private Spinner animalSelector;
  private ArrayAdapter<Animal> adapter;

  @Override

  //immediately displays app is open // ask about this
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    image = findViewById(R.id.image);
    animalSelector = findViewById(R.id.animal_selector);
    animalSelector.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        Animal animal = (Animal) adapterView.getItemAtPosition(position);
        Picasso.get().load(animal.getImageUrl()).into(image);

      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });
    new RetrieverTask().execute();


  }
//web view settings to fit device deleted

// runs the app


  private class RetrieverTask extends AsyncTask<Void, Void, List<Animal>> {


    @Override
    protected List<Animal> doInBackground(Void... voids) {
      try {
        Response<List<Animal>> response = WebServiceProxy.getInstance()
            .getAnimals(BuildConfig.API_KEY)
            .execute();
        if(response.isSuccessful()) {
        return response.body();
        } else {
          Log.e(getClass().getName(), response.message());
          cancel(true);
          return null;
        }
      } catch (IOException e) {
        Log.e(getClass().getName(), e.getMessage(), e);
        cancel(true);
        return null;
      }
    }

    @Override
    protected void onPostExecute(List<Animal> animals) {
      super.onPostExecute(animals);

      adapter = new ArrayAdapter<>(MainActivity.this
          , R.layout.item_animal_spinner, animals);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
      animalSelector.setAdapter(adapter);

    }


  }

}