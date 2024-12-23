### Guia de Proves del Sistema

#### Prova d’inici de sessió amb diferents credencials

##### Casos de prova

**Prova 1: Credencials incorrectes**

- **Acció:** Introdueix `Usuari: admin` i `Contrasenya: 1234` al formulari de Log In.
- **Resultat esperat:** Un missatge d’error hauria d’indicar que "la contrasenya és incorrecta."

**Prova 2: Usuari inexistent**

- **Acció:** Torna a intentar-ho amb `Usuari: pep` i qualsevol contrasenya.
- **Resultat esperat:** Un missatge d’error hauria d’indicar que "no existeix l’usuari."

**Prova 3: Credencials vàlides com a empleat**

- **Acció:** Introdueix `Usuari: employee` i `Contrasenya: employee`.
- **Resultat esperat:** Accés concedit. Es mostra la Main Screen.

---

#### Explora la Main Screen

##### Característiques:

- Les prestatgeries del supermercat amb els seus productes col·locats.
- Un botó per accedir al catàleg de productes.
- Dos botons per moure’t per la Main Screen i mirar totes les prestatgeries.

---

#### Accedeix al catàleg

- **Acció:** Clica el botó del catàleg.
- **Resultat esperat:** Es mostra la pantalla del catàleg, on pots buscar i explorar els productes.

---

#### Cerca i localitza productes

**Prova 1: Producte inexistent al supermercat**

- **Acció:** Cerca el producte `fruit` i selecciona’l per veure la seva informació.
- **Resultat esperat:** Un missatge d’error indica que "el producte no està col·locat al supermercat."

**Prova 2: Producte existent**

- **Acció:** Cerca el producte `butter`, selecciona’l i clica el botó de localització.
- **Resultat esperat:** Es torna a la Main Screen, amb la prestatgeria número 3 destacada com la que conté el producte `butter`.

---

#### Tanca la sessió

- **Acció:** A la Main Screen, selecciona el botó "Power Off."
- **Resultat esperat:** Apareix l’opció de Log Out. Després de clicar, es torna a la pantalla de Log In.

---

#### Inicia sessió com a administrador

- **Acció:** Introdueix `Usuari: admin` i `Contrasenya: admin`.
- **Resultat esperat:** Es torna a la Main Screen amb noves opcions:
  - Botó del catàleg (com abans).
  - Botons addicionals: "Guardar," "Guardar Com," i "Configuració de la Distribució."

---

#### Explora el catàleg amb privilegis d’administrador

- **Acció:** Accedeix al catàleg clicant el botó corresponent.
- **Resultat esperat:** Es mostren noves funcionalitats:
  - Un botó amb un símbol de suma per afegir nous productes.
  - Camps per modificar informació de productes, modificar relacions, eliminar productes i localitzar-los.

---

#### Configura la distribució del supermercat

- **Acció:** Des de la Main Screen, selecciona el botó "Configuració de la Distribució."
- **Resultat esperat:** Es mostra una nova vista amb:
  - Les prestatgeries actuals del supermercat.
  - Botons nous:
    - **Order By:** Per ordenar les prestatgeries.
    - **Swap:** Per intercanviar prestatgeries.
    - **Afegir prestatgeria:** Botons amb símbols de suma.
    - **Editar i borrar prestatgeria:** Botons amb símbols de llapis i paperera respectivament.
    - **Importar distribució:** Per carregar una configuració prèvia.
    - **Crear nova distribució:** Per redefinir completament la distribució.

---

### Efectes estudiats, casos d’ús i fitxers necessaris

#### Efectes estudiats

- Validació de credencials d’usuari (correctes, incorrectes, usuaris inexistents).
- Navegació per les vistes de l’aplicació: Main Screen, Catàleg, Configuració de la Distribució.
- Missatges d’error per accions no vàlides (com localitzar productes inexistents al supermercat).
- Opcions de gestió del supermercat com ordenar, intercanviar prestatgeries i gestionar productes.

#### Casos d’ús estudiats

- Log In.
- Log Out.
- Tancar Sessió.
- Gestionar prestatgeries.
- Buscador de productes.

#### Fitxers de dades necessaris

**Fitxer per defecte:**

- **Nom del fitxer:** `default.json`.
- **Contingut:** Informació sobre els productes (nom, preu, temperatura, etc.) del catàleg, més quins hi ha al supermercat.
