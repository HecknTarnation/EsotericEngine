package com.heckntarnation.rpgengine;

import com.heckntarnation.rpgengine.handlers.*;
import com.heckntarnation.rpgengine.heckscript.HeckScript;
import com.heckntarnation.rpgengine.heckscript.InvalidCharException;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ben
 */
public class RPGEngine {

    public static HeckScriptHandler heckHandler = new HeckScriptHandler();

    /**
     * Can be used to change handlers to custom ones, use the no parameter
     * version to just initialize without changing. Leave any parameter blank to
     * use its default.
     *
     * @param dHandler
     */
    public static void init(HeckScriptHandler dHandler) {
        heckHandler = dHandler == null ? heckHandler : dHandler;
        init();
    }

    /**
     * Initializes the engine.
     */
    public static void init() {
        test();
    }

    //testing
    public static void test() {
        File file = new File("./testscript.hs");
        HeckScript script = heckHandler.readHeckScriptFile(file);
        try {
            heckHandler.runHeckScirpt(script);
        } catch (InvalidCharException ex) {
            Logger.getLogger(RPGEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RPGEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
