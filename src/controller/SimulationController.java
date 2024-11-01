package controller;

import model.*;
import service.AnimalService;
import service.PlantService;
import service.SimulationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimulationController {
    private final String directory;
    private final SimulationService simulationService;
    private final AnimalService animalService;
    private final PlantService plantService;
    private final Scanner scanner;

    public SimulationController(SimulationService simulationService, String directory) {
        this.simulationService = simulationService;
        this.animalService = new AnimalService();
        this.plantService = new PlantService();
        this.scanner = new Scanner(System.in);
        this.directory = directory;
    }

    public void printMenu() {
        System.out.println("\n--- Главное меню ---");
        System.out.println("1. Просмотреть список организмов");
        System.out.println("2. Добавить организм");
        System.out.println("3. Удалить организм");
        System.out.println("4. Обновить параметры конфигурации");
        System.out.println("5. Просмотреть параметры конфигурации");
        System.out.println("6. Получить предсказания");
        System.out.println("7. Запустить автоматическое взаимодействие симуляции");
        System.out.println("0. Выйти");
        System.out.print("Выберите действие: ");
    }

    public void viewOrganisms() {
        List<Organism> organisms = simulationService.getOrganisms();

        if (organisms.isEmpty()) {
            System.out.println("В экосистеме нет организмов.");
        } else {
            System.out.println("Организмы в экосистеме:");
            for (Organism organism : organisms) {
                System.out.println(organism);
            }
        }
    }

    public void loadOrCreateSimulation() {
        File directory = new File(this.directory);
        File[] simulationFiles = directory.listFiles((dir, name) ->
                name.endsWith(".txt") && !name.contains("_log")
        );

        if (simulationFiles == null || simulationFiles.length == 0) {
            System.out.println("Симуляции не найдены. Создание новой...");
            createNewSimulation();
        } else {
            System.out.println("Выберите существующую симуляцию или создайте новую:");

            for (int i = 0; i < simulationFiles.length; i++) {
                System.out.println((i + 1) + ". " + simulationFiles[i].getName());
            }
            System.out.println("0. Создать новую симуляцию");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                createNewSimulation();
            } else if (choice > 0 && choice <= simulationFiles.length) {
                String selectedFile = simulationFiles[choice - 1].getName();
                String selectedLogFile = selectedFile.replace(".txt", "_log.txt");

                System.out.println("Загрузка симуляции " + selectedFile);
                this.simulationService.loadSimulation(this.directory + selectedFile, this.directory + selectedLogFile);
            } else {
                System.out.println("Неверный выбор, создание новой симуляции.");
                createNewSimulation();
            }
        }
    }

    private void createNewSimulation() {
        String baseFileName = directory + "simulation_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filePath = baseFileName  + ".txt";
        String logPath = baseFileName + "_log"  + ".txt";

        try {
            Files.createFile(Paths.get(filePath));
            Files.createFile(Paths.get(logPath));

            System.out.println("Создана новая симуляция: " + filePath);
            System.out.println("Лог-файл: " + logPath);

        } catch (IOException e) {
            System.err.println("Ошибка при создании файлов симуляции: " + e.getMessage());
        }

        this.simulationService.loadSimulation(filePath, logPath);
        this.simulationService.updateConfig(new Config(20, 60, 1000));
    }

    public void addOrganism() {
        int typeChoice = getValidatedChoice("Выберите тип организма: 1. Животное  2. Растение", 1, 2);

        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.println("Введите количество популяции: ");
        int populationSize = scanner.nextInt();

        System.out.println("Введите идеальную температуру существования: ");
        double idealTemperature = scanner.nextDouble();

        System.out.println("Введите идеальную влажность существования: ");
        double idealHumidity = scanner.nextDouble();
        scanner.nextLine();

        int specificTypeChoice = 0;

        if (typeChoice == 1) {
            specificTypeChoice = getValidatedChoice("Выберите тип животного: 1. Травоядное  2. Хищник", 1, 2);
        } else if (typeChoice == 2) {
            specificTypeChoice = getValidatedChoice("Выберите тип растения: 1. Кустарник  2. Цветок", 1, 2);
        }

        Organism organism = null;
        if (typeChoice == 1) {
            AnimalType animalType = (specificTypeChoice == 1) ? AnimalType.HERBIVORE : AnimalType.PREDATOR;
            organism = new Animal(null, name, animalType, populationSize, idealTemperature, idealHumidity);
        } else if (typeChoice == 2) {
            PlantType plantType = (specificTypeChoice == 1) ? PlantType.BUSH : PlantType.FLOWER;
            organism = new Plant(null, name, plantType, populationSize, idealTemperature, idealHumidity);
        }

        simulationService.addOrganism(organism);

        System.out.println("Организм добавлен: " + organism);
    }

    public void removeOrganism() {
        System.out.print("Введите ID организма для удаления: ");
        String id = scanner.nextLine();
        boolean removed = simulationService.removeOrganism(id);
        if (removed) {
            System.out.println("Организм с ID " + id + " был успешно удалён.");
        } else {
            System.out.println("Организм с ID " + id + " не найден.");
        }
    }

    public void updateConfig() {
        System.out.print("Введите новую температуру: ");
        double temperature = scanner.nextDouble();
        System.out.print("Введите новую влажность: ");
        double humidity = scanner.nextDouble();
        System.out.print("Введите новый уровень доступной воды: ");
        double waterLevel = scanner.nextDouble();
        scanner.nextLine();

        try {
            Config newConfig = new Config(temperature, humidity, waterLevel);
            simulationService.updateConfig(newConfig);
            System.out.println("Конфигурация обновлена.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewConfig() {
        Config config = simulationService.getConfig();

        System.out.println("Текущая конфигурация:");
        System.out.println("Temperature: " + config.getTemperature());
        System.out.println("Humidity: " + config.getHumidity());
        System.out.println("Water Level: " + config.getWaterLevel());
    }

    public void getPredictions() {
        List<Animal> animals = getAnimals();
        List<Plant> plants = getPlants();

        List<String> animalPredictions = updateAnimalPredictions(animals);
        List<String> plantPredictions = updatePlantPredictions(plants);

        for (String message : animalPredictions) {
            System.out.println(message);
        }

        for (String message : plantPredictions) {
            System.out.println(message);
        }
    }

    public void startInteraction() {
        if (getTotalCount() == 0) {
            System.out.println("Организмы в экосистеме отсутствуют.");
            return;
        }
        System.out.print("Введите количество ходов симуляции: ");
        int totalSteps = scanner.nextInt();
        scanner.nextLine();

        int currentStep = 0;
        Random random = new Random();

        List<String> logs = new ArrayList<>();
        System.out.println(logs);
        logs.add("Запуск симуляции: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")) + "\n");
        while (currentStep < totalSteps && getTotalCount() > 0) {
            logs.add("Ход " + (currentStep + 1) + ":");

            Config config = simulationService.getConfig();

            List<Organism> organisms = getInterimOrganisms();
            List<Organism> toRemove = new ArrayList<>();
            for (Organism organism : organisms) {
                if (organism instanceof Animal animal) {
                    processAnimalInteraction(animal, random, logs);
                    animalService.updateMortality(animal, config.getTemperature(), config.getHumidity(), config.getWaterLevel(), getTotalCount());
                    animalService.manipulatePopulationSize(animal);
                }
                else if (organism instanceof Plant plant) {
                    plantService.updateMortality(plant, config.getTemperature(), config.getHumidity(), config.getWaterLevel(), getTotalCount());
                    plantService.manipulatePopulationSize(plant);
                }

                if (organism.getPopulationSize() <= 0) {
                    logs.add(organism.getName() + " вымер.");
                    toRemove.add(organism);
                    continue;
                }

                logs.add(organism.getName() + " теперь имеет популяцию: " + organism.getPopulationSize());
            }

            for (Organism organism : toRemove) {
                removeOrganismInterim(organism.getId());
            }

            currentStep++;
        }


        if (currentStep < totalSteps) {
            logs.add("Симуляция завершена досрочно за " + currentStep);
        }

        System.out.println("Симуляция завершена.");

        simulationService.saveData();
        simulationService.saveLogs(logs);
    }

    public void removeOrganismInterim(String id) {
        simulationService.removeOrganismInterim(id);
    }

    public List<Organism> getInterimOrganisms() {
        return simulationService.getInterimOrganisms();
    }

    public List<Animal> getHerbivores() {
        List<Animal> herbivores = new ArrayList<>();
        for (Organism organism : getInterimOrganisms()) {
            if (organism instanceof Animal animal && animal.getType() == AnimalType.HERBIVORE) {
                herbivores.add(animal);
            }
        }
        return herbivores;
    }

    public List<Animal> getAnimals() {
        List<Animal> animals = new ArrayList<>();
        for (Organism organism : getInterimOrganisms()) {
            if (organism instanceof Animal animal) {
                animals.add(animal);
            }
        }
        return animals;
    }

    public List<Plant> getPlants() {
        List<Plant> plants = new ArrayList<>();
        for (Organism organism : getInterimOrganisms()) {
            if (organism instanceof Plant plant) {
                plants.add(plant);
            }
        }
        return plants;
    }

    public int getHerbivoreCount() {
        int herbivoreCount = 0;
        for (Organism organism : getInterimOrganisms()) {
            if (organism instanceof Animal animal && animal.getType() == AnimalType.HERBIVORE) {
                herbivoreCount += animal.getPopulationSize();
            }
        }
        return herbivoreCount;
    }

    public int getPlantCount() {
        int plantCount = 0;
        for (Organism organism : getInterimOrganisms()) {
            if (organism instanceof Plant plant) {
                plantCount += plant.getPopulationSize();
            }
        }
        return plantCount;
    }

    public int getTotalCount() {
        int totalCount = 0;
        for (Organism organism : getInterimOrganisms()) {
            totalCount += organism.getPopulationSize();
        }

        return totalCount;
    }

    public void processAnimalInteraction(Animal animal, Random random, List<String> logs) {
        int victims;
        if (animal.getType() == AnimalType.PREDATOR) {
            victims = animalService.hunt(animal, getHerbivoreCount(), logs);
            distributeVictims(victims, getHerbivores(), random);
        } else {
            victims = animalService.feed(animal, getPlantCount(), logs);
            distributeVictims(victims, getPlants(), random);
        }
    }

    private <T extends Organism> void distributeVictims(int victims, List<T> organisms, Random random) {
        while (victims > 0 && !organisms.isEmpty()) {
            T randomOrganism = organisms.get(random.nextInt(organisms.size()));
            int decrease = Math.min(victims, Math.max(1, random.nextInt(5) + 1));
            int newPopulationSize = randomOrganism.getPopulationSize() - decrease;

            if (newPopulationSize <= 0) {
                victims -= randomOrganism.getPopulationSize();
                randomOrganism.setPopulationSize(0);
                organisms.remove(randomOrganism);
            } else {
                randomOrganism.setPopulationSize(newPopulationSize);
                victims -= decrease;
            }
        }
    }

    public List<String> updateAnimalPredictions(List<Animal> animals) {
        Config config = simulationService.getConfig();
        double temperature = config.getTemperature();
        double humidity = config.getHumidity();
        double waterLevel = config.getWaterLevel();

        List<String> predictions = new ArrayList<>();

        if (animals.isEmpty()) {
            predictions.add("Животные отсутствуют");
            return predictions;
        }

        for (Animal animal : animals) {
            animalService.updateMortality(animal, temperature, humidity, waterLevel, getTotalCount());

            String prediction;
            if (animal.getType() == AnimalType.PREDATOR) {
                prediction = animalService.predictPopulation(animal, getHerbivoreCount());
            } else {
                prediction = animalService.predictPopulation(animal, getPlantCount());
            }

            predictions.add(animal.toString());
            predictions.add(prediction);
        }

        return predictions;
    }

    public List<String> updatePlantPredictions(List<Plant> plants) {
        Config config = simulationService.getConfig();
        double temperature = config.getTemperature();
        double humidity = config.getHumidity();
        double waterLevel = config.getWaterLevel();

        List<String> predictions = new ArrayList<>();

        if (plants.isEmpty()) {
            predictions.add("Растения отсутствуют");
            return predictions;
        }

        for (Plant plant : plants) {
            plantService.updateMortality(plant, temperature, humidity, waterLevel, getTotalCount());
            String prediction = plantService.predictPopulation(plant);

            predictions.add(plant.toString());
            predictions.add(prediction);
        }

        return predictions;
    }

    public int getValidatedChoice(String prompt, int min, int max) {
        int choice = 0;
        boolean valid = false;

        while (!valid) {
            System.out.println(prompt);
            choice = scanner.nextInt();
            scanner.nextLine();

            if (choice >= min && choice <= max) {
                valid = true;
            } else {
                System.out.printf("Неверный выбор. Пожалуйста, выберите число от %d до %d.%n", min, max);
            }
        }

        return choice;
    }

}
