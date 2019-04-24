package ba.unsa.etf.rma.lejla.projekatrma;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import java.util.ArrayList;


public class ListeFragment extends Fragment {

    private static ArrayAdapter<String> adapter ;

    static Boolean clickedKategorija;

    static String odabrano;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.liste_fragment, container, false);

        final Button dKategorije = (Button)view.findViewById(R.id.dKategorije);
        final ListView listaKategorija = (ListView)view.findViewById(R.id.listaKategorija);
        final EditText tekstPretraga = (EditText)view.findViewById(R.id.tekstPretraga);
        final Button dPretraga = (Button)view.findViewById(R.id.dPretraga);
        final Button dDodajKategoriju = (Button)view.findViewById(R.id.dDodajKategoriju);
        Button dDodajKnjigu = (Button) view.findViewById(R.id.dDodajKnjigu);
        final Button dAutori = (Button)view.findViewById(R.id.dAutori);


        dAutori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickedKategorija=false;

                dPretraga.setVisibility(View.GONE);
                dDodajKategoriju.setVisibility(View.GONE);
                tekstPretraga.setVisibility(View.GONE);
                dKategorije.setVisibility(View.VISIBLE);

                BazaOpenHelper baza = new BazaOpenHelper(getContext(), BazaOpenHelper.DATABASE_NAME, null, BazaOpenHelper.DATABSE_VERSION);
                ArrayList<String> ucitaniAutori= baza.ucitajAutore();
                KategorijeAkt.autoriStringovi=ucitaniAutori;
                ArrayAdapter<String> adapter2 = new AutorAdapter(getActivity(), ucitaniAutori);
                listaKategorija.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();

            }
        });

        dKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickedKategorija=true;

                dPretraga.setVisibility(View.VISIBLE);
                dDodajKategoriju.setVisibility(View.VISIBLE);
                tekstPretraga.setVisibility(View.VISIBLE);
                //Ucitavanje iz baze
                BazaOpenHelper baza = new BazaOpenHelper(getContext(), BazaOpenHelper.DATABASE_NAME, null, BazaOpenHelper.DATABSE_VERSION);
                KategorijeAkt.kategorije = baza.ucitajKategorije();

                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, KategorijeAkt.kategorije);

                listaKategorija.setAdapter(adapter);

            }
        });

        listaKategorija.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(clickedKategorija) {
                    odabrano = listaKategorija.getItemAtPosition(i).toString();
                }
                else {
                    odabrano = KategorijeAkt.autoriStringovi.get(i);

                }
                //---------------------------------------------------------------------------------------------------------
                if(getActivity().getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT) {
                    Fragment listaKnjiga = new KnjigeFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.mjesto, listaKnjiga);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else {
                    Fragment listaKnjiga = new KnjigeFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.mjesto2, listaKnjiga);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

            }
        });

        tekstPretraga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                dDodajKategoriju.setEnabled(false);
                if(editable.length()==0) {
                    dPretraga.setEnabled(false);
                }

                else {
                    dPretraga.setEnabled(true);
                }
            }
        });

        dPretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String unesena = tekstPretraga.getText().toString();
                BazaOpenHelper baza = new BazaOpenHelper(getContext(), BazaOpenHelper.DATABASE_NAME, null, BazaOpenHelper.DATABSE_VERSION);
                KategorijeAkt.kategorije = baza.ucitajKategorije();
                adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, KategorijeAkt.kategorije);
                adapter.getFilter().filter(unesena, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                        if(i==0) {
                            dDodajKategoriju.setEnabled(true);
                        }
                        else {
                            dDodajKategoriju.setEnabled(false);
                            int brojac;
                            for(brojac=0; brojac<i; brojac++) {
                                String a = adapter.getItem(brojac).toLowerCase();
                                String b = unesena.toLowerCase();
                                if(a.equalsIgnoreCase(b)) {
                                    break;
                                }
                            }
                            if(brojac==i) {
                                dDodajKategoriju.setEnabled(true);
                            }
                        }
                        dDodajKategoriju.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                BazaOpenHelper baza = new BazaOpenHelper(getContext(), BazaOpenHelper.DATABASE_NAME, null, BazaOpenHelper.DATABSE_VERSION);
                                baza.dodajKategoriju(unesena);

                                adapter.add(unesena);
                                dDodajKategoriju.setEnabled(false);
                                adapter.getFilter().filter(unesena);

                            }
                        });
                    }
                });
                listaKategorija.setAdapter(adapter);
            }
        });



        dDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity().getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT) {
                    Fragment dodajKnjigu = new DodavanjeKnjigeFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.mjesto, dodajKnjigu);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
                else {
                    Fragment dodajKnjigu = new DodavanjeKnjigeFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.mjesto, dodajKnjigu);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
            }
        });



        Button dodajOnline = (Button)view.findViewById(R.id.dDodajOnline);

        dodajOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment dodavanjeOnline = new FragmentOnline();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.mjesto, dodavanjeOnline);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return view;

    }

  static ArrayList<String> autoriStringovi(ArrayList<Autor> a) {
        ArrayList<String> s = new ArrayList<>();

        for(int i=0; i<a.size(); i++) {
            s.add(a.get(i).getImeiPrezime());
        }
        return s;
  }


}
