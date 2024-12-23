# Guia per provar la funcionalitat **"Gestionar Catàleg"**

## **Inici: Accedir a l'aplicació**

1. Obre l'aplicació i accedeix a la pantalla de Log In.
2. Introdueix les credencials:

   - **Usuari:** admin
   - **Contrasenya:** admin

3. Clica el botó "Log In".

   - **Resultat esperat:** Accés concedit i redirecció al menú principal (Main Screen).

4. A la Main Screen, selecciona el botó "Catàleg" per accedir a la vista de gestió del catàleg.

---

## **1. Crear producte nou**

### **1.1. Crear producte amb atributs vàlids**

#### **Acció:**

1. Clica el botó "Crear Producte" situat a la part inferior de la pantalla.
2. Introdueix les dades següents:
   - **Nom del producte:** NomDelProducte
   - **Temperatura:** AMBIENT
   - **Preu:** 10.99
   - **Imatge:** Selecciona una imatge vàlida del teu sistema.
   - **Paraules clau:** [Alimentació, Snacks]
3. Clica el botó "Confirm" per crear el producte.

#### **Resultats esperats:**

- El producte es crea correctament i apareix al catàleg.
- Es mostra un missatge de confirmació: "Product added successfully."

### **1.2. Crear producte amb nom no únic**

#### **Acció:**

1. Clica el botó rodó amb l'icona de "+" situat a la part inferior de la pantalla.
2. Introdueix les dades següents:
   - **Nom del producte:** Cereal
   - **Temperatura:** Ambient
   - **Preu:** 1.0
   - **Imatge:** Selecciona una imatge vàlida del teu sistema.
   - **Paraules clau:** [Alimentació, Snacks]
3. Clica el botó "Confirm" per crear el producte.

#### **Resultats esperats:**

- El producte no es crea.
- Es mostra el missatge d'error: "Product with name "Cereal" already exists.".

---

## **2. Borrar producte**

### **2.1. Borrar producte no col·locat al supermercat**

#### **Acció:**

1. Cerca i selecciona el producte "Coffee" del catàleg.
2. Clica el botó amb l'icona de la paperera per eliminar el producte.
3. Confirma l'acció per eliminar el producte.

#### **Resultats esperats:**

- El producte s'elimina correctament del catàleg.
- Es mostra un missatge de confirmació: "Producte eliminat correctament".

### **2.2 Borrar producte col·locat al supermercat**

#### **Acció:**

1. Cerca i selecciona el producte "Cereal" del catàleg.
2. Clica el botó amb l'icona de la paperera per eliminar el producte.
3. Confirma l'acció per eliminar el producte.

#### **Resultats esperats:**

- El producte no s'elimina del catàleg.
- Es mostra un missatge d'error': "Cannot delete product, it is placed in the shelves".

---

## **3. Modificar producte**

### **3.1 Modificar atributs de producte**

#### **Acció:**

1. Selecciona el producte "Coffee" del catàleg.
2. Modifica un o més dels atributs següents:
   - Preu
   - Temperatura
   - Paraules clau
3. Clica confirmar per acceptar els canvis a mesura que els vagis fent.

#### **Resultats esperats:**

- L'atribut modificat s'actualitza correctament al catàleg.

### **3.2 Modificar atributs amb valors invàlids**

#### **Acció:**

1. Selecciona el producte "Coffee" del catàleg.
2. Modifica un o més dels atributs següents amb valors invàlids:
   - Preu: -1.0
   - Temperatura: "FRED"
   - Paraules clau: [Alimentació, Snacks, Fruites]
3. Clica confirmar per acceptar els canvis a mesura que els vagis fent.

#### **Resultats esperats:**

- Els atributs no es modifiquen.
- Es mostra el missatge d'error "Cannot modify product, it is placed in the shelves".

---

## **4. Modificar similitud entre productes**

### **4.1 Modificar similitud vàlida**

#### **Acció:**

