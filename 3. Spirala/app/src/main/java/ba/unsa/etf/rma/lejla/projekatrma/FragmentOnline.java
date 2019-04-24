package ba.unsa.etf.rma.lejla.projekatrma;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone, KnjigePoznanikaResultReciever.Receiver {

    public static View v;
    ArrayList<Knjiga> popunjeniSpiner = new ArrayList<Knjiga>();
    int indeksOdabrane = 0;
    String odabranaKat = "Drama";


    @Override
    public void onReceiveResult(int resultCode, Bundle resulData) {

        switch (resultCode) {
            case 0:
                Toast.makeText(getContext(), "Poceo", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                ArrayList<Knjiga> k = resulData.getParcelableArrayList("result");
                popunjeniSpiner = k;
                ArrayList<String> knjige = new ArrayList<String>();
                for (int i = 0; i < k.size(); i++) {
                    knjige.add(k.get(i).getNaziv());
                }

                Spinner rezultat = (Spinner) v.findViewById(R.id.sRezultat);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, knjige);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                rezultat.setAdapter(adapter);

                Toast.makeText(getContext(), "Zavrsio", Toast.LENGTH_SHORT).show();

                break;
            case 2:
                break;
        }

    }

    @Override
    public void onDohvatiDone(ArrayList<Knjiga> k) {

        popunjeniSpiner = k;

        ArrayList<String> knjige = new ArrayList<String>();
        for (int i = 0; i < k.size(); i++) {
            knjige.add(k.get(i).getNaziv());
        }

        Spinner rezultat = (Spinner) v.findViewById(R.id.sRezultat);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, knjige);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rezultat.setAdapter(adapter);
    }

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> k) {
        popunjeniSpiner = k;

        ArrayList<String> knjige = new ArrayList<String>();
        for (int i = 0; i < k.size(); i++) {
            knjige.add(k.get(i).getNaziv());
        }

        Spinner rezultat = (Spinner) v.findViewById(R.id.sRezultat);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, knjige);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rezultat.setAdapter(adapter);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.online_fragment, container, false);
        v=view;


                //POPUNJAVANJE SPINERA SA KATEGORIJAMA

        final Spinner kategorije = (Spinner)view.findViewById(R.id.sKategorije);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, KategorijeAkt.kategorije);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategorije.setAdapter(adapter);

                //DUGME POVRATAK NA POCETAK

        Button povratak = (Button)view.findViewById(R.id.dPovratak);
        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment listaKategorije = new ListeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.mjesto, listaKategorije );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

                //DUGME PRETRAGA ONLINE

        Button pretraga = (Button)view.findViewById(R.id.dRun);
        final EditText nazivKojegTrazim = (EditText)view.findViewById(R.id.tekstUpit);
        pretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uneseno = nazivKojegTrazim.getText().toString();

                if(!(uneseno.equals(null) || uneseno.isEmpty() || uneseno.equals(""))) {

                    if (uneseno.toLowerCase().contains("autor:".toLowerCase())) {
                        String autor = "";
                        List<String> nazivi1 = new ArrayList<String>(Arrays.asList(uneseno.split(":")));
                        ArrayList<String> nazivi = (ArrayList<String>) nazivi1;
                        if (nazivi.size() == 2)
                            autor = nazivi.get(1);
                        new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone) FragmentOnline.this).execute(autor);
                    }
                    else if(uneseno.toLowerCase().contains("korisnik:".toLowerCase())) {

                        String id = "";
                        List<String> nazivi1 = new ArrayList<String>(Arrays.asList(uneseno.split(":")));
                        ArrayList<String> nazivi = (ArrayList<String>) nazivi1;
                        if (nazivi.size() == 2)
                            id = nazivi.get(1);

                        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), KnjigePoznanika.class);
                         KnjigePoznanikaResultReciever mReceiever = new KnjigePoznanikaResultReciever(new Handler());
                         mReceiever.setReceiver(FragmentOnline.this);
                         intent.putExtra("idKorisnika", id);
                         intent.putExtra("receiver", mReceiever);
                         getActivity().startService(intent);

                    }

                    else {
                        List<String> nazivi1 = new ArrayList<String>(Arrays.asList(uneseno.split(";")));
                        ArrayList<String> nazivi = (ArrayList<String>) nazivi1;

                        new DohvatiKnjige((DohvatiKnjige.IDohvatiKnigeDone) FragmentOnline.this).execute(nazivi);
                    }
                }

            }
        });


                //SLUŠA MIJENJANJE ODABRANE KNJIGE

        final Spinner odabranaKnjiga = (Spinner)view.findViewById(R.id.sRezultat);
        odabranaKnjiga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                indeksOdabrane=odabranaKnjiga.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

                //SLUŠA MIJENJANJE ODABRANE KATEGORIJE

        final Spinner kat =(Spinner)view.findViewById(R.id.sKategorije);
        kat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                odabranaKat=kat.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

                //DUGME ZA DODAVANJE KNJIGE

        Button dodajOnline =(Button)view.findViewById(R.id.dAdd);
        dodajOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Knjiga pom = popunjeniSpiner.get(indeksOdabrane);
                Knjiga nova = new Knjiga(pom.getId(), pom.getNaziv(), pom.getAutori(), pom.getOpis(), pom.getDatumObjavljivanja(), pom.getSlika(), pom.getBrojStrinica());
                nova.setKategorija(odabranaKat);
                dodajKnjigu(nova);

            }
        });

        return view;
    }

    public void dodajKnjigu(Knjiga k) {
        int i;
        for(i=0; i<KategorijeAkt.knjige.size(); i++) {
            if(KategorijeAkt.knjige.get(i).getId().equalsIgnoreCase(k.getId())) {
                Toast.makeText(getContext(), "Knjiga vec postoji", Toast.LENGTH_LONG).show();
                break;
            }
        }
        if(i==KategorijeAkt.knjige.size()) {
            KategorijeAkt.knjige.add(k);


            for(i=0; i<k.getAutori().size(); i++) {

                int j;
                for (j = 0; j < KategorijeAkt.autori.size(); j++) {
                    if (k.getAutori().get(i).getImeiPrezime().toLowerCase().equalsIgnoreCase(KategorijeAkt.autori.get(j).getImeiPrezime().toLowerCase())) {
                        KategorijeAkt.autori.get(j).dodajKnjigu(k.getId());
                        break;
                    }
                }
                if (j == KategorijeAkt.autori.size()) {

                    Autor a = k.getAutori().get(i);
                    a.dodajKnjigu(k.getId());

                    KategorijeAkt.autori.add(a);


                }


            }
            Toast.makeText(getContext(), "Knjiga je uspjesno dodana", Toast.LENGTH_LONG).show();
        }
    }
}
