package org.intermine.bio.dataconversion;

/*
 * Copyright (C) 2002-2011 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.intermine.dataconversion.ItemWriter;
import org.intermine.metadata.Model;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.sql.Database;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

/**
 *
 * @author Julie Sullivan
 */
public class BarInteractionsConverter extends BioDBConverter
{
    private static final Logger LOG =
            Logger.getLogger(BarInteractionsConverter.class);
    private static final String DATA_SOURCE_NAME = "BAR";
    private static final String DATASET_TITLE = "BAR Interactions data set";
    private static final int TAXON_ID = 3702;
    private Map<String, String> genes = new HashMap<String, String>();
    private Map<String, String> publications = new HashMap<String, String>();
    private Map<String, String> terms = new HashMap<String, String>();
    private static final String PUBMED_PREFIX = "PubMed";
    private static final String INTERACTION_TYPE_MI = "1110";
//    private static final String INTERACTION_DETECTION_MI = "0063";
//    private static final String INTERACTION_EXPERIMENT = "Not Available";
    private static final Map<String, String> PSI_TERMS = new HashMap<String, String>();

    static {
        PSI_TERMS.put("MI:1110", "predicted");
        PSI_TERMS.put("MI:0915", "physical");
        PSI_TERMS.put("MI:0218", "physical");
        PSI_TERMS.put("MI:0208", "genetic");
    }

    /**
     * Construct a new BarInteractionsConverter.
     * @param database the database to read from
     * @param model the Model used by the object store we will write to with the ItemWriter
     * @param writer an ItemWriter used to handle Items created
     */
    public BarInteractionsConverter(Database database, Model model, ItemWriter writer) {
        super(database, model, writer, DATA_SOURCE_NAME, DATASET_TITLE);
    }

    /**
     * {@inheritDoc}
     */
    public void process() throws Exception {

        Connection connection = null;

        if (getDatabase() == null) {
            // no Database when testing and no connection needed
            connection = null;
        } else {
            // a database has been initialised from properties starting with db.bar-interactions
            connection = getDatabase().getConnection();
        }
        processQueryResults(connection);
    }

    private void processQueryResults(Connection connection)
            throws SQLException, ObjectStoreException {
        ResultSet res = runInteractionsQuery(connection);
        while (res.next()) {
            String gene1 = res.getString(1).toUpperCase();
            String gene2 = res.getString(2).toUpperCase();
            Integer cv = new Integer(res.getInt(3));
            Integer index = new Integer(res.getInt(4));
            Double pcc = new Double(res.getDouble(5));
            String pubString = checkPubId(res.getString(6));
            String interactionsDetectionMI = res.getString(7);
            String interactionsDetection = res.getString(8);
            String interactionsTypeMI = res.getString(9);
            String interactionsType = res.getString(10);

            if (pubString == null) {
                continue;
            }

            // strings that represent the stored gene
            String geneRefId1 = getGene(gene1);
            String geneRefId2 = getGene(gene2);

            String interactionRefId = processInteraction(geneRefId1, geneRefId2);
            processInteractionDetails(interactionRefId, geneRefId1, geneRefId2, cv, pcc,
                    pubString, interactionsDetectionMI, interactionsTypeMI);
        }
        res.close();
    }

    /**
     * Return the pubmed identifier, after cleaning for various spurios entries in the db
     *
     * @param pubId the pubmed id
     * @return the corrected pubmed id
     */
    private String checkPubId (String pubId) {
        if (pubId == null) {
            return null;
        }
        if (!pubId.startsWith(PUBMED_PREFIX)) {
            return null;
        }
        // pubId = PubMed
        if (pubId.length() == PUBMED_PREFIX.length()) {
            return null;
        }
        // e.g. PubMed17076807, AI-1 MAIN
        if (pubId.contains(",")) {
            return pubId.substring(0, pubId.indexOf(","));
        }
        if (pubId.contains("\n")) {
            return pubId.substring(0, pubId.indexOf("\n"));
        }
        return pubId;
    }

    private String processInteraction(String geneRefId1, String geneRefId2)
            throws ObjectStoreException {

        Item interaction = createItem("Interaction");
        interaction.setReference("gene1", geneRefId1);
        interaction.setReference("gene2", geneRefId2);
        store(interaction);
        return interaction.getIdentifier();
    }

