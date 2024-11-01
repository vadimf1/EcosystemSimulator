package repository;

import java.io.*;

public class SimulationRepository implements SimulationRepositoryInterface {

    @Override
    public void saveData(String data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(data);
            System.out.println("Данные успешно сохранены в файл: " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    @Override
    public String readData(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder contentBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }

            return contentBuilder.toString();
        } catch (IOException ex) {
            System.err.println("Ошибка при чтении данных из файла: " + ex.getMessage());
        }

        return null;
    }
}
