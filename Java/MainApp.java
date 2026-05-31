import java.io.IOException;
import java.util.Scanner;

public class MainApp {
    private static final FileManager fileManager = new FileManager();
    private static final Cipher cipher = new Cipher();
    private static final Validator validator = new Validator();
    private static final BruteForce bruteForce = new BruteForce();
    private static final StatisticalAnalyzer analyzer = new StatisticalAnalyzer();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- МЕНЮ ШИФРА ЦЕЗАРЯ ---");
            System.out.println("1. Шифрование текста");
            System.out.println("2. Расшифровка с известным ключом");
            System.out.println("3. Взлом методом Brute Force");
            System.out.println("4. Взлом методом статистического анализа (по пробелу)");
            System.out.println("0. Выход");
            System.out.print("Выберите режим: ");

            String choice = scanner.nextLine();
            if (choice.equals("0")) {
                System.out.println("Программа завершена. До свидания!");
                break;
            }

            try {
                switch (choice) {
                    case "1" -> handleEncryption(scanner);
                    case "2" -> handleDecryption(scanner);
                    case "3" -> handleBruteForce(scanner);
                    case "4" -> handleStatAnalysis(scanner);
                    default -> System.out.println("Неверный пункт меню. Попробуйте снова.");
                }
            } catch (IOException e) {
                System.out.println("Ошибка при работе с файлом: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Произошла непредвиденная ошибка: " + e.getMessage());
            }
        }
    }

    private static void handleEncryption(Scanner scanner) throws IOException {
        String inputFile = askFile(scanner, "Введите путь к исходному файлу: ");
        //инпут.ткст в директории
        String outputFile = askOutputFile(scanner);
        int key = askKey(scanner);

        String content = fileManager.readFile(inputFile);
        String encrypted = cipher.encryptText(content, key);
        fileManager.writeFile(encrypted, outputFile);
        System.out.println("Успешно! Зашифрованный файл сохранен в: " + outputFile);
    }

    private static void handleDecryption(Scanner scanner) throws IOException {
        String inputFile = askFile(scanner, "Введите путь к зашифрованному файлу: ");
        String outputFile = askOutputFile(scanner);
        int key = askKey(scanner);

        String content = fileManager.readFile(inputFile);
        String decrypted = cipher.decryptText(content, key);
        fileManager.writeFile(decrypted, outputFile);
        System.out.println("Успешно! Расшифрованный файл сохранен в: " + outputFile);
    }

    private static void handleBruteForce(Scanner scanner) throws IOException {
        String inputFile = askFile(scanner, "Введите путь к зашифрованному файлу: ");
        String outputFile = askOutputFile(scanner);

        String content = fileManager.readFile(inputFile);
        String decrypted = bruteForce.decryptByBruteForce(content);
        fileManager.writeFile(decrypted, outputFile);
        System.out.println("Успешно! Результат взлома сохранен в: " + outputFile);
    }

    private static void handleStatAnalysis(Scanner scanner) throws IOException {
        String inputFile = askFile(scanner, "Введите путь к зашифрованному файлу: ");
        String outputFile = askOutputFile(scanner);

        String content = fileManager.readFile(inputFile);
        String decrypted = analyzer.decryptBySpaceAnalysis(content);
        fileManager.writeFile(decrypted, outputFile);
        System.out.println("Успешно! Результат частотного анализа сохранен в: " + outputFile);
    }

    private static String askFile(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String path = scanner.nextLine();
            if (validator.isFileExists(path) && validator.isSafeFilename(path)) {
                return path;
            }
            System.out.println("Файл не найден или недоступен! Попробуйте еще раз.");
        }
    }

    private static String askOutputFile(Scanner scanner) {
        while (true) {
            System.out.print("Введите путь для сохранения результата: ");
            String path = scanner.nextLine();
            if (validator.isSafeFilename(path)) {
                return path;
            }
            System.out.println("Недопустимое имя файла!");
        }
    }

    private static int askKey(Scanner scanner) {
        while (true) {
            System.out.print("Введите ключ (число от 0 до " + (Alphabet.getSize() - 1) + "): ");
            try {
                int key = Integer.parseInt(scanner.nextLine());
                if (validator.isValidKey(key)) {
                    return key % Alphabet.getSize(); //еормализуем по размеру алфавита
                }
            } catch (NumberFormatException ignored) {}
            System.out.println("Некорректный ключ. Введите целое положительное число.");
        }
    }
}