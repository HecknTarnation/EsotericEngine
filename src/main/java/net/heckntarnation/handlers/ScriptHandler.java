package net.heckntarnation.handlers;

import net.heckntarnation.EsotericEngine;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class ScriptHandler implements IHandler{

    protected Globals globals;

    @Override
    public void Init() {
        globals = JsePlatform.standardGlobals();
    }

    @Override
    public void Uninit() {

    }

    /**
     * Runs the given lua script
     * @param script
     */
    public void executeScript(LuaValue script){
        script.call();
    }

    /**
     * Loads and executes the lua script at the given path.
     * @param path
     */
    public void executeScript(String path){
        this.executeScript(this.loadScript(path));
    }

    /**
     * Returns a loaded lua script.
     * @param path
     * @return a LuaValue representing the script
     */
    public LuaValue loadScript(String path){
        return globals.load(path);
    }

}
