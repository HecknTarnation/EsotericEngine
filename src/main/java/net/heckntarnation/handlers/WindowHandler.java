package net.heckntarnation.handlers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import net.heckntarnation.EngineConfig;

import java.io.IOException;

public class WindowHandler implements IHandler {

    //Holds the reference to the Lanterna terminal
    protected Terminal terminal;


    @Override
    public void Init() {
        if(terminal != null){
            //TODO: logging
            return;
        }
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        try {
            this.terminal = factory.createTerminal();
            if(EngineConfig.DISPLAY.USE_PRIVATE_MODE) {
                this.terminal.enterPrivateMode();
            }
        } catch (IOException e) {
            //TODO: logging
        }
    }

    @Override
    public void Uninit() {
        try {
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the foreground and background color of the terminal
     * @param backgroundColor
     * @param foregroundColor
     */
    public void setColor(TextColor backgroundColor, TextColor foregroundColor) throws IOException {
        if (backgroundColor != null) {
            this.terminal.setBackgroundColor(backgroundColor);
        }
        if (foregroundColor != null) {
            this.terminal.setForegroundColor(foregroundColor);
        }
    }

    /**
     * Writes a string to the screen
     * @param stringToWrite
     * @throws IOException
     */
    public void putString(String stringToWrite) throws IOException {
        terminal.putString(stringToWrite);
        terminal.flush();
    }

    /**
     * Creates a Lanterna TextGraphics object
     * @return
     * @throws IOException
     */
    public final TextGraphics createTextGraphic() throws IOException {
        return terminal.newTextGraphics();
    }

    /**
     * Sets the cursor position
     * @param newPosition
     * @throws IOException
     */
    public void setCursorPosition(TerminalPosition newPosition) throws IOException {
        terminal.setCursorPosition(newPosition);
    }

    public void setCursorPosition(int x, int y) throws IOException {
        this.setCursorPosition(new TerminalPosition(x, y));
    }

    /**
     * Clears the screen
     * @throws IOException
     */
    public void clearScreen() throws IOException {
        terminal.clearScreen();
    }

    /**
     * Adds a listener for when the terminal is resized
     * @param listener
     */
    public void addResizeListener(TerminalResizeListener listener){
        terminal.addResizeListener(listener);
    }

}
