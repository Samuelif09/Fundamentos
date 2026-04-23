# 🧮 Arquitectura: Calculadora SoLiD

Este proyecto es una aplicación de consola en Java 21, estructurada paso a paso utilizando **Test-Driven Development (TDD)** y **Domain-Driven Design (DDD)** para cumplir estrictamente con todos los principios **SOLID**.

## 🛠️ Mapa de Responsabilidades (Componentes)

A continuación, se detalla la función única de cada archivo en nuestro ecosistema para certificar la Separación de Preocupaciones (Separation of Concerns).

### 🧩 1. Micro-Interfaces (Principio ISP: Lógica Segregada)
Separamos acciones conceptuales tan diminutas que nadie estará forzado a firmar contratos que no debe.
*   `EjecutableBinario.java`: Su única responsabilidad es procesar matemáticamente dos números y retornar un resultado.
*   `EjecutableUnario.java`: Idéntico al anterior, pero especializado en recibir un solo número para procesarse.
*   `ValidableBinario.java` / `ValidableUnario.java`: Su única función es comprobar (sin ejecutar) si ciertos números están permitidos antes de hacer cálculos peligrosos (como las raíces).

### 🏗️ 2. Contratos Base (Definiendo OCP y LSP)
*   `OperacionBinaria.java`: Es la clase base abstracta de dos valores. Permite que el sistema quede "Abierto" a que agreguemos multiplicaciones mañanas sin alterar el historial.
*   `OperacionUnaria.java`: Es la clase base abstracta de un valor. Establece el **Principio de Liskov** asegurando que toda subclase avise sus validaciones antes de explotar sorpresivamente.

### 📐 3. Dominio Aritmético (Principio SRP: Única Responsabilidad)
Clases funcionales ciegas al usuario. Solo saben seguir la física del mundo real:
*   `Suma.java`: Solo suma matemáticamente dos enteros.
*   `Resta.java`: Solo resta matemáticamente dos enteros.
*   `RaizCuadrada.java`: Define activamente en su regla que solo aceptará números `>= 0`, para posteriormente resolver raíces.
*   `LogaritmoNatural.java`: Define activamente en su regla que solo aceptará números `> 0`, para posteriormente resolver su matemática.

### ⚙️ 4. Servicios y Presentación (El Motor del Backend)
*   **`MotorOperaciones.java`**: Actúa como el **Gran Orquestador**. Guarda todas las operaciones en un catálogo (Registro en memoria) y se encarga de interceptar tu opción (Ej. Opción 1) redirigiendo el tráfico sin usar sentencias `switch/if` largas y complejas. Conecta tu teclado con la matemática de fondo verificando que no cometas un error.
*   **`Menu.java`**: La cara visual. Es la **Capa de Presentación**. Es complemente "ignorante", no sabe qué es un número ni cómo procesarlo, simplemente recibe variables de texto e inyecta menús amigables a la terminal u otras salidas gráficas futuras.

### 🧪 5. Casos de Pruebas Unitarias (TDD)
*   **`OperacionesTest.java`**: Los guardianes matemáticos. Verifican los resultados exactos (10 + 5 = 15.0) y evalúan que los contratos se cumplan en situaciones extremas (números negativos).
*   **`MotorOperacionesTest.java`**: Garantiza la estabilidad de todo el flujo de ruteo y rechaza amablemente situaciones como selecciones de menú aleatorias o flujos erróneos.
*   **`MenuTest.java`**: Verifica estéticamente que todo el código del consola imprima correctamente al atrapar sus bytes.
