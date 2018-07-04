import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\User\\Desktop\\basejava");
        findFiles(file);
    }

    //homeTask
    private static void findFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File currentFile : files) {
                    findFiles(currentFile);
                }
            }
        } else {
            System.out.println(file.getAbsolutePath());
        }
    }
}
