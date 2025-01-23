package net.heckntarnation.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
}
