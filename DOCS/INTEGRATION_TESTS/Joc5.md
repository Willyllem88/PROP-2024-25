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

#### **Acció:**

1. Selecciona l'opció "Crear Producte" al catàleg.
2. Introdueix les dades següents:
   - **Nom del producte:** ProvaProducte
   - **Temperatura:** AMBIENT
   - **Preu:** 10.99
   - **Imatge:** Selecciona una imatge vàlida.
   - **Paraules clau:** [Alimentació, Snacks]
3. Clica el botó "Crear".

#### **Resultats esperats:**

- El producte es crea correctament i apareix al catàleg.
- Es mostra un missatge de confirmació: "Producte creat correctament."

#### **Casos alternatius:**

- **Temperatura invàlida:**

  - **Acció:** Introdueix una temperatura no vàlida.
  - **Resultat esperat:** Es mostra un missatge d'error: "Temperatura invàlida. Selecciona una opció vàlida."

- **Preu invàlid:**

  - **Acció:** Introdueix un preu menor o igual a zero.
  - **Resultat esperat:** Es mostra un missatge d'error: "Preu invàlid. Introdueix un valor positiu."

- **Producte existent:**

  - **Acció:** Introdueix un nom de producte que ja existeix.
  - **Resultat esperat:** Es mostra un missatge d'error: "El producte ja existeix al catàleg."

- **Imatge invàlida:**
  - **Acció:** Selecciona una imatge en un format no vàlid.
  - **Resultat esperat:** Es mostra un missatge d'error: "Format de la imatge no vàlid."

---

## **2. Borrar producte existent**

#### **Acció:**

1. Selecciona un producte existent al catàleg.
2. Clica el botó "Eliminar".

#### **Resultats esperats:**

- El producte s'elimina correctament del catàleg.
- Es mostra un missatge de confirmació: "Producte eliminat correctament."

#### **Casos alternatius:**

- **Producte a prestatge:**
  - **Acció:** Selecciona un producte que està en un prestatge i clica "Eliminar".
  - **Resultat esperat:** Es mostra un missatge: "El producte està col·locat en un prestatge. Estàs segur que vols eliminar-lo?"
    - Si l'usuari confirma, el producte s'elimina del catàleg i del prestatge.
    - Si l'usuari cancel·la, no s'elimina el producte.

---

## **3. Modificar producte**

#### **Acció:**

1. Selecciona un producte existent al catàleg i clica "Editar".
2. Modifica un o més dels atributs següents:
   - Nom
   - Preu
   - Temperatura
   - Paraules clau
3. Confirma els canvis.

#### **Resultats esperats:**

- Els atributs seleccionats s'actualitzen correctament al catàleg i als prestatges on es troba el producte.
- Es mostra un missatge de confirmació: "Canvis aplicats correctament."

#### **Casos alternatius:**

- **Nom no únic:**

  - **Acció:** Introdueix un nom que ja existeix.
  - **Resultat esperat:** Es mostra un missatge d'error: "El nom del producte ja existeix. Introdueix un nom diferent."

- **Preu invàlid:**

  - **Acció:** Introdueix un preu menor o igual a zero.
  - **Resultat esperat:** Es mostra un missatge d'error: "Preu invàlid. Introdueix un valor positiu."

- **Temperatura inapropiada:**
  - **Acció:** Canvia la temperatura del producte a una incompatible amb el prestatge actual.
  - **Resultat esperat:** Es mostra un missatge d'advertència: "La nova temperatura és incompatible amb els prestatges actuals. El producte serà eliminat d'aquests prestatges. Vols continuar?"
    - Si l'usuari confirma, el producte s'elimina dels prestatges i la temperatura s'actualitza.
    - Si l'usuari cancel·la, no es realitzen canvis.

---

## **4. Modificar similitud entre productes**

#### **Acció:**

1. Selecciona un producte i clica "Editar Relacions".
2. Tria un altre producte del catàleg amb el qual vols modificar la similitud.
3. Introdueix el nou valor de similitud (interval [0, 1)).
4. Confirma els canvis.

#### **Resultats esperats:**

- La nova similitud s'actualitza correctament al catàleg.
- Es mostra un missatge de confirmació: "Similitud actualitzada correctament."

#### **Casos alternatius:**

- **Similitud invàlida:**
  - **Acció:** Introdueix un valor fora de l'interval [0, 1).
  - **Resultat esperat:** Es mostra un missatge d'error: "Similitud invàlida. El valor ha de ser entre 0 i 1."

---

## **5. Cercar productes**

#### **Acció:**

1. Introdueix informació d'un producte al cercador (nom, paraula clau, etc.).
2. Clica el botó "Buscar".

#### **Resultats esperats:**

- Es mostra una llista amb els productes que coincideixen amb la cerca.
- En seleccionar un producte, es mostren els detalls del producte i els prestatges on es troba.

#### **Casos alternatius:**

- **Producte no trobat:**
  - **Acció:** Introdueix un criteri de cerca que no coincideix amb cap producte.
  - **Resultat esperat:** Es mostra un missatge: "No s'ha trobat cap producte amb els criteris introduïts."

---

### Objecte de la Prova

#### **Casos d'ús que es proven:**

1. Crear productes nous.
2. Eliminar productes del catàleg.
3. Modificar atributs de productes existents.
4. Modificar la similitud entre productes.
5. Cercar productes dins del catàleg.

---

### Fitxers de dades necessaris

- **Catàleg inicial:**
  - **Nom del fitxer:** `catalog.json`.
  - **Contingut:** Configuració inicial del catàleg amb productes, relacions i atributs definits.
