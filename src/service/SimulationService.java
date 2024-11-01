package service;

import model.*;
import repository.SimulationRepository;

import java.util.*;

public class SimulationService implements SimulationServiceInterface{
    private final SimulationRepository repository;
    private String filePath;
    private String logPath;
    private List<Organism> organisms;
    private Config config;

    public SimulationService(SimulationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void loadSimulation(String filePath, String logPath) {
        this.filePath = filePath;
        this.logPath = logPath;
        organisms = getOrganisms();
        config = getConfig();
    }

    @Override
    public void saveData() {
        repository.saveData(getData(), filePath);
    }

    @Override
    public void saveLogs(List<String> logs) {
        String oldData = getLogsData();
        String newData = String.join("\n", logs);

        repository.saveData(String.join("\n", oldData, newData), logPath);
    }

    private String getData() {
        String settingsData = config.formatToFile();

        StringBuilder organismsData = new StringBuilder();
        for (Organism organism : organisms) {
            organismsData.append(organism.formatToFile());
        }

        return settingsData + organismsData;
    }

    private String getLogsData() {
        return repository.readData(logPath);
    }

    @Override
    public void updateConfig(Config config) {
        this.config = config;
        saveData();
    }

    @Override
    public List<Organism> getOrganisms() {
        List<Organism> organisms = new ArrayList<>();
        String data = repository.readData(filePath);
        if (data == null || data.isEmpty()) {
            return organisms;
        }

        String[] sections = data.split("\\[");
        for (String section : sections) {
            if (section.trim().isEmpty()) continue;

            String sectionType = section.substring(0, section.indexOf(']')).trim();
            String[] lines = section.substring(section.indexOf(']') + 1).trim().split(System.lineSeparator());

            Map<String, String> properties = new HashMap<>();
            for (String line : lines) {
                String[] keyValue = line.split("=", 2);
                if (keyValue.length == 2) {
                    properties.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }

            if ("Animal".equalsIgnoreCase(sectionType)) {
                organisms.add(parseAnimal(properties));
            } else if ("Plant".equalsIgnoreCase(sectionType)) {
                organisms.add(parsePlant(properties));
            }
        }

        return organisms;
    }

    @Override
    public List<Organism> getInterimOrganisms() {
        return organisms;
    }

    private Animal parseAnimal(Map<String, String> properties) {
        String id = properties.get("ID");
        String name = properties.get("Name");
        AnimalType type = AnimalType.valueOf(properties.get("Type"));
        int populationSize = Integer.parseInt(properties.get("PopulationSize"));
        double idealTemperature = Double.parseDouble(properties.get("IdealTemperature"));
        double idealHumidity = Double.parseDouble(properties.get("IdealHumidity"));

        return new Animal(id, name, type, populationSize, idealTemperature, idealHumidity);
    }

    private Plant parsePlant(Map<String, String> properties) {
        String id = properties.get("ID");
        String name = properties.get("Name");
        PlantType type = PlantType.valueOf(properties.get("Type"));
        int populationSize = Integer.parseInt(properties.get("PopulationSize"));
        double idealTemperature = Double.parseDouble(properties.get("IdealTemperature"));
        double idealHumidity = Double.parseDouble(properties.get("IdealHumidity"));

        return new Plant(id, name, type, populationSize, idealTemperature, idealHumidity);
    }

    @Override
    public Config getConfig() {
        String data = repository.readData(filePath);

        if (data == null || data.isEmpty()) {
            return null;
        }

        String[] sections = data.split("\\[");
        for (String section : sections) {
            if (section.trim().isEmpty()) continue;

            String sectionType = section.substring(0, section.indexOf(']')).trim();
            if ("config".equalsIgnoreCase(sectionType)) {
                String[] lines = section.substring(section.indexOf(']') + 1).trim().split(System.lineSeparator());
                Map<String, String> properties = new HashMap<>();

                for (String line : lines) {
                    String[] keyValue = line.split("=", 2);
                    if (keyValue.length == 2) {
                        properties.put(keyValue[0].trim(), keyValue[1].trim());
                    }
                }

                return parseConfig(properties);
            }
        }

        return null;
    }

    private Config parseConfig(Map<String, String> properties) {
        double humidity = Double.parseDouble(properties.get("Humidity"));
        double temperature = Double.parseDouble(properties.get("Temperature"));
        double waterLevel = Double.parseDouble(properties.get("WaterLevel"));
        return new Config(temperature, humidity, waterLevel);
    }

    @Override
    public void addOrganism(Organism organism) {
        organisms.add(organism);
        saveData();
    }

    @Override
    public boolean removeOrganism(String id) {
        boolean removed = organisms.removeIf(o -> o.getId().equals(id));
        saveData();
        return removed;
    }

    @Override
    public void removeOrganismInterim(String id) {
        organisms.removeIf(o -> o.getId().equals(id));
    }
}

