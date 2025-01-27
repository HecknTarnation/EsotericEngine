package net.heckntarnation;

import jexer.TApplication;

import java.io.File;

public class EngineConfig {

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
         * Can the application window be resized?
         */
        public static boolean IS_RESIZEABLE = false;

        /**
         * Does the application window have a border?
         */
        public static boolean IS_BORDERLESS = false;

        /**
         * Is the application window maximized?
         */
        public static boolean IS_MAXIMIZED = true;

        /**
         * Returns the desired pixel resolution of the window.
         * @return short[width, height]
         */
        public static int[] GetDesiredPixelResolution(){
            return new int[]{APPLICATION_WIDTH * (FONT_SIZE/2), APPLICATION_HEIGHT * FONT_SIZE};
        }
    }

    public class INPUT {

    }

    public class CORE {
        /**
         * Directory for the engine to cache data.
         * By default, it is a folder ('cache') located where the engine jar is.
         */
        public static File ENGINE_CACHE = new File(new File(ClassLoader.getSystemClassLoader().getResource(".").getPath()).getParentFile(), "cache");
        /**
         * The max size of the cache for localized strings. Stringed loaded from file are cached in memory until this limit is reached where, then, the oldest is removed.
         */
        public static int MAX_LOCALIZATION_CACHE_SIZE = 20;
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
            short width = EngineConfig.GAME.MAIN_WINDOW_WIDTH == -1 ? EngineConfig.DISPLAY.APPLICATION_WIDTH : EngineConfig.GAME.MAIN_WINDOW_WIDTH;
            short height = EngineConfig.GAME.MAIN_WINDOW_HEIGHT == -1 ? EngineConfig.DISPLAY.APPLICATION_WIDTH : EngineConfig.GAME.MAIN_WINDOW_HEIGHT;
            return new short[]{width, height};
        }
    }

}
