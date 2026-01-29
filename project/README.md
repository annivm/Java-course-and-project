Today – Tapahtumien hallinta komentoriviltä

Tämä projekti on Java-pohjainen komentorivisovellus, jolla voidaan hakea, suodattaa ja lisätä tapahtumia eri lähteistä. Sovellus tukee useita tapahtumantuottajia (CSV, SQLite ja Web) sekä monipuolisia suodattimia päivämäärän ja kategorian perusteella.

Projektin lähdekoodi:
https://github.com/annivm/5G00EU62-3005-Ohjelmoinnin-syventavat-tekniikat/tree/main/project

Toiminnallisuus

Sovellus mahdollistaa:

Tapahtumien listaamisen

Tapahtumien suodattamisen päivämäärän ja kategorian mukaan

Uusien tapahtumien lisäämisen

Käytössä olevien tapahtumantuottajien listaamisen

Tapahtumia haetaan kolmesta eri lähteestä:

CSV-tiedosto

SQLite-tietokanta

Verkkopalvelin

Projektin rakenne
│ EventFactory.java
│ EventManager.java
│ Today.java
│
├── commands
│   ├── AddEvent.java
│   ├── ListEvents.java
│   └── ListProviders.java
│
├── datamodel
│   ├── AnnualEvent.java
│   ├── AnnualEventComparator.java
│   ├── Category.java
│   ├── Event.java
│   ├── SingularEvent.java
│   └── SingularEventComparator.java
│
├── filters
│   ├── CategoryFilter.java
│   ├── DateCategoryFilter.java
│   ├── DateFilter.java
│   ├── EventFilter.java
│   └── IdentityFilter.java
│
├── providers
│   ├── CSVEventProvider.java
│   ├── EventProvider.java
│   ├── SQLiteEventProvider.java
│   └── web
│       ├── EventDeserializer.java
│       └── WebEventProvider.java

Kansioiden vastuut

Pääohjelma

Määrittelee komentorivikäyttöliittymän

Rekisteröi tapahtumantuottajat

providers

Tapahtumien lukeminen ja kirjoittaminen eri lähteistä

filters

Tapahtumien suodatuslogiikka

datamodel

Tapahtumiin ja kategorioihin liittyvät tietomallit

commands

Komentorivikäyttöliittymän komennot

Vaatimukset

Java 17

Maven

Käytetyt kirjastot:

OpenCSV

Picocli

Jackson

SQLite JDBC Driver

Tiedostot ja tallennus

Sovellus luo käyttäjän kotihakemistoon kansion:

~/.today


Kansioon luodaan tarvittaessa:

events.csv

events.sqlite3

Jos näitä ei ole ennestään, ne luodaan automaattisesti.
Tietokantaan luodaan taulut ja alustetaan kategoriat.

⚠️ CSV-tiedosto tai tietokanta ei saa olla auki, kun ohjelmaa ajetaan.

Kääntäminen ja ajaminen

Siirry projektin juurikansioon ja suorita:

mvn clean package


Aja ohjelma:

java -jar target/today.jar

Alias (valinnainen)

Esimerkiksi Git Bashissa:

nano ~/.bashrc


Lisää rivi:

alias today='java -cp "$PWD/target/today.jar" today.Today'


Lataa asetukset:

source ~/.bashrc


Tämän jälkeen ohjelma toimii komennolla:

today

Komennot
Tapahtumantuottajien listaus
today listproviders

Tapahtumien listaus
today listevents [-c=<kategoriat>] [-d=<päivämäärä>]


Parametrit:

-c, --category
Yksi tai useampi kategoria, pilkulla erotettuna
(esim. apple/macos,programming/java)

-d, --date
Päivämäärä muodossa MM-dd
Oletus: tämän päivän päivämäärä

Tapahtuman lisääminen
today addevent -c=<kategoria> -d=<yyyy-MM-dd> -desc=<kuvaus> [-p=<provider>]


Parametrit:

-c, --category tapahtuman kategoria

-d, --date päivämäärä muodossa yyyy-MM-dd

-desc, --description tapahtuman kuvaus

-p, --provider tapahtumantuottaja (oletus: csv)

Tunnetut ongelmat ja ratkaisut
1. Web-haut ilman secondary-kategoriaa

Ongelma johtui Category.equals()-vertailusta, joka vaati sekä primääri- että sekundäärikategorian.
Ratkaisu: muutettiin vertailu tukemaan pelkkää primäärikategoriaa ja lisättiin ignoreCase.

2. SQLiteProvider ja record-muutos

getCategoryId ei tunnistanut class → record -muutosta.
Ongelma johtui eri versioiden lähdekoodien sekoittumisesta.

3. Tapahtuman lisääminen eri tuottajille

Ratkaistiin hyödyntämällä EventManager-luokkaa, jonka avulla oikea provider voidaan hakea ja käsitellä erikseen.

Rajoitukset ja jatkokehitys

SQLite ei tue tällä hetkellä kuvauksia, joissa on heittomerkkejä tai lainausmerkkejä

Uusia kategorioita ei voi lisätä ohjelman kautta

Tapahtumia voi lisätä vain seuraaviin kategorioihin:

1 | test        | fake
2 | apple       | macos
3 | oracle      | java
4 | programming | rust


Haku onnistuu vain primäärikategorian perusteella

Sekundäärikategorian perusteella ei voi hakea