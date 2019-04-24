package ba.unsa.etf.rma.lejla.projekatrma;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.util.ArrayList;


public class KnjigeFragment extends Fragment {

    public static Knjiga kliknuta = new Knjiga();

    public static ArrayList<Knjiga> odabrane = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.knjige_fragment, container, false);


        Button dPovratak = (Button)view.findViewById(R.id.dPovratak);
        Button dPosalji = (Button)view.findViewById(R.id.dPosalji);

        odabrane = new ArrayList<Knjiga>();

        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment listaKategorije = new ListeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.mjesto, listaKategorije );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        if(ListeFragment.clickedKategorija) {

            String odabranaKategorija = ListeFragment.odabrano;
            BazaOpenHelper baza = new BazaOpenHelper(getContext(), BazaOpenHelper.DATABASE_NAME, null, BazaOpenHelper.DATABSE_VERSION);
            SQLiteDatabase db = baza.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Kategorija WHERE naziv LIKE '"+odabranaKategorija+"'", null);
            cursor.moveToFirst();
            long idKategorije =  cursor.getInt(cursor.getColumnIndex("_id"));
            try {
                odabrane=baza.knjigeKategorije(idKategorije);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        else {
            String odabraniAutor = ListeFragment.odabrano;
            BazaOpenHelper baza = new BazaOpenHelper(getContext(), BazaOpenHelper.DATABASE_NAME, null, BazaOpenHelper.DATABSE_VERSION);
            SQLiteDatabase db = baza.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Autor WHERE ime LIKE '"+odabraniAutor+"'", null);
            cursor.moveToFirst();
            odabrane=baza.knjigeAutora(cursor.getInt(cursor.getColumnIndex("_id")));
            cursor.close();


        }

            final ListView listaKnjiga = (ListView)view.findViewById(R.id.listaKnjiga);

            ArrayAdapter<Knjiga> adapter = new KnjigaAdapter(getActivity(), odabrane);

            listaKnjiga.setAdapter(adapter);


            listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    for (int i = 0; i < listaKnjiga.getChildCount(); i++) {
                        if(position == i ){
                            listaKnjiga.getChildAt(i).setBackgroundColor(0xffaabbed);
                            Knjiga obojena = odabrane.get(i);
                            BazaOpenHelper baza = new BazaOpenHelper(getContext(), BazaOpenHelper.DATABASE_NAME, null, BazaOpenHelper.DATABSE_VERSION);
                            SQLiteDatabase db = baza.getWritableDatabase();
                            ContentValues novi = new ContentValues();
                            novi.put("pregledana", 1);
                            db.update(BazaOpenHelper.KNJIGA_TABLE, novi, "idWebServis LIKE '"+obojena.getId()+"'", null);
                        }
                    }

                }

            });





        return view;
    }
}
