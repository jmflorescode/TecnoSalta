# TecnoSalta

Tienda de componentes de PC hecha en Java con JavaFX. Proyecto de Lenguajes II (cuarto semestre).

## Contenido

| Archivo | Qué es |
| --- | --- |
| [Informe_TecnoSalta.pdf](Informe_TecnoSalta.pdf) | Informe del proyecto: diseño, planificación y desarrollo por etapas |
| [Presentacion_TecnoSalta.pdf](Presentacion_TecnoSalta.pdf) | Presentación para la exposición |
| [ejecutable.zip](ejecutable.zip) | El programa listo para usar en Windows |
| [src](src) | Código fuente en Java |

## Cómo abrir el programa

1. Descargar [ejecutable.zip](ejecutable.zip) (botón de descarga arriba a la derecha de la página del archivo).
2. Descomprimirlo.
3. Entrar a la carpeta `ejecutable\TecnoSalta` y hacer doble clic en `TecnoSalta.exe`.

Funciona en cualquier Windows de 64 bits, aunque no tenga Java instalado, porque el programa trae su propio Java.

## Usuarios de prueba

| Rol | Email | Contraseña |
| --- | --- | --- |
| Administrador | admin@tecnosalta.com | admin123 |
| Cliente | cliente@tecnosalta.com | cliente123 |

## Organización del código

```text
src/main/java/com/tienda/
├── App.java          arranca JavaFX y abre el login
├── modelo/           Usuario, Producto, Categoria, Carrito, Pedido…
├── repositorio/      guarda y lee los datos (archivo tienda.dat)
├── servicio/         reglas del negocio: login, catálogo, carrito y compra
├── controlador/      un controlador por pantalla
└── util/             navegación, alertas y formatos
src/main/resources/com/tienda/
├── vistas/           un archivo FXML por pantalla
└── css/tienda.css
src/test/java/        pruebas JUnit del carrito y de la compra
```
