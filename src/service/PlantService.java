package service;

import model.Plant;

import java.util.List;

public class PlantService implements PlantServiceInterface {
    private static final double TEMPERATURE_TOLERANCE = 15.0;
    private static final double HUMIDITY_TOLERANCE = 15.0;
    private static final double BASE_MORTALITY_RATE = 0.01;
    private static final int REPRODUCTION_INTERVAL = 5;
    private static final int NATURAL_DEATH_INTERVAL = 15;

    @Override
    public void updateMortality(Plant plant, double currentTemperature, double currentHumidity, double availableWater, double totalCount) {
        double tempDifference = Math.abs(currentTemperature - plant.getIdealTemperature());
        double humidityDifference = Math.abs(currentHumidity - plant.getIdealHumidity());

        double humidityIncreaseRate = 0.003;
        double tempIncreaseRate = 0.005;

        double tempMortality = (tempDifference > 30.0)
                ? (tempDifference - 30.0) * 0.03
                : (tempDifference > TEMPERATURE_TOLERANCE ? (tempDifference - TEMPERATURE_TOLERANCE) * tempIncreaseRate : 0);

        double humidityMortality = (humidityDifference > HUMIDITY_TOLERANCE)
                ? (humidityDifference - HUMIDITY_TOLERANCE) * humidityIncreaseRate
                : 0;

        double waterMortality = 0.0;
        if (availableWater < totalCount) {
            double waterDeficit = totalCount - availableWater;
            waterMortality = waterDeficit * 0.001;
        }

        plant.setMortalityRate(Math.min(BASE_MORTALITY_RATE + tempMortality + humidityMortality + waterMortality, 1.0));
    }

    @Override
    public void manipulatePopulationSize(Plant plant) {
        int newPopulationSize = (int) (plant.getPopulationSize() * (1 - plant.getMortalityRate()));
        plant.setPopulationSize(Math.max(newPopulationSize, 0));
        plant.setCurrentStep(plant.getCurrentStep() + 1);

        if (plant.getCurrentStep() % REPRODUCTION_INTERVAL == 0) {
            reproduce(plant);
        }

        if (plant.getCurrentStep() % NATURAL_DEATH_INTERVAL == 0) {
            reproduce(plant);
        }
    }

    @Override
    public void reproduce(Plant plant) {
        int offspring = (int) (plant.getPopulationSize() * 0.4);
        plant.setPopulationSize(plant.getPopulationSize() + offspring);
    }

    @Override
    public void dieNaturally(Plant plant) {
        int deaths = (int) (plant.getPopulationSize() * 0.45);
        plant.setPopulationSize(plant.getPopulationSize() - deaths);
    }

    @Override
    public String predictPopulation(Plant plant) {
        if (plant.getMortalityRate() < 0.05) {
            return "Популяция, вероятно, будет расти";
        } else if (plant.getMortalityRate() <= 0.15) {
            return "Популяция, вероятно, останется стабильной";
        } else {
            return "Популяция, вероятно, будет уменьшаться";
        }
    }
}
