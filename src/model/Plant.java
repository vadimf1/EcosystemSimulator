package model;

import java.util.Locale;

public class Plant extends Organism {
    private PlantType type;
    private int currentStep;

    public Plant(String id, String name, PlantType type, int populationSize, double idealTemperature, double idealHumidity) {
        super(id, name, populationSize, idealTemperature, idealHumidity);
        this.type = type;
    }

    public PlantType getType() { return type; }
    public int getCurrentStep() { return currentStep; }

    public void setType(PlantType type) { this.type = type; }
    public void setCurrentStep(int currentStep) { this.currentStep = currentStep; }

    @Override
    public String formatToFile() {
        return String.format(Locale.US,
                        "\n[Plant]\n" +
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
                "Plant {ID='%s', Name='%s', Type=%s, PopulationSize=%d, IdealTemperature=%.2f, IdealHumidity=%.2f",
                getId(),
                getName(),
                type,
                getPopulationSize(),
                getIdealTemperature(),
                getIdealHumidity()
        );
    }
}

