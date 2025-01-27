package net.heckntarnation;

import jexer.TWindow;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public class Test {

    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException, URISyntaxException {
        EsotericEngine engine = EsotericEngine.Init();
        engine.WindowHandler.setWindowTitle("Test");
        engine.LocalizationHandler.setLanguage("en_us");
        engine.LocalizationHandler.loadLanguage("en_us", new File(ClassLoader.getSystemClassLoader().getResource("lang/en_us.json").toURI()));
        engine.WindowHandler.getMainWindow().addLabel(engine.LocalizationHandler.localizeString("test.test"), 0, 0);
        engine.WindowHandler.getMainWindow().addLabel(engine.LocalizationHandler.localizeString("test.test2"), 0, 1);
        EngineConfig.DISPLAY.IS_BORDERLESS = false;
        EngineConfig.DISPLAY.IS_MAXIMIZED = true;
        engine.WindowHandler.refreshApplicationSettings();
    }

}
