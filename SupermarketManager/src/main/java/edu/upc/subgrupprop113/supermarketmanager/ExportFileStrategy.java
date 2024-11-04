package edu.upc.subgrupprop113.supermarketmanager;
import java.util.ArrayList;
import java.util.List;

public interface ExportFileStrategy {
    //Instead of void should return a json file
    public abstract void exportSupermarket(List<Product> products, ArrayList<ShelvingUnit> shelvingUnits, String filePath);
}
