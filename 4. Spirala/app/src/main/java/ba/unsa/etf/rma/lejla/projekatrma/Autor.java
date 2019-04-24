package ba.unsa.etf.rma.lejla.projekatrma;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Autor implements Parcelable{

    private String imeiPrezime;
    private ArrayList<String> knjige = new ArrayList<String>();

    public Autor() {
    }

    public Autor(String imeiPrezime, ArrayList<String> knjige) {
        this.imeiPrezime = imeiPrezime;
        this.knjige = knjige;
    }

    protected Autor(Parcel in) {
        imeiPrezime=in.readString();
        knjige=in.createStringArrayList();
    }

    public static final Creator<Autor> CREATOR = new Creator<Autor>() {
        @Override
        public Autor createFromParcel(Parcel parcel) {
            return new Autor(parcel);
        }

        @Override
        public Autor[] newArray(int i) {
            return new Autor[i];
        }
    };

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    public void dodajKnjigu(String id) {
        int i;
        for (i=0; i<knjige.size(); i++) {
            if(knjige.get(i)==id) break;
        }
        if(i==knjige.size()) {
            knjige.add(id);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imeiPrezime);
        parcel.writeStringList(knjige);
    }
}
