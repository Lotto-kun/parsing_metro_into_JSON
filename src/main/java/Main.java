import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final Marker EXCEPTIONS_MARKER = MarkerFactory.getMarker("EXCEPTIONS");
    public static final String LINK = "https://skillbox-java.github.io/";
    public static final String PATH = "src/main/resources/moscowMetro.json";
    public static void main(String[] args) {
        ParseMoscowMetro metro = new ParseMoscowMetro(LINK);
        metro.parseLink();
        metro.writeToFileMetro(PATH);
        ParseJSONMoscowMetro metroFromJSON = new ParseJSONMoscowMetro(PATH);
        try {
            metroFromJSON.parseJSON();
        } catch (ParseException e) {
            LOGGER.error(EXCEPTIONS_MARKER, "ошибка при парсинге JSON файла", e);
            e.printStackTrace();
        }
        System.out.println("Количество станций на ветках метро");
        metroFromJSON.printStationCount();
    }
}
