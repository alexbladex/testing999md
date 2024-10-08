package gui.interaction;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
public class PropertyReader {
    private static final String FILE_PATH = "config.properties";
    private static Properties properties = new Properties();

    static {
        try (InputStream inputStream = new FileInputStream(FILE_PATH);
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            properties.load(reader); // try-with-resources
        } catch (Exception e) {
            e.printStackTrace(); // В продакшн доработать исключение
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static boolean getPropertyBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public static String[] getPropertyArray(String key) {
        String property = getProperty(key);
        return property != null ? property.split(",") : new String[0];
    }

    public static void main(String[] args) {
        String[] envUrls = PropertyReader.getPropertyArray("envUrls");
        for (String url : envUrls) {
            System.out.println(url);
        }
    }
}