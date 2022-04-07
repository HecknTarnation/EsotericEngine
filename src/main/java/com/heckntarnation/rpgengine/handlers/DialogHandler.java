package com.heckntarnation.rpgengine.handlers;

import com.heckntarnation.rpgengine.heckscript.HeckScript;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles dialog, mainly by implementing the HeckScript language.
 *
 * @author Ben
 */
public class DialogHandler {

    /**
     * Parses a dialog file, returning a ready-to-use dialog object.
     *
     * @param file
     * @return
     */
    public HeckScript readHeckScriptFile(File file) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DialogHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        //array list to hold the script
        ArrayList<String> lines = new ArrayList<>();
        try {
            //reads the script file into the array
            String line = br.readLine();
            while (line != null) {
                lines.add(line + " ");
                line = br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(DialogHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new HeckScript(file.getName(), lines);
    }

    /**
     * Runs a HeckScript object.
     *
     * @param script
     */
    public void runHeckScirpt(HeckScript script) {

    }
}
