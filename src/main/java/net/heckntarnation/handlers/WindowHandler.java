package net.heckntarnation.handlers;

import jexer.TApplication;
import jexer.TWindow;
import jexer.backend.Backend;
import jexer.backend.SwingBackend;
import jexer.menu.TMenu;
import net.heckntarnation.EngineConfig;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;

public class WindowHandler implements IHandler {

    //Holds the thread that is running the application.
    protected Thread applicationThread;
    //Holds the TApplication (Game window) instance
    protected TApplication tApplication;
    //Holds the main TWindow instance for gameplay
    protected TWindow mainWindow;

    @Override
    public void Init() {
        try {
            this.tApplication = new TApplication(EngineConfig.DISPLAY.BACKEND_TYPE, EngineConfig.DISPLAY.APPLICATION_WIDTH, EngineConfig.DISPLAY.APPLICATION_HEIGHT, EngineConfig.DISPLAY.FONT_SIZE);
        } catch (UnsupportedEncodingException e) {
            //TODO: logging
            throw new RuntimeException(e);
        }
        this.applicationThread = new Thread(tApplication);
        this.applicationThread.start();
        this.refreshApplicationSettings();
        short[] res = EngineConfig.GAME.GetDesiredMainWindowResolution();
        this.mainWindow = this.addWindow(EngineConfig.GAME.TITLE, 0, 0, res[0], res[1]);
        this.mainWindow.maximize();
    }

    @Override
    public void Uninit() {
        this.applicationThread.interrupt();
    }

    /**
     * Refreshes the application settings (width, height, resizable, maximized, borderless)
     * TODO: can cause a 'this.backBuffers[...] is null' error when called just after application window initialization.
     */
    public void refreshApplicationSettings(){
        tApplication.getScreen().setDimensions(EngineConfig.DISPLAY.APPLICATION_WIDTH, EngineConfig.DISPLAY.APPLICATION_HEIGHT);
        if (tApplication.getBackend() instanceof SwingBackend) {
            SwingBackend swingBackend = (SwingBackend) tApplication.getBackend();
            int[] resolution = EngineConfig.DISPLAY.GetDesiredPixelResolution();
            swingBackend.getSwingComponent().setDimensions(resolution[0], resolution[1]);
            swingBackend.getSwingComponent().getFrame().dispose();
            swingBackend.getSwingComponent().getFrame().setResizable(EngineConfig.DISPLAY.IS_RESIZEABLE);
            swingBackend.getSwingComponent().getFrame().setUndecorated(EngineConfig.DISPLAY.IS_BORDERLESS);
            swingBackend.getSwingComponent().getFrame().setExtendedState(EngineConfig.DISPLAY.IS_MAXIMIZED ? Frame.MAXIMIZED_BOTH : Frame.NORMAL);
            swingBackend.getSwingComponent().getFrame().setVisible(true);
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
