package edu.upc.subgrupprop113.supermarketmanager.mappers;

import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.models.Product;

import java.util.List;
/**
 * A utility class for mapping `Product` objects to their corresponding `ProductDto` representations.
 * <p>
 * This class provides methods to convert individual `Product` instances or lists of `Product` objects
 * into `ProductDto` objects, which are suitable for data transfer purposes.
 * </p>
 */
public class ProductMapper {

    /**
     * Converts a `Product` instance to its corresponding `ProductDto` representation.
     *
     * @param product the `Product` object to be converted.
     * @return a `ProductDto` object containing the data from the given `Product`.
     */
    public ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setTemperature(product.getTemperature().toString());
        dto.setImgPath(product.getImgPath());
        dto.setKeyWords(product.getKeyWords());
        return dto;
    }

    /**
     * Converts a list of `Product` objects to a list of their corresponding `ProductDto` representations.
     *
     * @param products the list of `Product` objects to be converted.
     * @return a list of `ProductDto` objects containing the data from the given `Product` list.
     */
    public List<ProductDto> toDto(List<Product> products) {
        return products.stream().map(this::toDto).toList();
    }
}

