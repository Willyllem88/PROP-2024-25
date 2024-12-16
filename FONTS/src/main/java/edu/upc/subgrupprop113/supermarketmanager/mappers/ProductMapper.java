package edu.upc.subgrupprop113.supermarketmanager.mappers;

import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ProductTemperature;

import java.util.ArrayList;
import java.util.List;

import static edu.upc.subgrupprop113.supermarketmanager.utils.AssetsImageHandler.getImageName;
import static edu.upc.subgrupprop113.supermarketmanager.utils.AssetsImageHandler.setAbsoluteImgPath;

/**
 * Mapper class responsible for transforming between {@link Product} entities and {@link ProductDto} objects.
 * <p>
 * This class handles bidirectional mappings, allowing conversion from entity to DTO and updating existing entities
 * based on DTO data. Additionally, it manages related product mappings through the use of a `RelatedProductMapper`.
 * </p>
 */
public class ProductMapper {

    private static final String INVALID_TEMPERATURE_ERROR = "Shelving units with invalid temperature.";

    /**
     * Mapper for handling transformations between related product entities and their DTOs.
     */
    private final RelatedProductMapper relatedProductMapper;

    /**
     * Constructs a `ProductMapper` with the specified `RelatedProductMapper`.
     *
     * @param relatedProductMapper the mapper used for related products.
     */
    public ProductMapper(RelatedProductMapper relatedProductMapper) {
        this.relatedProductMapper = relatedProductMapper;
    }

    /**
     * Updates an existing `Product` entity with values from a `ProductDto`.
     * <p>
     * This method does not create a new entity but modifies the given entity to reflect the values in the DTO.
     * </p>
     *
     * @param product    the `Product` entity to update.
     * @param productDto the `ProductDto` containing the new values.
     * @throws IllegalArgumentException if the temperature in the DTO is invalid.
     */
    public void toEntity(final Product product, final ProductDto productDto) {
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        try {
            product.setTemperature(ProductTemperature.valueOf(productDto.getTemperature()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(INVALID_TEMPERATURE_ERROR);
        }
        product.setImgPath(getImageName(productDto.getImgPath()));
        product.setKeyWords(productDto.getKeywords());
    }

    /**
     * Converts a `Product` entity to a `ProductDto`.
     *
     * @param product the `Product` entity to convert.
     * @return a `ProductDto` representing the entity.
     */
    public ProductDto toDto(final Product product) {
        if(product == null) return null;

        ProductDto dto = new ProductDto();
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setImgPath(setAbsoluteImgPath(product.getImgPath()));
        dto.setTemperature(product.getTemperature().name());
        dto.setKeywords(new ArrayList<>(product.getKeyWords()));
        dto.setRelatedProducts(relatedProductMapper.toDto(product.getRelatedProducts()));
        return dto;
    }

    /**
     * Converts a list of `Product` entities to a list of `ProductDto` objects.
     *
     * @param products the list of `Product` entities to convert.
     * @return a list of `ProductDto` objects, or `null` if the input list is null.
     */
    public List<ProductDto> toDto(final List<Product> products) {
        if (products == null) return new ArrayList<>();
        List<ProductDto> dtos = new ArrayList<>(products.size());
        for (Product product : products) {
            dtos.add(toDto(product));
        }
        return dtos;
    }
}

