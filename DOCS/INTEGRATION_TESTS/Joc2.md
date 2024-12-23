### Guia de Proves del Sistema - Gestió de la Distribució i Prestatgeries

#### Inicia sessió amb credencials d’administrador

- **Acció:** Introdueix `Usuari: admin` i `Contrasenya: admin`.
- **Resultat esperat:** Accés concedit. Es mostra la Main Screen.

---

#### Configura la distribució del supermercat

1. **Afegir una prestatgeria nova entre la 0 i la 1:**

   - **Acció:** Selecciona el botó de suma entre les prestatgeries 0 i 1 i assigna el tipus `refrigerated`.
   - **Resultat esperat:** La prestatgeria 1 es converteix en `fridge`, la 2 passa a ser la 1 anterior, i la resta s’ajusten.

2. **Canviar tipus de temperatura d’una prestatgeria existent:**

   - **Acció:** Clica el botó a la dreta de la prestatgeria 2 i canvia el tipus a `freezer`.
   - **Resultat esperat:** La prestatgeria es converteix en `freezer`.

3. **Afegir una prestatgeria nova a l’esquerra:**

   - **Acció:** Afegeix una prestatgeria nova al tipus desitjat.
   - **Resultat esperat:** La prestatgeria apareix a l’esquerra amb un missatge de confirmació.

4. **Eliminar prestatgeries:**
   - **Prova 1:** Selecciona el botó de la paperera de la prestatgeria 1.
     - **Resultat esperat:** La prestatgeria s’elimina directament amb un missatge de confirmació.
   - **Prova 2:** Clica el botó de la paperera de la prestatgeria 0.
     - **Acció:** Quan aparegui el popup, selecciona "No."
     - **Resultat esperat:** No es fa cap canvi.
     - **Acció:** Torna a provar i selecciona "Sí."
     - **Resultat esperat:** La prestatgeria s’elimina amb un missatge de confirmació.

---

#### Edició d’una prestatgeria

1. **Entrar en mode edició d’una prestatgeria:**

   - **Acció:** Clica el botó del llapis de la prestatgeria 2.
   - **Resultat esperat:** Es mostra una vista d’edició amb:
     - Botons a l’esquerra:
       - Buidar la prestatgeria.
       - Eliminar la prestatgeria.
       - Canviar el tipus de temperatura.
       - Confirmar canvis.
     - La prestatgeria a la dreta amb botons per afegir i gestionar productes.

2. **Gestió de productes:**

   - **Prova 1:** Afegeix el producte "bread".
     - **Resultat esperat:** Missatge d’incompatibilitat si la temperatura no és adequada.
   - **Prova 2:** Afegeix un producte compatible amb la temperatura de la prestatgeria.
     - **Resultat esperat:** El producte s’afegeix correctament.

3. **Canviar tipus de temperatura:**

   - **Prova 1:** Intenta canviar la temperatura a `ambient` sense buidar la prestatgeria.
     - **Resultat esperat:** Missatge d’error.
   - **Prova 2:** Buida la prestatgeria i canvia la temperatura a `ambient`.
     - **Resultat esperat:** El canvi es realitza correctament.

4. **Confirmar canvis:**

   - **Acció:** Afegeix el "bread" i clica el botó de confirmar.
   - **Resultat esperat:** Es torna a la vista de distribució amb els canvis aplicats.

5. **Descartar canvis:**

   - **Acció:** Torna a la mateixa prestatgeria, elimina el "bread" i selecciona "Go Back."
   - **Resultat esperat:** Els canvis no es guarden.

6. **Eliminar prestatgeries des d’edició:**
   - **Acció:** Utilitza el botó de la paperera dins la vista d’edició.
   - **Resultat esperat:** Funciona igual que a la distribució, incloent popups de confirmació.

---

#### Crear una nova distribució

1. **Acció:** Clica el botó de nova distribució.

   - **Resultat esperat:** Apareix un avís indicant que es perdrà la configuració actual si no es guarda.

2. **Definir la nova distribució:**
   - **Prova 1:** Introdueix valors incorrectes (per exemple, 0 a totes les temperatures i alçada 3).
     - **Resultat esperat:** Missatge d’error indicant que no es pot crear.
   - **Prova 2:** Introdueix valors vàlids (3 d’ambient, 1 de fridge, 1 de frozen, alçada 3).
     - **Resultat esperat:** Es mostra la nova configuració amb les prestatgeries ordenades (3 ambient, 1 fridge, 1 frozen).

---

#### Tornar a l’estat original

1. **Acció:** Fes logout sense guardar canvis.
   - **Resultat esperat:** Apareix un avís indicant que es perdran els canvis.
   - **Acció:** Inicia sessió de nou com a admin.
   - **Resultat esperat:** La configuració inicial es restaura tal com estava abans dels canvis.

---

#### Funció de swap

1. **Intercanviar prestatgeries:**

   - **Acció:** Selecciona "Swap," clica les prestatgeries 0 i 1.
   - **Resultat esperat:** Les prestatgeries intercanvien posicions.

2. **Intercanviar productes:**

   - **Prova 1:** Selecciona dos productes de prestatgeries del mateix tipus.
     - **Resultat esperat:** Els productes intercanvien posicions.
   - **Prova 2:** Selecciona un producte buit i un producte d’una altra prestatgeria del mateix tipus.
     - **Resultat esperat:** Intercanvi correcte.
   - **Prova 3:** Intenta intercanviar productes de prestatgeries de tipus diferents.
     - **Resultat esperat:** Missatge d’error.

3. **Seleccions persistents:**

   - **Acció:** Selecciona un producte i una prestatgeria i navega per les prestatgeries.
   - **Resultat esperat:** Les seleccions es mantenen fins que no es completi un intercanvi o es deseleccioni manualment.

4. **Descartar swaps:**
   - **Acció:** Selecciona "Go Back" mentre fas swaps.
   - **Resultat esperat:** Torna a la vista de distribució amb cap canvi aplicat.

---

### Efectes estudiats, casos d’ús i fitxers necessaris

#### Efectes estudiats

- Navegació per les vistes de l’aplicació: Main Screen, Catàleg, Configuració de la Distribució.
- Missatges d’error per accions no vàlides (com intentar intercanviar productes incompatibles o eliminar prestatgeries sense confirmació).
- Opcions de gestió del supermercat com ordenar, intercanviar prestatgeries i gestionar productes.
- Confirmació i descart de canvis durant l’edició de prestatgeries o la distribució.

#### Casos d’ús estudiats

- Log In
- Log Out
- Gestionar prestatgeries
- Afegir prestatgeries
- Eliminar prestatgeries
- Modificar el tipus de temperatura
- Afegir, borrar i gestionar productes
- Buscador de productes
- Swap de prestatgeries i productes entre prestatgeries
- Creació d’una nova distribució
- Restauració de l’estat original després de descartar canvis

#### Fitxers de dades necessaris

**Fitxer per defecte:**

- **Nom del fitxer:** `default.json`.
- **Contingut:**
  - Informació sobre els productes:
    - Nom.
    - Preu.
    - Tipus de temperatura requerida.
  - Configuració actual de les prestatgeries:
    - Tipus (ambient, fridge, freezer).
    - Posició.
    - Productes col·locats.
- **Missió:** Permet carregar i guardar l’estat del supermercat durant les proves.
