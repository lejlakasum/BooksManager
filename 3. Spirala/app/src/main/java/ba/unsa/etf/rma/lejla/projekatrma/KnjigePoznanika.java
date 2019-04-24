package ba.unsa.etf.rma.lejla.projekatrma;


import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.widget.Toast;

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


public class KnjigePoznanika extends IntentService {


    public int STATUS_START = 0;
    public int STATUS_FINISH = 1;
    public int STATUS_ERROR = 2;
    ArrayList<Knjiga> dohvacene= new ArrayList<Knjiga>();

    public KnjigePoznanika() {
        super(null);
    }

    public KnjigePoznanika(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        final String idKorisnika = intent.getStringExtra("idKorisnika");
        Bundle bundle = new Bundle();

        receiver.send(STATUS_START, Bundle.EMPTY);

        //POZIVANJE WEB SERVISA

        String query = null;
        try {
            query = URLEncoder.encode(idKorisnika, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url1 = "https://www.googleapis.com/books/v1/users/"+query+"/bookshelves/";


        try {
            URL url = new URL(url1);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String rezultat = convertStreamToString(in);

            JSONObject jo = new JSONObject(rezultat);

            JSONArray items = jo.getJSONArray("items");

            for (int j = 0; j < items.length(); j++) {
                JSONObject polica = items.getJSONObject(j);
                String idP = polica.getString("id");
                Toast.makeText(this, idP, Toast.LENGTH_SHORT).show();

                String url2 = "https://www.googleapis.com/books/v1/users/102654000518894056523/bookshelves/"+idP+"/volumes";

                try {
                    URL urlPolica = new URL(url2);

                    HttpURLConnection urlConnection2 = (HttpURLConnection) urlPolica.openConnection();
                    InputStream in2 = new BufferedInputStream(urlConnection2.getInputStream());

                    String rezultat2 = convertStreamToString(in2);

                    JSONObject jo2 = new JSONObject(rezultat2);

                    JSONArray items2 = jo2.getJSONArray("items");


                    for (int i = 0; i < items2.length(); i++) {
                        JSONObject knjiga = items2.getJSONObject(i);

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


                            for (int k = 0; k < autori.length(); k++) {
                                autori2.add(new Autor(autori.get(k).toString(), new ArrayList<String>()));
                            }
                        }

                        Knjiga nova = new Knjiga(id, naziv, autori2, opis, datumObjavljivanja, slika, brojStranica);
                        dohvacene.add(nova);

                    }

                } catch (MalformedURLException e) {
                    bundle.putString(Intent.EXTRA_TEXT, e.toString());
                    receiver.send(STATUS_ERROR, bundle);
                    e.printStackTrace();

                } catch (IOException e) {
                    bundle.putString(Intent.EXTRA_TEXT, e.toString());
                    receiver.send(STATUS_ERROR, bundle);
                    e.printStackTrace();

                } catch (JSONException e) {
                    bundle.putString(Intent.EXTRA_TEXT, e.toString());
                    receiver.send(STATUS_ERROR, bundle);
                    e.printStackTrace();
                }

            }
            bundle.putParcelableArrayList("result", dohvacene);
            receiver.send(STATUS_FINISH, bundle);
        }
        catch (MalformedURLException e) {
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, bundle);
            e.printStackTrace();

        } catch (IOException e) {
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, bundle);
            e.printStackTrace();

        } catch (JSONException e) {
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, bundle);
            e.printStackTrace();
        }
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
