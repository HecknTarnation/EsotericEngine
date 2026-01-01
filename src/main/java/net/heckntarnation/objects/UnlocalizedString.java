package net.heckntarnation.objects;

import net.heckntarnation.EsotericEngine;

public class UnlocalizedString {

    public String key;

    public UnlocalizedString(String key){
        this.key = key;
    }

    public String localize(){
        return EsotericEngine.GetInstance().LocalizationHandler.localizeString(this.key);
    }

}
