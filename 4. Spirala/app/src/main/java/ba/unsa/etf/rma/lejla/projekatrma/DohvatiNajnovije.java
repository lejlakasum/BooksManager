package ba.unsa.etf.rma.lejla.projekatrma;


import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DohvatiNajnovije extends AsyncTask<String, Integer, Void> {

            //ATRIBUTI

    ArrayList<Knjiga> dohvacene= new ArrayList<Knjiga>();
    private IDohvatiNajnovijeDone pozivatelj;
    public DohvatiNajnovije(IDohvatiNajnovijeDone p) { pozivatelj=p;}



    public interface IDohvatiNajnovijeDone {
        public void onNajnovijeDone(ArrayList<Knjiga> k);

    }


    @Override
    protected Void doInBackground(String... strings) {

        String query = null;
        try {
            query = URLEncoder.encode(strings[0], "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url1 = "https://www.googleapis.com/books/v1/volumes?q=author:"+query+"&orderBy=newest&maxResults=5";


        try {
            URL url = new URL(url1);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String rezultat = convertStreamToString(in);

            JSONObject jo = new JSONObject(rezultat);

            JSONArray items = jo.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject knjiga = items.getJSONObject(i);

                String id = knjiga.getString("id");
                String naziv = "", opis = "", datumObjavljivanja = "";
                JSONArray autori = new JSONArray();
                URL slika = null;
                JSONObject imageLink = null;
                int brojStranica = 0;
                ArrayList<Autor> autori2 = new ArrayList<Autor>();

                JSONObject volumeInfo;
                if (knjiga.has("volumeInfo")) {
                    volumeInfo = knjiga.getJSONObject("volumeInfo");
                    if (volumeInfo.has("title"))
                        naziv = volumeInfo.getString("title");


                    if (volumeInfo.has("authors"))
                        autori = volumeInfo.getJSONArray("authors");


                    if (volumeInfo.has("description"))
                        opis = volumeInfo.getString("description");


                    if (volumeInfo.has("publishedDate"))
                        datumObjavljivanja = volumeInfo.getString("publishedDate");


                    if (volumeInfo.has("imageLinks"))
                        imageLink = volumeInfo.getJSONObject("imageLinks");


                    if (volumeInfo.has("imageLinks") && imageLink.has("thumbnail"))
                        slika = new URL(imageLink.get("thumbnail").toString());


                    if (volumeInfo.has("pageCount"))
                        brojStranica = volumeInfo.getInt("pageCount");


                    for (int j = 0; j < autori.length(); j++) {
                        autori2.add(new Autor(autori.get(j).toString(), new ArrayList<String>()));
                    }
                }

                Knjiga nova = new Knjiga(id, naziv, autori2, opis, datumObjavljivanja, slika, brojStranica);
                dohvacene.add(nova);

            }


        }
     catch (MalformedURLException e) {
        e.printStackTrace();

    } catch (IOException e) {
        e.printStackTrace();

    } catch (JSONException e) {
        e.printStackTrace();
    }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pozivatelj.onNajnovijeDone(dohvacene);
    }

    private String convertStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {

                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }
}
