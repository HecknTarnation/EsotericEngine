package net.heckntarnation;

import jexer.TWindow;

import java.io.UnsupportedEncodingException;

public class Test {

    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
        EsotericEngine engine = EsotericEngine.Init();
        engine.WindowHandler.setWindowTitle("Test");
        engine.WindowHandler.maximizeApplication();

    }

}
