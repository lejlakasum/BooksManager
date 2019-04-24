Lejla Kasum
17280
2017/18
RMA


Spirala 1

Uraðeno:

1. Zadatak
	a) Kreiran je traženi Layout sa izgledom kao na slici, dodani su traženi Views i dodjeljeni su im odgovarajuæi IDs
	b) Uraðeno filtriranje na pritisak dugme Pretraga. Dugme Pretraga je onemoguæeno dok je ediText prazan, a omoguæava se nakon unošenja nekog znaka
	c) Dugme DodajKategoriju se omoguæava tek nakon što se ustanovi da unesena kategorija ne postoji, odnosno da nije identièna nekoj koja veæ postoji
	d) Nakon dodavanja kategorije, ona se dodaje u listu kategorija, i onemoguæava se dugme dodaj kategoriju, kako se jedna te ista ne bi mogla
	   dodavati više puta
	e) Na poèetku su ubaèene veæ neke standardne kategorije
	f) Sve kategorije èuvaju se u listi kategorija u poèetnoj aktivnosti

2. Zadatak
	a) Kreirana nova aktivnost i Layout na koji su dodani traženi Viwes sa pripadajuæim IDs, a pokreæe se klikom na dugme Dodaj Knjigu unutar èijeg
	   listener-a je kreiran intent za pozivanje nove aktivnosti iz main aktivnosti
	b) Spinner se popunjava iz liste kategorija prosljeðene putem metode putExtra
	c) Nije dozvoljeno dodati knjigu ukoliko su polja eAutor ili eNazivKnjige prazna
	d) Knjiga se upisuje u listu dodanih knjiga koja se èuva kao static ArrayList u main aktivnosti (KategorijeAkt)
	c) Dugme poništi èisti sva polja i vraæa se na prethodnu aktivnost

3. Zadatak
	a) Kreirana nova aktivnost koja se pokreæe u sluèaju klika na neki Item u ListView na poèetnoj aktivnosti. Taèna kategorija na koju je izvršen klik
	   prosljeðuje se putem metode putExtra
	b) Kreiran CustomAdapter -> KnjigaAdapter, koji popunjava listu knjiga iz odabrane kategorije
	c) Za potrebe izgleda jednog reda kreiran Layout element_liste_knjiga, koji se sastoji od traženih Views
	d) Dodano dugme Povratak koje vraæa korisnika na glavnu - KategorijeAkt - aktivnost

4. Zadatak
	a) Klikom na dugme Naði sliku otvara se galerija i korisnik ima moguænost odabrati sliku. To je postignuto pozivom implicitnog intenta
	   i metodom startActivityForResult.
	b) Uraðen je Override metode onActivityResult u kojoj se dohvata odabrana slika, popunjava se u imageView i sprema se u internu memoriju
	   za kasnije korištenje, tako da se uz svako knjigu èuva naziv slike (orginalni naziv slike iz galerije dobija se pomoæu metode getFileName)
	   i èuva se path
	c) U adapteru KnjigaAdapter se slika dohvata iz interne memorije i prikazuje uz odgovarajuæu knjigu (pomoæu atributa u svakoj knjizi koji govore
	   naziv slike i path)

5. Zadatak
	a) Implementirano je da se klikom na neki Item liste u aktivnosti ListaKnjigaAkt taj item oboji u zadanu boju

Nije uraðeno:

5. Zadatak
	a) Nakon izlaska iz aktivnosti i ponovnog vraæanja na istu nije implementirano da obojene Items ostanu obojene



