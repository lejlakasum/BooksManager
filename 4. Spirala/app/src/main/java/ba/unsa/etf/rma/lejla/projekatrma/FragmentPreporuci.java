package ba.unsa.etf.rma.lejla.projekatrma;


import android.Manifest;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class FragmentPreporuci extends Fragment {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    ArrayList<String> imena = new ArrayList<>();
    ArrayList<String> emailAdrese = new ArrayList<>();

    Spinner kontakti2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_preporuci, container, false);


        TextView naziv = (TextView) view.findViewById(R.id.tNaslov);
        TextView autor = (TextView) view.findViewById(R.id.tAutor);
        TextView opis = (TextView) view.findViewById(R.id.tOpis);
        Button posalji = (Button) view.findViewById(R.id.dPosalji);
        Spinner kontakti = (Spinner)view.findViewById(R.id.sKontakti);
        kontakti2=kontakti;


        dohvatiKontakte();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, emailAdrese);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kontakti.setAdapter(adapter);

        naziv.setText(KnjigeFragment.kliknuta.getNaziv());
        opis.setText(KnjigeFragment.kliknuta.getOpis());
        if (KnjigeFragment.kliknuta.getAutori().size() > 0) {
            autor.setText(KnjigeFragment.kliknuta.getAutori().get(0).getImeiPrezime());
        }

        posalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posaljiEmail();
            }
        });


        return view;

    }



    public void posaljiEmail() {
        Knjiga k = KnjigeFragment.kliknuta;
        Log.i("Send email", "");
        String[] TO = {kontakti2.getSelectedItem().toString()};

        String imeOdabranog = imena.get(kontakti2.getSelectedItemPosition());


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Preporuka");
        if (k.getAutori().size() > 0) {
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Zdravo " + imeOdabranog + "\n" + "Pročitaj knjigu " + k.getNaziv() + " od " + k.getAutori().get(0).getImeiPrezime());
        } else {
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Zdravo " + "\n" + "Pročitaj knjigu " + k.getNaziv());
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            Log.i("E-mail je poslan", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There is no email client installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void dohvatiKontakte() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            ArrayList<String> emlRecs = new ArrayList<String>();
            HashSet<String> emlRecsHS = new HashSet<String>();
            Context context = getActivity();
            ContentResolver cr = context.getContentResolver();
            String[] PROJECTION = new String[] { ContactsContract.RawContacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_ID,
                    ContactsContract.CommonDataKinds.Email.DATA,
                    ContactsContract.CommonDataKinds.Photo.CONTACT_ID };
            String order = "CASE WHEN "
                    + ContactsContract.Contacts.DISPLAY_NAME
                    + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                    + ContactsContract.Contacts.DISPLAY_NAME
                    + ", "
                    + ContactsContract.CommonDataKinds.Email.DATA
                    + " COLLATE NOCASE";
            String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
            Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
            if (cur.moveToFirst()) {
                do {
                    // names comes in hand sometimes
                    String name = cur.getString(1);
                    String emlAddr = cur.getString(3);

                    // keep unique only
                    if (emlRecsHS.add(emlAddr.toLowerCase())) {
                        emailAdrese.add(emlAddr);
                        imena.add(name);
                        emlRecs.add(emlAddr);
                    }
                } while (cur.moveToNext());
            }

            cur.close();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                dohvatiKontakte();
            } else {
                Toast.makeText(getContext(), "Bez odobrenja nije moguce ucitati kontakte", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

