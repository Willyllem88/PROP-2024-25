module edu.upc.subgrupprop113.supermarketmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.upc.subgrupprop113.supermarketmanager to javafx.fxml;
    exports edu.upc.subgrupprop113.supermarketmanager;
}