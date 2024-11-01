package service;

import model.Animal;

import java.util.List;

public class AnimalService implements AnimalServiceInterface {
    private static final double TEMPERATURE_TOLERANCE = 15.0;
    private static final double HUMIDITY_TOLERANCE = 15.0;
    private static final double BASE_MORTALITY_RATE = 0.01;
    private static final double BASE_FEED_SUCCESS_RATE = 0.7;
    private static final double BASE_HUNT_SUCCESS_RATE = 0.7;
    private static final double GROWTH_FACTOR_ON_SUCCESS = 1.05;
    private static final double REDUCTION_FACTOR_ON_FAILURE = 0.9;
    private static final int REPRODUCTION_INTERVAL = 5;
    private static final int NATURAL_DEATH_INTERVAL = 15;

    @Override
    public void updateMortality(Animal animal, double currentTemperature, double currentHumidity, double availableWater, double totalCount) {
        double tempDifference = Math.abs(currentTemperature - animal.getIdealTemperature());
        double humidityDifference = Math.abs(currentHumidity - animal.getIdealHumidity());

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

        animal.setMortalityRate(Math.min(BASE_MORTALITY_RATE + tempMortality + humidityMortality + waterMortality, 1.0));
    }

    @Override
    public void manipulatePopulationSize(Animal animal) {
        int newPopulationSize = (int) (animal.getPopulationSize() * (1 - animal.getMortalityRate()));
        animal.setPopulationSize(Math.max(newPopulationSize, 0));
        animal.setCurrentStep(animal.getCurrentStep() + 1);

        if (animal.getCurrentStep() % REPRODUCTION_INTERVAL == 0) {
            reproduce(animal);
        }

        if (animal.getCurrentStep() % NATURAL_DEATH_INTERVAL == 0) {
            dieNaturally(animal);
        }
    }

    @Override
    public void reproduce(Animal animal) {
        int offspring = (int) (animal.getPopulationSize() * 0.3);
        animal.setPopulationSize(animal.getPopulationSize() + offspring);
    }

    @Override
    public void dieNaturally(Animal animal) {
        int deaths = (int) (animal.getPopulationSize() * 0.45);
        animal.setPopulationSize(animal.getPopulationSize() - deaths);
    }

    @Override
    public int feed(Animal animal, int plantsPopulationSize, List<String> logs) {
        int populationSize = animal.getPopulationSize();

        double plantFactor;
        if (plantsPopulationSize > 0 && plantsPopulationSize > populationSize) {
            plantFactor = Math.min(1.5, (double) plantsPopulationSize / (populationSize + 10));
        }
        else {
            plantFactor = 0;
        }

        double eatSuccessRate = BASE_FEED_SUCCESS_RATE * plantFactor;

        if (Math.random() < eatSuccessRate) {
            int victims = populationSize;
            int newPopulationSize = (int) (populationSize * GROWTH_FACTOR_ON_SUCCESS);
            animal.setPopulationSize(newPopulationSize);
            logs.add(animal.getName() + " успешно питался.");

            return (int) (victims * 0.1);
        } else {
            int newPopulationSize = (int) (populationSize * REDUCTION_FACTOR_ON_FAILURE);
            animal.setPopulationSize(newPopulationSize);
            logs.add(animal.getName() + " не смог найти достаточно еды.");

            return 0;
        }
    }

    @Override
    public int hunt(Animal animal, int herbivorePopulationSize, List<String> logs) {
        int populationSize = animal.getPopulationSize();

        double huntSuccessRate;
        double herbivoreFactor;
        if (herbivorePopulationSize > 0 && herbivorePopulationSize > populationSize) {
            herbivoreFactor = Math.min(1.5, (double) herbivorePopulationSize / (populationSize + 10));
            huntSuccessRate = BASE_HUNT_SUCCESS_RATE * herbivoreFactor;
        }
        else {
            huntSuccessRate = 0;
        }

        if (Math.random() < huntSuccessRate) {
            int victims = populationSize;
            int newPopulationSize = (int) (populationSize * 1.05);
            animal.setPopulationSize(newPopulationSize);
            logs.add(animal.getName() + " успешно охотился.");

            return (int) (victims * 0.1);
        } else {
            int newPopulationSize = (int) (populationSize * 0.9);
            animal.setPopulationSize(newPopulationSize);
            logs.add(animal.getName() + " не смог охотиться успешно.");

            return 0;
        }
    }

    @Override
    public String predictPopulation(Animal animal, int foodPopulationSize) {
        double mortalityImpact = 1.0 - animal.getMortalityRate();
        double foodImpact = (double) foodPopulationSize / animal.getPopulationSize();

        double overallFactor = mortalityImpact * foodImpact;

        if (overallFactor > 1.2) {
            return "Популяция, вероятно, будет расти";
        } else if (overallFactor >= 0.8 && overallFactor <= 1.2) {
            return "Популяция, вероятно, останется стабильной";
        } else {
            return "Популяция, вероятно, будет уменьшаться";
        }
    }
}
