import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\User\\Desktop\\basejavaProject\\basejava\\src");
        findFiles(file, "");
    }

    //homeTask
    private static void findFiles(File file, String tab) {
        tab += "\t";
        File[] files = file.listFiles();
        if (files != null) {
            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    System.out.println(tab + "directory: " + currentFile.getName());
                    findFiles(currentFile, tab);
                }
            }
        }
    }
}
