package net.heckntarnation;

import java.io.File;

public class EngineConfig {

    public class DISPLAY {
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
        public static String ENGINE_CACHE = null;
        /**
         * The max size of the cache for localized strings. Stringed loaded from file are cached in memory until this limit is reached where, then, the oldest is removed.
         */
        public static int MAX_LOCALIZATION_CACHE_SIZE = 20;
        /**
         * Determines if the built-in language files should be loaded
         */
        public static boolean LOAD_BUILTIN_LANGUAGES = true;
    }

    public class GAME {
        /**
         * The title of the game.
         */
        public static String TITLE = "Esoteric Engine";
    }

}
