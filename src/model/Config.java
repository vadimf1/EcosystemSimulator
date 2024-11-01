package model;

import java.util.Locale;

public class Config {
    private double temperature;
    private double humidity;
    private double waterLevel;

    public Config(double temperature, double humidity, double availableWater) {
        if (!isValid(temperature, humidity, availableWater)) {
            throw new IllegalArgumentException("Ошибка: некорректные параметры конфигурации.");
        }

        this.temperature = temperature;
        this.humidity = humidity;
        this.waterLevel = availableWater;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWaterLevel() {
        return waterLevel;
    }

    public String formatToFile() {
        return String.format(Locale.US,
                "[config]\n" +
                        "Temperature=%.1f\n" +
                        "Humidity=%.1f\n" +
                        "WaterLevel=%.1f\n",
                getTemperature(),
                getHumidity(),
                getWaterLevel());
    }

    @Override
    public String toString() {
        return "SimulationSettings{" + "temperature=" + temperature +
                ", humidity=" + humidity + ", availableWater=" + waterLevel + '}';
    }

    private boolean isValid(double temperature, double humidity, double waterLevel) {
        return (temperature <= 70 && temperature >= -70) && (humidity >= 0) && (waterLevel >= 0);
    }
}
