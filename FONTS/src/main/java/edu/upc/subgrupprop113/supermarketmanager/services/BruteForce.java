package edu.upc.subgrupprop113.supermarketmanager.services;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;

import java.util.ArrayList;
import java.util.List;

import static edu.upc.subgrupprop113.supermarketmanager.utils.HelperFunctions.*;

public class BruteForce implements OrderingStrategy {

    private int shelfHeight;
    private double bestScore;
    private double highestSimilarity;
    private List<ShelvingUnit> optimalDistribution;

    @Override
    public ArrayList<ShelvingUnit> orderSupermarket(List<ShelvingUnit> initialShelves, List<Product> products) {
        // Ajustamos los valores iniciales de la clase
        this.shelfHeight = initialShelves.getFirst().getHeight();
        this.bestScore = Double.POSITIVE_INFINITY;
        this.highestSimilarity = 0.0;
        // Creamos una copia profunda de las estanterías para almacenar la mejor solución
        this.optimalDistribution = deepCopyShelves((ArrayList<ShelvingUnit>) initialShelves, true);

        // Llamamos a la función recursiva
        recursivelyPlaceProducts(
                0,
                new ArrayList<>(products),
                (ArrayList<ShelvingUnit>) initialShelves,
                null,
                0.0,
                0.0
        );

        // Devolvemos la mejor distribución encontrada
        return (ArrayList<ShelvingUnit>) this.optimalDistribution;
    }

    /**
     * Función recursiva principal del backtracking.
     *
     * @param currentShelfIndex Índice global de la posición donde estamos colocando.
     * @param remainingProducts Lista de productos pendientes de colocar.
     * @param shelves Estado actual de las estanterías.
     * @param previousProduct Último producto colocado (para calcular similitud).
     * @param currentScore Suma acumulada de (1 - similarity) de la secuencia.
     * @param currentSimilarity Suma acumulada de similarity de la secuencia.
     */
    private void recursivelyPlaceProducts(
            int currentShelfIndex,
            List<Product> remainingProducts,
            ArrayList<ShelvingUnit> shelves,
            Product previousProduct,
            double currentScore,
            double currentSimilarity)
    {
        // 1) Caso base: ¿ya terminamos? (o bien no hay productos, o se agotaron los huecos)
        if (isSolutionComplete(currentShelfIndex, remainingProducts, shelves, shelfHeight)) {
            updateBestSolutionIfNecessary(currentScore, currentSimilarity, shelves);
            return;
        }

        // 2) Poda: si esta rama no podrá mejorar la mejor solución, salimos
        if (shouldPruneBranch(currentScore, currentSimilarity)) {
            return;
        }

        // 3) Cálculo de índices y datos para esta posición
        int totalPositions = shelves.size() * shelfHeight;
        int positionsLeft = totalPositions - currentShelfIndex;       // Huecos que quedan por rellenar
        int productsLeft  = remainingProducts.size();                 // Productos que quedan
        int nextShelfIndex = calculateNextShelfIndex(currentShelfIndex, shelves.size(), shelfHeight);
        int height = getShelfHeight(currentShelfIndex, shelves.size(), shelfHeight);
        ShelvingUnit currentShelf = getCurrentShelf(shelves, currentShelfIndex);

        // ----------------------------------------------------------------------
        // A) Intentar colocar cada producto (si hay productos por colocar)
        // ----------------------------------------------------------------------
        if (productsLeft > 0) {
            for (int i = 0; i < productsLeft; i++) {
                Product candidateProduct = remainingProducts.get(i);

                // Chequeo de compatibilidad (temperatura, etc.)
                if (!isShelfCompatible(currentShelf, candidateProduct)) {
                    continue;
                }

                // Guardamos estado previo (backtracking)
                double oldScore        = currentScore;
                double oldSimilarity   = currentSimilarity;
                Product oldPrevious    = previousProduct;

                // Calculamos la nueva similitud
                double similarity = calculateSimilarity(previousProduct, candidateProduct);
                currentSimilarity += similarity;
                currentScore     += (1.0 - similarity);

                // Colocamos el producto en la estantería y lo quitamos de la lista
                currentShelf.addProduct(candidateProduct, height);
                remainingProducts.remove(i);
                previousProduct = candidateProduct;

                // Llamada recursiva para la siguiente posición
                recursivelyPlaceProducts(
                        nextShelfIndex,
                        remainingProducts,
                        shelves,
                        previousProduct,
                        currentScore,
                        currentSimilarity
                );

                // BACKTRACK: revertimos cambios
                currentShelf.removeProduct(height);
                remainingProducts.add(i, candidateProduct);
                currentScore      = oldScore;
                currentSimilarity = oldSimilarity;
                previousProduct   = oldPrevious;
            }
        }

        // ----------------------------------------------------------------------
        // B) DEJAR ESTE HUECO VACÍO
        // ----------------------------------------------------------------------
        // Solo lo hacemos si hay MÁS huecos que productos (positionsLeft > productsLeft).
        // Si positionsLeft == productsLeft, estamos obligados a rellenar todos para colocar todos.
        if (positionsLeft > productsLeft) {
            recursivelyPlaceProducts(
                    nextShelfIndex,
                    remainingProducts,
                    shelves,
                    previousProduct,
                    currentScore,
                    currentSimilarity
            );
        }
    }

    /**
     * Determina si se debe podar la rama actual.
     */
    private boolean shouldPruneBranch(double currentScore, double currentSimilarity) {
        // P.ej.: si el score actual ya es peor que el mejor y la similitud no supera la mejor
        return (currentScore >= this.bestScore) && (currentSimilarity <= this.highestSimilarity);
    }

    /**
     * Actualiza la mejor solución conocida, si la actual es mejor.
     */
    private void updateBestSolutionIfNecessary(double currentScore, double currentSimilarity, List<ShelvingUnit> shelves) {
        // Podemos definir "mejor" como "score más bajo y similitud más alta"
        // Ajusta la condición de comparación según tu criterio:
        if (currentScore < this.bestScore && currentSimilarity > this.highestSimilarity) {
            // Copiamos la disposición actual
            this.optimalDistribution = deepCopyShelves((ArrayList<ShelvingUnit>) shelves, false);
            this.bestScore = currentScore;
            this.highestSimilarity = currentSimilarity;
        }
    }
}
