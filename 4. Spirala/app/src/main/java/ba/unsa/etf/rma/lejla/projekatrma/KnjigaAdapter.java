package ba.unsa.etf.rma.lejla.projekatrma;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;



public class KnjigaAdapter extends ArrayAdapter<Knjiga> {


    private Context con;
    String odabranaKategorija;



    public KnjigaAdapter(@NonNull Context context, ArrayList<Knjiga> knjige) {
        super(context, R.layout.element_liste_knjiga, knjige);
        con=context;

    }


    @NonNull
    @Override
   public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)  {
        LayoutInflater inf = LayoutInflater.from(getContext());
        View customView = inf.inflate(R.layout.element_liste_knjiga, parent, false);

        Knjiga k = getItem(position);


            TextView eAutor = (TextView) customView.findViewById(R.id.eAutor);
            TextView eNaziv = (TextView) customView.findViewById(R.id.eNaziv);
            TextView eOpis = (TextView)customView.findViewById(R.id.eOpis);
            TextView eDatum = (TextView)customView.findViewById(R.id.eDatumObjavljivanja);
            TextView eStranice = (TextView)customView.findViewById(R.id.eBrojStranica);

            ImageView eNaslovna = (ImageView)customView.findViewById(R.id.eNaslovna);



            eNaziv.setText(k.getNaziv());
            eOpis.setText(k.getOpis());
            eDatum.setText(k.getDatumObjavljivanja());
            eStranice.setText(String.valueOf(k.getBrojStrinica()));

            if(k.getPregledana()==1) {
                customView.setBackgroundColor(0xffaabbed);
            }

            for(int i=0; i<k.getAutori().size(); i++) {
                String pom = eAutor.getText().toString();
                if(i+1!=k.getAutori().size())
                pom= pom +  k.getAutori().get(i).getImeiPrezime() + ",";
                else {
                    pom= pom +  k.getAutori().get(i).getImeiPrezime();
                }
                eAutor.setText(pom);
            }




        if(k.getNaslovnaStrana()!=null && k.getPath()!=null) {
            try {
                File f = new File(k.getPath(), k.getNaslovnaStrana());
                Bitmap b = getResizedBitmap(BitmapFactory.decodeStream(new FileInputStream(f)), 200);

                eNaslovna.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(!(k.getSlikaString().isEmpty() || k.getSlikaString().equals("") || k.getSlikaString()==null)) {
            Picasso.get().load(k.getSlikaString()).into(eNaslovna);
        }
        else {

                //eNaslovna.setImageResource(R.drawable.etf);
            eNaslovna.setImageResource(android.R.color.transparent);
        }

        Button dPreporuci = (Button)customView.findViewById(R.id.dPreporuci);

            dPreporuci.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AppCompatActivity activity = (AppCompatActivity)view.getContext();
                    Fragment preporuci = new FragmentPreporuci();
                    activity.getFragmentManager().beginTransaction().replace(R.id.mjesto, preporuci).addToBackStack(null).commit();

                    KnjigeFragment.kliknuta = KnjigeFragment.odabrane.get(position);

                }
            });

        return customView;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }



}
