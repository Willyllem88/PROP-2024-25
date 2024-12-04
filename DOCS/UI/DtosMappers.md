# Documentation for DTOs and Mappers

## 1. **DTOs (Data Transfer Objects)**

DTOs were created to facilitate data transfer between the different layers of the application (Presentation, Business, and Data). These objects ensure that the presentation layer does not directly depend on domain-specific classes such as ShelvingUnits or Products.

### Necessity of DTOs
DTOs are necessary in the app’s architecture because they decouple the **presentation layer** from the **domain layer**. This is part of the **three-layer design** (Presentation, Business, and Data), where the **presentation layer** should not be directly coupled to domain-specific classes. Instead, the presentation layer communicates with simplified data representations (DTOs) to ensure separation of concerns, flexibility, and easier testing.

## 2. **Mappers**

Mappers are responsible for transforming data between domain objects and DTOs. They allow us to parse data from one form to another, ensuring the correct data representation for each layer.

### 2.1 **ProductMapper**
- Converts **Product** objects to **ProductDto** objects and vice versa.
- Contains the method `toEntity`, which should only be used for updating an existing **Product** (i.e., it maps data from a **ProductDto** to a **Product** object). This method is **not used for creating new products**.
- The `toDto` method converts a **Product** object to a **ProductDto**, which is used in the presentation layer.

### 2.2 **ShelvingUnitMapper**
- Converts **ShelvingUnit** objects to **ShelvingUnitDto** objects and vice versa.
- It uses the **ProductMapper** to map the list of products within a shelving unit.
- The `toDto` method converts a **ShelvingUnit** object to a **ShelvingUnitDto**, which is used in the presentation layer.

### 2.3 **RelatedProductMapper**
- Converts **RelatedProduct** objects to **RelatedProductDto** objects.
- The `toDto` method maps **RelatedProduct** objects, keeping only the names of the related products, avoiding cyclic mappings that could occur when directly mapping the entire product objects.

### 2.4 **Necessity of Mappers**
Mappers are required to decouple the data between different layers. They handle the conversion between the domain models (e.g., **Product**, **ShelvingUnit**) and the DTOs used for data transfer. This separation of concerns is essential in a three-layered architecture. Mappers also help ensure that the **presentation layer** does not need to be aware of the internal structure of domain models, providing flexibility and maintainability.

## 3. **Methods Created in the Domain Controller**

The following methods were added or modified to the **Domain Controller** to manage access to DTOs and update data:

### 3.1 **ShelvingUnitDto getShelvingUnit(int position)**
- Retrieves a specific **ShelvingUnitDto** by its position (index) in the list of shelving units.

### 3.2 **List<ShelvingUnitDto> getShelvingUnits()**
- Retrieves a list of all **ShelvingUnitDto** objects.

### 3.3 **ProductDto getProduct(String productName)**
- Retrieves a **ProductDto** based on the product name.

### 3.4 **List<ProductDto> getProducts()**
- Retrieves a list of all **ProductDto** objects.

### 3.5 **createProduct(ProductDto productDto)**
- Creates a new **Product** based on the provided **ProductDto**.

### 3.6 **modifyProduct(ProductDto productDto)**
- Updates an existing **Product** with the data from the provided **ProductDto**.

### 3.7 **modifyProductRelation(RelatedProductDto relatedProductDto)**
- Updates the relation between two products using the provided **RelatedProductDto**.

These methods allow the **presentation layer** to interact with the business logic via DTOs, ensuring that the business layer remains decoupled from the user interface. The domain controller acts as an intermediary, ensuring the proper flow of data between the different layers of the application.
