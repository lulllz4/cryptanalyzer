import java.util.HashMap;
import java.util.Map;

public class StatisticalAnalyzer {
    private final Cipher cipher = new Cipher();

    //взлом на основе самого частого символа
    public String decryptBySpaceAnalysis(String encryptedText) {
        Map<Character, Integer> frequencies = new HashMap<>();
        for (char c : encryptedText.toCharArray()) {
            char lowerC = Character.toLowerCase(c);
            if (Alphabet.contains(lowerC)) {
                frequencies.put(lowerC, frequencies.getOrDefault(lowerC, 0) + 1);
            }
        }

        //поиск зашифр символа с наибольшим появлением
        char mostFrequentChar = ' ';
        int maxCount = -1;
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentChar = entry.getKey();
            }
        }

        //а если это пробел?
        int encryptedIndex = Alphabet.getIndex(mostFrequentChar);
        int spaceIndex = Alphabet.getIndex(' ');

        //вычисление сдвига
        int detectedShift = (encryptedIndex - spaceIndex + Alphabet.getSize()) % Alphabet.getSize();

        System.out.println("[Частотный анализ] Определен сдвиг относительно пробела: " + detectedShift);
        return cipher.decryptText(encryptedText, detectedShift);
    }
}