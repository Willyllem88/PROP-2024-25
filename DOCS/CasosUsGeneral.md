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
    - [4.3.2.1 Colocar òptimament productes de les prestatgeries](#4321-colocar-òptimament-productes-de-les-prestatgeries)
    - [4.3.2.2 Colocar òptimament productes del càtaleg](#4322-colocar-òptimament-productes-del-càtaleg)
    - [4.3.3. Afegir producte a prestatge](#433-afegir-producte-a-prestatge)
    - [4.3.4. Treure producte de prestatge](#434-treure-producte-de-prestatge)
    - [4.3.5. Swap producte](#435-swap-producte)
    - [4.3.6. Canviar tipus de prestatgeria](#436-canviar-tipus-de-prestatgeria)
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

1. L'usuari tanca El sistema des del menú.
2. Si hi ha una sessió d'usuari iniciada, la tancarà.
3. El sistema tanca El sistema després de confirmar que la sessió s'ha tancat correctament.

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
2. El sistema mostra diferents opcions de gestió (prestatges, productes, buscador, exportar configuració, importar configuració, moure't pel supermercat).
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

- **Configuració ja carregada:** Si ja existia una configuració associada a la sessió, el sistema pregunta si es vol sobreescriure i si es vol exportar (guardar).
- **Arxiu no vàlid:** Si l'arxiu no té el format adequat o està malmès, es mostra un missatge d'error.

### 4.2. Exportar la configuració del supermercat

**Nom:** Exportar la configuració del supermercat
**Actor:** Administrador
**Comportament:**

1. L'usuari selecciona l'opció per exportar la configuració del supermercat.
2. El sistema genera un fitxer amb la configuració actual (prestatges i catàleg tot i que estiguin buits).
3. El sistema demana a l'usuari on vol guardar el fitxer.
4. El sistema guarda el fitxer a la ubicació especificada.

**Casos alternatius:**

- **Error d'exportació:** Si hi ha un error al desar el fitxer, el sistema mostrarà un missatge d'error i recomenarà tornar-ho a intentar.

### 4.3. Gestionar prestatgeries

**Nom:** Gestionar prestatgeries
**Actor:** Administrador
**Comportament:**

1. L'administrador indica que vol gestionar les prestatgeries.
2. El sistema mostra les opcions de gestió de prestatgeries (crea distribució, ordenar productes, afegir producte, treure producte, swap productes, canviar tipus de prestatgeria, afegir prestatgeria, eliminar prestatgeria, swap prestatgeries, buidar prestatgeria).
3. L'administrador selecciona l'opció desitjada.

### 4.3.1. Crear distribució de prestatgeries

**Nom:** Crear distribució de prestatgeries
**Actor:** Administrador
**Comportament:**

1. L'administrador indica l'altura de les prestatgeries, els tipus de prestatgeria i el nombre de cada tipus.
2. El sistema crea les prestatgeries dels tipus indicats i els hi assigna una posició seqüencialment.

**Casos alternatius:**

- **Supermercat no buit:**
  1. El sistema indica que el supermercat no està buit i demana confirmació per sobreescriure la configuració actual, si es sobre escriu pregunta si es vol exportar.
  2. Si l'administrador confirma, el sistema sobreescriu la configuració actual i si es desitja s'exporta la distribució.
  3. Si l'administrador cancel·la, el sistema no farà res.

### 4.3.2.1 Colocar òptimament productes de les prestatgeries

**Nom:** Ordenar òptimament productes de les prestatgeries
**Actor:** Administrador
**Comportament:**

1. L'administrador indica al sistema que vol que coloqui els productes del supermercat.
2. El sistema col·loca els productes de les prestatgeries de manera que es maximitzi la probabilitat de que els seus clients comprin més.

**Casos alternatius:**

- **Prestatges buits:** Si no hi ha cap producte al supermercat, el sistema no farà res.

### 4.3.2.2 Colocar òptimament productes del càtaleg

**Nom:** Ordenar òptimament productes de les prestatgeries
**Actor:** Administrador
**Comportament:**

1. L'administrador indica al sistema que vol que coloqui els productes del càtaleg al supermercat.
2. El sistema col·loca els productes del catàleg a les prestatgeries de manera que es maximitzi la probabilitat de que els seus clients comprin més.

**Casos alternatius:**

- **Càtaleg buit:** Si no hi ha cap producte al càtaleg, el sistema no farà res.

### 4.3.3. Afegir producte a prestatge

**Nom:** Afegir producte
**Actor:** Administrador
**Comportament:**

1. L'administrador indica el producte, la prestatgeria i la altura.
2. El sistema afegeix el producte.

**Casos alternatius:**

- **Posició ocupada:** Es mostra un error per espai insuficient.
- **Temperatura incompatible:** Es mostra un error per incompatibilitat entre la temperatura del producte i de la prestatgeria.
- **Prestatge invàlid:** No existeix la prestatgeria i/o l'altura.

### 4.3.4. Treure producte de prestatge

**Nom:** Treure producte
**Actor:** Administrador
**Comportament:**

1. L'administrador indica la prestatgeria i la altura.
2. El sistema elimina el producte de la prestatgeria.

**Casos alternatius:**

- **Posició buida:** Es mostra un error per espai buit.
- **Prestatge invàlid:** No existeix la prestatgeria i/o l'altura.

### 4.3.5. Swap producte

**Nom:** Swap producte
**Actor:** Administrador
**Comportament:**

1. L'administrador selecciona dues prestatgeries i les corresponents altures.
2. El sistema intercanvia la posició dels productes.

**Casos alternatius:**

- **Prestatge invàlid:** No existeix alguna prestatgeria i/o l'altura.
- **Temperatura incompatible:** Es mostra un error per incompatibilitat entre prestatgeries.

### 4.3.6. Canviar tipus de prestatgeria

**Nom:** Cambiar tipus de prestatgeria
**Actor:** Administrador
**Comportament:**

1. L'administrador selecciona una prestatgeria i canvia el seu tipus.
2. El sistema modifica el tipus de la prestatgeria.

**Casos alternatius:**

- **Prestatgeria invàlid:** S'ha seleccionat una prestatgeria que no existeix.
- **Prestatgeria no buit:** Es mostra un avís de confirmació, alertant que els productes de la prestatgeria es retiraran d'aquesta.

### 4.3.7. Afegir prestatgeria

**Nom:** Afegir prestatgeria
**Actor:** Administrador
**Comportament:**

1. L'administrador indica la posició i el tipus de prestatgeria.
2. El sistema assigna un uID i crea la prestatgeria a la esquerra de la ubicació seleccionada.

**Casos alternatius:**
- **Primera prestatgeria:**
  1. Si no hi ha cap prestatgeria, el sistema demanarà l'altura de la prestatgeria.
  2. L'usuari indica l'altura.
  3. El sistema crea la prestatgeria.
   
- **Índex no disponible:** Es mostra un missatge d'error.

### 4.3.8. Eliminar prestatgeria

**Nom:** Eliminar prestatgeria
**Actor:** Administrador
**Comportament:**

1. L'administrador indica la posició a eliminar.
2. El sistema elimina la prestatgeria.

**Casos alternatius:**

- **Prestatge amb productes:** Es mostra un avís de confirmació indicant que els productes de la prestatgeria es treuran.
- **Prestatge inexistent:** S'ha seleccionat una prestatgeria que no existeix.

### 4.3.9. Swap prestatgeries

**Nom:** Swap prestatgeries
**Actor:** Administrador
**Comportament:**

1. L'administrador indica dos posicions de prestatgeries.
2. El sistema intercanvia les prestatgeries.

**Casos alternatius:**

- **Prestatgeria invalida:** Com a mínim, una de les dues posicions de les prestatgeries és invàlida.

### 4.3.10. Buidar prestatgeria

**Nom:** Buidar prestatgeria
**Actor:** Administrador
**Comportament:**

1. L'administrador selecciona una posició de prestatgeria.
2. El sistema pregunta si s'està segur de l'acció que es realitzarà i adverteix de que l'acció és irreversible.
3. Si l'administrador confirma, el sistema buida la prestatgeria.
4. Si l'administrador cancel·la, no es realitza cap canvi.

**Casos alternatius:**

- **Prestatgeria inexistent:** S'ha seleccionat una prestatgeria que no existeix.
- **Prestatgeria sense productes:** El prestatge ja està buit i per tant no es realitza cap canvi.

### 4.4. Gestionar catàleg

**Nom:** Gestionar catàleg
**Actor:** Administrador
**Comportament:**

1. L'administrador podrà triar diferentes opcions per tal de fer els canvis que consideri al catàleg. Les opcions són crear prdoucte, borrar producte, modificar producte, modificar similitud entre productes i buscar productes.

### 4.4.1. Crear producte

**Nom:** Crear producte nou
**Actor:** Administrador
**Comportament:**

1. L'usuari introdueix el nom, la temperatura, el preu, la imatge i les paraules clau.
2. El sistema crea el producte i l'afegeix al catàleg.

**Casos alternatius:**

- **Temperatura invàlida:** La temperatura és invalida
- **Preu invalid:** El preu és menor o igual a zero.
- **Producte existent:** El producte ja existeix.
- **Imatge invàlida:** El format de la imatge no és correcte.

### 4.4.2. Borrar producte

**Nom:** Borrar producte existent
**Actor:** Administrador
**Comportament:**

1. L'usuari selecciona un producte del catàleg per eliminar-lo.
2. S'elimina el producte del catàleg.

**Casos alternatius:**

- **Producte a prestatge:** Si el producte està colocat en algun prestatge llavors el sistema pregunta a l'usurari si està segur de voler eliminar el producte.
  - En cas afirmatiu, el sistema elimina el producte del catàleg i del/s prestatge/s.
  - En cas negatiu, el sistema no elimina el producte ni del catàleg ni del/s prestatge/s.

### 4.4.3. Modificar producte

**Nom:** Modificar producte
**Actor:** Administrador
**Comportament:**

1. L'usuari selecciona la opció d'editar un producte des del catàleg.
2. El sistema mostra els atributs editables del producte (nom, preu, temperatura i paraules clau).
3. L'usuari modifica els atributs desitjats.
4. El sistema actualitza totes les instàncies del producte a prestatges i la del catàleg per satisfer els canvis.

**Casos alternatius:**

- **Nom no únic:** Si el nom del producte ja existeix, es mostra un error i es demana un nou nom.
- **Preu invàlid:** Si el preu és menor o igual a zero, es mostra un error.
- **Temperatura inapropiada:** Si es modifica la temperatura del producte i aquest es trobava en una prestatgeria amb una temperatura incompatible amb la nova, s'adverteix a l'usuari que el producte serà eliminat del prestatge.
  - Si l'usuari confirma, el sistema elimina el producte del prestatge i actualitza la temperatura del producte.
  - Si l'usuari cancel·la, el sistema no realitza cap canvi en la temperatura del producte.

### 4.4.4. Modificar similitud entre productes

**Nom:** Modificar similitud entre productes
**Actor:** Administrador
**Comportament:**

1. L'usuari selecciona la opció d'editar relacions d'un producte del catàleg.
2. El sistema demana a l'usuari que trii el producte del catàleg al qual vol modificar la similitud amb el primer producte.
3. L'usuari tria el segon producte.
4. El sistema demana a l'usuari que introdueixi la nova similitud entre els dos productes.
5. L'usuari introdueix la nova similitud.
6. El sistema actualitza la similitud entre els dos productes a totes les seves instàncies.

**Casos alternatius:**

- **Similitud invàlida:** Si la similitud no és un valor del interval [0, 1) es mostra un error (similitud = 1 reservada per dos productes iguals).

### 4.5. Buscador de productes

**Actor:** Usuari

**Comportament:**

1. L'usuari introdueix informació d'un producte al cercador.
2. El sistema busca el producte internament a les estructures de dades corresponents en base a diferents criteris (nom, paraules clau...).
3. El sistema mostra una llista amb els resultats de la cerca.
4. L'usuari tria el resultat que vulgui.
5. El sistema mostra informació del producte i si es troba en algun/s prestatge/s indica a quin/s.

**Casos alternatius:**

- **Producte no trobat:** Si no es troba cap producte, el sistema mostrarà un missatge indicant que no hi ha cap coincidència.

---
