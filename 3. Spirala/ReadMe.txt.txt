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


Spirala 2

Ura�eno:

1. Zadatak

Implementiran tra�eni ListaFragment i dodani buttoni sa tra�enim specifikacijama.
Prilikom klika na button Dodaj knjigu, otvara se novi fragment gdje je omogu�en unos novih knjiga kao i u prvoj spricali
Za potrebe liste autora kreiran je novi adapter i novi layout koji ima dva textView-a, jedan za naziv autora a drugi za broj napisanih knjiga
Omogu�eno je da se ne izlistavaju duplikati tako �to se stringovi konvertuju u lowerCase i porede se imena autora bez razmaka.
Implementirano je sakrivanje tra�enih buttona

2. Zadatak

Kreiran novi fragment, sa istim funkcionalnostima kao i aktivnost u prethodnoj sprirali.
Knjige se izlistavaju u zavisnosti od tog da li je odabran autor ili kategorija, a to se cuva kao atribut fragmenta koji upravlja listama.
Dugme dPovratak otvara ponovo pocetni fragment

3. Zadatak
Implementiran masterDetail Flow, kada se promijeni orijentacija ure�aja u landscape (odnosno za ekrane �irine >450) fragemnt ListeFragment
se nalazi na lijevoj strani ekrana a KnjigeFragment na desnoj, odnosno klikom na neku kategoriju ili autora, knjige se izlistavaju u desnom dijelu ekrana

4. Zadatak

Svi stringovi su registrovani u values/strings, i prevedeni na engleski jezik
Kori�tene boje su za boju teksta na buttons i boje buttona upi�i knjigu i poni�ti u fragmentu DodavanjeKnjige, te je promjenjena boja zaglavlja
Sve boje su registrovane u res/values/colors

Nije ura�eno:

3. Zadatak

Kada se pritisne dugme Dodaj Knjigu u landscape modu, fragemtn za Dodavanje Knjige se ne prikazuje na cijelom ekranu nego samo na jednoj polovini
(u jednom frame-u)

