package repository;

public interface SimulationRepositoryInterface {
    void saveData(String data, String filePath);
    String readData(String filePath);
}
