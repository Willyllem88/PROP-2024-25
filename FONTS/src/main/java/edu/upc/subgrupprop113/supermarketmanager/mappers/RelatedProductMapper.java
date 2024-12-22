package edu.upc.subgrupprop113.supermarketmanager.mappers;

import edu.upc.subgrupprop113.supermarketmanager.dtos.RelatedProductDto;
import edu.upc.subgrupprop113.supermarketmanager.models.RelatedProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class responsible for transforming between {@link RelatedProduct} entities and {@link RelatedProductDto} objects.
 * <p>
 * This class provides utility methods for converting single `RelatedProduct` entities or lists of entities
 * to their corresponding DTO representations.
 * </p>
 */
public class RelatedProductMapper {

    /**
     * Converts a single {@link RelatedProduct} entity to a {@link RelatedProductDto}.
     * <p>
     * If the entity is null, this method returns null. It maps the names of the related products to the DTO
     * if the products themselves are not null.
     * </p>
     *
     * @param relatedProduct the {@link RelatedProduct} entity to convert.
     * @return a {@link RelatedProductDto} representing the entity, or null if the input is null.
     */
    public RelatedProductDto toDto(RelatedProduct relatedProduct) {
        if (relatedProduct == null) return null;
        RelatedProductDto dto = new RelatedProductDto();
        if (relatedProduct.getProduct1() != null) {
            dto.setProduct1(relatedProduct.getProduct1().getName());
        } else {
            dto.setProduct1(null);
        }
        if (relatedProduct.getProduct2() != null) {
            dto.setProduct2(relatedProduct.getProduct2().getName());
        } else {
            dto.setProduct2(null);
        }
        if (relatedProduct.getValue() >= 0.0f && relatedProduct.getValue() <= 1.0f) {
            dto.setValue(relatedProduct.getValue());
        } else {
            dto.setValue(0.0f);
        }
        return dto;
    }

    /**
     * Converts a list of {@link RelatedProduct} entities to a list of {@link RelatedProductDto} objects.
     * <p>
     * If the list is null, this method returns null. Each entity in the list is converted using
     * the {@link #toDto(RelatedProduct)} method.
     * </p>
     *
     * @param relatedProducts the list of {@link RelatedProduct} entities to convert.
     * @return a list of {@link RelatedProductDto} objects, or null if the input list is null.
     */
    public List<RelatedProductDto> toDto(List<RelatedProduct> relatedProducts) {
        if (relatedProducts == null) return null;
        List<RelatedProductDto> dtos = new ArrayList<>(relatedProducts.size());
        for (RelatedProduct relatedProduct : relatedProducts) {
            dtos.add(toDto(relatedProduct));
        }
        return dtos;
    }
}

