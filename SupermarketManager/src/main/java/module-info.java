module edu.upc.subgrupprop113.supermarketmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens edu.upc.subgrupprop113.supermarketmanager to com.fasterxml.jackson.databind, javafx.fxml;
    exports edu.upc.subgrupprop113.supermarketmanager;
}