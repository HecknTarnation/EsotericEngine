package net.heckntarnation.handlers;

import net.heckntarnation.EngineConfig;
import net.heckntarnation.EsotericEngine;
import net.heckntarnation.objects.LimitedHashMap;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LocalizationHandler implements IHandler {

    protected String current_language;
    protected HashMap<String, File> loaded_languages;
    protected LimitedHashMap<String, String> cachedKeys;

    @Override
    public void Init() {
        this.loaded_languages = new HashMap<String, File>();
        this.cachedKeys = new LimitedHashMap<>(EngineConfig.CORE.MAX_LOCALIZATION_CACHE_SIZE);
        try {
            if (EngineConfig.CORE.LOAD_BUILTIN_LANGUAGES) {
                loadLanguage("en_us", EsotericEngine.GetInstance().FileHandler.getResourceAndCache("lang/en_us.json", "lang/en_us.json"));
            }
        }catch(IOException e){
            //TODO: logging
        }
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
        this.cachedKeys.clear();
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
            this.cachedKeys.put(key, lang.get(key));
            return lang.get(key);
        }
    }

    /**
     * Merge two language files. This can be used on built-in files to add/overwrite them
     */
    public void mergeLang(){}
}

