package ba.unsa.etf.rma.lejla.projekatrma;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class DodavanjeKnjigeFragment extends Fragment {

    public Knjiga zaDodati=new Knjiga();
    private View v;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dodavanje_knjige_fragment, container, false);
        v=view;

        final Spinner sKategorije = (Spinner)view.findViewById(R.id.sKategorijaKnjige);
        BazaOpenHelper baza = new BazaOpenHelper(getContext(), BazaOpenHelper.DATABASE_NAME, null, BazaOpenHelper.DATABSE_VERSION);
        KategorijeAkt.kategorije = baza.ucitajKategorije();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, KategorijeAkt.kategorije);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKategorije.setAdapter(adapter);

        //Reference
        final Button dNadjiSliku = (Button)view.findViewById(R.id.dNadjiSliku);
        final Button dPonisti = (Button)view.findViewById(R.id.dPonisti);
        final Button dUpisiKnjigu=(Button)view.findViewById(R.id.dUpisiKnjigu);
        final Button dPretraga=(Button)view.findViewById(R.id.dPretraga);
        final EditText imeAutora = (EditText)view.findViewById(R.id.imeAutora);
        final EditText nazivKnjige = (EditText)view.findViewById(R.id.nazivKnjige);
        final ImageView naslovnaStr = (ImageView)view.findViewById(R.id.naslovnaStr);


        //Dugme ponisti

        dPonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment listaKategorije = new ListeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.mjesto, listaKategorije );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Odabir slike

        dNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent slika = new Intent();
                slika.setAction(Intent.ACTION_GET_CONTENT);
                slika.setType("image/*");
                if(slika.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(slika, 1);
                }

            }
        });



        // Dodavanje knjige

        dUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imeAutora.getText().toString().length()==0 || nazivKnjige.getText().toString().length()==0) {
                    Toast.makeText(getActivity(), "Polja ne smiju biti prazna", Toast.LENGTH_LONG).show();
                }
                else {


                    ArrayList<Autor> as = new ArrayList<Autor>();
                    as.add(new Autor(imeAutora.getText().toString(), new ArrayList<String>()));
                    zaDodati.setAutori(as);
                    zaDodati.setNaziv(nazivKnjige.getText().toString());
                    zaDodati.setKategorija(sKategorije.getSelectedItem().toString());
                    int i;
                    for(i=0; i<KategorijeAkt.autori.size(); i++) {
                        if(KategorijeAkt.autori.get(i).getImeiPrezime().toLowerCase().equalsIgnoreCase(imeAutora.getText().toString().toLowerCase())) {
                            KategorijeAkt.autori.get(i).dodajKnjigu(nazivKnjige.getText().toString().trim()+String.valueOf(i));
                            break;
                        }
                    }
                    if(i==KategorijeAkt.autori.size()) {
                        KategorijeAkt.autori.add(new Autor(imeAutora.getText().toString(), new ArrayList<String>()));
                        KategorijeAkt.autori.get(KategorijeAkt.autori.size()-1).dodajKnjigu(nazivKnjige.getText().toString().trim()+String.valueOf(i));
                    }

                    KategorijeAkt.knjige.add(zaDodati);
                    imeAutora.setText("");
                    nazivKnjige.setText("");
                    naslovnaStr.setImageResource(android.R.color.transparent);
                    Toast.makeText(getActivity(), R.string.toast_dodana_knjiga, Toast.LENGTH_SHORT).show();
                    zaDodati = new Knjiga();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final ImageView naslovnaStr = (ImageView)v.findViewById(R.id.naslovnaStr);
        final EditText nazivKnjige = (EditText)v.findViewById(R.id.nazivKnjige);

        if(resultCode==getActivity().RESULT_OK && requestCode==1)
        {


            try {
                Uri selectedimg = data.getData();
                naslovnaStr.setImageBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedimg));

                String ime = getFileName(selectedimg);
                zaDodati.setNaslovnaStrana(ime);
                zaDodati.setPath(saveToInternalStorage(getBitmapFromUri(data.getData()), ime));

            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @NonNull
    private String saveToInternalStorage(Bitmap bitmapImage, String ime){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        File mypath=new File(directory,ime);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);

            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 30, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }



    }

