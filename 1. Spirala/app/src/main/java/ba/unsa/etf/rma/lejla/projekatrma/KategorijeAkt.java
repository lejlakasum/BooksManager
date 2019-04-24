package ba.unsa.etf.rma.lejla.projekatrma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity {


    static ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();

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
        final ArrayList<String> kategorije = new ArrayList<String>();
        kategorije.add("Drama");
        kategorije.add("Krimi");
        kategorije.add("Triler");
        kategorije.add("Romansa");
        kategorije.add("Pustolovina");




        //Dohavatnje referenci
        final EditText tekstPretraga = (EditText)findViewById(R.id.tekstPretraga);
        final Button dPretraga = (Button)findViewById(R.id.dPretraga);
        final Button dDodajKategoriju = (Button)findViewById(R.id.dDodajKategoriju);
        Button dDodajKnjigu = (Button) findViewById(R.id.dDodajKnjigu);
        final ListView listaKategorija = (ListView)findViewById(R.id.listaKategorija);

        //Adapter za listu kategorija koji se prilikom otvaranje app odmah prikazuje
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(KategorijeAkt.this, android.R.layout.simple_list_item_1, kategorije);
        listaKategorija.setAdapter(adapter);

        //Onemogucava dugme za pretragu ako je editText prazan,
        //i onemogucava dugme dodajkategoriju dok se ne pritisne dugme pretrage
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

        //Izvrsava trazeno nakon klika na dugme pretraga
        dPretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String unesena = tekstPretraga.getText().toString();

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
                                kategorije.add(unesena);
                                adapter.add(unesena);
                                dDodajKategoriju.setEnabled(false);
                                adapter.getFilter().filter(unesena);
                                Toast.makeText(KategorijeAkt.this, "Kategorija uspješno dodana", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                listaKategorija.setAdapter(adapter);
            }
        });

        //Izvrsava trazeno nakon klika na dugme

        dDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent prebaci = new Intent(KategorijeAkt.this, DodavanjeKnjigeAkt.class);
                prebaci.putExtra("kategorije", kategorije);
                KategorijeAkt.this.startActivity(prebaci);

            }
        });

        listaKategorija.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String odabrana = listaKategorija.getItemAtPosition(i).toString();

                Intent prebaci2 = new Intent(KategorijeAkt.this, ListaKnjigaAkt.class);
                prebaci2.putExtra("odabranaKategorija", odabrana);
                KategorijeAkt.this.startActivity(prebaci2);
            }
        });

    }
}
