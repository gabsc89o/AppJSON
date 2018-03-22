package com.example.usuari.accesojson;

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

public class EdadActivity extends AppCompatActivity {
    ListView lvedad;
    public ArrayList<String> misdatos2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edad);
        lvedad = this.findViewById(R.id.lvedad);
        Comunicacion2 com2 = new Comunicacion2();
        com2.execute("http://datos.alcobendas.org/dataset/de24ee8b-9215-4ffd-8aa1-3ad40ce8559f/resource/d06a74c0-6b24-4490-a6ec-0c6083d39d64/download/poblacion-actual-por-distritos-grupos-de-edad-y-sexopara-tableau.json");
        lvedad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(EdadActivity.this, misdatos2.get(position), Toast.LENGTH_LONG).show();
                }
            });
    }
    public void volver(View v){this.finish();}

    private class Comunicacion2  extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String[] datos = null;
            //int[] hom = null;
            //int[] muj = null;
            try {
                JSONArray jarray = new JSONArray(s);
                datos = new String[jarray.length()];
                for (int i =0;i<jarray.length();i++){
                    JSONObject job=jarray.getJSONObject(i);
                    String nac = job.getString("Intervalo de edad");
                    datos[i]="Edad : "+nac;
                    misdatos2.add("Hombres: "+ job.getInt("Hombres")+"\nMujeres: "+job.getInt("Mujeres"));
                    //muj[i] = job.getInt("Mujeres");
                    datos[i]+="\n"+"Cantidad : "+(job.getInt("Hombres")+job.getInt("Mujeres"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adp = new ArrayAdapter<String>(EdadActivity.this,android.R.layout.simple_list_item_1,datos);
            lvedad.setAdapter(adp);

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
