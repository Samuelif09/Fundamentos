import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.net.URL;

public class TestFxml {
    public static void main(String[] args) throws Exception {
        Platform.startup(() -> {
            try {
                URL resource = TestFxml.class.getResource("/com/openlib/market/frontend/views/perfil.fxml");
                if (resource == null) {
                    System.out.println("Resource not found!");
                    System.exit(1);
                }
                FXMLLoader loader = new FXMLLoader(resource);
                Parent root = loader.load();
                System.out.println("Loaded successfully!");
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
