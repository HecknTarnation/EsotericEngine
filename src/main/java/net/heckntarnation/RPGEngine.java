package net.heckntarnation;

import net.heckntarnation.handlers.*;

public class RPGEngine {

    private static RPGEngine _instance;
    public InputHandler InputHandler;

    /**
     * Creates and initializes the engine. This must be run before anything else in the engine can be used.
     * @return the engine object, logging a warning if already initialized
     */
    public static RPGEngine Init(){
        if (_instance != null){
            //TODO: logging
            return _instance;
        }
        return new RPGEngine();
    }

    /**
     * Runs the UnInit method on all handlers. It is safe to let the engine object fall out of scope after this.
     */
    public void UnInit(){
        InputHandler.Uninit();

        _instance = null;
        System.gc();
    }

    /**
     * @return the instance of the engine object.
     */
    public static RPGEngine GetInstance(){
        return _instance;
    }

    protected RPGEngine(){
        RPGEngine._instance = this;
        //Initialize default handlers.
        this.InputHandler = new InputHandler();
        InputHandler.Init();
    }

    /**
     * Returns the initialization state of the engine.
     * @return true if initialized, else false
     */
    public boolean GetInitializationState(){
        return _instance != null;
    }

    /**
     * Returns some info about the JVM in an Object[]
     *
     * @return an array [(int) Available Processors, <br>
     *     (long) Max Memory, <br>
     *     (long) Total Memory, <br>
     *     (long) Free Memory]
     */
    public static Object[] getJVMInfo() {
        Object[] info = new Object[4];
        info[0] = Runtime.getRuntime().availableProcessors();
        info[1] = Runtime.getRuntime().maxMemory();
        info[2] = Runtime.getRuntime().totalMemory();
        info[3] = Runtime.getRuntime().freeMemory();
        return info;
    }
}