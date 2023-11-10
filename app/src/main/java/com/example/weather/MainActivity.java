package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText incity;
    private androidx.appcompat.widget.AppCompatButton mainbtn;
    private TextView pogoda;
    private TextView feels;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incity = findViewById(R.id.incity);
        mainbtn = findViewById(R.id.mainbtn);
        pogoda = findViewById(R.id.pogoda);
        feels = findViewById(R.id.feels);


        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(incity.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Введите город", Toast.LENGTH_SHORT).show();
                }
                else {
                    String city = incity.getText().toString();
                    String key = "379be89d6b1793116e85f617df971203";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid=" +key+ "&units=metric&lang=ru";

                    new GetURLDate().execute(url);
                }
            }});
    }
    private class GetURLDate extends AsyncTask<String,String,String>{

        protected void onPreExecute(){
            super.onPreExecute();
            pogoda.setText("Ожидайте...");
            feels.setText("Вычисляем");
        }


        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader  reader  = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (connection != null)
                    connection.disconnect();

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(result);
                pogoda.setText("Температура " +obj.getJSONObject("main").getDouble("temp"));
                feels.setText("Чувствуется " +obj.getJSONObject("main").getDouble("feels_like"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        }
    }
}