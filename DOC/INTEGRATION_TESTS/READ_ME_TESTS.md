# Descripció dels Tests

A continuació, es descriu breument l'objectiu de cada un dels tests implementats:

---

## **Tests d'Algoritmes**

### 1. **TEST_APPROXIMATION_SUPERMARKET**
Test per veure com es comporta l'algorisme amb una determinada distribució del supermercat.

### 2. **TEST_BRUTEFORCE_SUPERMARKET**
Test per veure com es comporta l'algorisme amb una determinada distribució del supermercat.

### 3. **TEST_GREEDY_SUPERMARKET**
Test per veure com es comporta l'algorisme amb una determinada distribució del supermercat.

---

## **Tests del Catàleg**

### 4. **TEST_CATALOG**
Es un test on no hi ha cap error i es pot observar el correcte funcionament de les funcions relacionades amb el cataleg.

### 5. **TEST_FAIL_COMMANDS_CATALOG**
Avalua com el catàleg gestiona ordres no vàlides o malformades, garantint robustesa davant errors dels usuaris.

### 6. **TEST_FAIL_PRODUCTS_CATALOG**
Prova de diferents errors en la creació de productes com temperatures inexistents, posar mes paràmetres que els indicats etc.

### 7. **TEST_FAIL_RELATIONS_CATALOG**
Test per veure errors en les relacions entre productes, com que falti alguna per especificar o que hi hagin que no estiguin entre 0 i 1.

### 8. **TEST_FAIL_USER_CATALOG**
Prova com el catàleg manega errors relacionats amb els usuaris, com ara accessos no autoritzats o comandes sense estar cap usuari connectat.

---

## **Tests del Supermercat**

### 9. **TEST_SUPERMARKET**
Es un test on no hi ha cap error i es pot observar el correcte funcionament de les funcions relacionades amb el supermercat.

### 10. **TEST_FAIL_INDEX_SUPERMARKET**
Simula errors relacionats amb índexs fora de rang, tant en les posicions o alçades.

### 11. **TEST_FAIL_NEG_INDEX_SUPERMARKET**
Es veuen errors d'utilitzar indexs negatius a diferents funcions.

### 12. **TEST_FAIL_PRODUCTS_SUPERMARKET**
Es veuen errors per diferents productes invalids.

### 13. **TEST_FAIL_TEMPERATURE_SUPERMARKET**
Simula errors en la gestió de temperatures al supermercat, veient que alguns canvis de temperatura provoquen errors si els fas.

---
