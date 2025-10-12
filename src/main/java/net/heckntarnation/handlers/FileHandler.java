package net.heckntarnation.handlers;

import net.heckntarnation.EngineConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FileHandler implements IHandler{
    @Override
    public void Init() {

    }

    @Override
    public void Uninit() {

    }

    /**
     * Reads the complete, text, file to a list with each line correlating to a list element.
     * @param file
     * @return a List<String> containing the file.
     */
    public List<String> readFiletoList(File file){
        try {
            return Files.readAllLines(file.getAbsoluteFile().toPath());
        } catch (IOException e) {
            //TODO: logging
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the complete, text, file.
     * @param file
     * @return a string containing the contents of the file.
     */
    public String readFile(File file){
        return String.join(System.lineSeparator(), readFiletoList(file));
    }



    /**
     * Gets a resource, as a InputStream, included in a jar file, given a classloader path
     * @param path
     * @return
     */
    public InputStream getResource(String path){
        return ClassLoader.getSystemClassLoader().getResourceAsStream(path);
    }

    /**
     * Gets a resource and extracts to the cache
     */
    public File getResourceAndCache(String inputPath, String outputPath) throws IOException {
        File outputFile = new File(EngineConfig.CORE.ENGINE_CACHE + outputPath);
        if(outputFile.exists()){outputFile.delete();}
        outputFile.mkdirs();
        outputFile.createNewFile();
        try {
            Files.copy(getResource(inputPath), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputFile;
    }
}
