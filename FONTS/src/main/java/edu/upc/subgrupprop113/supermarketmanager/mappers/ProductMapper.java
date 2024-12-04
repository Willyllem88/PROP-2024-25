package edu.upc.subgrupprop113.supermarketmanager.mappers;

import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ProductTemperature;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    private static final String INVALID_TEMPERATURE_ERROR = "Shelving units with invalid temperature.";

    private final RelatedProductMapper relatedProductMapper;

    public ProductMapper(RelatedProductMapper relatedProductMapper) {
        this.relatedProductMapper = relatedProductMapper;
    }

    public void toEntity(final Product product, final ProductDto productDto) {
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        try {
            product.setTemperature(ProductTemperature.valueOf(productDto.getTemperature()));
        }
        catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(INVALID_TEMPERATURE_ERROR);
        }
        product.setImgPath(productDto.getImgPath());
        product.setKeyWords(productDto.getKeywords());
    }

    public ProductDto toDto(final Product product) {
        ProductDto dto = new ProductDto();
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setImgPath(product.getImgPath());
        dto.setRelatedProducts(relatedProductMapper.toDto(product.getRelatedProducts()));
        return dto;
    }

    public List<ProductDto> toDto(final List<Product> products) {
        if (products == null) return null;
        List<ProductDto> dtos = new ArrayList<>(products.size());
        for (Product product : products) {
            dtos.add(toDto(product));
        }
        return dtos;
    }
}
