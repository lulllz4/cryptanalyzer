public class BruteForce {
    private final Cipher cipher = new Cipher();

    public String decryptByBruteForce(String encryptedText) {
        int bestKey = 0;
        int maxScore = -1;

        //перебор вариантов сдвигов
        for (int key = 0; key < Alphabet.getSize(); key++) {
            String decryptedAttempt = cipher.decryptText(encryptedText, key);
            int currentScore = calculateTextValidityScore(decryptedAttempt);

            if (currentScore > maxScore) {
                maxScore = currentScore;
                bestKey = key;
            }
        }

        System.out.println("[Brute Force] Наиболее вероятный ключ: " + bestKey);
        return cipher.decryptText(encryptedText, bestKey);
    }

    //метрика валидности текста с учетом знаков препинания и пробелов
    private int calculateTextValidityScore(String text) {
        int score = 0;
        //учет что после запятой или точки или знаков ! ? идет пробел
        if (text.contains(", ")) score += 5;
        if (text.contains(". ")) score += 5;
        if (text.contains("! ")) score += 2;
        if (text.contains("? ")) score += 2;

        String[] words = text.split(" ");
        for (String word : words) {
            if (word.length() > 1 && word.length() < 15) {
                score += 1;
            }
        }
        return score;
    }
}