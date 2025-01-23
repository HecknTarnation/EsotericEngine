package net.heckntarnation.handlers;

import jexer.TApplication;
import jexer.TWindow;
import jexer.backend.Backend;
import jexer.backend.SwingBackend;
import jexer.menu.TMenu;
import net.heckntarnation.EngineVars;

import java.awt.*;
import java.io.UnsupportedEncodingException;

public class WindowHandler implements IHandler {

    //Holds the thread that is running the application.
    protected Thread applicationThread;
    //Holds the TApplication (Game window) instance
    protected TApplication tApplication;
    //Holds the main TWindow instance for gameplay
    protected TWindow mainWindow;

    //Is the application maximized
    protected boolean isMaximized = false;

    @Override
    public void Init() {
        try {
            this.tApplication = new TApplication(EngineVars.DISPLAY.BACKEND_TYPE, EngineVars.DISPLAY.APPLICATION_WIDTH, EngineVars.DISPLAY.APPLICATION_HEIGHT, EngineVars.DISPLAY.FONT_SIZE);
        } catch (UnsupportedEncodingException e) {
            //TODO: logging
            throw new RuntimeException(e);
        }
        this.applicationThread = new Thread(tApplication);
        this.applicationThread.start();
        short[] res = EngineVars.GAME.GetDesiredMainWindowResolution();
        this.mainWindow = this.addWindow(EngineVars.GAME.TITLE, 0, 0, res[0], res[1]);

    }

    @Override
    public void Uninit() {
        this.applicationThread.interrupt();
    }

    /**
     * Refreshes the window settings (width, height, resizable)
     */
    public void refreshWindowSettings(){
        tApplication.getBackend().getScreen().setDimensions(EngineVars.DISPLAY.APPLICATION_WIDTH, EngineVars.DISPLAY.APPLICATION_HEIGHT);
        if (tApplication.getBackend() instanceof SwingBackend) {
            SwingBackend swingBackend = (SwingBackend) tApplication.getBackend();
            int[] resolution = EngineVars.DISPLAY.GetDesiredPixelResolution();
            swingBackend.getSwingComponent().setDimensions(resolution[0], resolution[1]);
            swingBackend.getSwingComponent().getFrame().setResizable(EngineVars.DISPLAY.IS_RESIZEABLE);
        }
    }

    /**
     * Returns the main window instance.
     * @returns TWindow
     */
    public TWindow getMainWindow(){
        return this.mainWindow;
    }

    /**
     * Sets the title of the window.
     * @param title
     */
    public void setWindowTitle(String title){
        tApplication.getBackend().setTitle(title);
    }

    /**
     * Adds a menu to the top of the game window.
     * @param title
     * @return
     */
    public TMenu addMenu(String title){
        return this.tApplication.addMenu(title);
    }

    /**
     * Add a sub-window inside the game window.
     * @param title
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public TWindow addWindow(String title, int x, int y, int width, int height){
        return this.tApplication.addWindow(title, x, y, width, height);
    }

    /**
     * TODO: Function currently doesn't work as intended.
     * Maximize the application. If already maximized, un-maximize.
     * {Swing backend only}
     */
    public void maximizeApplication() throws InterruptedException {
        if (tApplication.getBackend() instanceof SwingBackend){
            SwingBackend swingBackend = (SwingBackend) tApplication.getBackend();
            //Toggles the maximized state
            swingBackend.getSwingComponent().getFrame().setExtendedState(swingBackend.getSwingComponent().getFrame().getExtendedState() != Frame.MAXIMIZED_BOTH
                    ? Frame.MAXIMIZED_BOTH : Frame.NORMAL);

            int width = swingBackend.getSwingComponent().getFrame().getWidth();
            int height = swingBackend.getSwingComponent().getFrame().getHeight();
            tApplication.getBackend().getScreen().setDimensions(width/(EngineVars.DISPLAY.FONT_SIZE), height/EngineVars.DISPLAY.FONT_SIZE);
            if(this.isMaximized) {
                short[] res = EngineVars.GAME.GetDesiredMainWindowResolution();
                this.mainWindow.setDimensions(0, 1, res[0], res[1]);
            }else {
                this.mainWindow.maximize();
            }
            this.isMaximized = !this.isMaximized;
        }
    }

    /**
     * Returns the current size, in pixels, of the application
     * {Swing backend only}
     * @return int[width, height], null if not swing backend.
     */
    public int[] getApplicationResolution(){
        Backend backend = this.tApplication.getBackend();
        if (backend instanceof SwingBackend){
            SwingBackend swingBackend = (SwingBackend) backend;
            int width = swingBackend.getSwingComponent().getWidth();
            int height = swingBackend.getSwingComponent().getHeight();
            return new int[]{width, height};
        }
        return null;
    }

    /**
     * Returns the current size, in characters, of the application.
     * @return int[width, height]
     */
    public int[] getApplicationCharacterResolution(){
        return new int[]{this.tApplication.getScreen().getWidth(), this.tApplication.getScreen().getHeight()};
    }
}
