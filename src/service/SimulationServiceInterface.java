package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SimulationServiceInterface {
    public void loadSimulation(String filePath, String logPath);
    void saveData();
    void saveLogs(List<String> logs);
    void updateConfig(Config config);
    List<Organism> getOrganisms();
    List<Organism> getInterimOrganisms();
    Config getConfig();
    void addOrganism(Organism organism);
    boolean removeOrganism(String id);
    void removeOrganismInterim(String id);
}
