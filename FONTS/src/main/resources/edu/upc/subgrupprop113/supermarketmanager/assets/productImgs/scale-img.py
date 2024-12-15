from PIL import Image
import os

def process_images():
    # Directorio actual
    current_dir = os.getcwd()

    # Filtrar solo los archivos PNG
    png_files = [f for f in os.listdir(current_dir) if f.lower().endswith('.png')]

    # Crear un directorio de salida si no existe
    output_dir = os.path.join(current_dir, "processed_images")
    os.makedirs(output_dir, exist_ok=True)

    for file in png_files:
        try:
            # Abrir la imagen
            with Image.open(file) as img:
                # Hacer la imagen cuadrada (rellenando con fondo blanco)
                max_dim = max(img.size)
                square_img = Image.new("RGBA", (max_dim, max_dim), (255, 255, 255, 0))
                square_img.paste(img, ((max_dim - img.width) // 2, (max_dim - img.height) // 2))

                # Redimensionar a 128x128
                resized_img = square_img.resize((128, 128), Image.Resampling.LANCZOS)

                # Guardar la imagen procesada
                output_path = os.path.join(output_dir, file)
                resized_img.save(output_path)

                print(f"Processed: {file} -> {output_path}")
        except Exception as e:
            print(f"Error processing {file}: {e}")

if __name__ == "__main__":
    process_images()