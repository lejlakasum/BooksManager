package ba.unsa.etf.rma.lejla.projekatrma;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
   public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)  {
        LayoutInflater inf = LayoutInflater.from(getContext());
        View customView = inf.inflate(R.layout.element_liste_knjiga, parent, false);

        Knjiga k = getItem(position);


            TextView eAutor = (TextView) customView.findViewById(R.id.eAutor);
            TextView eNaziv = (TextView) customView.findViewById(R.id.eNaziv);
            ImageView eNaslovna = (ImageView)customView.findViewById(R.id.eNaslovna);
            eNaziv.setText(k.getNazivKnjige());
            eAutor.setText(k.getImeAutora());

        if(k.getNaslovnaStrana()!=null && k.getPath()!=null) {
            try {
                File f = new File(k.getPath(), k.getNaslovnaStrana());
                Bitmap b = getResizedBitmap(BitmapFactory.decodeStream(new FileInputStream(f)), 200);

                eNaslovna.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            eNaslovna.setImageResource(android.R.color.transparent);
        }

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
