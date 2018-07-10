import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\User\\Desktop\\basejava\\src");
        findFiles(file, 0);
    }

    //homeTask
    private static void findFiles(File file, int depth) {
        depth++;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            builder.append("\t");
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    System.out.println(builder.toString() + "directory: " + currentFile.getName());
                    findFiles(currentFile, depth);
                } else {
                    System.out.println(builder.toString() + "file: " + currentFile.getName());
                }
            }
        }
    }
}
