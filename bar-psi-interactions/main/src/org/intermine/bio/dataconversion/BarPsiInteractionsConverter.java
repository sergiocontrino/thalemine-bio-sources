package org.intermine.bio.dataconversion;

/*
 * Copyright (C) 2002-2015 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.intermine.dataconversion.ItemWriter;
import org.intermine.metadata.Model;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.util.FormattedTextParser;
import org.intermine.xml.full.Item;


/**
 *
 * @author  sc
 */
public class BarPsiInteractionsConverter extends BioFileConverter

{
    private static final Logger LOG = Logger.getLogger(BarPsiInteractionsConverter.class);

    // default values
    private static final String DATASET_NAME = "BAR interactions data set";
    private static final String DATASOURCE_NAME = "BAR";

    private String dataSourceName = null;
    private String dataSetName = null;

    // for the moment dealing only with ath
    private Item org;
    private static final String ATH_TAXID = "3702";

    // Some constants
    private static final String UNIPROT = "uniprotkb:";
    private static final String TAIR = "tair:";
    private static final String PSI = "psi-mi:";
    private static final String PUBMED = "pubmed:";
    private static final String TAXID = "taxid:";
    private static final String DOI = "digital object identifier:";
    private static final String SEP = "-";

    // maps with <identifier, item identifier>
    private Map<String, String> pubs = new HashMap<String, String>();
    private Map<String, String> genes = new HashMap<String, String>();
    private Map<String, String> terms = new HashMap<String, String>();

    private Map<String, Item> expItems = new HashMap<String, Item>();
    private Map<String, String> miCodes = new HashMap<String, String>();
    private Map<MultiKey, Item> interactions = new HashMap<MultiKey, Item>();
    private String dataSetRef = null;

    // PUBMED LOOKUP
    private static final Map<String, String> pubLookup;
    static {
        Map<String, String> aMap = new HashMap<String, String>();;
        aMap.put("10.1126/science.1203877", "21798944");
        aMap.put("10.1126/science.1203659", "21798943");
        aMap.put("10.1016/j.devcel.2014.04.004", "24823379");
        aMap.put("10.1371/journal.pone.0027364", "22096563");
        aMap.put("10.1038/msb.2011.66", "21952135");
        aMap.put("10.1126/science.1251358", "24833385");
        aMap.put("10.1074/jbc.M110.157008", "20870712");
        aMap.put("10.3390/ijms13066582", "22837651");
        aMap.put("10.1371/journal.pone.0108344", "25295873");
        pubLookup = Collections.unmodifiableMap(aMap);
    }

    /**
     * Constructor
     * @param writer the ItemWriter used to handle the resultant items
     * @param model the Model
     * @throws ObjectStoreException
     *
     */
    public BarPsiInteractionsConverter(ItemWriter writer, Model model)
        throws ObjectStoreException {
//        super(writer, model, DATA_SOURCE_NAME, DATASET_TITLE);
        super(writer, model);
        createOrganismItem();
    }

    /**
     *
     *
     * {@inheritDoc}
     */
    public void process(Reader reader) throws Exception {
        File currentFile = getCurrentFile();

        if ("mitabOut.txt".equals(currentFile.getName())) {
            createDataSource();
            processFile(reader, org);
        } else {
//            LOG.warn("WWSS skipping file: " + currentFile.getName());
            throw new IllegalArgumentException("Unexpected file: "
                    + currentFile.getName());
        }
    }

    /**
     * create datasource and dataset
     *
     */
    private void createDataSource() throws ObjectStoreException {

        Item dataSource = createItem("DataSource");
        dataSource.setAttribute("name", getDataSourceName());
        store(dataSource);

        Item dataSet = createItem("DataSet");
        dataSet.setAttribute("name", getDataSetName());
        dataSet.setReference("dataSource", dataSource.getIdentifier());
        store(dataSet);

        dataSetRef = dataSet.getIdentifier(); // used in experiment
    }

