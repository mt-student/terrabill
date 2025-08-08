# TerraBill

**Smarte Auftragsverwaltung für Gartenprofis**

---

##  Vision

> „Mehr Zeit im Grünen, weniger Zeit im Papierkram.“  
> TerraBill ist die digitale Organisationszentrale für Kleinbetriebe im Garten- und Landschaftsbau – mobil, verständlich und effizient.

---

##  Features

- **Kundenverwaltung** – Stammdaten inklusive Adresse, Firmenname, Kontaktinformationen  
- **Anfragenverwaltung** – Neue Aufträge erfassen, nach Status sortieren (offen, in Bearbeitung, abgeschlossen)  
- **Jobplanung** – Aufträge Kunden zuweisen, Datum & Zeit festlegen, Kostenschätzung hinzufügen  
- **Offline-fähige Speicherung** – Nutzung ohne Internet über lokale SQLite-DB (Room)  
- **Demo-Daten** – Beispielkunden mit lustigen Gartenfantasienamen und realen Adressen in Köln & Bergisch Gladbach  
- **Leichte API-Integration** – NanoHTTPD ermöglicht einfache HTTP-Anfragen wie `/api/request`

---

##  Tech Stack

| Komponente         | Technologie / Beschreibung                                  |
|-------------------|-------------------------------------------------------------|
| Programmiersprache | Kotlin (moderne Sprache, Null-Sicherheit)                  |
| Persistence       | Room (lokale SQLite-Datenbank mit Type-Safety und Migration) |
| HTTP-Server       | NanoHTTPD (leichtgewichtiger Java-Server für APIs)         |
| Datenmodell       | Entities: `Customer`, `Request`, `Job`                      |
| Architektur       | MVP / Single Module, zukünftiger Ausbau Richtung MVVM möglich  |
| Demo-Inhalte      | Vorbefüllte Datensätze beim ersten Launch                   |

---

##  Setup & Installation

```bash
git clone https://github.com/mt-student/terrabill.git
cd terrabill
