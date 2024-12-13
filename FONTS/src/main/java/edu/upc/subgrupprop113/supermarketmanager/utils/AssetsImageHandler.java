package edu.upc.subgrupprop113.supermarketmanager.utils;

import edu.upc.subgrupprop113.supermarketmanager.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


public class AssetsImageHandler {
    public static final String ASSETS_PATH = "assets/productImgs";

    private AssetsImageHandler() {
        //This is intentional
    }

    public static String setAbsoluteImgPath(String imgName) {
        return getDefaultDirectoryImagesPath().toAbsolutePath().toString() + '/' + imgName;
    }

    public static String getImageName(String sourcePath) {
        Path source = Paths.get(sourcePath);
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
            return Paths.get(Main.class.getResource(ASSETS_PATH).toURI());
        } catch (Exception e) {
            throw new IllegalStateException("Assets path not found!");
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
        Path source = Paths.get(sourcePath);
        if (!Files.exists(source)) {
            throw new IllegalArgumentException("Source file does not exist.");
        }
        if (!source.toString().endsWith(".png")) {
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
        return destination.toString();
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
        Path source = Paths.get(imgPath);
        if (source.getParent().toString().equals(getDefaultDirectoryImagesPath().toString())) {
            try {
                File file = new File(imgPath);
                if (!file.delete())
                    throw new IllegalStateException("Failed to delete the file. File may not exist or be in use.");
            } catch (Exception e) {
                throw new IllegalArgumentException("Error locating the file.");
            }
        }
    }
}
