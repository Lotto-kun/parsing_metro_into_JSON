import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ParseJSONMoscowMetro {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseJSONMoscowMetro.class);
    private static final Marker EXCEPTIONS_MARKER = MarkerFactory.getMarker("EXCEPTIONS");
    private final String path;
    private Map<String, String> metroLines = new TreeMap<>(new LineNumberComparator());
    private Map<String, List<String>> metroStations = new TreeMap<>(new LineNumberComparator());

    public ParseJSONMoscowMetro(String path) {
        this.path = path;
    }

    public void parseJSON() throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonData = (JSONObject) parser.parse(getJsonFile());
        JSONArray linesArray = (JSONArray) jsonData.get("lines");
        parseLines(linesArray);
        JSONObject stationsObject = (JSONObject) jsonData.get("stations");
        parseStations(stationsObject);
    }

    private void parseLines (JSONArray linesArray) {
        linesArray.forEach(lineObject -> {
            JSONObject lineJsonObject = (JSONObject) lineObject;
            metroLines.put((String)lineJsonObject.get("number"), (String)lineJsonObject.get("name"));
        });
    }

    private void parseStations(JSONObject stationsObject) {
        stationsObject.keySet().forEach(lineNumberObject ->
        {
            String lineNumber = (String) lineNumberObject;
            List stationList = new ArrayList<>();
            JSONArray stationsArray = (JSONArray) stationsObject.get(lineNumberObject);
            stationsArray.forEach(stationObject -> stationList.add(stationObject));
            metroStations.put(lineNumber, stationList);
        });
    }


    private String getJsonFile() {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            lines.forEach(builder::append);
        } catch (Exception ex) {
            LOGGER.error(EXCEPTIONS_MARKER, "Ошибка чтения файла" , ex);
        }
        return builder.toString();
    }
    public void printStationCount(){
        for (String line : metroLines.keySet()) {
            System.out.println(line + " " + metroLines.get(line));
            System.out.println("Количество станций: " + metroStations.get(line).size());;
        }
    }
}
