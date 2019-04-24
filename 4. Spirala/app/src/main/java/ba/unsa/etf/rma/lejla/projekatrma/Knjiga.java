package ba.unsa.etf.rma.lejla.projekatrma;


import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Knjiga implements Parcelable {


    private String id="";
    private String naziv="";
    private ArrayList<Autor> autori;
    private String opis="";
    private String datumObjavljivanja="";
    private URL slika;
    private String slikaString="";
    private int brojStrinica=0;


    private int pregledana=0;

    String kategorija;
    String naslovnaStrana=null;
    String path=null;

    public int getPregledana() {
        return pregledana;
    }

    public void setPregledana(int pregledana) {
        this.pregledana = pregledana;
    }

    public String getSlikaString() {return slikaString;}

    public void setSlikaString(String slikaString)
    {
        this.slikaString=slikaString;
    if(!(slikaString=="" && slikaString.isEmpty()))
        try {
            this.slika=new URL(slikaString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getNaslovnaStrana() {
        return naslovnaStrana;
    }

    public void setNaslovnaStrana(String naslovnaStrana) {
        this.naslovnaStrana = naslovnaStrana;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public Knjiga() {
        this.autori=new ArrayList<Autor>();
    }

    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStrinica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStrinica = brojStrinica;
        if(slika!=null) {
            this.slikaString = slika.toString();
        }
    }

    public Knjiga(Parcel in) {
        id=in.readString();
        naziv=in.readString();
        in.readTypedList(getAutori(), Autor.CREATOR);
        opis=in.readString();
        datumObjavljivanja=in.readString();
        //fali slika URL
        slikaString=in.readString();
        brojStrinica=in.readInt();


        kategorija=in.readString();
        naslovnaStrana=in.readString();
        path=in.readString();
    }

    public static final Creator<Knjiga> CREATOR = new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel parcel) {
            return new Knjiga(parcel);
        }

        @Override
        public Knjiga[] newArray(int i) {
            return new Knjiga[i];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public URL getSlika() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStrinica() {
        return brojStrinica;
    }

    public void setBrojStrinica(int brojStrinica) {
        this.brojStrinica = brojStrinica;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(naziv);
        parcel.writeTypedList(autori);
        parcel.writeString(opis);
        parcel.writeString(datumObjavljivanja);
        //fali slika
        parcel.writeString(slikaString);
        parcel.writeInt(brojStrinica);

        parcel.writeString(kategorija);
        parcel.writeString(naslovnaStrana);
        parcel.writeString(path);

    }
}
