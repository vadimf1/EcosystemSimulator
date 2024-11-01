package model;

import java.util.List;
import java.util.UUID;

public abstract class Organism {
    protected String id;
    protected String name;
    protected int populationSize;
    private double mortalityRate = 0;
    private double idealTemperature;
    private double idealHumidity;


    public Organism(String id, String name, int populationSize, double idealTemperature, double idealHumidity) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.name = name;
        this.populationSize = populationSize;
        this.idealTemperature = idealTemperature;
        this.idealHumidity = idealHumidity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getPopulationSize() { return populationSize; }
    public double getIdealTemperature() { return idealTemperature; }
    public double getMortalityRate() { return mortalityRate; }
    public double getIdealHumidity() { return idealHumidity; }


    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPopulationSize(int populationSize) { this.populationSize = populationSize; }
    public void setIdealTemperature(double idealTemperature) { this.idealTemperature = idealTemperature; }
    public void setIdealHumidity(double idealHumidity) { this.idealHumidity = idealHumidity; }
    public void setMortalityRate(double mortalityRate) { this.mortalityRate = mortalityRate; }

    public abstract String formatToFile();

    @Override
    public String toString() {
        return String.format(
                "Organism [ID: %s, Name: %s, Population Size: %d" +
                        "Ideal Temperature: %.1f°C, Ideal Humidity: %.1f%%]",
                id, name, populationSize, idealTemperature, idealHumidity
        );
    }

}
