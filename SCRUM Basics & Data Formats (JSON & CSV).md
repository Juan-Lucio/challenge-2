# 🎤 Presentation: SCRUM Basics & Data Formats (JSON & CSV)

---

## 📌 Slide 1 – Title
**Project Context**  
- Automation of JSON → CSV for University Scientometrics Department  
- Developed under **SCRUM methodology**  
- Focus: Reduce errors & accelerate report generation  

---

## 📌 Slide 2 – What is SCRUM?
- An **Agile framework** for managing complex projects  
- Based on **iteration (sprints)** and **incremental value delivery**  
- Empowers **collaboration, adaptability, and transparency**  

---

## 📌 Slide 3 – SCRUM Roles
- 🧑‍💼 **Product Owner** → Defines vision, prioritizes backlog  
- 🧑‍🤝‍🧑 **Scrum Team** → Developers delivering increments (Beatriz in this project)  
- 👩‍⚖️ **Scrum Master** → Facilitates process, removes blockers  

---

## 📌 Slide 4 – SCRUM Events
- 📅 **Sprint Planning** → Define goals & backlog items  
- 🔄 **Daily Scrum** → 15-min standup for progress & blockers  
- 📦 **Sprint Review** → Demonstrate increment to stakeholders  
- 🪞 **Sprint Retrospective** → Identify improvements  

---

## 📌 Slide 5 – SCRUM Artifacts
- 📋 **Product Backlog** → Ordered list of all requirements  
- 📌 **Sprint Backlog** → Items selected for current sprint  
- ✅ **Increment** → Working deliverable (e.g., working code, report, demo)  

---

## 📌 Slide 6 – JSON: Overview
- **JavaScript Object Notation** (lightweight data-interchange format)  
- Structured as **key–value pairs**  
- Easy for humans to read, easy for machines to parse  
- Widely used in **APIs, data exchange, and storage**  

---

## 📌 Slide 7 – JSON: Technical Characteristics
- **Text-based** (UTF-8 encoding)  
- Supports data types: string, number, boolean, array, object, null  
- Allows **nested structures**  

---

## 📌 Slide 8 – CSV: Overview
- **Comma-Separated Values** (flat text format for tabular data)  
- Each line = **record (row)**  
- Fields separated by commas, semicolons, or tabs  
- Widely supported by **Excel, databases, BI tools**  

---

## 📌 Slide 9 – CSV: Technical Characteristics
- Simple structure: **rows & columns**  
- Lacks schema → needs documentation for interpretation  
- Requires escaping for quotes & special characters  

---

## 📌 Slide 10 – JSON vs CSV: Why Both Matter
| Feature        | JSON (Input) | CSV (Output) |
|----------------|--------------|--------------|
| Structure      | Hierarchical, nested | Flat, tabular |
| Readability    | Human + machine | Human + spreadsheet apps |
| Use Case       | Data exchange (APIs, storage) | Reports, analytics |
| Project Role   | Source format | Final report format |

---

## 📌 Slide 11 – Why SCRUM + JSON/CSV for This Project?
- **SCRUM** → Iterative approach ensures deliverables each sprint  
- **JSON** → Captures scientific data in flexible structure  
- **CSV** → Standard format for reporting & analysis  
- Combined → Enables an **automated, accurate, and maintainable** system  

---

## 📌 Slide 12 – Conclusion
- SCRUM provides the **methodological framework**  
- JSON & CSV provide the **data foundation**  
- Together they enable:  
  - ⏱️ Faster integration  
  - 📊 Reliable reports  
  - 🚀 Scalable solution for the university  

📖 **Example**:  
```json
{
  "author": "Beatriz",
  "articles": [
    {"title": "Automation", "year": 2025},
    {"title": "Data Processing", "year": 2024}
  ]
}
Esto es una línea de texto antes
