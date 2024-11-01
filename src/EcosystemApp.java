import controller.SimulationController;

import java.util.*;

public class EcosystemApp {
    private Scanner scanner;
    private final SimulationController controller;

    public EcosystemApp(SimulationController controller) {
        this.scanner = new Scanner(System.in);
        this.controller = controller;
    }

    public void run() {
        System.out.println("=== Ecosystem Simulation App ===");
        controller.loadOrCreateSimulation();

        boolean running = true;
        while (running) {
            controller.printMenu();
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> controller.viewOrganisms();
                case 2 -> controller.addOrganism();
                case 3 -> controller.removeOrganism();
                case 4 -> controller.updateConfig();
                case 5 -> controller.viewConfig();
                case 6 -> controller.getPredictions();
                case 7 -> controller.startInteraction();
                case 0 -> running = false;
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        System.out.println("Выход из симуляции.");
    }
}
