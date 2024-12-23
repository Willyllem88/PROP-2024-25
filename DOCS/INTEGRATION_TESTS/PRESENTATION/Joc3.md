### Guia per provar els casos d'ús d'importar, guardar i guardar com

#### **Inici: Accedir a l'aplicació**

1. Obre l'aplicació i accedeix a la pantalla de Log In.
2. Introdueix les credencials:
   - **Usuari:** admin
   - **Contrasenya:** admin
3. Clica el botó "Log In".

   - **Resultat esperat:** Accés concedit i redirecció al menú principal (Main Screen).

4. A la Main Screen, clica el botó "Configuració de la Distribució" per accedir a la vista de gestió de prestatgeries i configuracions.

---

#### **1. Importar la configuració del supermercat**

##### Acció:

1. A la vista "Configuració de la Distribució", selecciona l'opció "Importar Configuració".
2. Apareixerà un explorador de fitxers; selecciona un fitxer de configuració no vàlid (per exemple, `testPersistenceDifferentHeights.json`).
3. Confirma la importació.
4. Després torna a importar i posa el fitxer `testPersistenceCorrect.json`.

##### Resultats esperats:

- Si el fitxer és vàlid:
  - Es carrega la nova configuració del supermercat, incloent les prestatgeries i els productes definits.
  - Es mostra un missatge de confirmació: "Configuració importada correctament."
- Si el fitxer no és vàlid:
  - Es mostra un missatge d’error: "El fitxer seleccionat no és vàlid o està malmès."

---

#### **2. Guardar la configuració del supermercat**

##### Acció:

1. Realitza canvis a la configuració actual del supermercat (per exemple, afegeix o elimina prestatgeries o modifica tipus de temperatura).
2. Selecciona el botó "Guardar" a la vista de "Configuració de la Distribució".

##### Resultats esperats:

- La configuració actual es desa al fitxer predeterminat (`default.json`).
- Es mostra un missatge de confirmació: "Configuració desada correctament."

---

#### **3. Guardar com una nova configuració**

##### Acció:

1. Realitza canvis a la configuració actual del supermercat.
2. Selecciona el botó "Guardar Com" a la vista de "Configuració de la Distribució".
3. Introdueix un nom per al nou fitxer (per exemple, `nova_config.json`).
4. Confirma l'acció.

##### Resultats esperats:

- La configuració actual es desa al fitxer especificat (`nova_config.json`).
- Es mostra un missatge de confirmació: "Configuració desada correctament com a `nova_config.json`."

---

#### **4. Confirmar si s’ha guardat**

##### Acció:

1. Tanca l’aplicació i torna-la a obrir.
2. Tanca la sessió i torna a entrar amb admin/admin.

##### Resultats esperats:

- En ambdós casos, la configuració ha de reflectir els canvis fets anteriorment.

---

### Objecte de la Prova

#### **Casos d'ús que es proven:**

1. Importar la configuració del supermercat.
2. Guardar la configuració actual del supermercat.
3. Guardar com una nova configuració.

---

### Fitxers de dades necessaris

#### **Fitxer per defecte:**

- **Nom del fitxer:** `default.json`.
- **Contingut:** Configuració inicial del supermercat amb informació de prestatgeries (tipus, posició, productes).

#### **Fitxers d'importació:**

- **Fitxer vàlid:** `testPersistenceCorrect.json`:
  - Prestatgeries i productes configurats correctament.
- **Fitxer invàlid:** `testPersistenceDifferentHeights.json`:
  - Configuració amb alçades no compatibles o format incorrecte.

#### **Fitxers generats:**

- Després de la prova, es generen fitxers nous amb noms específics segons l'usuari (per exemple, `nova_config.json`).

---

### Efectes estudiats

#### **Navegació:**

- Interacció amb l'explorador de fitxers per importar i guardar configuracions.
- Navegació entre vistes (Main Screen, Configuració de la Distribució).

#### **Confirmació d'accions:**

- Missatges de confirmació després de les accions d'importació i guardat.

#### **Gestió d'errors:**

- Visualització de missatges d'error per fitxers no vàlids, espai ple o problemes de permisos.

#### **Persistència:**

- Verificació que els canvis es guarden i es carreguen correctament després de tancar i reobrir l'aplicació.
