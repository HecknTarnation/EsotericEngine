package net.heckntarnation;

import com.googlecode.lanterna.*;
import net.heckntarnation.objects.UnlocalizedString;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

public class Test {

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        EngineConfig.DISPLAY.IS_BORDERLESS = false;
        EngineConfig.DISPLAY.IS_MAXIMIZED = true;
        EsotericEngine engine = EsotericEngine.Init();
        engine.LocalizationHandler.setLanguage("en_us");
        engine.WindowHandler.putString(engine.LocalizationHandler.localizeString("hello.world"));
        engine.WindowHandler.doMenu(new UnlocalizedString[]{new UnlocalizedString("test_menu.1")});
    }

}
