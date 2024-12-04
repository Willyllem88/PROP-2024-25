package edu.upc.subgrupprop113.supermarketmanager.dtos;

import java.util.ArrayList;
import java.util.List;

public class ProductDto {
    private String name;
    private float price;
    private String temperature;
    private String imgPatg;
    private List<String> keywords;
    private List<RelatedProductDto> relatedProducts;

    public ProductDto() {
    }

    public ProductDto(String name, float price, String temperature, String imgPatg, List<String> keywords, List<RelatedProductDto> relatedProducts) {
        this.name = name;
        this.price = price;
        this.temperature = temperature;
        this.imgPatg = imgPatg;
        this.keywords = keywords;
        this.relatedProducts = relatedProducts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getImgPatg() {
        return imgPatg;
    }

    public void setImgPatg(String imgPatg) {
        this.imgPatg = imgPatg;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<RelatedProductDto> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<RelatedProductDto> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }
}
