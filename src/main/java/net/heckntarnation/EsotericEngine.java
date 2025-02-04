package net.heckntarnation;

import net.heckntarnation.handlers.*;

import java.io.UnsupportedEncodingException;

public class EsotericEngine {

    private static EsotericEngine _instance;
    public InputHandler InputHandler;
    public WindowHandler WindowHandler;
    public LocalizationHandler LocalizationHandler;
    public JSONHandler JSONHandler;
    public FileHandler FileHandler;
    public ScriptHandler ScriptHandler;

    /**
     * Creates and initializes the engine. This must be run before anything else in the engine can be used.
     * @return the engine object, logging a warning if already initialized
     */
    public static EsotericEngine Init() throws UnsupportedEncodingException {
        if (_instance != null) {
            //TODO: logging
            return _instance;
        }
        //Ensures that Swing uses OpenGL instead of software rendering. This is needed for some overlays, such as Steam.
        //Can be changed by changing the EngineVar DISPLAY.USE_SOFTWARE_RENDERING = true.
        System.setProperty("sun.java2d.opengl", !EngineConfig.DISPLAY.USE_SOFTWARE_RENDERING + "");

        return new EsotericEngine();
    }

    /**
     * Runs the UnInit method on all handlers. It is safe to let the engine object fall out of scope after this.
     */
    public void UnInit(){
        InputHandler.Uninit();
        WindowHandler.Uninit();
        LocalizationHandler.Uninit();
        JSONHandler.Uninit();
        FileHandler.Uninit();
        ScriptHandler.Uninit();

        _instance = null;
        System.gc();
    }

    /**
     * @return the instance of the engine object.
     */
    public static EsotericEngine GetInstance(){
        return _instance;
    }

    protected EsotericEngine() throws UnsupportedEncodingException {
        EsotericEngine._instance = this;
        //Initialize default handlers.
        this.InputHandler = new InputHandler();
        InputHandler.Init();
        this.WindowHandler = new WindowHandler();
        WindowHandler.Init();
        this.LocalizationHandler = new LocalizationHandler();
        LocalizationHandler.Init();
        this.JSONHandler = new JSONHandler();
        JSONHandler.Init();
        this.FileHandler = new FileHandler();
        FileHandler.Init();
        this.ScriptHandler = new ScriptHandler();
        ScriptHandler.Init();
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