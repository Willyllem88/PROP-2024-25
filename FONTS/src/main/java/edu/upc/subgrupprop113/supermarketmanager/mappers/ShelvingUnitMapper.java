package edu.upc.subgrupprop113.supermarketmanager.mappers;

import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;

import java.util.ArrayList;
import java.util.List;

public class ShelvingUnitMapper {
    private final ProductMapper productMapper;

    public ShelvingUnitMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ShelvingUnitDto toDto(final ShelvingUnit shelvingUnit) {
        ShelvingUnitDto dto = new ShelvingUnitDto();
        dto.setUid(shelvingUnit.getUid());
        dto.setTemperature(shelvingUnit.getTemperature().toString());
        dto.setProducts(productMapper.toDto(shelvingUnit.getProducts()));
        return dto;
    }

    public List<ShelvingUnitDto> toDto(final List<ShelvingUnit> shelvingUnits) {
        if (shelvingUnits == null) return null;
        List<ShelvingUnitDto> dtos = new ArrayList<>(shelvingUnits.size());
        for (ShelvingUnit shelvingUnit : shelvingUnits) {
            dtos.add(toDto(shelvingUnit));
        }
        return dtos;
    }
}
