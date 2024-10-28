package edu.upc.subgrupprop113.supermarketmanager;

public interface ImportFileStrategy {
    //Instead of void should return a json file
    public abstract void importSupermarket(String filePath);
    public abstract void importCatalog(String filePath);
    public abstract void importShelvingUnits(String filePath);
}
