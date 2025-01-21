package net.heckntarnation;

import jexer.TApplication;

public class EngineVars {

    public class DISPLAY {
        public static TApplication.BackendType BACKEND_TYPE = TApplication.BackendType.SWING;
        /**
         * Determines if rendering is done with OpenGL, default, or software rendering. Note: overlays such as Steam do not support software rendering
         */
        public static boolean USE_SOFTWARE_RENDERING = false;
        /**
         * Window's width, in characters.
         */
        public static short APPLICATION_WIDTH = 120;
        /**
         * Window's height, in characters.
         */
        public static short APPLICATION_HEIGHT = 60;
        /**
         * Size of the font used. This will increase the pixel size of each 'character' space, make sure to adjust the width and height values as well.
         */
        public static short FONT_SIZE = 16;
        /**
         * Can the window be resized.
         */
        public static boolean IS_RESIZEABLE = false;

        /**
         * Returns the pixel resolution of the window.
         * @return short[width, height]
         */
        public static int[] GetPixelResolution(){
            return new int[]{APPLICATION_WIDTH * (FONT_SIZE/2), APPLICATION_HEIGHT * FONT_SIZE};
        }
    }

    public class INPUT {

    }

    public class GAME {
        /**
         * The title of the game.
         */
        public static String TITLE = "Esoteric Engine";

        /**
         * Main sub-window's width and height, in characters. -1 means it will match DISPLAY.WINDOW_WIDTH/HEIGHT
         */
        public static short MAIN_WINDOW_WIDTH = -1, MAIN_WINDOW_HEIGHT = -1;

        /**
         * Returns the desired resolution, in characters, of the main window.
         * @return short[width, height]
         */
        public static short[] GetDesiredMainWindowResolution(){
            short width = EngineVars.GAME.MAIN_WINDOW_WIDTH == -1 ? EngineVars.DISPLAY.APPLICATION_WIDTH : EngineVars.GAME.MAIN_WINDOW_WIDTH;
            short height = EngineVars.GAME.MAIN_WINDOW_HEIGHT == -1 ? EngineVars.DISPLAY.APPLICATION_WIDTH : EngineVars.GAME.MAIN_WINDOW_HEIGHT;
            return new short[]{width, height};
        }
    }

}
