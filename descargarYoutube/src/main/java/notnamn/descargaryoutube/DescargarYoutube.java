package notnamn.descargaryoutube;

/**
 *
 * @author notNamn
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Scanner;

public class DescargarYoutube {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Descargador de Videos ===");

        // Ingresar la URL del video
        String videoUrl = promptForVideoUrl(scanner);

        // Seleccionar formato
        String format = promptForFormat(scanner);

        // Seleccionar calidad
        String quality = promptForQuality(scanner);

        // Obtener la ruta del directorio en el escritorio
        String outputFolder = getOutputDirectory();


        createOutputDirectory(outputFolder);

        
        String command = buildCommand(videoUrl, format, quality, outputFolder);


        executeCommand(command);

        scanner.close();
    }

    private static String promptForVideoUrl(Scanner scanner) {
        String videoUrl;
        do {
            System.out.println("Ingrese la URL del video:");
            videoUrl = scanner.nextLine();
            if (videoUrl.isEmpty()) {
                System.out.println("La URL no puede estar vacía.");
            }
        } while (videoUrl.isEmpty());
        return videoUrl;
    }

    private static String promptForFormat(Scanner scanner) {
        String format = "";
        do {
            System.out.println("Seleccione el formato:");
            System.out.println("1. mp3");
            System.out.println("2. mp4");
            String formatOption = scanner.nextLine();
            switch (formatOption) {
                case "1":
                    format = "mp3";
                    break;
                case "2":
                    format = "mp4";
                    break;
                default:
                    System.out.println("Opción no válida. Seleccione 1 o 2.");
            }
        } while (format.isEmpty());
        return format;
    }

    private static String promptForQuality(Scanner scanner) {
        String quality = "";
        do {
            System.out.println("Seleccione la calidad:");
            System.out.println("1. Alta");
            System.out.println("2. Media");
            System.out.println("3. Baja");
            String qualityOption = scanner.nextLine();
            switch (qualityOption) {
                case "1":
                    quality = "alta";
                    break;
                case "2":
                    quality = "media";
                    break;
                case "3":
                    quality = "baja";
                    break;
                default:
                    System.out.println("Opción no válida. Seleccione 1, 2 o 3.");
            }
        } while (quality.isEmpty());
        return quality;
    }

    private static String getOutputDirectory() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, "Desktop", "Archivos Descargados").toString();
    }

    private static void createOutputDirectory(String outputFolder) {
        File directory = new File(outputFolder);
        if (!directory.exists()) {
            boolean isCreated = directory.mkdirs();
            if (isCreated) {
                System.out.println("Directorio de descargas creado en: " + outputFolder);
            } else {
                System.out.println("No se pudo crear el directorio de descargas.");
                System.exit(1); // Terminar la ejecución si no se puede crear el directorio
            }
        } else {
            System.out.println("El directorio ya existe: " + outputFolder);
        }
    }

    private static String buildCommand(String url, String format, String quality, String outputFolder) {
        String qualityOption = "";

        // Definir la opción de calidad
        switch (quality) {
            case "alta":
                qualityOption = "best";
                break;
            case "media":
                qualityOption = "medium";
                break;
            case "baja":
                qualityOption = "worst";
                break;
            default:
                qualityOption = "best";
        }

        // Construir el comando según el formato
        if (format.equals("mp3")) {
            return String.format("yt-dlp -f %s -x --audio-format mp3 -o \"%s/%%(title)s.%%(ext)s\" %s", qualityOption, outputFolder, url);
        } else if (format.equals("mp4")) {
            return String.format("yt-dlp -f %s -o \"%s/%%(title)s.%%(ext)s\" %s", qualityOption, outputFolder, url);
        } else {
            throw new IllegalArgumentException("Formato no soportado");
        }
    }

    private static void executeCommand(String command) {
        try {
            System.out.println("Iniciando la descarga...");
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Descarga completada con código de salida: " + exitCode);

        } catch (Exception e) {
            System.out.println("Ocurrió un error durante la descarga: " + e.getMessage());
        }
    }
}

