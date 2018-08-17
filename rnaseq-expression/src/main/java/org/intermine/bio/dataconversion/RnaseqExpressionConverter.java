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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
 * @author sc
 */
public class RnaseqExpressionConverter extends BioFileConverter
{
    // TODO get those from project file?
    private static final String TAX_ID = "3702";
    private static final String DATASET_TITLE = "RNA-seq expression";
    private static final String DATASOURCE_NAME = "Araport";

    private static final String EXP_DATASET = "SRA";
    private static final String EXP_DATASOURCE = "NCBI";

    private static final Logger LOG = Logger.getLogger(RnaseqExpressionConverter.class);
    private static final String CATEGORY = "RNA-Seq";
    private static final String TPM = "TPM";

    private Item org;
    private Map<String, String> experiments = new HashMap<String, String>();
    private Map<String, String> geneItems = new HashMap<String, String>();
    private Map<String, String> transcriptItems = new HashMap<String, String>();

    private int totHeaders = 0;

    private String dataSetRef = null;
    /**
     * Constructor
     * @param writer the ItemWriter used to handle the resultant items
     * @param model the Model
     * @throws ObjectStoreException e
     */
    public RnaseqExpressionConverter(ItemWriter writer, Model model)
        throws ObjectStoreException {
        super(writer, model, DATASOURCE_NAME, DATASET_TITLE);
        createOrganismItem();
        createDataSource();
    }

    /**
     *
     *
     * {@inheritDoc}
     */
    public void process(Reader reader) throws Exception {
        //
        // Files can refer to genes, transcripts or experiments.
        // File name is expected to contains the relevant term
        // and to be in the same format, i.e.
        // [prefix]RNAseq-expression[sep]{type}[suffix]
        // where all [] are optional and fixed and {type} = {experiment, gene, transcript}
        // e.g.:
        // 113.RNAseq-expression.gene.tsv
        //
        // TODO: impose order(e/g/t) here?
        //
        File currentFile = getCurrentFile();
        if (currentFile.getName().contains("gene")) {
            LOG.info("Loading RNAseq expressions for GENES");
            processFile(reader, "gene", org);
        } else if (currentFile.getName().contains("transcript")) {
            LOG.info("Loading RNAseq expressions for TRANSCRIPTS");
            processFile(reader, "transcript", org);
        } else if (currentFile.getName().contains("experiment")) {
            LOG.info("Loading RNAseq expressions METADATA");
            processFile(reader, "experiment", org);
        } else {
            throw new IllegalArgumentException("Unexpected file: "
                    + currentFile.getName());
        }
    }

    /**
     * Process all rows of the expression files
     *
     * @param reader
     *            a reader for the
     *            SAMPLE_NRsample.gene and SAMPLE_NRsample.transcript
     *            files
     * @throws IOException
     * @throws ObjectStoreException
     */
    private void processFile(Reader reader, String type, Item organism)
        throws IOException, ObjectStoreException {
        Iterator<?> tsvIter;
        try {
            tsvIter = FormattedTextParser.parseTabDelimitedReader(reader);
        } catch (Exception e) {
            throw new BuildException("cannot parse file: " + getCurrentFile(), e);
        }
        String [] headers = null;
        String [] currentExp = null;
        int lineNumber = 0;

        while (tsvIter.hasNext()) {
            String[] line = (String[]) tsvIter.next();
            LOG.debug("BIOENTITY " + line[0]);
            if (lineNumber == 0) {
                // column headers - strip off any extra columns
                int end = 0;
                for (int i = 0; i < line.length; i++) {
                    if (StringUtils.isEmpty(line[i])) {
                        break;
                    }
                    end++;
                }
                headers = new String[end];
                System.arraycopy(line, 0, headers, 0, end);
                totHeaders = headers.length;
            } else {
                String primaryId = line[0]; //Gene id
                // if empty lines at the end of the file
                if (StringUtils.isEmpty(primaryId)) {
                    break;
                }
                if ("gene".equalsIgnoreCase(type)) {
                    createFeature(primaryId, "Gene");
                }
                if ("transcript".equalsIgnoreCase(type)) {
                    createFeature(primaryId, "Transcript");
                }
                if ("experiment".equalsIgnoreCase(type)) {
                    // file has the format
                    // SRA accession Category Sample Description
                    // in our model
                    // SRA accession, tissue, description

                    currentExp = new String[totHeaders];
                    System.arraycopy(line, 0, currentExp, 0, totHeaders);
                    LOG.info("EEE " + currentExp[0] + ": " + currentExp[1]);

                    String expId = currentExp[0];
                    if (!experiments.containsKey(expId)) {
                        Item experiment = createExperiment(expId, currentExp[1], currentExp[2]);
                        experiments.put(expId, experiment.getIdentifier());
                    }
                    continue; // experiment file: no info on bioentity
                }
                // scores start from column 2 and end at totHeaders which is headers[1,SampleNumber]
                for (int i = 1; i < totHeaders; i++) {
                    String col = headers[i].replace("_TPM", "");
                    if (!experiments.containsKey(col)) {
                        Item experiment = createExperiment(col);
                        experiments.put(col, experiment.getIdentifier());
                    }
                    Item score = createRNASeqExpression(line[i], type);
                    if (type.equalsIgnoreCase("gene")) {
                        score.setReference("expressionOf", geneItems.get(primaryId));
                    }
                    if (type.equalsIgnoreCase("transcript")) {
                        score.setReference("expressionOf", transcriptItems.get(primaryId));
                    }
                    score.setReference("experiment", experiments.get(col));
                    score.setReference("organism", organism);
                    store(score);
                }
            }
            lineNumber++;
        }
    }

