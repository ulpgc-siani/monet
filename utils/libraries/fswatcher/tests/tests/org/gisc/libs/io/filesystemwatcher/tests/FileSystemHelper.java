package tests.org.gisc.libs.io.filesystemwatcher.tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileSystemHelper {
        
    public static File createFile(String filename) throws IOException {
        File file = new File(filename);
        file.createNewFile();       
        return file;
    }
    
    public static void writeFile(File file, String content) throws IOException {
        FileWriter writer = new FileWriter(file);
        try {
            writer.write(content);
        } 
        finally {
            writer.close();
        }
    }
    
    public static void deleteFile(String filename) {        
        File file = new File(filename);
        file.delete();
    }    
}
