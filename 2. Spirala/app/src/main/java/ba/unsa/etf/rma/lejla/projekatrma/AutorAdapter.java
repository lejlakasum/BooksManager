package ba.unsa.etf.rma.lejla.projekatrma;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class AutorAdapter extends ArrayAdapter<String> {

    public AutorAdapter(@NonNull Context context, ArrayList<String> autori) {
        super(context, R.layout.lista_autora, autori);


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inf = LayoutInflater.from(getContext());
        View customView = inf.inflate(R.layout.lista_autora, parent, false);

        String a = getItem(position);
        
        int brKnjiga = 0;
        for(int i=0; i<KategorijeAkt.knjige.size(); i++) {
            if(KategorijeAkt.knjige.get(i).getImeAutora().toLowerCase().equalsIgnoreCase(a.toLowerCase())) {
                brKnjiga++;
            }
        }

        TextView autor = (TextView)customView.findViewById(R.id.autor);
        TextView brojKnjiga = (TextView)customView.findViewById(R.id.brojKnjiga);
        
        autor.setText(a);
        brojKnjiga.setText(String.valueOf(brKnjiga));
        
        return customView;
    }
}

