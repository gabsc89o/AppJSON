package com.example.usuari.accesojson;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvdatos;
    public ArrayList<String> misdatos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvdatos = this.findViewById(R.id.lvjson);
        lvdatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MainActivity.this, misdatos.get(position), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void Mostrar(View v){
        Comunicacion com = new Comunicacion();
        com.execute("http://datos.alcobendas.org/dataset/61a5001c-08d9-470d-a90b-4a31982c9c8c/resource/b48ed90d-6b86-4cce-8801-440fa08c6b09/download/poblacion-por-nacionalidades2017-2018.json");
    }

    public void Edad(View v){
        Intent i = new Intent(this,EdadActivity.class);
        this.startActivity(i);
    }
    private class Comunicacion  extends AsyncTask<String, Void, String>{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String[] datos = null;
            try {
                JSONArray jarray = new JSONArray(s);
                datos = new String[jarray.length()];
                for (int i =0;i<jarray.length();i++){
                    JSONObject job=jarray.getJSONObject(i);
                    String nac = job.getString("Nacionalidad");
                    datos[i]="Nacionalidad : "+nac;
                    int num = job.getInt("Número de empadronados");
                    datos[i]+="\n"+"Cantidad : "+num;
                    misdatos.add(nac);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,datos);
            lvdatos.setAdapter(adp);
        }

        @Override
        protected String doInBackground(String... strings) {
            String data="";
            try {
                URL url = new URL(strings[0]);
                URLConnection con = url.openConnection();
                String s;
                InputStream is = con.getInputStream();
                BufferedReader bf = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                while ((s=bf.readLine())!=null){
                    data += s;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }
    }
}
