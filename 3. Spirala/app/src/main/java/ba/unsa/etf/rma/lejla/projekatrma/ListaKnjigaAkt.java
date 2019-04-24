package ba.unsa.etf.rma.lejla.projekatrma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaKnjigaAkt extends AppCompatActivity {



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);

        String odabranaKategorija = getIntent().getStringExtra("odabranaKategorija");

        ArrayList<Knjiga> odabrane = new ArrayList<>();
        for(int i=0; i<KategorijeAkt.knjige.size(); i++) {
            if(KategorijeAkt.knjige.get(i).getKategorija().equals(odabranaKategorija)) {
                odabrane.add(KategorijeAkt.knjige.get(i));
            }
        }

        final ListView listaKnjiga = (ListView)findViewById(R.id.listaKnjiga);

        ArrayAdapter<Knjiga> adapter = new KnjigaAdapter(this, odabrane);

        listaKnjiga.setAdapter(adapter);

        Button dPovratak = (Button)findViewById(R.id.dPovratak);

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

        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent povratak = new Intent(ListaKnjigaAkt.this, KategorijeAkt.class);

                ListaKnjigaAkt.this.startActivity(povratak);

            }
        });

    }
}