    /**
     * Create and store a RnaseqExpression item on the first time called.
     *
     * @param score the expression score
     * @param type gene or transcript
     * @return an Item representing the GeneExpressionScore
     */
    private Item createRNASeqExpression(String score, String type) throws ObjectStoreException {
        Item expression = createItem("RnaseqExpression");
        expression.setAttribute("expressionLevel", score);
        expression.setAttribute("unit", TPM);
        expression.setAttribute("type", type);
        return expression;
    }

    /**
     * Create and store a BioEntity item on the first time called.
     *
     * @param primaryId the primaryIdentifier
     * @param type gene or transcript
     * @throws ObjectStoreException
     */
    private void createFeature(String primaryId, String type) throws ObjectStoreException {
        Item feature = null;
        LOG.debug("BIO: " + type + " -- " + primaryId);
        if ("Gene".equals(type)) {
            if (!geneItems.containsKey(primaryId)) {
                feature = createItem("Gene");
                feature.setAttribute("primaryIdentifier", primaryId);
                store(feature);
                geneItems.put(primaryId, feature.getIdentifier());
            }
        } else if ("Transcript".equals(type)) {
            if (!transcriptItems.containsKey(primaryId)) {
                feature = createItem("Transcript");
                feature.setAttribute("primaryIdentifier", primaryId);
                store(feature);
                transcriptItems.put(primaryId, feature.getIdentifier());
            }
        }
    }

    /**
     * Create and store a organism item on the first time called.
     *
     * @throws ObjectStoreException os
     */
    protected void createOrganismItem() throws ObjectStoreException {
        org = createItem("Organism");
        org.setAttribute("taxonId", TAX_ID);
        store(org);
    }

    /**
     * Create and store an Experiment item on the first time called.
     *
     * used if an experiment has not been previously found in the metadata (experiment) file
     *
     * @param name the cell line name
     * @return an Item representing the Experiment
     */
    private Item createExperiment(String name) throws ObjectStoreException {
        LOG.warn("EXPERIMENT " + name
                + " missing tissue information: should you check the consistency of naming "
                + "for your data files?");
        Item e = createItem("RnaseqExperiment");
        e.setAttribute("SRAaccession", name);
        e.setAttribute("category", CATEGORY);
        e.setReference("dataSet", dataSetRef);
        store(e);
        return e;
    }

    /**
     * Create and store an Experiment item on the first time called.
     *
     * @param name the experiment name (SRA accession)
     * @param tissue the tissue/organ
     * @param description the title of the experiment
     * @return an Item representing the Experiment
     */
    private Item createExperiment(String name, String tissue, String description)
        throws ObjectStoreException {
        LOG.debug("EXPE: " + name);
        Item e = createItem("RnaseqExperiment");
        e.setAttribute("SRAaccession", name);
        e.setAttribute("tissue", tissue);
        e.setAttribute("description", description);
        e.setReference("dataSet", dataSetRef);
        store(e);
        return e;
    }

    /**
     * create the experiments datasource and dataset
     *
     */
    private void createDataSource() throws ObjectStoreException {

        Item dataSource = createItem("DataSource");
        dataSource.setAttribute("name", EXP_DATASOURCE);
        store(dataSource);

        Item dataSet = createItem("DataSet");
        dataSet.setAttribute("name", EXP_DATASET);
        dataSet.setReference("dataSource", dataSource.getIdentifier());
        store(dataSet);

        dataSetRef = dataSet.getIdentifier(); // used in experiment
    }


}

