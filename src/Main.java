import controller.SimulationController;
import repository.SimulationRepository;
import service.SimulationService;

public class Main {
    private static final String SIMULATION_DIRECTORY = "resources/";

    public static void main(String[] args) {
        SimulationRepository repository = new SimulationRepository();
        SimulationService service = new SimulationService(repository);
        SimulationController controller = new SimulationController(service, SIMULATION_DIRECTORY);
        EcosystemApp app = new EcosystemApp(controller);

        app.run();
    }
}