    private void processInteractionDetails(String interactionRefId, String geneRefId1,
            String geneRefId2, Integer cv, Double pcc, String pubString, String
            interactionsDetectionMI, String interactionsTypeMI)
                    throws ObjectStoreException {
        if (StringUtils.isBlank(interactionsTypeMI)) {
            // we now accept null interaction types
            Item detail = createItem("InteractionDetail");
            detail.setAttribute("cv", cv.toString());
            detail.setAttribute("pcc", pcc.toString());
            detail.addToCollection("dataSets", getDataSourceItem(getDataSetTitle(TAXON_ID)));
            detail.addToCollection("allInteractors", geneRefId1);
            detail.addToCollection("allInteractors", geneRefId2);
            detail.setReference("experiment", getExperiment(pubString, interactionsDetectionMI));
            detail.setReference("interaction", interactionRefId);
            store(detail);

        } else {
            String[] ids = StringUtils.split(interactionsTypeMI, "|");
            String[] items = getTerms(interactionsTypeMI);
            for (int i = 0; i < items.length; i++ ) {
                Item detail = createItem("InteractionDetail");
                detail.setAttribute("cv", cv.toString());
                detail.setAttribute("pcc", pcc.toString());
                detail.setAttribute("type", PSI_TERMS.get("MI:" + ids[i]));
                detail.addToCollection("dataSets", getDataSourceItem(getDataSetTitle(TAXON_ID)));
                detail.addToCollection("allInteractors", geneRefId1);
                detail.addToCollection("allInteractors", geneRefId2);
                detail.setReference("relationshipType", items[i]);
                detail.setReference("experiment", getExperiment(pubString, interactionsDetectionMI));
                detail.setReference("interaction", interactionRefId);
                store(detail);
            }
        }
    }

    private String getExperiment(String pubString, String interactionsDetectionMI)
            throws ObjectStoreException {
        Item experiment = createItem("InteractionExperiment");
        // only dealing with pubmed publications now
            String pubRefId = getPublicationFromPMID(pubString);
            if (StringUtils.isNotEmpty(pubRefId)) {
                experiment.setReference("publication", pubRefId);
            }
        experiment.setAttribute("name", StringUtils.replace(pubString, "\n", ", ", -1));
        if (StringUtils.isBlank(interactionsDetectionMI)) {
            Integer expId = store(experiment);
            return experiment.getIdentifier();
        }

        ReferenceList collection = new ReferenceList();
        collection.setName("interactionDetectionMethods");
        String[] items = getTerms(interactionsDetectionMI);
        for(int i = 0; i < items.length; i++) {
            collection.addRefId(items[i]);
        }
        Integer expId = store(experiment);
        store(collection, expId);
        return experiment.getIdentifier();
    }

    private String getPublicationFromPMID(String pubString) throws ObjectStoreException {
        String regexp = "PubMed(\\d+)";
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(pubString);
        String pubRefId = null;
        if(m.find()) {
            String pubMedId = m.group(1);
            pubRefId = publications.get(pubMedId);
            if (pubRefId == null) {
                Item publication = createItem("Publication");
                publication.setAttribute("pubMedId", pubMedId);
                store(publication);
                pubRefId = publication.getIdentifier();
                publications.put(pubMedId, pubRefId);
            }
        }
        return pubRefId;
    }

    private String getGene(String identifier)
            throws ObjectStoreException {
        String geneRefId = genes.get(identifier);

        // we've already seen this gene, don't store again
        if (geneRefId != null) {
            return geneRefId;
        }

        // create new gene
        Item gene = createItem("Gene");
        gene.setReference("organism", getOrganismItem(TAXON_ID));
        gene.setAttribute("primaryIdentifier", identifier);

        // put in our map
        geneRefId = gene.getIdentifier();
        genes.put(identifier, geneRefId);

        // store to database
        store(gene);

        return gene.getIdentifier();
    }

    private String[] getTerms(String identifier) throws ObjectStoreException {
        String[] ids = StringUtils.split(identifier, "|");
        String[] items = new String[ids.length];
        for(int i = 0; i < ids.length; i++) {
            String refId = terms.get(ids[i]);
            if (refId == null) {
                Item item = createItem("InteractionTerm");
                item.setAttribute("identifier", "MI:" + ids[i]);
                refId = item.getIdentifier();
                store(item);
                terms.put(ids[i], refId);
            }
            items[i] = refId;
        }
        return items;
    }

    /**
     * Default implementation that makes a data set title based on the data source name.
     * {@inheritDoc}
     */
    @Override
    public String getDataSetTitle(int taxonId) {
        return DATA_SOURCE_NAME + " Interactions data set";
    }

    /**
     * Return the interactions from the bar-interactions table
     * This is a protected method so that it can be overriden for testing.
     * @param connection the bar database connection
     * @throws SQLException if there is a problem while querying
     * @return the interactions
     */
    protected ResultSet runInteractionsQuery(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        String query = "select \"Protein1\", \"Protein2\", \"Quality\", \"Index\", \"Pcc\", \"Bind_id\", " +
                      "\"Interactions_detection_mi\", \"Interactions_detection\", \"Interactions_type_mi\", " +
                      "\"Interactions_type\" from interactions;";
        //String query = "select protein1, protein2, quality, index, pcc, bind_id, " +
        //        "interactions_detection_mi, interactions_detection, interactions_type_mi, " +
        //        "interactions_type from interactions;";
        ResultSet res = stmt.executeQuery(query);
        return res;
    }
}
