package ba.unsa.etf.rma.lejla.projekatrma;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

        TextView autor = (TextView)customView.findViewById(R.id.autor);
        TextView brojKnjiga = (TextView)customView.findViewById(R.id.brojKnjiga);

        BazaOpenHelper baza = new BazaOpenHelper(getContext(), BazaOpenHelper.DATABASE_NAME, null, BazaOpenHelper.DATABSE_VERSION);
        SQLiteDatabase db = baza.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Autor WHERE ime LIKE '"+getItem(position)+"'", null);
        cursor.moveToFirst();
        int IDAutora = cursor.getInt(cursor.getColumnIndex("_id"));
        cursor.close();
        cursor=db.rawQuery("SELECT * FROM Autorstvo WHERE idautora="+IDAutora, null);
        
        autor.setText(getItem(position));
        brojKnjiga.setText(String.valueOf(cursor.getCount()));
        cursor.close();
        
        return customView;
    }
}

