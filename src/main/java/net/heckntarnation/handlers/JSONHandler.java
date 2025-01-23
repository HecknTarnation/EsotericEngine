package net.heckntarnation.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import net.heckntarnation.EsotericEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONHandler implements IHandler {

    protected Gson gson;

    @Override
    public void Init() {
        this.gson = new Gson();
    }

    @Override
    public void Uninit() {
        this.gson = null;
    }

    public HashMap parseHashMapFromFile(File file){
        String readFile = EsotericEngine.GetInstance().FileHandler.readFile(file);
        return gson.fromJson(readFile, HashMap.class);
    }
}
