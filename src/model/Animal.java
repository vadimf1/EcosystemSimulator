package model;

import java.util.List;
import java.util.Locale;

public class Animal extends Organism {
    private AnimalType type;
    private int currentStep;

    public Animal(String id, String name, AnimalType type, int populationSize, double idealTemperature, double idealHumidity) {
        super(id, name, populationSize, idealTemperature, idealHumidity);
        this.type = type;
        this.populationSize = populationSize;
    }

    public AnimalType getType() { return type; }
    public int getCurrentStep() { return currentStep; }

    public void setType(AnimalType type) { this.type = type; }
    public void setCurrentStep(int currentStep) { this.currentStep = currentStep; }

    @Override
    public String formatToFile() {
        return String.format(Locale.US,
                "\n[Animal]\n" +
                        "ID=%s\n" +
                        "Name=%s\n" +
                        "Type=%s\n" +
                        "PopulationSize=%d\n" +
                        "IdealTemperature=%.1f\n" +
                        "IdealHumidity=%.1f\n",
                getId(),
                getName(),
                getType(),
                getPopulationSize(),
                getIdealTemperature(),
                getIdealHumidity()
        );
    }

    @Override
    public String toString() {
        return String.format(
                "Animal {ID='%s', Name='%s', Type=%s, PopulationSize=%d, IdealTemperature=%.2f, IdealHumidity=%.2f",
                getId(),
                getName(),
                type,
                getPopulationSize(),
                getIdealTemperature(),
                getIdealHumidity()
        );
    }
}
