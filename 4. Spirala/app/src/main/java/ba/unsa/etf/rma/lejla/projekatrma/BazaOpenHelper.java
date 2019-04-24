package ba.unsa.etf.rma.lejla.projekatrma;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by Lejla on 30.05.2018..
 */

public class BazaOpenHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "mojabaza.db";
    public static final int DATABSE_VERSION = 1;
    public static final String KATEGORIJA_TABLE = "Kategorija";
    public static final String AUTOR_TABLE = "Autor";
    public static final String AUTORSTVO_TABLE = "Autorstvo";
    public static final String KNJIGA_TABLE = "Knjiga";
    Context con;

    public BazaOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        con=context;
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table Kategorija (_id integer primary key autoincrement, naziv text not null);");
        sqLiteDatabase.execSQL("create table Knjiga (_id integer primary key autoincrement, naziv text not null, opis text," +
                " datumObjavljivanja text, brojStranica integer, idWebServis text, idkategorije integer, slika text, pregledana integer);");
        sqLiteDatabase.execSQL("create table Autor (_id integer primary key autoincrement, ime text not null);");
        sqLiteDatabase.execSQL("create table Autorstvo (_id integer primary key autoincrement, idautora integer not null, idknjige integer not null);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Kategorija");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Knjiga");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Autor");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Autorstvo");

        onCreate(sqLiteDatabase);

    }

    public long dodajKategoriju(String kategorija) {

        try {
            ContentValues novi = new ContentValues();
            novi.put("naziv",kategorija);
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(BazaOpenHelper.KATEGORIJA_TABLE, null, novi);
            long posljednjiID;
            String selectQuery = "SELECT  * FROM " + KATEGORIJA_TABLE;
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToLast();
            posljednjiID = Long.parseLong(cursor.getString(cursor.getColumnIndex("_id")));
            cursor.close();
            Toast.makeText(con, "Kategorija uspješno dodana", Toast.LENGTH_LONG).show();
            return posljednjiID;

        }
        catch (Exception e) {
            Toast.makeText(con, "Greška dodajKategoriju: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    public ArrayList<String> ucitajKategorije() {
        try {
            ArrayList<String> kategorije = new ArrayList<>();
            String zahtjev = "SELECT * FROM " + BazaOpenHelper.KATEGORIJA_TABLE;
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(zahtjev, null);

            if (cursor.moveToFirst()) {
                do {
                    kategorije.add(cursor.getString(cursor.getColumnIndex("naziv")));
                } while (cursor.moveToNext());
            }
            cursor.close();

            return kategorije;
        }
        catch (Exception e) {
            Log.e("UCITAVANJE KATEGORIJE", e.getMessage());
            return null;
        }

    }

    public void obrisiSveKategorije() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(KATEGORIJA_TABLE, null, null);
    }

    public long dodajKnjigu(Knjiga knjiga) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor k = db.rawQuery("SELECT * FROM Knjiga WHERE idWebServis LIKE '"+knjiga.getId()+"'", null);
            if(k.getCount()!=0) {
                k.close();
                throw new Exception("Knjiga već postoji");

            }
            k.close();
            ArrayList<Autor> autori = knjiga.getAutori();
            ArrayList<Long> autorsID = new ArrayList<>();

            String[] koloneRezulat = new String[]{ "_id", "ime"};
            for(int i=0; i<autori.size(); i++) {
                Cursor cursor = db.rawQuery("SELECT * FROM Autor WHERE ime LIKE '"+autori.get(i).getImeiPrezime()+"'", null);
                cursor.moveToLast();
                if(cursor.getCount()!=0) {

                    autorsID.add((long) cursor.getInt(cursor.getColumnIndex("_id")));

                }
                else {
                    autorsID.add(this.dodajAutora(autori.get(i)));
                }
                cursor.close();
            }

            Cursor cursor = db.rawQuery("SELECT * FROM Kategorija WHERE naziv LIKE '"+knjiga.getKategorija()+"'", null);
            cursor.moveToLast();

            ContentValues novi = new ContentValues();
            novi.put("naziv",knjiga.getNaziv());
            novi.put("opis", knjiga.getOpis());
            novi.put("datumObjavljivanja", knjiga.getDatumObjavljivanja());
            novi.put("brojStranica", knjiga.getBrojStrinica());
            novi.put("idWebServis", knjiga.getId());
            novi.put("idkategorije", cursor.getInt(cursor.getColumnIndex("_id")));
            novi.put("slika", knjiga.getSlikaString());
            novi.put("pregledana", 0);

            db.insert(BazaOpenHelper.KNJIGA_TABLE, null, novi);
            cursor.close();

            long posljednjiID;
            String selectQuery = "SELECT  * FROM " + KNJIGA_TABLE;
            cursor = db.rawQuery(selectQuery, null);

            cursor.moveToLast();
            posljednjiID = Long.parseLong(cursor.getString(cursor.getColumnIndex("_id")));

            this.dodajAutostvo(autorsID, posljednjiID);

            cursor.close();
            Toast.makeText(con, "Knjiga je uspješno dodana", Toast.LENGTH_LONG).show();
            return posljednjiID;

        }
        catch (Exception e) {
            Toast.makeText(con, "Greška dodajKnjigu: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    public long dodajAutora(Autor a) {

        try {
            ContentValues novi = new ContentValues();
            novi.put("ime",a.getImeiPrezime());
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(BazaOpenHelper.AUTOR_TABLE, null, novi);
            long posljednjiID;
            String selectQuery = "SELECT  * FROM " + AUTOR_TABLE;
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToLast();
            posljednjiID = Long.parseLong(cursor.getString(cursor.getColumnIndex("_id")));
            cursor.close();
            return posljednjiID;

        }
        catch (Exception e) {
            Toast.makeText(con, "Greška dodajAutora: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }

    }

    public void dodajAutostvo(ArrayList<Long> autori, long IDKnjige) {

        try {
            for (int i = 0; i < autori.size(); i++) {
                ContentValues novi = new ContentValues();
                novi.put("idautora", autori.get(i));
                novi.put("idknjige", IDKnjige);
                SQLiteDatabase db = this.getWritableDatabase();
                db.insert(AUTORSTVO_TABLE, null, novi);
            }
        }
        catch (Exception e) {
            Toast.makeText(con, "Greška dodajAutorstvo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public ArrayList<Knjiga> knjigeKategorije(long idKategorije) throws MalformedURLException {
        ArrayList<Knjiga> knjige = new ArrayList<>();
        try {


            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM Knjiga WHERE idkategorije=" + idKategorije, null);

                if (cursor.moveToFirst()) {
                    do {
                        Knjiga nova = new Knjiga();
                        nova.setNaziv(cursor.getString(cursor.getColumnIndex("naziv")));
                        nova.setOpis(cursor.getString(cursor.getColumnIndex("opis")));
                        nova.setDatumObjavljivanja(cursor.getString(cursor.getColumnIndex("datumObjavljivanja")));
                        nova.setBrojStrinica(cursor.getInt(cursor.getColumnIndex("brojStranica")));
                        nova.setId(cursor.getString(cursor.getColumnIndex("idWebServis")));
                        nova.setKategorija(ListeFragment.odabrano);
                        nova.setSlikaString(cursor.getString(cursor.getColumnIndex("slika")));
                        nova.setAutori(this.autoriKnjige(cursor.getLong(cursor.getColumnIndex("_id"))));
                        nova.setPregledana(cursor.getInt(cursor.getColumnIndex("pregledana")));
                        knjige.add(nova);

                    } while (cursor.moveToNext());
                }
                cursor.close();

        }
        catch (Exception e) {
            Toast.makeText(con, "greska knjigeKategorije: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
            return knjige;
    }

    public ArrayList<Autor> autoriKnjige(long idKnjige) {

        ArrayList<Autor> autori = new ArrayList<>();
        ArrayList<Long> IDautora = new ArrayList<>();
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM Autorstvo WHERE idknjige=" + idKnjige, null);
            if (cursor.moveToFirst()) {
                do {
                    IDautora.add((long) cursor.getInt(cursor.getColumnIndex("idautora")));
                } while (cursor.moveToNext());
            }
            cursor.close();

            for(int i=0; i<IDautora.size(); i++) {
                cursor = db.rawQuery("SELECT * FROM Autor WHERE _id="+IDautora.get(i), null);
                cursor.moveToFirst();
                Autor novi = new Autor();
                novi.setKnjige(null);
                novi.setImeiPrezime(cursor.getString(cursor.getColumnIndex("ime")));
                autori.add(novi);
                cursor.close();
            }
        }
        catch (Exception e) {
            Toast.makeText(con, "greska autoriKnjige: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return autori;
    }

    public ArrayList<String> ucitajAutore() {
        ArrayList<String> autori = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Autor", null);
        if(cursor.moveToFirst()) {
            do {
                autori.add(cursor.getString(cursor.getColumnIndex("ime")));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return autori;
    }

    public ArrayList<Knjiga> knjigeAutora(long idAutora) {

        ArrayList<Knjiga> knjige = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorAutor = db.rawQuery("SELECT * FROM Autorstvo WHERE idautora="+idAutora, null);

        if(cursorAutor.moveToFirst()) {
            do {
                int idKnjige = cursorAutor.getInt(cursorAutor.getColumnIndex("idknjige"));
                Cursor cursor = db.rawQuery("SELECT * FROM Knjiga WHERE _id="+idKnjige, null);
                if (cursor.moveToFirst()) {
                    do {
                        Knjiga nova = new Knjiga();
                        nova.setNaziv(cursor.getString(cursor.getColumnIndex("naziv")));
                        nova.setOpis(cursor.getString(cursor.getColumnIndex("opis")));
                        nova.setDatumObjavljivanja(cursor.getString(cursor.getColumnIndex("datumObjavljivanja")));
                        nova.setBrojStrinica(cursor.getInt(cursor.getColumnIndex("brojStranica")));
                        nova.setId(cursor.getString(cursor.getColumnIndex("idWebServis")));
                        //nova.setKategorija(ListeFragment.odabrano);
                        nova.setSlikaString(cursor.getString(cursor.getColumnIndex("slika")));
                        nova.setAutori(this.autoriKnjige(cursor.getLong(cursor.getColumnIndex("_id"))));
                        nova.setPregledana(cursor.getInt(cursor.getColumnIndex("pregledana")));
                        knjige.add(nova);

                    } while (cursor.moveToNext());
                }
                cursor.close();

            } while(cursorAutor.moveToNext());
            cursorAutor.close();
        }


        return knjige;

    }
}































