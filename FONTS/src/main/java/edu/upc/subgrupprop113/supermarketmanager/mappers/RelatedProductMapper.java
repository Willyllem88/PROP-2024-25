package edu.upc.subgrupprop113.supermarketmanager.mappers;

import edu.upc.subgrupprop113.supermarketmanager.dtos.RelatedProductDto;
import edu.upc.subgrupprop113.supermarketmanager.models.RelatedProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class RelatedProductMapper {
    RelatedProductDto toDto(RelatedProduct relatedProduct) {
        if (relatedProduct == null) return null;
        RelatedProductDto dto = new RelatedProductDto();
        if (relatedProduct.getProduct1() != null) {
            dto.setProduct1(relatedProduct.getProduct1().getName());
        }
        else dto.setProduct1(null);
        if (relatedProduct.getProduct2() != null) {
            dto.setProduct2(relatedProduct.getProduct2().getName());
        }
        else dto.setProduct2(null);
        return dto;
    }

    List<RelatedProductDto> toDto(List<RelatedProduct> relatedProducts) {
        if (relatedProducts == null) return null;
        List<RelatedProductDto> dtos = new ArrayList<>(relatedProducts.size());
        for (RelatedProduct relatedProduct : relatedProducts) {
            dtos.add(toDto(relatedProduct));
        }
        return dtos;
    }
}
