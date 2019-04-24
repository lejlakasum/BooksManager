package ba.unsa.etf.rma.lejla.projekatrma;


public class Knjiga {


    String imeAutora;
    String nazivKnjige;
    String kategorija;
    String naslovnaStrana=null;
    String path=null;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public Knjiga(String imeAutora, String nazivKnjige, String kategorija, String naslovnaStrana) {
        this.imeAutora = imeAutora;
        this.nazivKnjige = nazivKnjige;
        this.kategorija = kategorija;
        this.naslovnaStrana = naslovnaStrana;
    }

    public Knjiga() {}

    public String getImeAutora() {
        return imeAutora;
    }

    public void setImeAutora(String imeAutora) {
        this.imeAutora = imeAutora;
    }

    public String getNazivKnjige() {
        return nazivKnjige;
    }

    public void setNazivKnjige(String nazivKnjige) {
        this.nazivKnjige = nazivKnjige;
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




}
