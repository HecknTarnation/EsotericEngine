package net.heckntarnation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public class Test {

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        EngineConfig.DISPLAY.IS_BORDERLESS = false;
        EngineConfig.DISPLAY.IS_MAXIMIZED = true;
        EsotericEngine engine = EsotericEngine.Init();
        engine.LocalizationHandler.setLanguage("en_us");
        engine.WindowHandler.putString(engine.LocalizationHandler.localizeString("hello.world"));
    }

}
