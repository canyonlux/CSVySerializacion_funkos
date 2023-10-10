import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final static String COMMA_DELIMITER = ","; //delimitador ,

    public static void main(String[] args) {
        Locale.setDefault(new Locale("es", "ES")); //formato de números y fechas en español
        Scanner scanner = new Scanner(System.in);
        List<Funko> funkos = readFunkosFromCSV("src/funkos.csv");  // Leer el archivo CSV de Funkos
        while (true) {
            System.out.println("Menú:");
            System.out.println("1. Mostrar el Funko más caro");
            System.out.println("2. Mostrar la media de precio de los Funkos");
            System.out.println("3. Agrupar Funkos por modelo");
            System.out.println("4. Contar Funkos por modelo");
            System.out.println("5. Filtrar Funkos lanzados en 2023");
            System.out.println("6. Respaldar Funkos en un archivo");
            System.out.println("7. Restaurar Funkos desde un archivo");
            System.out.println("8. Salir");

            // Leer la opción del usuario
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            //seleccionamos la accion deseada
            switch (opcion) {
                case 1:
                    funkoMasCaro(funkos);
                    break;
                case 2:
                    mediaPreciosFunkos(funkos);
                    break;
                case 3:
                    funkosPorModelos(funkos);
                    break;
                case 4:
                    numeroFunkosPorModelos(funkos);
                    break;
                case 5:
                    funkos2023(funkos);
                    break;
                case 6:
                    backupFunkos(funkos, "funkos.dat");
                    break;
                case 7:
                    List<Funko> restoredFunkos = restoreFunkos("funkos.dat");
                    System.out.println("Funkos restaurados desde el archivo:");
                    restoredFunkos.forEach(System.out::println);
                    break;
            }
        }

    }

    // Método para leer Funkos desde un archivo CSV
    private static List<Funko> readFunkosFromCSV(String funkos) {

        try (BufferedReader br = new BufferedReader(new FileReader(funkos))) {   // Abrimos un archivo CSV para lectura utilizando BufferedReader
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  // Creamos un formateador de fecha para el formato "yyyy-MM-dd"

            // Leemos las líneas del archivo CSV, lo dividimos en campos y lo mapeamos a objetos Funko
            return br.lines()
                    .map(line -> line.split(COMMA_DELIMITER)) // Divide cada línea en campos usando coma como separador
                    .map(data -> {
                        try {
                            // Extraemos los datos de cada campo
                            String CODIGO = data[0];
                            String NOMBRE = data[1];
                            String MODELO = data[2];
                            double PRECIO = Double.parseDouble(data[3]);
                            Date FECHA_LANZAMIENTO = dateFormat.parse(data[4]); // Parseamos la fecha

                            // Creamos un nuevo objeto Funko con los datos extraídos
                            return new Funko(CODIGO, NOMBRE, MODELO, PRECIO, FECHA_LANZAMIENTO);
                        } catch (ParseException e) {
                            // En caso de error al parsear la fecha, lanzamos una excepción RuntimeException
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList()); // Recopilamos los objetos Funko en una lista y la devolvemos
        } catch (IOException e) {
            // En caso de error de lectura del archivo, imprimimos la traza de excepción y devolvemos una lista vacía
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static void funkoMasCaro(List<Funko> funkos) {
        // Encontrar el Funko más caro utilizando streams y el método max
        Optional<Funko> mostExpensiveFunko = funkos.stream()
                .max(Comparator.comparing(Funko::getPRECIO));
        mostExpensiveFunko.ifPresent(funko -> {                      // Si se encuentra el Funko más caro, realizar acciones
            System.out.println("Funko más caro:");
            System.out.println("Nombre: " + funko.getNOMBRE());
            System.out.println("Precio: " + formatCurrency(funko.getPRECIO()));
        });
    }

    static void mediaPreciosFunkos(List<Funko> funkos) {
        // Calcular la media de precios utilizando streams y la función average
        double averagePrice = funkos.stream()
                .mapToDouble(Funko::getPRECIO)
                .average()
                .orElse(0.0);

        // Imprimir la media de precios formateada
        System.out.println("Media de precio de Funkos: " + formatCurrency(averagePrice));
    }

    private static void funkosPorModelos(List<Funko> funkos) {
        // Utiliza streams y la función groupingBy para agrupar Funkos por modelo
        Map<String, List<Funko>> funkosByModel = funkos.stream()
                .collect(Collectors.groupingBy(Funko::getMODELO));

        // Imprime el encabezado
        System.out.println("Funkos agrupados por modelos:");

        // Itera a través de cada grupo de Funkos por modelo e imprime los nombres
        funkosByModel.forEach((model, modelFunkos) -> {
            System.out.println("Modelo: " + model);
            modelFunkos.forEach(funko -> System.out.println("Nombre: " + funko.getNOMBRE()));
        });
    }

    private static void numeroFunkosPorModelos(List<Funko> funkos) {
        // Utiliza streams y la función groupingBy para agrupar Funkos por modelo y contarlos
        Map<String, Long> countByModel = funkos.stream()
                .collect(Collectors.groupingBy(Funko::getMODELO, Collectors.counting()));

        // Imprime el encabezado
        System.out.println("Número de funkos por modelos:");

        // Itera a través de cada grupo de Funkos por modelo e imprime el modelo y la cantidad
        countByModel.forEach((model, count) -> {
            System.out.println("Modelo: " + model);
            System.out.println("Cantidad: " + count);
        });
    }

    private static void funkos2023(List<Funko> funkos) {
        // Obtiene una instancia de Calendar para trabajar con fechas
        Calendar calendar = Calendar.getInstance();

        // Establece la fecha de inicio del año 2023 (1 de enero)
        calendar.set(2023, Calendar.JANUARY, 1);
        Date startOf2023 = calendar.getTime();

        // Establece la fecha de finalización del año 2023 (31 de diciembre)
        calendar.set(2023, Calendar.DECEMBER, 31);
        Date endOf2023 = calendar.getTime();

        // Filtra los Funkos lanzados en el año 2023 utilizando un flujo (stream)
        List<Funko> funkosReleasedIn2023 = funkos.stream()
                .filter(funko -> funko.getFECHA_LANZAMIENTO().after(startOf2023) && funko.getFECHA_LANZAMIENTO().before(endOf2023))
                .collect(Collectors.toList());

        // Imprime un encabezado
        System.out.println("Funkos lanzados en 2023:");

        // Itera a través de la lista de Funkos filtrados y muestra el nombre de cada uno
        funkosReleasedIn2023.forEach(funko -> System.out.println("Nombre: " + funko.getNOMBRE()));
    }


    private static String formatCurrency(double amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return currencyFormat.format(amount);
    }

    private static void backupFunkos(List<Funko> funkos, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            // Crea un ObjectOutputStream para escribir en el archivo especificado

            // Escribe la lista de Funkos en el archivo
            oos.writeObject(funkos);

            // Muestra un mensaje indicando que los Funkos se han respaldado con éxito
            System.out.println("Funkos respaldados en el archivo: " + filename);
        } catch (IOException e) {
            // imprime el rastro de la excepción
            e.printStackTrace();
        }
    }


    private static List<Funko> restoreFunkos(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            // Crea un ObjectInputStream para leer desde el archivo especificado

            // Lee la lista de Funkos desde el archivo y la convierte en un objeto
            return (List<Funko>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // imprime el rastro de la excepción y devuelve una lista vacía como resultado
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}

