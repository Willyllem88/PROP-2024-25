# Casos d'Ús - Gestió de Sessions del Supermercat

Possibles actors:

- Administrador
- Empleat
- Usuari (engloba als dos)

---

## Índex - Casos d'Ús

- [Casos d'Ús - Gestió de Sessions del Supermercat](#casos-dús---gestió-de-sessions-del-supermercat)
  - [Índex - Casos d'Ús](#índex---casos-dús)
  - [Casos d'Ús](#casos-dús)
    - [1. Log In](#1-log-in)
    - [2. Tancar aplicació](#2-tancar-aplicació)
    - [3. Tancar sessió](#3-tancar-sessió)
    - [4. Gestionar supermercat](#4-gestionar-supermercat)
      - [4.1. Importar la configuració del supermercat](#41-importar-la-configuració-del-supermercat)
      - [4.2. Exportar la configuració del supermercat](#42-exportar-la-configuració-del-supermercat)
      - [4.3. Gestionar prestatgeries](#43-gestionar-prestatgeries)
      - [4.3.1. Crear distribució de prestatgeries](#431-crear-distribució-de-prestatgeries)
      - [4.3.2. Ordenar òtpimament productes de les prestatgeries](#432-ordenar-òptimament-productes-de-les-prestatgeries)
      - [4.3.3. Afegir producte](#433-afegir-producte)
      - [4.3.4. Treure producte](#434-treure-producte)
      - [4.3.5. Swap producte](#435-swap-producte)
      - [4.3.6. Cambiar tipus de prestatgeria](#436-cambiar-tipus-de-prestatgeria)
      - [4.3.7. Afegir prestatgeria](#437-afegir-prestatgeria)
      - [4.3.8. Eliminar prestatgeria](#438-eliminar-prestatgeria)
      - [4.3.9. Swap prestatgeries](#439-swap-prestatgeries)
      - [4.3.10. Buidar prestatgeria](#4310-buidar-prestatgeria)
      - [4.4. Gestionar catàleg](#44-gestionar-catàleg)
      - [4.4.1. Crear producte](#441-crear-producte)
      - [4.4.2. Borrar producte](#442-borrar-producte)
      - [4.4.3. Modificar producte](#443-modificar-producte)
      - [4.4.4. Modificar similitud entre productes](#444-modificar-similitud-entre-productes)
      - [4.5. Buscador de productes](#45-buscador-de-productes)

---

---

## Casos d'Ús

### 1. Log In

**Nom:** Log In
**Actor:** Usuari
**Comportament:**

1. L'usuari introdueix el nom d'usuari i contrasenya.
2. El sistema valida les credencials i concedeix accés segons el rol (empleat o administrador).
3. Si les credencials són correctes, l'usuari pot accedir al sistema.

**Casos alternatius:**

- **Accés invàlid:** Si les credencials (usuari o contrasenya) són incorrectes, el sistema mostra un missatge d'error i demana novament l'entrada de les credencials.

---

### 2. Tancar aplicació

**Nom:** Tancar aplicació
**Actor:** Usuari
**Comportament:**

1. L'usuari tanca l'aplicació des del menú.
2. Si hi ha una sessió d'usuari iniciada, la tancarà.
3. El sistema tanca l'aplicació després de confirmar que la sessió s'ha tancat correctament.

---

### 3. Tancar sessió

**Nom:** Tancar sessió
**Actor:** Usuari
**Comportament:**

1. L'actor selecciona l'opció de tancar sessió.
2. El sistema neteja les dades temporals associades a la sessió.
3. El sistema tanca la sessió.
4. Finalment, el sistema redirigeix l'usuari a la pantalla de _login_.

**Casos alternatius:**

- **Sessió d'administrador:** Si l'usuari és administrador, el sistema pregunta a l'usuari si vol desar els darrers canvis no guardats.
  - Si l'administrador confirma, el sistema desa els canvis pendents.
  - Si l'administrador no confirma, la sessió es tanca sense desar els canvis.

---

### 4. Gestionar supermercat

**Nom:** Gestionar supermercat
**Actor:** Usuari
**Comportament:**

1. L'usuari indica que vol gestionar el supermercat.
2. El sistema mostra diferents opcions de gestió (prestatges, productes, buscador, exportar configuració, importar configuració).
3. L'usuari podrà seleccionar l'opció desitjada.

**Casos alternatius:**

- **Permisos insuficients:** Si l'usuari no és administrador, es denegarà l'accés a certes funcions.

### 4.1. Importar la configuració del supermercat

**Nom:** Importar la configuració del supermercat
**Actor:** Administrador
**Comportament:**

1. L'usuari selecciona l'opció per carregar una nova configuració.
2. El sistema demana el fitxer que conté la nova configuració del supermercat.
3. El sistema valida el fitxer i carrega la nova configuració (prestatges i catàleg).

**Casos alternatius:**

- **Arxiu no vàlid:** Si l'arxiu no té el format adequat o està malmès, es mostra un missatge d'error.

### 4.2. Exportar la configuració del supermercat

**Nom:** Exportar la configuració del supermercat
**Actor:** Administrador
**Comportament:**

1. L'usuari selecciona l'opció per exportar la configuració del supermercat.
2. El sistema genera un fitxer amb la configuració actual (prestatges i catàleg).
3. El sistema demana a l'usuari on vol guardar el fitxer.
4. El sistema guarda el fitxer a la ubicació especificada.

**Casos alternatius:**

- Si hi ha un error al desar el fitxer, el sistema mostrarà un missatge d'error i recomenarà tornar-ho a intentar.

### 4.3. Gestionar prestatgeries

**Nom:** Gestionar prestatgeries
**Actor:** Administrador
**Comportament:**

1. L'administrador podrà triar diferentes opcions per tal de fer els canvis que consideri a les prestatgeries. Les opcions són els següents casos d'ús.

### 4.3.1. Crear distribució de prestatgeries

**Nom:** Crear distribució de prestatgeries
**Actor:** Administrador
**Comportament:**

1. L'administrador indica al sistema quantes prestatgeries de cada tipus vol.
2. Si el supermercat és buit, el sistema crea n prestatgeries dels tipus indicats i els hi assigna una posició.

**Casos alternatius:**

- **Supermercat no buit:** Si el supermercat ja té prestatgeries, el sistema col·locarà les noves n prestatgeries a continuació de la darrera prestatgeria.

### 4.3.2. Ordenar òptimament productes de les prestatgeries

**Nom:** Ordenar òptimament productes de les prestatgeries
**Actor:** Administrador
**Comportament:**

1. L'administrador indica al sistema que vol que ordeni els productes del supermercat al seu criteri (el del sistema).
2. El sistema ordena els productes col·locats a les prestatgeries de manera que es maximitzi la probabilitat de que els seus clients comprin més.

**Casos alternatius:**

- **Supermercat buit:** Si no hi ha cap producte al supermercat, el sistema no farà res.

### 4.3.3. Afegir producte

**Nom:** Afegir producte
**Actor:** Administrador
**Comportament:**

1. L'administrador afegeix productes a la prestatgeria seleccionada.
2. L'aplicació comprova l'espai i la compatibilitat dels productes.

**Casos alternatius:**

- **Posició ocupada:** Es mostra un error per espai insuficient.
- **Productes incompatibles:** Es mostra un avís.
- **Prestatge invàlid:** No existeix la prestatgeria del producte.

### 4.3.4. Treure producte

**Nom:** Treure producte
**Actor:** Administrador
**Comportament:**

1. L'administrador selecciona un producte d'una prestatgeria per treure'l.
2. El sistema elimina el producte de la prestatgeria.

### 4.3.5. Swap producte

**Nom:** Swap producte
**Actor:** Administrador
**Comportament:**

1. L'administrador selecciona dos productes col·locats a prestatgeries per intercanviar-los.
2. El sistema intercanvia la posició dels productes.

**Casos alternatius:**

- **Prestatgeria buida:** Si un dels prestatges és buit, llavors el sistema col·loca el producte a la prestatgeria buida i elimina el producte de la prestatgeria d'origen.
- **Prestatgeries buides:** Si no hi ha productes a cap del dos prestatges, el sistema no farà res.

### 4.3.6. Cambiar tipus de prestatgeria

**Nom:** Cambiar tipus de prestatgeria
**Actor:** Administrador
**Comportament:**

1. L'administrador selecciona una prestatgeria i canvia el seu tipus.
2. L'aplicació modifica el tipus de la prestatgeria.

**Casos alternatius:**

- **Prestatgeria invàlid:** S'ha seleccionat una prestatgeria que no existeix.
- **Prestatgeria no buit:** Es mostra un avís de confirmació, alertant que els productes de la prestatgeria es retiraran d'aquesta.

### 4.3.7. Afegir prestatgeria

**Nom:** Afegir prestatgeria
**Actor:** Administrador
**Comportament:**

1. L'administrador selecciona la posició i el tipus de prestatge (nevera, congelador, etc.).
2. L'aplicació crea el prestatge a la ubicació seleccionada.

**Casos alternatius:**

- **Índex no disponible:** Es mostra un missatge d'error.

### 4.3.8. Eliminar prestatgeria

**Nom:** Eliminar prestatgeria
**Actor:** Administrador
**Comportament:**

1. L'administrador selecciona una prestatgeria existent per eliminar-la.
2. L'aplicació elimina la prestatgeria.

**Casos alternatius:**

- **Prestatge amb productes:** Es mostra un avís de confirmació indicant que els productes del prestatge desapereixeran.
- **Prestatge inexistent:** Es mostra un missatge d'error.

### 4.3.9. Swap prestatgeries

**Nom:** Swap prestatgeries
**Actor:** Administrador
**Comportament:**

1. L'administrador selecciona dos prestatges per intercanviar-los.
2. L'aplicació intercanvia els prestatges mantenint productes.

**Casos alternatius:**

- **Ubicació no vàlida:** Es cancel·la l'operació i es mostra un avís.

### 4.3.10. Buidar prestatgeria

**Nom:** Buidar prestatgeries
**Actor:** Administrador
**Comportament:**

1. L'administrador selecciona un prestatge per buidar-lo.
2. El sistema pregunta si s'està segur de l'acció que es realitzarà i adverteix de que l'acció és irreversible.
3. Si l'administrador confirma, el sistema buida el prestatge.
4. Si l'administrador cancel·la, no es realitza cap canvi.

**Casos alternatius:**

- **Prestatge inexistent:** Es mostra un missatge d'error.
- **Prestatge sense productes:** El prestatge ja està buit i per tant no es realitza cap canvi.

### 4.4. Gestionar catàleg

**Nom:** Gestionar catàleg
**Actor:** Administrador
**Comportament:**

1. L'administrador podrà triar diferentes opcions per tal de fer els canvis que consideri al catàleg. Les opcions són els següents casos d'ús.

### 4.4.1. Crear producte

**Nom:** Crear producte nou
**Actor:** Administrador
**Comportament:**

1. L'usuari introdueix les dades d'un nou producte.
2. El sistema valida i desa el producte.

**Casos alternatius:**

- **Dades invàlides:** Es mostra un error.
- **Conflicte per duplicat:** Es demana un altre identificador.

### 4.4.2. Borrar producte

**Nom:** Borrar producte existent
**Actor:** Administrador
**Comportament:**

1. L'usuari selecciona un producte per eliminar.
2. Es confirma i elimina el producte dels prestatges i del catàleg.

**Casos alternatius:**

- **Cancel·lació:** El producte no s'elimina.
- **Producte associat a operacions:** Es mostra un error.

### 4.4.3. Modificar producte

**Nom:** Modificar producte
**Actor:** Administrador
**Comportament:**

1. L'usuari edita els atributs d'un producte.
2. El sistema valida i actualitza la informació.

**Casos alternatius:**

- **Dades invàlides:** Es mostra un error.
- **Cancel·lació:** No es guarden els canvis.

### 4.4.4. Modificar similitud entre productes

**Nom:** Modificar similitud entre productes
**Actor:** Administrador
**Comportament:**

1. L'usuari selecciona dos productes i estableix la seva similitud.
2. El sistema guarda la relació de similitud.

**Casos alternatius:**

- **Productes incompatibles:** Es mostra un error.
- **Cancel·lació:** No es realitzen canvis.

### 4.5. Buscador de productes

**Actor:** Usuari

**Comportament:**

1. L'usuari introdueix informació d'un producte al cercador.
2. El sistema busca el producte internament a les estructures de dades corresponents en base a diferents criteris (identificador, paraules clau...).
3. El sistema mostra una llista amb els resultats de la cerca.
4. L'usuari tria el resultat que vulgui.
5. El sistema mostra informació del prestatge on es troba el producte i del producte en sí.

**Casos alternatius:**

- **Producte no trobat:** Si no es troba cap producte, el sistema mostrarà un missatge indicant que no hi ha cap coincidència.

---

---

---
