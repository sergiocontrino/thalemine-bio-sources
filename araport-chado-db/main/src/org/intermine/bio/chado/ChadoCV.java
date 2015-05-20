package org.intermine.bio.chado;

/*
 * Copyright (C) 2002-2015 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.intermine.xml.full.Item;

/**
 * Objects of this class represent a controlled vocabulary from chado.
 * @author Kim Rutherford
 */
public class ChadoCV
{
    private final String cvName;
    private final Map<Integer, ChadoCVTerm> termMap = new HashMap<Integer, ChadoCVTerm>();
    private final Map<String, Item> termItemMap = new HashMap<String, Item>();
    private Map<String, Item> cvItem = new HashMap<String, Item>();

    /**
     * Create a new ChadoCV.
     * @param cvName the name of the cv in chado that this object represents.
     */
    public ChadoCV(String cvName) {
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
    /**
     * Add a cvterm and its chado id to this cv.
     * @param cvtermId the chado id = cvterm.cvterm_id
     * @param chadoCvTerm the ChadoCVTerm object
     */
    public void addByChadoId(Integer cvtermId, ChadoCVTerm chadoCvTerm) {
        termMap.put(cvtermId, chadoCvTerm);
    }

    /**
     * Return the ChadoCVTerm object for a given cvterm_id.
     * @param cvtermId the chado id = cvterm.cvterm_id
     * @return the ChadoCVTerm
     */
    public ChadoCVTerm getByChadoId(Integer cvtermId) {
        return termMap.get(cvtermId);
    }

    
    public void addByChadoName(String name, Item item) {
    	termItemMap.put(name, item);
    }
    
    public Item getByCVTermName(String name) {
        return termItemMap.get(name);
    }
    
    /**
     * Return a Set of the root CVTerms in this CV - ie. those with no parents.
     * @return the cvterms
     */
    public Set<ChadoCVTerm> getRootCVTerms() {
        HashSet<ChadoCVTerm> rootTerms = new HashSet<ChadoCVTerm>();

        for (ChadoCVTerm cvterm: termMap.values()) {
            if (cvterm.getDirectParents().size() == 0) {
                rootTerms.add(cvterm);
            }
        }

        return rootTerms;
    }

    /**
     * Return a Set of all the CVTerms in this CV.
     * @return the cvterms
     */
    public Set<ChadoCVTerm> getAllCVTerms() {
        return new HashSet<ChadoCVTerm>(termMap.values());
    }
}
