package ba.unsa.etf.rma.lejla.projekatrma;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity {

    static Context con;
    Boolean siriL = false;
    static ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
    static ArrayList<String> kategorije = new ArrayList<String>();
    static ArrayList<Autor> autori = new ArrayList<Autor>();

    public static ArrayList<Autor> getAutori() {
        return autori;
    }

    public static void setAutori(ArrayList<Autor> autori) {
        KategorijeAkt.autori = autori;
    }

    public static ArrayList<Knjiga> getKnjige() {
        return knjige;
    }

    public static void setKnjige(ArrayList<Knjiga> knjige) {
        KategorijeAkt.knjige = knjige;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);



        //Lista kategorija
        kategorije.clear();
        kategorije.add("Drama");
        kategorije.add("Krimi");
        kategorije.add("Triler");
        kategorije.add("Romansa");
        kategorije.add("Pustolovina");

        Fragment frag = new ListeFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.mjesto, frag);
            transaction.addToBackStack(null);
            transaction.commit();


    }


}
