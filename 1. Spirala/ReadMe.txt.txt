Lejla Kasum
17280
2017/18
RMA


Spirala 1

Ura�eno:

1. Zadatak
	a) Kreiran je tra�eni Layout sa izgledom kao na slici, dodani su tra�eni Views i dodjeljeni su im odgovaraju�i IDs
	b) Ura�eno filtriranje na pritisak dugme Pretraga. Dugme Pretraga je onemogu�eno dok je ediText prazan, a omogu�ava se nakon uno�enja nekog znaka
	c) Dugme DodajKategoriju se omogu�ava tek nakon �to se ustanovi da unesena kategorija ne postoji, odnosno da nije identi�na nekoj koja ve� postoji
	d) Nakon dodavanja kategorije, ona se dodaje u listu kategorija, i onemogu�ava se dugme dodaj kategoriju, kako se jedna te ista ne bi mogla
	   dodavati vi�e puta
	e) Na po�etku su uba�ene ve� neke standardne kategorije
	f) Sve kategorije �uvaju se u listi kategorija u po�etnoj aktivnosti

2. Zadatak
	a) Kreirana nova aktivnost i Layout na koji su dodani tra�eni Viwes sa pripadaju�im IDs, a pokre�e se klikom na dugme Dodaj Knjigu unutar �ijeg
	   listener-a je kreiran intent za pozivanje nove aktivnosti iz main aktivnosti
	b) Spinner se popunjava iz liste kategorija proslje�ene putem metode putExtra
	c) Nije dozvoljeno dodati knjigu ukoliko su polja eAutor ili eNazivKnjige prazna
	d) Knjiga se upisuje u listu dodanih knjiga koja se �uva kao static ArrayList u main aktivnosti (KategorijeAkt)
	c) Dugme poni�ti �isti sva polja i vra�a se na prethodnu aktivnost

3. Zadatak
	a) Kreirana nova aktivnost koja se pokre�e u slu�aju klika na neki Item u ListView na po�etnoj aktivnosti. Ta�na kategorija na koju je izvr�en klik
	   proslje�uje se putem metode putExtra
	b) Kreiran CustomAdapter -> KnjigaAdapter, koji popunjava listu knjiga iz odabrane kategorije
	c) Za potrebe izgleda jednog reda kreiran Layout element_liste_knjiga, koji se sastoji od tra�enih Views
	d) Dodano dugme Povratak koje vra�a korisnika na glavnu - KategorijeAkt - aktivnost

4. Zadatak
	a) Klikom na dugme Na�i sliku otvara se galerija i korisnik ima mogu�nost odabrati sliku. To je postignuto pozivom implicitnog intenta
	   i metodom startActivityForResult.
	b) Ura�en je Override metode onActivityResult u kojoj se dohvata odabrana slika, popunjava se u imageView i sprema se u internu memoriju
	   za kasnije kori�tenje, tako da se uz svako knjigu �uva naziv slike (orginalni naziv slike iz galerije dobija se pomo�u metode getFileName)
	   i �uva se path
	c) U adapteru KnjigaAdapter se slika dohvata iz interne memorije i prikazuje uz odgovaraju�u knjigu (pomo�u atributa u svakoj knjizi koji govore
	   naziv slike i path)

5. Zadatak
	a) Implementirano je da se klikom na neki Item liste u aktivnosti ListaKnjigaAkt taj item oboji u zadanu boju

Nije ura�eno:

5. Zadatak
	a) Nakon izlaska iz aktivnosti i ponovnog vra�anja na istu nije implementirano da obojene Items ostanu obojene



