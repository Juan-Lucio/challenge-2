# 🎤 Presentation: SCRUM Basics & Data Formats (JSON & CSV)

---

## 📌 Slide 1 – Title
### **Project Context**
This project focuses on automating the conversion of **JSON files** into **CSV reports** for the university’s Scientometrics Department.  
To ensure success, we apply the **SCRUM methodology** for project management and rely on a strong understanding of **data formats**.

---

## 📌 Slide 2 – What is SCRUM?
SCRUM is an **Agile framework** used to manage complex projects.  
- It organizes work into **short cycles (sprints)**.  
- Promotes **collaboration, adaptability, and continuous delivery**.  
- Helps teams deliver **value incrementally** instead of waiting for a final product.  

👉 In our case: SCRUM ensures the project advances step by step, with working results at the end of each sprint.  

---

## 📌 Slide 3 – SCRUM Roles
- 👩‍💼 **Product Owner**: Defines what needs to be built and why.  
- 👩‍💻 **Development Team**: Builds the product (Beatriz is the developer).  
- 🧑‍🏫 **Scrum Master**: Ensures the team follows SCRUM, removes obstacles.  

👉 These roles guarantee **clarity, focus, and accountability**.  

---

## 📌 Slide 4 – SCRUM Events
- **Sprint Planning** → Define goals and select tasks.  
- **Daily Scrum** → 15-min sync on progress and blockers.  
- **Sprint Review** → Show the results to stakeholders.  
- **Sprint Retrospective** → Reflect and improve for the next sprint.  

👉 These events create a **rhythm** for the project, ensuring transparency and continuous improvement.  

---

## 📌 Slide 5 – SCRUM Artifacts
- 📋 **Product Backlog** → Full list of requirements.  
- 📌 **Sprint Backlog** → Selected tasks for one sprint.  
- ✅ **Increment** → A tested, usable piece of the product.  

👉 Artifacts make work **visible and measurable**.  

---

## 📌 Slide 6 – JSON: Overview
**JavaScript Object Notation (JSON)** is a lightweight text format to store and exchange data.  
- Human-readable and machine-readable.  
- Commonly used in APIs and databases.  
- Flexible for representing **complex, nested data structures**.  

👉 JSON is the **input format** we must process in this project.  

---

## 📌 Slide 7 – JSON: Technical Characteristics
- **Encoding**: Text, usually UTF-8.  
- **Data types**: string, number, boolean, null, object, array.  
- **Structure**: Uses `{}` for objects and `[]` for arrays.  
- **Scalability**: Can handle nested and hierarchical data.  

📖 **Example**:  
```json
{
  "author": "Beatriz",
  "articles": [
    {"title": "Automation", "year": 2025},
    {"title": "Data Processing", "year": 2024}
  ]
}
