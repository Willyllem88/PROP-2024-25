# Image Handling in the Application

## Directory Structure
- All images are stored in the **`assets/imgsPaths`** directory, located relative to the `Main.class` file.
- This directory acts as the centralized location for all image assets used in the application.

## Image Handling by Layers

### **Domain and Data Layers**
- Images are referenced and managed only by their **filename** in these layers.
- This simplifies the handling and storage of image-related data within the application's backend logic.

### **Mapping to DTOs**
- During the mapping process to DTOs, the application generates the **absolute path** for each image.
- This ensures that the front-end receives a fully resolvable path to the image, removing the need for additional handling or path resolution on the front-end side.
## Summary
- **Product Images are stored** in `assets/imgsPaths` relative to `Main.class`.
- **Backend uses filenames**, and the absolute path is generated only when mapping to DTOs.

### Example Usage
```
    //For product images
    Image img = new Image(productDto.getImagePath());
    
    //For temperature images
    Image img = new Image(domainController.getTemperatureIcon(shelvingUnitDto.getTemperature()));
    
    //For error images
    new Image(domainController.getErrorImage());
```