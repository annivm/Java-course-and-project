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

```
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
```
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

If missing, the following are created automatically:
- `events.csv`
- `events.sqlite3`

⚠️ CSV file or database must **not be open** while running the application.

If no data exists, no events are shown until the user adds them.

---

## ▶️ Build & Run

Build the project:

```
mvn clean package
```
Run:
```
java -jar target/today.jar
```
🔧 Optional: CLI Alias

Example for Git Bash / Linux shell:
```
nano ~/.bashrc
```

Add:
```
alias today='java -cp "$PWD/target/today.jar" today.Today'
```

Reload:
```
source ~/.bashrc
```

Now you can run:
```
today
```

📌 Subcommands:

```
listproviders -> Shows a list of event providers
listevents -> Shows a list of events
addevent -> Adds a new event
```

Parameters and usage:

Shows a list of events:

```
today listevents [-c=<categoryOptionString>] [-d=<dateOptionString>]
```

```
 -c, --category=<categoryOptionString>
        Category or categories of events to list, separated by commas
 -d, --date=<dateOptionString>
        Date of events to list in the format MM-dd (default is today)
```



Adds a new event:

``` 
today addevent -c=<eventCategory> -d=<eventDate>
 -desc=<eventDescription> [-p=<eventProviderId>]
```

```
 -c, --category=<eventCategory>
        The category of the event
 -d, --date=<eventDate>
        The date of the event (yyyy-MM-dd)
-desc, --description=<eventDescription>
        The description of the event
 -p, --provider=<eventProviderId>
        The identifier of the event provider
```



⚠️ Known Issues & Solutions


Web search without secondary category

Caused by Category.equals() comparing both primary and secondary

-> Fixed to allow primary-only matching

-> Case-insensitive comparison added


SQLite provider category handling

Issue caused by mixing class and record versions

-> Resolved by aligning implementations


Adding events to specific providers

-> Implemented using EventManager to resolve providers dynamically

🚧 Limitations & Future Improvements
SQLite does not support descriptions with quotes (`' " ``)

Categories cannot be added via CLI

Only predefined categories are supported:
```
1 | test        | fake
2 | apple       | macos
3 | oracle      | java
4 | programming | rust
```

Filtering works only by primary category

Secondary-category-only filtering is not supported

📝 Notes

Default event provider when adding events is CSV.

---

