# Today – Event Manager (CLI)

Java 17 -pohjainen komentorivisovellus tapahtumien hakemiseen, suodattamiseen ja lisäämiseen
useista eri lähteistä (CSV, SQLite, Web).

📦 Course project – *Ohjelmoinnin syventävät tekniikat*

🔗 Source code:
https://github.com/annivm/5G00EU62-3005-Ohjelmoinnin-syventavat-tekniikat/tree/main/project

---

## ✨ Features

- List events by date
- Filter events by category
- Add new events
- Use multiple event providers:
  - CSV
  - SQLite
  - Web API

---

## 🗂 Project Structure

.
├── Today.java # CLI entry point
├── EventManager.java # Core logic
├── EventFactory.java
│
├── commands # CLI commands
│ ├── AddEvent.java
│ ├── ListEvents.java
│ └── ListProviders.java
│
├── datamodel # Event & category models
├── filters # Event filtering logic
├── providers # Data sources
│ ├── CSVEventProvider.java
│ ├── SQLiteEventProvider.java
│ └── web
│ └── WebEventProvider.java

yaml
Kopioi koodi

---

## 🛠 Requirements

- **Java 17**
- **Maven**
- Libraries:
  - OpenCSV
  - Picocli
  - Jackson
  - SQLite JDBC Driver

---

## 💾 Local Storage

The application uses a directory in the user’s home folder:

~/.today

yaml
Kopioi koodi

If missing, the following are created automatically:
- `events.csv`
- `events.sqlite3`

⚠️ CSV file or database must **not be open** while running the application.

If no data exists, no events are shown until the user adds them.

---

## ▶️ Build & Run

Build the project:

```bash
mvn clean package
Run:

bash
Kopioi koodi
java -jar target/today.jar
🔧 Optional: CLI Alias
Example for Git Bash / Linux shell:

bash
Kopioi koodi
nano ~/.bashrc
Add:

bash
Kopioi koodi
alias today='java -cp "$PWD/target/today.jar" today.Today'
Reload:

bash
Kopioi koodi
source ~/.bashrc
Now you can run:

bash
Kopioi koodi
today
📌 Commands
List providers
bash
Kopioi koodi
today listproviders
List events
bash
Kopioi koodi
today listevents [-c=<categories>] [-d=<MM-dd>]
Options:

-c, --category
One or more categories separated by commas
Example: apple/macos,programming/java

-d, --date
Date in format MM-dd (default: today)

Add event
bash
Kopioi koodi
today addevent -c=<category> -d=<yyyy-MM-dd> -desc=<description> [-p=<provider>]
⚠️ Known Issues & Solutions
Web search without secondary category
Caused by Category.equals() comparing both primary and secondary

Fixed to allow primary-only matching

Case-insensitive comparison added

SQLite provider category handling
Issue caused by mixing class and record versions

Resolved by aligning implementations

Adding events to specific providers
Implemented using EventManager to resolve providers dynamically

🚧 Limitations & Future Improvements
SQLite does not support descriptions with quotes (`' " ``)

Categories cannot be added via CLI

Only predefined categories are supported:

bash
Kopioi koodi
1 | test        | fake
2 | apple       | macos
3 | oracle      | java
4 | programming | rust
Filtering works only by primary category

Secondary-category-only filtering is not supported

📝 Notes
Default event provider when adding events is CSV.

markdown
Kopioi koodi

---

### Miksi tämä näyttää paremmalta GitHubissa
- Lyhyet kappaleet
- Emojit ohjaavat silmää (GitHubissa ok)
- “Scanattava” rakenne → arvioija löytää heti olennaisen
- Näyttää aidolta open source / kurssiprojektilta

Jos haluat, voin vielä:
- tehdä **vielä minimalistisemman** version arvioijaa varten
- poistaa emojit (jos kurssi on konservatiivinen)
- tehdä **englanninkielisen version CV:tä varten**
::contentReference[oaicite:0]{index=0}
