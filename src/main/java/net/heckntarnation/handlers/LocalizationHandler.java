package net.heckntarnation.handlers;

import net.heckntarnation.EngineVars;
import net.heckntarnation.EsotericEngine;
import net.heckntarnation.objects.LimitedHashMap;

import java.io.File;
import java.util.Dictionary;
import java.util.HashMap;

public class LocalizationHandler implements IHandler {

    protected String current_language;
    protected HashMap<String, File> loaded_languages;
    protected LimitedHashMap<String, String> cachedKeys;

    @Override
    public void Init() {
        this.loaded_languages = new HashMap<String, File>();
        this.cachedKeys = new LimitedHashMap<>(EngineVars.CONFIG.MAX_LOCALIZATION_CACHE_SIZE);
    }

    @Override
    public void Uninit() {
        this.loaded_languages.clear();
        this.loaded_languages = null;
    }

    /**
     * Change the current language
     * @param lang
     */
    public void setLanguage(String lang){
        this.current_language = lang;

    }

    /**
     * Load a language so that it is accessible for localization.
     * @param lang
     * @param file
     */
    public void loadLanguage(String lang, File file){
        this.loaded_languages.put(lang, file);
    }

    public String localizeString(String key){
        if (cachedKeys.containsKey(key)){
            return cachedKeys.get(key);
        }else{
            HashMap<String, String> lang = EsotericEngine.GetInstance().JSONHandler.parseHashMapFromFile(this.loaded_languages.get(this.current_language));
            this.cachedKeys.put(this.current_language, key);
            return lang.get(key);
        }
    }
}

