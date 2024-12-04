package edu.upc.subgrupprop113.supermarketmanager.dtos;

public class RelatedProductDto {
    private float value;

    private String product1;

    private String product2;

    public RelatedProductDto() {
    }

    public RelatedProductDto(float value, String product1, String product2) {
        this.value = value;
        this.product1 = product1;
        this.product2 = product2;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getProduct1() {
        return product1;
    }

    public void setProduct1(String product1) {
        this.product1 = product1;
    }

    public String getProduct2() {
        return product2;
    }

    public void setProduct2(String product2) {
        this.product2 = product2;
    }
}
