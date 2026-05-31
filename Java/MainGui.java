import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainGui extends JFrame {
    private final JTextField inputPathField = new JTextField(25);
    private final JTextField outputPathField = new JTextField(25);
    private final JTextField keyField = new JTextField(5);
    private final JComboBox<String> modeComboBox = new JComboBox<>(new String[]{
            "Шифрование",
            "Расшифровка (с ключом)",
            "Взлом (Brute Force)",
            "Взлом (Стат. анализ)"
    });
    private final JLabel statusLabel = new JLabel("Status: Ожидание действий пользователя...");

    //подключение логики
    private final FileManager fileManager = new FileManager();
    private final Cipher cipher = new Cipher();
    private final Validator validator = new Validator();
    private final BruteForce bruteForce = new BruteForce();
    private final StatisticalAnalyzer analyzer = new StatisticalAnalyzer();

    public MainGui() {
        //главное окно
        setTitle("Caesar Cipher Tool");
        setSize(550, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Центрирование на экране
        setLayout(new BorderLayout(10, 10));

        //1. панель выбора файлов
        JPanel filePanel = new JPanel(new GridLayout(2, 3, 5, 5));
        filePanel.setBorder(BorderFactory.createTitledBorder("Работа с файлами"));

        filePanel.add(new JLabel(" Исходный файл:"));
        filePanel.add(inputPathField);
        JButton btnBrowseInput = new JButton("Обзор...");
        filePanel.add(btnBrowseInput);

        filePanel.add(new JLabel(" Сохранить в:"));
        filePanel.add(outputPathField);
        JButton btnBrowseOutput = new JButton("Обзор...");
        filePanel.add(btnBrowseOutput);

        //2. панель настроек
        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Параметры"));

        settingsPanel.add(new JLabel("Режим:"));
        settingsPanel.add(modeComboBox);

        settingsPanel.add(new JLabel("Ключ (сдвиг):"));
        keyField.setText("3"); //значение по умолчанию
        settingsPanel.add(keyField);

        //объединение врехней части
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.add(filePanel);
        topContainer.add(settingsPanel);
        add(topContainer, BorderLayout.CENTER);

        //3. нижняя панель (запуск статус)
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        JButton btnExecute = new JButton("ВЫПОЛНИТЬ ОПЕРАЦИЮ");
        btnExecute.setFont(new Font("Arial", Font.BOLD, 14));
        btnExecute.setBackground(new Color(70, 130, 180));
        btnExecute.setForeground(Color.WHITE);

        bottomPanel.add(btnExecute, BorderLayout.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        //проводник для выбора исх файла
        btnBrowseInput.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(new File("."));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                inputPathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        //проводник для выбора файла оконч
        btnBrowseOutput.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(new File("."));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                outputPathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        //блок/разбл поля ключа в зависимости от режима взлома
        modeComboBox.addActionListener(e -> {
            int mode = modeComboBox.getSelectedIndex();
            //если выбран брут форс или стат анализ то ключ вводить не нужно
            keyField.setEnabled(mode == 0 || mode == 1);
        });

        //главная кнопка действия
        btnExecute.addActionListener(e -> processCryptoAction());
    }

    private void processCryptoAction() {
        String inputPath = inputPathField.getText().trim();
        String outputPath = outputPathField.getText().trim();
        int mode = modeComboBox.getSelectedIndex();

        //1. валидация файлов
        if (!validator.isFileExists(inputPath) || !validator.isSafeFilename(inputPath)) {
            showError("Ошибка: Указан неверный или несуществующий исходный файл!");
            return;
        }
        if (outputPath.isEmpty() || !validator.isSafeFilename(outputPath)) {
            showError("Ошибка: Укажите корректный путь для сохранения результата!");
            return;
        }

        //2. валидация и парсинг ключа (только для шифр/дешифр)
        int key = 0;
        if (mode == 0 || mode == 1) {
            try {
                key = Integer.parseInt(keyField.getText().trim());
                if (!validator.isValidKey(key)) {
                    showError("Ошибка: Ключ должен быть положительным числом!");
                    return;
                }
                key = key % Alphabet.getSize(); //нормализация
            } catch (NumberFormatException ex) {
                showError("Ошибка: Ключ должен быть целым числом!");
                return;
            }
        }

        //3. выполнение операции
        try {
            statusLabel.setForeground(Color.BLACK);
            statusLabel.setText("Обработка файла... Пожалуйста, подождите.");

            String content = fileManager.readFile(inputPath);
            String resultText = "";

            switch (mode) {
                case 0 -> resultText = cipher.encryptText(content, key);
                case 1 -> resultText = cipher.decryptText(content, key);
                case 2 -> resultText = bruteForce.decryptByBruteForce(content);
                case 3 -> resultText = analyzer.decryptBySpaceAnalysis(content);
            }

            fileManager.writeFile(resultText, outputPath);

            statusLabel.setForeground(new Color(34, 139, 34)); //зеленый цвет
            statusLabel.setText("Успешно! Результат сохранен в: " + new File(outputPath).getName());
            JOptionPane.showMessageDialog(this, "Операция успешно завершена!", "Готово", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            showError("Ошибка ввода-вывода при работе с файлом: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Произошла непредвиденная ошибка: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        statusLabel.setForeground(Color.RED);
        statusLabel.setText(message);
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGui gui = new MainGui();
            gui.setVisible(true);
        });
    }
}