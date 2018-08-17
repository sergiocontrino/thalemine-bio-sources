package org.intermine.bio.chado;

import java.util.HashMap;
import java.util.Map;

import org.intermine.xml.full.Item;

public class CVInfo {

    private final String cvName;
    private Map<String, Item> cvItem = new HashMap<String, Item>();

    
    /**
     * Create a new ChadoCV.
     * @param cvName the name of the cv in chado that this object represents.
     */
    public CVInfo(String cvName) {
        this.cvName = cvName;
    }

    /**
     * Return the cvName that was passed to the constructor.
     * @return the cv name
     */
    public final String getCvName() {
        return cvName;
    }
    
    public void addByChadoCVName(String name, Item item) {
        cvItem.put( name, item);
    }
    
    public Item getByCVName(String name) {
        return cvItem.get(name);
    }
    
}