1. Selecciona el producte "Coffee" del catàleg.
2. Clica el botó "Edit Relations" per modificar la similitud amb un altre producte.
3. Selecciona el producte "Canned Beans" de la taula i clica la cel·la de similitud per modificar-la.
4. Introdueix el valor 0.5.
5. Clica "Enter" o a qualsevol altre lloc de la pantalla per confirmar la modificació.

#### **Resultats esperats:**

- La nova similitud s'actualitza correctament al catàleg.

### **4.2 Modificar similitud amb valor invàlid**

#### **Acció:**

1. Selecciona el producte "Coffee" del catàleg.
2. Clica el botó "Edit Relations" per modificar la similitud amb un altre producte.
3. Selecciona el producte "Canned Beans" de la taula i clica la cel·la de similitud per modificar-la.
4. Introdueix el valor 2.0.
5. Clica "Enter" o a qualsevol altre lloc de la pantalla per confirmar la modificació.

#### **Resultats esperats:**

- La similitud no es modifica.
- Es mostra el missatge d'error "Relation value must be between 0 and 1".

## **5. Modificar paraules clau de productes**

#### **Acció:**

1. Selecciona el producte "Jam" del catàleg.
2. Clica el botó "Edit Keywords" per modificar les paraules clau del producte.
3. S'obre una finestra amb les paraules clau actuals del producte.
4. Introdueix les paraules clau següents fent servir el botó "Add Keyword":
   - [Begudes, Cafè, Matí]
5. Elimina la paraula clau fruit clicant-la i clicant el botó "Remove selected".
6. Fes doble clic a la paraula "jar" per modificar-la a "pot".
7. Per anar confirmant els subcanvis clica "Enter" o a qualsevol altre lloc de la pantalla.
8. Clica el botó "Accept" per confirmar els canvis.

#### **Resultats esperats:**

- Les paraules clau del producte "Jam" ara són: {Begudes, Cafè, Matí, pot, spread, sweet, preserve}.

---

## **6. Trobar producte al supermercat**

### **6.1 Trobar producte col·locat al supermercat**

#### **Acció:**

1. Selecciona el producte "Canned Beans" del catàleg.
2. Clica el botó "Find in supermarket".

#### **Resultats esperats:**

- Es navega a la pantalla principal amb el producte seleccionat mostrat en un prestatge.

### **6.2 Trobar producte no col·locat al supermercat**

#### **Acció:**

1. Selecciona el producte "Coffee" del catàleg.
2. Clica el botó "Find in supermarket".

#### **Resultats esperats:**

- Es mostra el missatge d'error: "The product is not available in the supermarket".

---

## **7. Cercar productes**

#### **Acció:**

1. Introdueix informació d'un producte al cercador (nom, paraula clau, etc.).

#### **Resultats esperats:**

- Es mostra una llista amb els productes que coincideixen amb la cerca (nom, paraules clau).
- En seleccionar un producte, es mostren els detalls del producte a la part esquerra de la pantalla.

---

### Objecte de la Prova

#### **Casos d'ús que es proven:**

1. Crear productes nous.
2. Eliminar productes del catàleg.
3. Modificar atributs de productes existents.
4. Modificar la similitud entre productes.
5. Modificar paraules clau de productes.
6. Trobar productes al supermercat.
7. Cercar productes dins del catàleg.

---

### Fitxers de dades necessaris

- **Catàleg inicial:**
  - **Nom del fitxer:** `default.json`.
  - **Contingut:** Configuració inicial del catàleg amb productes, relacions i atributs predefinits.

---

### Efectes estudiats

- Validar que el producte es crea correctament.
- Validar que el producte no es crea si ja existeix.
- Validar que el producte s'elimina correctament.
- Validar que el producte no es pot eliminar si està col·locat al supermercat.
- Validar que els atributs del producte es modifiquen correctament.
- Validar que els atributs del producte no es modifiquen si el producte està col·locat al supermercat.
- Validar que la similitud entre productes es modifica correctament.
- Validar que la similitud entre productes no es modifica si el valor no és vàlid.
- Validar que les paraules clau del producte es modifiquen correctament.
- Validar que el producte es troba al supermercat.
- Validar que el producte no es troba al supermercat.
- Validar que la cerca de productes retorna els resultats correctes.
