# Programación Orientada a Objetos (POO) en Java

## 1. Conceptos básicos de POO

- **Clase**: Modelo que define atributos (estado) y métodos (comportamiento).
  - **Cuándo usarlo**: Se usa siempre que necesitas modelar entidades del mundo real o de un sistema con comportamiento definido, como "Persona", "Animal", "Producto".
- **Objeto**: Instancia de una clase.

  - **Cuándo usarlo**: Cada vez que necesites trabajar con una entidad específica, como un usuario en un sistema, o un ítem en una tienda, instancias del modelo definido por las clases.

- **Encapsulamiento**: Ocultar los detalles internos de una clase, permitiendo solo el acceso controlado mediante métodos públicos.

  - **Cuándo usarlo**: Úsalo para proteger el estado de un objeto de modificaciones externas indebidas, evitando errores y manteniendo la consistencia de los datos.

- **Polimorfismo**: Capacidad de diferentes clases de responder a un mismo mensaje o método de manera específica.

  - **Cuándo usarlo**: Ideal cuando necesitas que diferentes tipos de objetos compartan una interfaz común, pero que respondan de manera distinta, por ejemplo, distintos tipos de "Vehículo" (bicicleta, coche) con un método `mover()`.

- **Herencia**: Permite que una clase (subclase) herede atributos y métodos de otra (superclase).
  - **Cuándo usarlo**: Úsalo cuando una clase nueva comparte gran parte del comportamiento de otra clase existente, pero requiere algunos comportamientos adicionales o diferentes. Ideal para modelar jerarquías (e.g., `Animal` -> `Perro`, `Gato`).

---

## 2. Ventajas de la POO

- **Reutilización de código**: Se facilita el uso de librerías y módulos.
  - **Cuándo usarlo**: Siempre que desees evitar escribir código duplicado y aprovechar funcionalidades ya implementadas.
- **Escalabilidad**: Los programas crecen de manera organizada.

  - **Cuándo usarlo**: Cuando trabajas en proyectos grandes que necesitan expandirse con nuevos módulos o funcionalidades sin cambiar demasiado lo que ya está hecho.

- **Facilidad de mantenimiento**: Modificar el comportamiento de un objeto sin cambiar su interfaz.
  - **Cuándo usarlo**: Es útil cuando esperas que el sistema cambie o evolucione con el tiempo, ya que permite hacer cambios sin afectar otras partes del código.

---

## 3. Ejemplo de POO (Floristería)

### Ejemplo en POO

Supón que el individuo A quiere enviar flores al individuo B. El individuo A interactúa con una floristería, que realiza la tarea sin que A necesite saber cómo lo hace. El objeto `Floristería` se encarga de delegar la entrega de las flores a otra floristería en la ciudad de B.

```java
class Individuo {
    void enviarFlores(Individuo B, Flores x, Floristeria C) {
        Direccion d = B.obtenerDireccion();
        String n = this.obtenerNombre();
        C.enviarFlores(n, x, d);
    }
}

class Floristeria {
    void enviarFlores(String nombre, Flores x, Direccion d) {
        // Enviar las flores a través de algún método
    }
}

class FloristeriaCultivadora extends Floristeria {
    void enviarFlores(String nombre, Flores x, Direccion d) {
        // Lógica para enviar flores cultivadas
    }
}

class FloristeriaNoCultivadora extends Floristeria {
    void enviarFlores(String nombre, Flores x, Direccion d) {
        // Lógica para contactar a un proveedor y enviar las flores
    }
}
```

### **Cuándo usarlo**

Este ejemplo muestra cómo abstraer la complejidad del sistema mediante clases. Usa este enfoque cuando tienes varias responsabilidades y necesitas delegarlas en diferentes objetos (floristerías), lo que simplifica la solución y mejora la reutilización del código.

---

## 4. Pilares de la POO

### Encapsulamiento y Ocultación de la Información

La clase controla el acceso a sus atributos mediante métodos públicos, evitando que usuarios externos modifiquen directamente el estado.

**Ejemplo de Encapsulamiento:**

```java
public class Ejemplo {
    private int estado;

    public Ejemplo(int estado) {
        this.estado = (estado < 0) ? 0 : estado;  // Forzamos estado positivo
    }

    public int getEstado() {
        return this.estado;
    }

    public boolean setEstado(int x) {
        if (x >= 0) {
            this.estado = x;
            return true;
        }
        return false;
    }
}
```

### **Cuándo usarlo**

