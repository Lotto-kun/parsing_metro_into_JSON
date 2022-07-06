import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ParseMoscowMetro {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseMoscowMetro.class);
    private static final Marker EXCEPTIONS_MARKER = MarkerFactory.getMarker("EXCEPTIONS");
    private final String link;
    private Document doc = null;
    private Map<String, String> metroLines = new TreeMap<>(new LineNumberComparator());
    private Map<String, List<String>> metroStations = new TreeMap<>(new LineNumberComparator());

    public ParseMoscowMetro(String link) {
        this.link = link;
    }

    public void parseLink() {
        System.out.println("Начинаем парсинг");
        try {
            doc = Jsoup.connect(link).maxBodySize(0).get();
        } catch (IOException e) {
            LOGGER.error(EXCEPTIONS_MARKER, "Не смогло подключиться по ссылке", e);
            e.printStackTrace();
        }
        if (doc != null) {
            parseStations();
            parseLines();
        } else {
            System.out.println("Ошибка получения кода. Проверьте правильность ссылки");
        }
    }

    private void parseLines() {
        Elements lines = doc.select("span.js-metro-line");
        lines.forEach(line -> metroLines.put(line.attributes().get("data-line"), line.text()));
        System.out.println("Станции пропарсены");
    }

    private void parseStations() {
        Elements stations = doc.select("p.single-station");

        stations.forEach(station -> {
            assert station.parent() != null;
            String line = station.parent().attributes().get("data-line");
            String name = station.select("span.name").text();
            if (metroStations.containsKey(line)) {
                metroStations.get(line).add(name);
            } else {
                List<String> stationList = new ArrayList<>();
                stationList.add(name);
                metroStations.put(line, stationList);
            }
        });
        System.out.println("Линии пропарсены");
    }

    public void writeToFileMetro(String destinationPath) {
        JSONObject metroJSON = new JSONObject();
        saveStations(metroJSON);
        saveLines(metroJSON);
        Path destination = Paths.get(destinationPath);

        if (Files.notExists(destination)) {
            try {
                Files.createDirectories(destination.getParent());
            } catch (Exception e) {
                LOGGER.error(EXCEPTIONS_MARKER, "ошибка создания директории назначения", e);
                e.printStackTrace();
                return;
            }
        }

        try {
            Files.write(destination, metroJSON.toJSONString().getBytes());
            System.out.println("Схема метро сохранена в файл");
        } catch (IOException e) {
            LOGGER.error(EXCEPTIONS_MARKER, "ошибка записи в файл", e);
            e.printStackTrace();
        }
    }

    private void saveStations(JSONObject metroJSON) {
        JSONObject stationsList = new JSONObject(metroStations);
        metroJSON.put("stations", stationsList);
    }

    private void saveLines(JSONObject metroJSON) {
        JSONArray linesList = new JSONArray();
        for (String key : metroLines.keySet()) {
            JSONObject line = new JSONObject();
            line.put("number", key);
            line.put("name", metroLines.get(key));
            linesList.add(line);
        }
        metroJSON.put("lines", linesList);
    }

}
