import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(JUnit4.class)
public class MainTest {

    @Test
    public void testFunkoMasCaro() throws Exception {
        // Crear una instancia de la clase Main
        Main main = new Main();

        // Crear una lista de Funkos de prueba
        List<Funko> funkos = new ArrayList<>();
        funkos.add(new Funko("001", "Funko1", "Modelo1", 50.0, new Date()));
        funkos.add(new Funko("002", "Funko2", "Modelo2", 75.0, new Date()));
        funkos.add(new Funko("003", "Funko3", "Modelo3", 100.0, new Date()));

        // Utilizar reflexión para llamar al método privado funkoMasCaro
        Method method = Main.class.getDeclaredMethod("funkoMasCaro", List.class);
        method.setAccessible(true);
        Funko result = (Funko) method.invoke(main, funkos);

        // Verificar si el Funko devuelto coincide con el Funko más caro esperado
        assertEquals("003", result.getNOMBRE());
    }

}


