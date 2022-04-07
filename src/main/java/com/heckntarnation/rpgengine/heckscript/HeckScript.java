package com.heckntarnation.rpgengine.heckscript;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ben
 */
public class HeckScript {

    /**
     * This HashMap is used to store global variables for HeckScript.
     */
    public static HashMap<String, Object> GlobalVars = new HashMap<>();

    public static void init() {
        GlobalVars.put("test", "This is a test var.");
    }

    public String name;
    public ArrayList<String> lines;

    public HeckScript(String name, ArrayList<String> lines) {
        this.name = name;
        this.lines = lines;
    }

}
