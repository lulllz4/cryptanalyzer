import java.util.HashMap;
import java.util.Map;

public class Alphabet {
    //обычный массив для экономии памяти
    public static final char[] CHARS = {
            'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '
    };

    //хэш-карта для ускорения поиска индекса символа
    private static final Map<Character, Integer> CHAR_TO_INDEX = new HashMap<>();

    static {
        for (int i = 0; i < CHARS.length; i++) {
            CHAR_TO_INDEX.put(CHARS[i], i);
        }
    }

    public static int getSize() {
        return CHARS.length;
    }

    public static boolean contains(char c) {
        return CHAR_TO_INDEX.containsKey(Character.toLowerCase(c));
    }

    public static int getIndex(char c) {
        return CHAR_TO_INDEX.getOrDefault(Character.toLowerCase(c), -1);
    }

    public static char getChar(int index) {
        //защита от выхода за край
        int size = getSize();
        int normalizedIndex = (index % size + size) % size;
        return CHARS[normalizedIndex];
    }
}