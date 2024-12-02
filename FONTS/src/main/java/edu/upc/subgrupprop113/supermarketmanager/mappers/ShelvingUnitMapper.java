package edu.upc.subgrupprop113.supermarketmanager.mappers;

import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;

import java.util.List;

/**
 * A utility class for mapping `ShelvingUnit` objects to their corresponding `ShelvingUnitDto` representations.
 * <p>
 * This class provides methods to convert individual `ShelvingUnit` instances or lists of `ShelvingUnit` objects
 * into `ShelvingUnitDto` objects, which are suitable for data transfer purposes.
 * </p>
 */
public class ShelvingUnitMapper {

    /**
     * The `ProductMapper` used to convert `Product` objects into `ProductDto` objects.
     */
    private final ProductMapper productMapper;

    /**
     * Constructs a `ShelvingUnitMapper` with the specified `ProductMapper`.
     *
     * @param productMapper the `ProductMapper` instance to be used for converting `Product` objects.
     */
    public ShelvingUnitMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /**
     * Converts a `ShelvingUnit` instance to its corresponding `ShelvingUnitDto` representation.
     *
     * @param unit the `ShelvingUnit` object to be converted.
     * @return a `ShelvingUnitDto` object containing the data from the given `ShelvingUnit`.
     */
    public ShelvingUnitDto toDto(ShelvingUnit unit) {
        ShelvingUnitDto dto = new ShelvingUnitDto();
        dto.setUid(unit.getUid());
        dto.setTemperature(unit.getTemperature().toString());
        dto.setProducts(productMapper.toDto(unit.getProducts()));
        return dto;
    }

    /**
     * Converts a list of `ShelvingUnit` objects to a list of their corresponding `ShelvingUnitDto` representations.
     *
     * @param units the list of `ShelvingUnit` objects to be converted.
     * @return a list of `ShelvingUnitDto` objects containing the data from the given `ShelvingUnit` list.
     */
    public List<ShelvingUnitDto> toDto(List<ShelvingUnit> units) {
        return units.stream().map(this::toDto).toList();
    }
}