    /**
     * Process all rows of the mitab file, available at
     * ****
     *
     * @param reader
     *            a reader for the mitab file
     *
     * @throws IOException
     * @throws ObjectStoreException
     *
     * FILE FORMAT tsv
     * NO HEADER
     *
     * InteractorA id!InteractorB id!A 2id!B 2id!A Aliases!B Aliases!
     * Interaction detection methods!First author!pubmedid!A taxid!B taxid!
     * Interaction types!Source databases!Interaction identifier(s)!Confidence score
     *
     *
     * EXAMPLE
     *
     * tair:At4g23810!tair:At2g41090!uniprotkb:Q9SUP6!uniprotkb:P30187!tair:ATWRKY53|tair:WRKY53!-!
     * -!-!pubmed:17360592!taxid:3702!taxid:3702!
     * psi-mi:"MI:2165"(BAR)!-!-!
     *
     *
     */
    private void processFile(Reader reader, Item organism)
        throws IOException, ObjectStoreException {
        Iterator<?> tsvIter;
        try {
            tsvIter = FormattedTextParser.parseTabDelimitedReader(reader);
        } catch (Exception e) {
            throw new BuildException("cannot parse file: " + getCurrentFile(), e);
        }

        int lineNumber = 0;

        while (tsvIter.hasNext()) {
            String[] line = (String[]) tsvIter.next();

            String geneIdA = parseToken(line [0]);
            String geneIdB = parseToken(line [1]);
            String detectionMethod = parseToken(line [6]);
            String pubMedId = parseToken(line [8]);
            String taxidA = parseToken(line[9]);
            String type = parseToken(line[11]);
            String score = line [14];

            // Not processing the following fields
            //
            // String protIdA = parseToken(line [2]);
            // String protIdB = parseToken(line [3]);
            // String geneSynA = parseToken(line [4]);
            // String geneSynB = parseToken(line [5]);
            // String FirstAuthor = line [7];
            // String db = parseToken(line [12]);
            // String taxidB = parseToken(line[10]);
            //
            // no data in file
            // String ids = line [13];

            // dealing only with ATH for now
            if (!taxidA.equalsIgnoreCase(ATH_TAXID)) {
                continue;
            }

            String refIdA = createBioEntity(geneIdA, "Gene");
            String refIdB = createBioEntity(geneIdB, "Gene");

            Item interaction = getInteraction(refIdA, refIdB);
            Item interactionDetail =  createItem("InteractionDetail");

            if (StringUtils.isNotBlank(pubMedId)) {
                Item exp = createPublication(pubMedId, interactionDetail);

                if (miCodes.get(detectionMethod) != null) {
                    String termItemId = getTerm(detectionMethod);
                    exp.addToCollection("interactionDetectionMethods", termItemId);
                }
            }

            interactionDetail.setAttribute("name", geneIdA.concat(SEP.concat(geneIdB)));

            if (StringUtils.isNumeric(score)) {
                interactionDetail.setAttribute("confidence", score);
            }

            if (miCodes.get(type) != null) {
                interactionDetail.setAttribute("relationshipType", miCodes.get(type));
                interactionDetail.setAttribute("type", getDescription(miCodes.get(type)));
            }
            interactionDetail.setReference("interaction", interaction);
            interactionDetail.addToCollection("dataSets", dataSetRef);
            interactionDetail.setAttribute("role1", "n/a");
            interactionDetail.setAttribute("role2", "n/a");
            // interactionDetail.addCollection(allInteractors);

            store(interactionDetail);
            lineNumber++;
        }
        LOG.info("MI CODES FINAL:" + miCodes.keySet());
        int expNr = storeAllExperiments();
        LOG.info("Created " + expNr + " experiments after parsing " + lineNumber
                + " interactions records.");
    }

    private Item getInteraction(String refId, String gene2RefId) throws ObjectStoreException {
        MultiKey key = new MultiKey(refId, gene2RefId);
        Item interaction = interactions.get(key);
        if (interaction == null) {
            interaction = createItem("Interaction");
            interaction.setReference("participant1", refId);
            interaction.setReference("participant2", gene2RefId);
            interactions.put(key, interaction);
            store(interaction);
        }
        return interaction;
    }

