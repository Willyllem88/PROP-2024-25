package edu.upc.subgrupprop113.supermarketmanager.dtos;

import java.util.List;

public class ShelvingUnitDto {
    private int uid;

    private String temperature;

    private List<ProductDto> products;

    public ShelvingUnitDto() {
    }

    public ShelvingUnitDto(int uid, String temperature, List<ProductDto> products) {
        this.uid = uid;
        this.temperature = temperature;
        this.products = products;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
