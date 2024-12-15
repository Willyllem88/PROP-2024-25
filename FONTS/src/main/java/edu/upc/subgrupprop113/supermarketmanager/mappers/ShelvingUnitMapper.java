package edu.upc.subgrupprop113.supermarketmanager.mappers;

import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for converting between {@link ShelvingUnit} entities and {@link ShelvingUnitDto} objects.
 */
public class ShelvingUnitMapper {

    /**
     * Mapper for handling transformations between {@link Product} entities and {@link ProductDto} objects.
     */
    private final ProductMapper productMapper;

    /**
     * Constructs a {@code ShelvingUnitMapper} with the specified {@link ProductMapper}.
     *
     * @param productMapper the mapper used to transform products contained in shelving units.
     */
    public ShelvingUnitMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /**
     * Converts a {@link ShelvingUnit} entity to a {@link ShelvingUnitDto}.
     *
     * @param shelvingUnit the {@link ShelvingUnit} entity to convert.
     * @return the corresponding {@link ShelvingUnitDto}.
     */
    public ShelvingUnitDto toDto(final ShelvingUnit shelvingUnit) {
        ShelvingUnitDto dto = new ShelvingUnitDto();
        dto.setUid(shelvingUnit.getUid());
        dto.setTemperature(shelvingUnit.getTemperature().toString());
        dto.setProducts(productMapper.toDto(shelvingUnit.getProducts()));
        return dto;
    }

    /**
     * Converts a list of {@link ShelvingUnit} entities to a list of {@link ShelvingUnitDto} objects.
     *
     * @param shelvingUnits the list of {@link ShelvingUnit} entities to convert.
     * @return a list of {@link ShelvingUnitDto} objects, or {@code null} if the input list is {@code null}.
     */
    public List<ShelvingUnitDto> toDto(final List<ShelvingUnit> shelvingUnits) {
        if (shelvingUnits == null) return new ArrayList<>();
        List<ShelvingUnitDto> dtos = new ArrayList<>(shelvingUnits.size());
        for (ShelvingUnit shelvingUnit : shelvingUnits) {
            dtos.add(toDto(shelvingUnit));
        }
        return dtos;
    }
}