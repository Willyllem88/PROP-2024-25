module edu.upc.subgrupprop113.supermarketmanager {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;
    requires java.desktop;

    opens edu.upc.subgrupprop113.supermarketmanager to com.fasterxml.jackson.databind, javafx.fxml;
    exports edu.upc.subgrupprop113.supermarketmanager;
    exports edu.upc.subgrupprop113.supermarketmanager.controllers;
    opens edu.upc.subgrupprop113.supermarketmanager.controllers to com.fasterxml.jackson.databind, javafx.fxml;
    exports edu.upc.subgrupprop113.supermarketmanager.controllers.components;
    opens edu.upc.subgrupprop113.supermarketmanager.controllers.components to com.fasterxml.jackson.databind, javafx.fxml;
    exports edu.upc.subgrupprop113.supermarketmanager.dtos;
    opens edu.upc.subgrupprop113.supermarketmanager.dtos to com.fasterxml.jackson.databind, javafx.fxml;
    exports edu.upc.subgrupprop113.supermarketmanager.factories;
    opens edu.upc.subgrupprop113.supermarketmanager.factories to com.fasterxml.jackson.databind, javafx.fxml;
    exports edu.upc.subgrupprop113.supermarketmanager.mappers;
    opens edu.upc.subgrupprop113.supermarketmanager.mappers to com.fasterxml.jackson.databind, javafx.fxml;
    exports edu.upc.subgrupprop113.supermarketmanager.models;
    opens edu.upc.subgrupprop113.supermarketmanager.models to com.fasterxml.jackson.databind, javafx.fxml;
    exports edu.upc.subgrupprop113.supermarketmanager.services;
    opens edu.upc.subgrupprop113.supermarketmanager.services to com.fasterxml.jackson.databind, javafx.fxml;
    exports edu.upc.subgrupprop113.supermarketmanager.utils;
    opens edu.upc.subgrupprop113.supermarketmanager.utils to com.fasterxml.jackson.databind, javafx.fxml;
}