    /**
    * Parse the various fields of the record
    * @param value the field
    *
    **/
    private String parseToken(String value) {
        String token;
        // getting only the first element (fields with multiple values are not in use
        if (value.contains("|")) {
            token = value.split("|")[0];
        } else {
            token = value;
        }
        if (token.startsWith("-")) {
            return null;
        }

        // 0,1,4,5
        if (token.startsWith(TAIR)) {
            return token.replace(TAIR, "").toUpperCase();
        }
        // 2,3
        if (token.startsWith(UNIPROT)) {
            return token.replace(UNIPROT, "");
        }
        // 6,11,12
        if (token.startsWith(PSI)) {
            // fill map with definitions
            String miCode = token.replace(PSI, "").substring(1, 8);
            if (!miCodes.containsKey(miCode)) {
                String description = getDescription(token);
                miCodes.put(miCode, description);
                try {
                    createTerm (miCode, description);
                } catch (ObjectStoreException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return miCode;
        }
        // 8
        if (token.startsWith(PUBMED)) {
            return token.replace(PUBMED, "");
        }
        if (token.startsWith(DOI)) {
            String doiId = token.replace(DOI, "");
            if (pubLookup.containsKey(doiId)) {
                return pubLookup.get(doiId);
            }
            LOG.warn("MISSING PubMedID for publication: " + doiId);
            return DOI;
        }
        // 9,10
        if (token.startsWith(TAXID)) {
            return token.replace(TAXID, "");
        }
        return token;
    }

    /**
     * @param token
     * @return
     */
    private String getDescription(String token) {
        if (token.contains("(")) {
            String a = StringUtils.substringAfterLast(token, "(");
            String description = StringUtils.substringBeforeLast(a, ")");
            return description;
        }
        if (token.startsWith("physical association")) {
            return "physical";
        }
        if (token.startsWith("predicted interaction")) {
            return "predicted";
        }
        if (token.startsWith("genetic")) {
            return "genetic";
        }
        return token;
    }

    /**
     * Create and store a BioEntity item on the first time called.
     *
     * @param primaryId the primaryIdentifier
     * @param type the type of bioentity (gene, exon..)
     * @throws ObjectStoreException
     */
    private String createBioEntity(String primaryId, String type) throws ObjectStoreException {
        // doing only genes here
        Item bioentity = null;

        if ("Gene".equals(type)) {
            if (!genes.containsKey(primaryId)) {
                bioentity = createItem("Gene");
                bioentity.setAttribute("primaryIdentifier", primaryId);
//                bioentity.setAttribute("symbol", "s_".concat(primaryId));
                store(bioentity);
                genes.put(primaryId, bioentity.getIdentifier());
            }
        }
        return genes.get(primaryId);
    }

    /**
     * Create and store a Publication item on the first time called.
     *
     * @param primaryId the primaryIdentifier
     * @param type gene or exon
     * @throws ObjectStoreException
     */
    private Item createPublication(String primaryId, Item interaction) throws ObjectStoreException {
        Item pub = null;
        Item exp = null;
        if (!pubs.containsKey(primaryId)) {
            pub = createItem("Publication");
            pub.setAttribute("pubMedId", primaryId);
            store(pub);
            pubs.put(primaryId, pub.getIdentifier());
            exp = createExperiment(primaryId, pub);
        } else {
            exp = expItems.get(primaryId);
        }
        interaction.setReference("experiment", exp.getIdentifier());
        return exp;
    }

    /**
     * @param primaryId
     * @param pub
     * @return
     * @throws ObjectStoreException
     */
    private Item createExperiment(String primaryId, Item pub)
        throws ObjectStoreException {
        Item exp;
        exp = createItem("InteractionExperiment");
        exp.setAttribute("name", "Exp-" + primaryId);
        exp.setReference("publication", pub);
        expItems.put(primaryId, exp);
        return exp;
    }

    private Integer storeAllExperiments () throws ObjectStoreException{
        int tot = 0;
        for (Item exp : expItems.values()) {
            store(exp);
            tot++;
        }
        return tot;
    }

    /**
     * create and store protein interaction terms
     * @param identifier identifier for interaction term
     * @return id representing term object
     *
     */
    private String getTerm(String identifier) throws ObjectStoreException {
        String itemId = terms.get(identifier);
        if (itemId == null) {
            Item term = createItem("InteractionTerm");
            term.setAttribute("identifier", identifier);
            itemId = term.getIdentifier();
            terms.put(identifier, itemId);
            store(term);
        }
        return itemId;
    }

    private String createTerm (String identifier, String name) throws ObjectStoreException {
        String itemId = terms.get(identifier);
        if (itemId == null) {
            Item term = createItem("InteractionTerm");
            term.setAttribute("identifier", identifier);
            term.setAttribute("name", name);
            itemId = term.getIdentifier();
            terms.put(identifier, itemId);
            store(term);
        }
        return itemId;
    }

    /**
     * Create and store a organism item on the first time called.
     *
     * @throws ObjectStoreException os
     */
    protected void createOrganismItem() throws ObjectStoreException {
        org = createItem("Organism");
        org.setAttribute("taxonId", ATH_TAXID);
        store(org);
    }

    /**
     * Set the name of the DataSource Item to create for this converter.
     * @param name the name
     */
    public void setDataSourceName(String name) {
        this.dataSourceName = name;
    }

    /**
     * Return the data source name set by setDataSourceName().
     * @return the data source name
     */
    public String getDataSourceName() {
        if (dataSourceName == null) {
            return DATASOURCE_NAME;
        } else {
            return dataSourceName;
        }
    }

    /**
     * Set the name of the DataSource Item to create for this converter.
     * @param name the name
     */
    public void setDataSetName(String name) {
        this.dataSetName = name;
    }

    /**
     * Return the data source name set by setDataSourceName().
     * @return the data source name
     */
    public String getDataSetName() {
        if (dataSetName == null) {
            return DATASET_NAME;
        } else {
            return dataSetName;
        }
    }

}
