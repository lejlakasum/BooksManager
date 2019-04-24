package ba.unsa.etf.rma.lejla.projekatrma;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class KnjigeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.knjige_fragment, container, false);


        Button dPovratak = (Button)view.findViewById(R.id.dPovratak);

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

        ArrayList<Knjiga> odabrane = new ArrayList<>();

        if(ListeFragment.clickedKategorija) {

            String odabranaKategorija = ListeFragment.odabrano;


            for (int i = 0; i < KategorijeAkt.knjige.size(); i++) {
                if (KategorijeAkt.knjige.get(i).getKategorija().equals(odabranaKategorija)) {
                    odabrane.add(KategorijeAkt.knjige.get(i));
                }
            }
        }
        else {
            String odabraniAutor = ListeFragment.odabrano;
            for (int i = 0; i < KategorijeAkt.knjige.size(); i++) {
                for(int j=0; j<KategorijeAkt.knjige.get(i).getAutori().size(); j++) {
                    if (KategorijeAkt.knjige.get(i).getAutori().get(j).getImeiPrezime().toLowerCase().equalsIgnoreCase(odabraniAutor.toLowerCase())) {
                        odabrane.add(KategorijeAkt.knjige.get(i));
                    }
                }
            }

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
                        }
                    }

                }

            });



        return view;
    }
}
