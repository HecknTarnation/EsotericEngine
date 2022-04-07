package com.heckntarnation.rpgengine;

import com.heckntarnation.rpgengine.handlers.*;
import com.heckntarnation.rpgengine.heckscript.HeckScript;
import java.io.File;

/**
 *
 * @author Ben
 */
public class RPGEngine {

    public static DialogHandler dioHandler = new DialogHandler();

    /**
     * Can be used to change handlers to custom ones, use the no parameter
     * version to just initialize without changing. Leave any parameter blank to
     * use its default.
     *
     * @param dHandler
     */
    public static void init(DialogHandler dHandler) {
        dioHandler = dHandler == null ? dioHandler : dHandler;
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
        HeckScript script = dioHandler.readHeckScriptFile(file);
        dioHandler.runHeckScirpt(script);
    }

}
