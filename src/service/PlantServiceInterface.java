package service;

import model.Plant;

public interface PlantServiceInterface {
    void updateMortality(Plant plant, double currentTemperature, double currentHumidity, double availableWater, double totalCount);
    void reproduce(Plant plant);
    void dieNaturally(Plant plant);
    void manipulatePopulationSize(Plant plant);
    String predictPopulation(Plant plant);
}
