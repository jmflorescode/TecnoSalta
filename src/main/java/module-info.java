module com.tienda {
    // Librerías que usa el proyecto
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires jbcrypt;

    // JavaFX necesita ver la clase App para arrancarla
    exports com.tienda;

    // FXML crea los controladores por reflexión: hay que abrirle el paquete
    opens com.tienda.controlador to javafx.fxml;
}
