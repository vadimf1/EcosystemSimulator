package service;

import model.*;
import java.util.List;

public interface AnimalServiceInterface {
    int hunt(Animal animal, int herbivorePopulationSize, List<String> logs);
    int feed(Animal animal, int plantsPopulationSize, List<String> logs);
    void updateMortality(Animal animal, double currentTemperature, double currentHumidity, double availableWater, double totalCount);
    void reproduce(Animal animal);
    void dieNaturally(Animal animal);
    void manipulatePopulationSize(Animal animal);
    String predictPopulation(Animal animal, int foodPopulationSize);
}
