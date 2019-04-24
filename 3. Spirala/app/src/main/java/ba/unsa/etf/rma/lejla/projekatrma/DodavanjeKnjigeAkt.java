package ba.unsa.etf.rma.lejla.projekatrma;

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
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

public class DodavanjeKnjigeAkt extends AppCompatActivity {

    public Knjiga zaDodati=new Knjiga();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_knjige_akt);

        ArrayList<String> kategorije = getIntent().getStringArrayListExtra("kategorije");

        final Spinner sKategorije = (Spinner)findViewById(R.id.sKategorijaKnjige);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kategorije);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKategorije.setAdapter(adapter);

        //Reference
        final Button dNadjiSliku = (Button)findViewById(R.id.dNadjiSliku);
        final Button dPonisti = (Button)findViewById(R.id.dPonisti);
        final Button dUpisiKnjigu=(Button)findViewById(R.id.dUpisiKnjigu);
        final Button dPretraga=(Button)findViewById(R.id.dPretraga);
        final EditText imeAutora = (EditText)findViewById(R.id.imeAutora);
        final EditText nazivKnjige = (EditText)findViewById(R.id.nazivKnjige);
        final ImageView naslovnaStr = (ImageView)findViewById(R.id.naslovnaStr);


        nazivKnjige.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.length()==0) {
                    dNadjiSliku.setEnabled(false);

                }

                else {
                    dNadjiSliku.setEnabled(true);

                }

            }
        });

        //Dugme ponisti

        dPonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imeAutora.setText("");
                nazivKnjige.setText("");
                naslovnaStr.setImageResource(android.R.color.transparent);
                Intent povratak = new Intent(DodavanjeKnjigeAkt.this, KategorijeAkt.class);
                DodavanjeKnjigeAkt.this.startActivity(povratak);

            }
        });

        //Odabir slike

        dNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent slika = new Intent();
                slika.setAction(Intent.ACTION_GET_CONTENT);
                slika.setType("image/*");
                if(slika.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(slika, 1);
                }

            }
        });



        // Dodavanje knjige

        dUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imeAutora.getText().toString().length()==0 || nazivKnjige.getText().toString().length()==0) {
                    Toast.makeText(DodavanjeKnjigeAkt.this, "Polja ne smiju biti prazna", Toast.LENGTH_LONG).show();
                }
                else {


                    ArrayList<Autor> as = new ArrayList<Autor>();
                    as.add(new Autor(imeAutora.getText().toString(), null));
                    zaDodati.setAutori(as);
                    zaDodati.setNaziv(nazivKnjige.getText().toString());
                    zaDodati.setKategorija(sKategorije.getSelectedItem().toString());

                    KategorijeAkt.knjige.add(zaDodati);
                    imeAutora.setText("");
                    nazivKnjige.setText("");
                    naslovnaStr.setImageResource(android.R.color.transparent);
                    Toast.makeText(DodavanjeKnjigeAkt.this, "Knjiga uspješno dodana", Toast.LENGTH_SHORT).show();
                    zaDodati = new Knjiga();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final ImageView naslovnaStr = (ImageView)findViewById(R.id.naslovnaStr);
        final EditText nazivKnjige = (EditText)findViewById(R.id.nazivKnjige);

        if(resultCode==RESULT_OK && requestCode==1)
        {


            try {
                Uri selectedimg = data.getData();
                naslovnaStr.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));

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
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @NonNull
    private String saveToInternalStorage(Bitmap bitmapImage, String ime){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

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
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
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