- Cuando deseas **proteger el estado interno** del objeto y ofrecer una interfaz controlada. Por ejemplo, si tienes un sistema de banca, es crucial que las cuentas bancarias no puedan ser modificadas directamente sin control.

---

## 5. Herencia y Polimorfismo

### Ejemplo de Herencia (ColorPoint extendiendo Point)

```java
public class Point {
    protected int x, y;

    public Point(int xval, int yval) {
        this.x = xval;
        this.y = yval;
    }

    public int getX() {
        return x;
    }

    public void setX(int xval) {
        this.x = xval;
    }

    public void dibujar() {
        // Lógica para dibujar el punto
    }
}

public class ColorPoint extends Point {
    private Color c;

    public ColorPoint(int xval, int yval, Color cval) {
        super(xval, yval);
        this.c = cval;
    }

    public Color getC() {
        return this.c;
    }

    public void setC(Color cval) {
        this.c = cval;
    }

    @Override
    public void setX(int xval) {
        if (condFiltro(this.c)) {
            super.setX(xval);
        }
    }

    private boolean condFiltro(Color c) {
        // Verificación del color
        return true;
    }
}
```

### **Cuándo usarlo**

- Usa la **herencia** cuando dos clases comparten características comunes, pero necesitan alguna diferenciación. En este ejemplo, `ColorPoint` hereda la posición (`x`, `y`) de `Point`, pero añade un color. Es útil cuando las subclases agregan nueva funcionalidad sin duplicar código.

- **Polimorfismo**: Se usa cuando quieres que un método se comporte de manera diferente dependiendo de la subclase que lo implemente.

---

## 6. Clases abstractas e interfaces

### Ejemplo de clase abstracta en Java

```java
public abstract class Poligono {
    abstract Point centro();
    abstract void girar(int grados);

    void dibujar() {
        // Implementación del dibujo de un polígono
    }
}

public class Cuadrado extends Poligono {
    Point centro() {
        // Implementación para un cuadrado
        return new Point(0, 0);
    }

    void girar(int grados) {
        // Lógica para girar el cuadrado
    }
}
```

### **Cuándo usarlo**

- Las **clases abstractas** se usan cuando deseas proporcionar un **comportamiento base común** para varias subclases, pero quieres forzar a las subclases a implementar algunos métodos específicos (como `centro()` y `girar()` en este caso).

---

### Ejemplo de una interface en Java

```java
public interface Girable {
    public Point centro();
    public void girar(int grados);
}

public class Cuadrado implements Girable {
    @Override
    public Point centro() {
        return new Point(0, 0);
    }

    @Override
    public void girar(int grados) {
        // Implementación de giro
    }
}
```

### **Cuándo usarlo**

- Las **interfaces** son útiles cuando quieres definir un **comportamiento común** que puede ser implementado por diferentes clases que no necesariamente están relacionadas. Por ejemplo, tanto un `Cuadrado` como un `Círculo` pueden ser "girables" (`Girable`), pero no necesitan heredar uno del otro.

---

## 7. Clases Genéricas

Las clases genéricas permiten definir clases que trabajen con cualquier tipo de objeto, proporcionando flexibilidad.

### Ejemplo de clase genérica `Box<T>`

```java
public class Box<T> {
    private T t;

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return this.t;
    }
}

Box<Integer> integerBox = new Box<>();
integerBox.set(123);
Integer value = integerBox.get();  // Obtiene 123
```

### **Cuándo usarlo**

- Usa **clases genéricas** cuando deseas trabajar con estructuras de datos

o contenedores que sean **independientes del tipo de dato**. Por ejemplo, una `Box` puede contener cualquier tipo de objeto (Integer, String, etc.), y la clase sigue siendo útil para todos ellos.

---

## 8. Iteradores en Java

Los iteradores permiten recorrer colecciones de datos de manera uniforme.

### Ejemplo de iterador en Java

```java
Vector<Integer> list = new Vector<>();
for (int i = 0; i < 10; i++) {
    list.add(i * i);
}

Iterator<Integer> iter = list.iterator();
while (iter.hasNext()) {
    Integer elem = iter.next();
    System.out.println(elem);
}
```

### Alternativa abreviada con `for-each`

```java
for (Integer elem : list) {
    System.out.println(elem);
}
```

### **Cuándo usarlo**

- Usa **iteradores** cuando necesitas **recorrer una colección** de datos de manera uniforme, sin importar la implementación interna de esa colección (Vector, ArrayList, etc.).

---
