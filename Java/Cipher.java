public class Cipher {

    public String encryptText(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            boolean isUpperCase = Character.isUpperCase(c);
            char lowerC = Character.toLowerCase(c);

            if (Alphabet.contains(lowerC)) {
                int originalIndex = Alphabet.getIndex(lowerC);
                //позиция + сдвиг % размер алфавита
                int newIndex = (originalIndex + shift) % Alphabet.getSize();
                char encryptedChar = Alphabet.getChar(newIndex);

                result.append(isUpperCase ? Character.toUpperCase(encryptedChar) : encryptedChar);
            } else {
                //если нет символа то скип или сейф
                result.append(c);
            }
        }
        return result.toString();
    }

    public String decryptText(String encryptedText, int shift) {
        return encryptText(encryptedText, Alphabet.getSize() - (shift % Alphabet.getSize()));
    }
}