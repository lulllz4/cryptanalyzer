import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Validator {

    public boolean isFileExists(String filePath) {
        if (filePath == null || filePath.isBlank()) return false;
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path);
    }

    public boolean isValidKey(int key) {
        return key >= 0;
    }

    //защита сист файлов от манипуляций
    public boolean isSafeFilename(String filePath) {
        return !filePath.contains(".bash_profile") && !filePath.contains("hosts");
    }
}