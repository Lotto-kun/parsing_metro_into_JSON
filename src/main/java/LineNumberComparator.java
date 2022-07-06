import java.util.Comparator;

public class LineNumberComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        if (o1.matches("[A-Z]+[0-9]+") && o2.matches("[0-9]+[A-Z]*")) {
            return 1;
        }
        if (o2.matches("[A-Z]+[0-9]+") && o1.matches("[0-9]+[A-Z]*")) {
            return -1;
        }
        if (o1.matches("[0-9]+") && o2.matches("[0-9]+")) {
            return Integer.compare(Integer.parseInt(o1), Integer.parseInt(o2));
        }
        if (o1.matches("[A-Z]+[0-9]+") && o2.matches("[A-Z]+[0-9]+")) {
            String[] letters1 = o1.split("[0-9]+");
            String[] letters2 = o2.split("[0-9]+");
            if (letters1[0].compareTo(letters2[0]) == 0) {
                String[] numbers1 = o1.split("[A-Z]+");
                String[] numbers2 = o2.split("[A-Z]+");
                return Integer.compare(Integer.parseInt(numbers1[1]), Integer.parseInt(numbers2[1]));
            } else {
                return letters1[0].compareTo(letters2[0]);
            }

        }
        if (o1.matches("[0-9]+[A-Z]+") && o2.matches("[0-9]+[A-Z]+")) {
            String[] numbers1 = o1.split("[A-Z]+");
            String[] numbers2 = o2.split("[A-Z]+");
            if (Integer.parseInt(numbers1[0]) == Integer.parseInt(numbers2[0])) {
                String[] letters1 = o1.split("[0-9]+");
                String[] letters2 = o2.split("[0-9]+");
                return letters1[0].compareTo(letters2[0]);
            } else {
                return Integer.compare(Integer.parseInt(numbers1[1]), Integer.parseInt(numbers2[1]));
            }
        }
        if (o1.matches("[0-9]+[A-Z]+") && o2.matches("[0-9]+")) {
            String[] numbers = o1.split("[A-Z]+");
            if (numbers[0].equals(o2)) {
                return 1;
            } else {
                return Integer.compare(Integer.parseInt(numbers[0]), Integer.parseInt(o2));
            }
        }
        if (o2.matches("[0-9]+[A-Z]+") && o1.matches("[0-9]+")) {
            String[] numbers = o2.split("[A-Z]+");
            if (numbers[0].equals(o1)) {
                return -1;
            } else {
                return Integer.compare(Integer.parseInt(o1), Integer.parseInt(numbers[0]));
            }
        }

        return 0;
    }
}
