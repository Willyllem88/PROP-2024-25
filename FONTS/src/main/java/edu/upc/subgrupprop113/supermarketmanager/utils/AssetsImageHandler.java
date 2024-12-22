package edu.upc.subgrupprop113.supermarketmanager.utils;

import edu.upc.subgrupprop113.supermarketmanager.Main;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;


public class AssetsImageHandler {
    public static final String ASSETS_PRODUCTS_PATH = "assets/productImgs";
    public static final String ASSETS_TEMPERATURES_PATH = "assets/temperatureIcons";
    private static final String ASSETS_NOT_FOUND = "Assets path not found!";
    private static final String PATH_PREFIX = "file:";
    
    private AssetsImageHandler() {
        //This is intentional
    }

    public static String setAbsoluteImgPath(String imgName) {
        return PATH_PREFIX + getDefaultDirectoryImagesPath().resolve(imgName).toAbsolutePath();
    }

    public static String getImageName(String sourcePath) {
        Path source;
        if (sourcePath.startsWith("file:")) {
            source = Paths.get(sourcePath.substring(5)); // Elimina el prefijo "file:"
        } else {
            source = Paths.get(sourcePath);
        }

        return source.getFileName().toString();
    }

    /**
     * Retrieves the default directory path for image assets.
     * <p>
     * This method attempts to locate and resolve the path to the assets directory where image files are stored. If the path
     * cannot be resolved, an exception is thrown.
     * </p>
     *
     * @return the {@code Path} object representing the default directory for image assets.
     * @throws IllegalStateException if the assets path cannot be resolved or is not found.
     */
    public static Path getDefaultDirectoryImagesPath() {
        try {
            return Paths.get(Objects.requireNonNull(Main.class.getResource(ASSETS_PRODUCTS_PATH)).toURI());
        } catch (Exception e) {
            throw new IllegalStateException(ASSETS_NOT_FOUND);
        }
    }


    /**
     * Retrieves the default directory path for temperature icon assets.
     * <p>
     * This method attempts to locate and resolve the path to the assets directory where image files are stored. If the path
     * cannot be resolved, an exception is thrown.
     * </p>
     *
     * @return the {@code Path} object representing the default directory for image assets.
     * @throws IllegalStateException if the assets path cannot be resolved or is not found.
     */
    public static Path getDefaultDirectoryTemperaturesPath() {
        try {
            return Paths.get(Objects.requireNonNull(Main.class.getResource(ASSETS_TEMPERATURES_PATH)).toURI());
        } catch (Exception e) {
            throw new IllegalStateException(ASSETS_NOT_FOUND);
        }
    }

    /**
     * Copies a PNG file to assets.
     *
     * @param sourcePath the absolute path of the source PNG file
     * @return the absolute path of the new image in assets.
     * @throws IllegalArgumentException if the source file does not exist or is not a PNG file
     */
    public static String saveNewImageToAssets(String sourcePath) {
        // Validate source file
        Path source;
        if (sourcePath.startsWith("file:")) {
            source = Paths.get(sourcePath.substring(5)); // Elimina el prefijo "file:"
        } else {
            source = Paths.get(sourcePath);
        }

        if (!Files.exists(source)) {
            throw new IllegalArgumentException("Source file does not exist.");
        }
        if (!sourcePath.endsWith(".png")) {
            throw new IllegalArgumentException("Source file is not a PNG file.");
        }

        String fileName = source.getFileName().toString();
        String extension = "";
        String baseName = "";
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            extension = fileName.substring(dotIndex);
        }
        if (dotIndex > 0) { // Ensure there's a dot in the file name
            baseName = fileName.substring(0, dotIndex);
        }
        String newFileName =  baseName + '-' + UUID.randomUUID() + extension;

        // Create the destination path with the new name
        Path destinationDir = getDefaultDirectoryImagesPath();
        Path destination = destinationDir.resolve(newFileName);

        // Copy the file
        try {
            Files.copy(source, destination);
        }
        catch (IOException e) {
            throw new IllegalStateException("Failed to copy image to assets folder");
        }
        return PATH_PREFIX + destination;
    }

    /**
     * Deletes an image file from the assets directory if the file resides within the default directory for images.
     * <p>
     * This method checks if the specified image file path is located within the assets directory. If the file exists
     * and is deletable, it removes the file. Otherwise, it throws an appropriate exception.
     * </p>
     *
     * @param imgPath the absolute path to the image file to be deleted.
     * @throws IllegalArgumentException if the file cannot be located.
     * @throws IllegalStateException if the file deletion operation fails (e.g., the file does not exist or is in use).
     */
    public static void deleteAssetsImage(String imgPath) {
        Path source;
        try {
            URI uri = new URI(imgPath);
            source = Paths.get(uri);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid image path.");
        }

        if (source.getParent().toString().equals(getDefaultDirectoryImagesPath().toString())) {
            try {
                File file = new File(source.toString());
                if (!file.delete())
                    throw new IllegalStateException("Failed to delete the file. File may not exist or be in use.");
            } catch (Exception e) {
                throw new IllegalArgumentException("Error locating the file.");
            }
        }

    }

    /**
     * Retrieves the absolute path to the "AMBIENT.png" icon, which represents ambient temperature storage.
     *
     * @return The absolute path as a {@code String}.
     *         This path is composed of the default temperature directory and the file name "AMBIENT.png".
     */
    public static String getAmbientIconPath() {
        Path iconPath = getDefaultDirectoryTemperaturesPath().resolve("AMBIENT.png");
        return PATH_PREFIX + iconPath.toAbsolutePath().toString();
    }

    /**
     * Retrieves the absolute path to the "REFRIGERATED.png" icon, which represents refrigerated storage.
     *
     * @return The absolute path as a {@code String}.
     *         This path is composed of the default temperature directory and the file name "REFRIGERATED.png".
     */
    public static String getRefrigeratedIconPath() {
        Path iconPath = getDefaultDirectoryTemperaturesPath().resolve("REFRIGERATED.png");
        return PATH_PREFIX + iconPath.toAbsolutePath().toString();
    }

    /**
     * Retrieves the absolute path to the "FROZEN.png" icon, which represents frozen storage.
     *
     * @return The absolute path as a {@code String}.
     *         This path is composed of the default temperature directory and the file name "FROZEN.png".
     */
    public static String getFrozenIconPath() {
        Path iconPath = getDefaultDirectoryTemperaturesPath().resolve("FROZEN.png");
        return PATH_PREFIX + iconPath.toAbsolutePath().toString();
    }

    public static String getLogoIconPath() {
        try {
            Path path = Paths.get(Main.class.getResource("assets/logo.png").toURI());
            return PATH_PREFIX + path.toAbsolutePath();
        } catch (Exception e) {
            throw new IllegalStateException(ASSETS_NOT_FOUND, e);
        }
    }

    /**
     * Retrieves the absolute path to the default error image.
     * <p>
     * This image is used as a fallback when a temperature-related icon or other expected image is not found.
     * The method constructs the path using the assets directory for temperature images and appends the
     * "assets/error-img.png" file name.
     * </p>
     *
     * @return The absolute path to the error image as a {@code String}.
     * @throws IllegalStateException if the assets directory path cannot be resolved.
     */
    public static String getErrorImage() {
        try {
            Path path = Paths.get(Main.class.getResource("assets/error-img.png").toURI());
            return PATH_PREFIX + path.toAbsolutePath();
        } catch (Exception e) {
            throw new IllegalStateException(ASSETS_NOT_FOUND, e);
        }
    }
}
