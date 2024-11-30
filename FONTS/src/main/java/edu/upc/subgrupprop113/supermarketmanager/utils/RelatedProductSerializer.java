package edu.upc.subgrupprop113.supermarketmanager.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import edu.upc.subgrupprop113.supermarketmanager.models.RelatedProduct;

import java.io.IOException;

/**
 * Custom serializer for the {@link RelatedProduct} class.
 * This serializer is used to control the JSON representation of a RelatedProduct object.
 *
 * <p>The custom serialization ensures that the output JSON for each RelatedProduct instance
 * includes only the fields: {@code value}, {@code product1}, and {@code product2},
 * simplifying the structure and removing nested product details.</p>
 */
public class RelatedProductSerializer extends JsonSerializer<RelatedProduct> {
    @Override
    public void serialize(RelatedProduct relatedProduct, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("value", relatedProduct.getValue());
        gen.writeStringField("product1", relatedProduct.getProduct1().getName());
        gen.writeStringField("product2", relatedProduct.getProduct2().getName());
        gen.writeEndObject();
    }
}