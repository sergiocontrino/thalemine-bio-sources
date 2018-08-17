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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.dataconversion.ItemWriter;
import org.intermine.metadata.Model;
import org.intermine.metadata.Util;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.sql.Database;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

/**
 *
 * @author sc
 */
public class BarExpressionsConverter extends BioDBConverter
{
    private static final Logger LOG =
        Logger.getLogger(BarExpressionsConverter.class);
    // used to correct a data issue in this data set..
    private static final String BAD_BAR = "atgenexp";
    private static final String DATA_SOURCE_NAME = "BAR";

    private static final int TAXON_ID = 3702;
    private static final String SAMPLE_CONTROL = "control";
    private static final String SAMPLE_TREATMENT = "treatment";

    private static final List<String> SAMPLE_ATTRS =
            Arrays.asList(
                    "name",
                    "alias",
                    "description",
                    "control",
                    "replication",
                    "file");

    private static final List<String> PROPERTY_TYPES =
            Arrays.asList(
                    "stock",
                    "geneticVar",
                    "tissue",
                    "diseased",
                    "growthCondition",
                    "growthStage",
                    "timePoint");

    private static final List<String> SOURCES_ALT_HEADER =
            Arrays.asList(
                    "atgenexp_hormone",
                    "atgenexp_pathogen");


    //pi, item Id
    private Map<String, String> labIdRefMap = new HashMap<String, String>();
    //barId, item Id
//    private Map<Integer, String> experimentIdRefMap = new HashMap<Integer, String>();
    private Map<String, String> experimentIdRefMap = new HashMap<String, String>();
    //barId, item Id
    private Map<Integer, String> sampleIdRefMap = new HashMap<Integer, String>();
    //probeset, item Id
    private Map<String, String> probeIdRefMap = new HashMap<String, String>();
    //propertyType.propertyValue, item Id
    private Map<String, String> propertyIdRefMap = new HashMap<String, String>();
    //propertyType.propertyValue, objectId
    private Map<String, Integer> propertyIdMap = new HashMap<String, Integer>();
    //property objectId, list of sampleRefId
    private Map<Integer, Set<String>> propertySampleMap = new HashMap<Integer, Set<String>>();

    //sample id, sample objectId
    private Map<Integer, Integer> sampleMap = new HashMap<Integer, Integer>();

    //sample_repl, list of replicates sample_Id
    private Map<String, Set<Integer>> replicatesMap = new HashMap<String, Set<Integer>>();
    //sample_id treat, list of controls sample_Id
    private Map<Integer, Set<Integer>> treatmentControlsMap = new HashMap<Integer, Set<Integer>>();

    //sample_Id, probeset, avgSignal
    private Map<Integer, Map<String, Double>> averagesMap =
            new HashMap<Integer, Map<String, Double>>();

    private String dataSetRef = null;

    /**
     * Construct a new BarExpressionsConverter.
     * @param database the database to read from
     * @param model the Model used by the object store we will write to with the ItemWriter
     * @param writer an ItemWriter used to handle Items created
     */
//    public BarExpressionsConverter(Database database, Model model, ItemWriter writer) {
//        super(database, model, writer, DATA_SOURCE_NAME, DATASET_TITLE);
    public BarExpressionsConverter(Database database, Model model, ItemWriter writer) {
        super(database, model, writer);
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
            // a database has been initialised from properties starting with db.bar-expressions
            connection = getDatabase().getConnection();
        }

        createDataSource();
        processExperiments(connection);
        processSamples(connection);
        createSamplesAverages(connection);
        processSampleProperties(connection);
        setSamplePropertiesRefs(connection);
        setSampleRepRefs(connection);
        processSampleData(connection);
    }

    /**
     * create datasource and dataset
     *
     */
    private void createDataSource()
        throws ObjectStoreException {

        Item dataSource = createItem("DataSource");
        dataSource.setAttribute("name", DATA_SOURCE_NAME);

        Item dataSet = createItem("DataSet");
        dataSet.setAttribute("name", getDataSourceName());

        store(dataSource);

        dataSet.setReference("dataSource", dataSource.getIdentifier());
        store(dataSet);
        dataSetRef = dataSet.getIdentifier(); // used in experiment
    }

    /**
     * process the experiments (bar projects)
     * @param connection
     */
    private void processExperiments(Connection connection)
        throws SQLException, ObjectStoreException {
        ResultSet res = getExperiments(connection);
        while (res.next()) {
            String experimentBarId = res.getString(1);
            String title = res.getString(2);
            String pi = res.getString(3);
            String affiliation = res.getString(4);
            String address = res.getString(5);

            String experimentRefId = createExperiment(experimentBarId, title,
                    pi, affiliation, address);
            experimentIdRefMap.put(experimentBarId, experimentRefId);
        }
        res.close();
    }

    /**
     * process the samples
     * @param connection
     */
    private void processSamples(Connection connection)
        throws SQLException, ObjectStoreException {
        ResultSet res = getSamples(connection);
        Map<Integer, String> sampleControlMap = new HashMap<Integer, String>();

        while (res.next()) {
            String experimentBarId = res.getString(1);
            Integer sampleBarId = new Integer(res.getInt(2));
            String name = res.getString(3);
            String alias = res.getString(4);
            String description = res.getString(5);
            // TODO: correct data?
            String control = getCorrectValue(res.getString(6), sampleBarId);
            String replication = getCorrectValue(res.getString(7), sampleBarId);
            String file = res.getString(8);
            // to overcome data issue in seed_db (see MINE-257)
            if (StringUtils.isBlank(replication)) {
                continue;
            }
            String type = null;
            // add to the replicates map
            // Note: "replicates" also for set of controls
            Util.addToSetMap(replicatesMap, replication, sampleBarId);
            // set sample type and fill treatment-controls map
            if (control.equalsIgnoreCase(replication)) {
                type = SAMPLE_CONTROL;
            } else {
                type = SAMPLE_TREATMENT;
                // save controls
                sampleControlMap.put(sampleBarId, control);
            }

            String sampleRefId = createSample(experimentBarId,
                    sampleBarId, name, alias, description, control,
                    replication, file, type);
            sampleIdRefMap.put(sampleBarId, sampleRefId);
        }
        res.close();

        // add to the treatmentControls map (sample-id, set of controls)
        for (Entry<Integer, String> sc: sampleControlMap.entrySet()) {
            Set<Integer> controls = replicatesMap.get(sc.getValue());
            treatmentControlsMap.put(sc.getKey(), controls);
        }

        LOG.debug("REPLICATES: " + replicatesMap);
        LOG.debug("CONTROLS: " + treatmentControlsMap);
    }

    /**
     * to correct data issues
     * @param queried the value returned by the query
     * @param sampleBarId
     */
    private String getCorrectValue(String queried, Integer sampleBarId) {
        if (getDataSourceName().equalsIgnoreCase(BAD_BAR) && sampleBarId == 244) {
            return "CTRL_7";
        }
        return queried;
    }

    /**
     * create the averages for the groups of replicates
     * note that this includes also groups of controls
     * @param connection
     */
    // TODO possibly better using java, instead of db, test?

    private void createSamplesAverages(Connection connection)
        throws SQLException, ObjectStoreException {
        // scan replicates map and for each group of replicates get for each probe
        // the averages signal.
        // put this in a map, and link this map to all the samples in the group
        // (using another map)
        LOG.info("START sample averages");
        long bT = System.currentTimeMillis();

        for (Set<Integer> replicates: replicatesMap.values()) {
            // we don't fill the avg map if there is only 1 replicate treatment
            // (we will use the sample signal directly).
            // still doing the avg for the control (it is used for the ratio)
            // TODO: avoid avg for single replicate controls as well (save map probeset-signal?)
            if (replicates.size() == 1
                    && !treatmentControlsMap.containsValue(replicates)) {
                continue;
            }
            //probeset, avgSignal
            Map<String, Double> thisAveragesMap =
                    new HashMap<String, Double>();
            String inClause = getInClause(replicates);

            // get the averages for this set
            ResultSet res = getAverages(connection, inClause);
            while (res.next()) {
                String probeset = res.getString(1);
                Double avgSignal = res.getDouble(2);
                thisAveragesMap.put(probeset, avgSignal);
            }
            // fill averages map
            for (Integer sample: replicates) {
                averagesMap.put(sample, thisAveragesMap);
            }
        }
        LOG.info("AVG TIME: " + (System.currentTimeMillis() - bT) + " ms");
    }

    /**
     * builds the in clause string for the sql statement from the elements of the set
     * @param the set of sample
     */
    private String getInClause(Set<Integer> controls) {
        StringBuffer sb = new StringBuffer();
        for (Integer term : controls) {
            sb.append(term + ",");
        }
        sb.delete(sb.lastIndexOf(","), sb.length());
        return sb.toString();
    }

    /**
     * process the sample properties
     * @param connection
     */
    private void processSampleProperties(Connection connection)
        throws SQLException, ObjectStoreException {
        String source = this.getDataSourceName();

        ResultSet res = getSampleProperties(connection, source);
        while (res.next()) {
            Integer sampleBarId = new Integer(res.getInt(1));
            String stock = res.getString(2);
            String geneticVar = res.getString(3);
            String tissue = res.getString(4);
            String diseased = res.getString(5);
            String growthCondition = res.getString(6);
            String growthStage = res.getString(7);
            String timePoint = res.getString(8);

            String sampleRefId = createSampleProperties(sampleBarId,
                    stock, geneticVar, tissue, diseased,
                    growthCondition, growthStage, timePoint);
        }
        res.close();
    }

    /**
     * process the sample data (expressions)
     * @param connection
     */
    private void processSampleData(Connection connection)
        throws SQLException, ObjectStoreException {
        ResultSet res = getSampleData(connection);
        while (res.next()) {
            Integer sampleBarId = new Integer(res.getInt(1));
            String probeSet = res.getString(2);
            Double signal = res.getDouble(3);
            String call = res.getString(4);
            Double pValue = res.getDouble(5);

            String sampleDataRefId = createSampleData(sampleBarId,
                    probeSet, signal, call, pValue);
        }
        res.close();
    }

    /**
     * create an experiment
     * @param bar Id
     * @param title
     * @param pi
     * @param affiliation
     * @param address
     */
    private String createExperiment(String experimentBarId, String title,
            String pi, String affiliation, String address)
        throws ObjectStoreException {
        Item experiment = createItem("Experiment");
        experiment.setAttribute("experimentBarId", experimentBarId.toString());
        experiment.setAttribute("title", title);
        experiment.setAttribute("category", this.getDataSourceName());

        // check if lab already stored
        if (!labIdRefMap.containsKey(pi)) {
            LOG.info("LAB: " + pi);
            String labRefId = createLab(pi, affiliation, address);
            labIdRefMap.put(pi, labRefId);
        }

        experiment.setReference("lab", labIdRefMap.get(pi));
        experiment.setReference("dataSet", dataSetRef);

        store(experiment);
        return experiment.getIdentifier();
    }

    /**
     * create a lab
     * @param pi
     * @param affiliation
     * @param address
     */
    private String createLab(String pi, String affiliation, String address)
        throws ObjectStoreException {
        Item lab = createItem("Lab");
        lab.setAttribute("name", pi);
        if (StringUtils.isNotBlank(affiliation)) {
            lab.setAttribute("affiliation", affiliation);
        }
        if (StringUtils.isNotBlank(address)) {
            lab.setAttribute("address", address);
        }
        store(lab);
        return lab.getIdentifier();
    }

    /**
     * create a sample
     * @param experiment bar Id
     * @param sample bar Id
     * @param name
     * @param alias
     * @param description
     * @param control        obsolete
     * @param replication    obsolete
     * @param file
     * @param type
     *
     */
    private String createSample (String experimentBarId, Integer sampleBarId,
            String name, String alias, String description,
            String control, String replication, String file, String type)
        throws ObjectStoreException {

        // create list of values
        List<String> sampleValues =
                Arrays.asList(
                        name,
                        alias,
                        description,
                        control,
                        replication,
                        file);

        Item sample = createItem("Sample");
        sample.setAttribute("barId", sampleBarId.toString());

        int i = 0;
        for (String s: SAMPLE_ATTRS) {
            String attr = SAMPLE_ATTRS.get(i);
            String value = sampleValues.get(i);
            if (StringUtils.isBlank(value)) {
                LOG.debug("SAMPLE " + sampleBarId + ": empty sample value for " + s);
                i++;
                continue;
            }
            sample.setAttribute(attr, value);
            i++;
        }
        sample.setAttribute("type", type);

        sample.setReference("experiment", experimentIdRefMap.get(experimentBarId));
        Integer sampleObjId = store(sample);
        sampleMap.put(sampleBarId, sampleObjId);
        return sample.getIdentifier();
    }

    /**
     * set the references between samples (all) and their respective replicates
     * and between samples (treatments) and their controls
     * @param connection
     */
    private void setSampleRepRefs(Connection connection) throws ObjectStoreException {

        // replicates
        for (Entry<String, Set<Integer>> group: replicatesMap.entrySet()) {
            //String thisRep = group.getKey();
            ReferenceList collection = new ReferenceList();
            collection.setName("replicates");
            for (Integer sample: group.getValue()) {
                String sampleRef = sampleIdRefMap.get(sample);
                collection.addRefId(sampleRef);
            }
            // storing the reference for all (also to self!)
            if (!collection.equals(null)) {
                for (Integer sample: group.getValue()) {
                    Integer sampleOId = sampleMap.get(sample);
                    store(collection, sampleOId);
                }
            }
        }

        // treatment controls
        for (Entry<Integer, Set<Integer>> controls: treatmentControlsMap.entrySet()) {

            Integer treatment = controls.getKey();
            if (treatment == null) {
                continue;
            }
            ReferenceList collection = new ReferenceList();
            collection.setName("controls");
            if (controls.getValue() != null) {
                for (Integer sample: controls.getValue()) {
                    String sampleRef = sampleIdRefMap.get(sample);
                    collection.addRefId(sampleRef);
                }
            }
            // storing the references
            if (!collection.equals(null)) {
                Integer sampleOId = sampleMap.get(treatment);
                store(collection, sampleOId);
            }
        }
    }

    /**
     * create sample properties
     * @param sample bar id
     * @param stock
     * @param geneticVar
     * @param tissue
     * @param diseased
     * @param growthCondition
     * @param growthStage
     * @param timePoint
     */
    private String createSampleProperties(Integer sampleBarId,
            String stock, String geneticVar, String tissue, String diseased,
            String growthCondition, String growthStage, String timePoint)
            throws ObjectStoreException {

        // create list of values
        List<String> propertyValues =
                Arrays.asList(
                        stock,
                        geneticVar,
                        tissue,
                        diseased,
                        growthCondition,
                        growthStage,
                        timePoint);
        // needed to compensate for missing experiments for some samples
        String sampleIdRef = sampleIdRefMap.get(sampleBarId);

        if (sampleIdRef == null) {
            LOG.warn("SAMPLE id = " + sampleBarId
                    + ". The experiment for this sample is missing.");
            return "orphaned sample";
        }

        int i = 0;
        for (String p: PROPERTY_TYPES) {
            String name = PROPERTY_TYPES.get(i);
            String value = propertyValues.get(i);
            if (StringUtils.isBlank(value)) {
                LOG.debug("SAMPLE " + sampleBarId + ": empty prop value for " + p);
                i++;
                continue;
            }
            String propertyRefId = propertyIdRefMap.get(name.concat(value));
            if (propertyRefId == null) {
                // prop not yet seen: store it
                Item property = createItem("SampleProperty");
                property.setAttribute("name", name);
                property.setAttribute("value", value);
                Integer propObjId = store(property);

                propertyRefId = property.getIdentifier();
                propertyIdRefMap.put(name.concat(value), propertyRefId);
                propertyIdMap.put(name.concat(value), propObjId);
                Set<String> others = new HashSet<String>();
                others.add(sampleIdRef);
                propertySampleMap.put(propObjId, others);
                LOG.debug("SAMPLE " + sampleBarId + ": created property "
                        + p + " - " + value);
            } else {
             // setting ref if prop already in..
                Integer propObjId = propertyIdMap.get(name.concat(value));
                LOG.debug("SAMPLE " + sampleBarId + ": adding property "
                        + p + " - " + value + " to collection");
                Set<String> others = propertySampleMap.get(propObjId);
                others.add(sampleIdRef);
                propertySampleMap.put(propObjId, others);
            }
            i++;
        }
        return sampleIdRef;
    }

    /**
     * set the references between sample properties and samples
     * @param connection
     */
    private void setSamplePropertiesRefs(Connection connection)
        throws ObjectStoreException {
        for (Entry<Integer, Set<String>> prop: propertySampleMap.entrySet()) {
            Integer thisProp = prop.getKey();
            ReferenceList collection = new ReferenceList();
            collection.setName("samples");
            for (String sampleRef: prop.getValue()) {
                collection.addRefId(sampleRef);
            }
            if (!collection.equals(null)) {
                store(collection, thisProp);
            }
        }
    }

    /**
     * create sample data (expressions)
     * @param sample bar id
     * @param probe
     * @param signal
     * @param call
     * @param pValue
     */
    private String createSampleData(Integer sampleBarId,
            String probeSet, Double signal, String call, Double pValue)
        throws ObjectStoreException {
        // needed to compensate for missing experiments for some samples
        String sampleIdRef = sampleIdRefMap.get(sampleBarId);

        if (sampleIdRef == null) {
            // this is already logged in createSampleProperties
            return "orphaned sample";
        }

        // get the map of averages (replicates)
        Map<String, Double> avgMap = new HashMap<String, Double>();
        if (averagesMap.containsKey(sampleBarId)) {
            avgMap = averagesMap.get(sampleBarId);
        }

        // check this is a treatment and get the avg map for the control too
        // NOTES: - rounding for ratio is done after the calculation
        //        - there are 0 averages in the controls (-> ratio null)
        //        - if a single replicate (no avgMap) we use the sample signal directly
        //        - if there is a single control replicate, we still do the avg
        Map<String, Double> controlAvgMap = new HashMap<String, Double>();
        String ratio = null;
        String displayAvgControl = null;
        Double logRatio = null;
        Double avgSignal = getAvgSignal(signal, avgMap.get(probeSet));
        String displayAvgSignal = getFormat(avgSignal, "#.##");
        if (treatmentControlsMap.containsKey(sampleBarId)) {
            controlAvgMap = averagesMap.
                    get(treatmentControlsMap.get(sampleBarId).toArray()[0]);

            Double avgControl = controlAvgMap.get(probeSet);
            ratio = getRatio(avgSignal, avgControl, "#.##");
            displayAvgControl = getFormat(avgControl, "#.##");

            // do log if avgSignal != 0;
            logRatio = getLog(avgSignal, avgControl);
        }

        String probeRefId = probeIdRefMap.get(probeSet);
        if (probeRefId == null) {
            Item probe = createItem("Probe");
            probe.setAttribute("name", probeSet);
            store(probe);
            probeRefId = probe.getIdentifier();
            probeIdRefMap.put(probeSet, probeRefId);
        }

        Item sampleData = createItem("Expression");
        sampleData.setAttribute("signal", signal.toString());
        if (StringUtils.isNotBlank(call)) {
            sampleData.setAttribute("call", call);
        }
        sampleData.setAttribute("pValue", pValue.toString());
        sampleData.setAttribute("averageSignal", displayAvgSignal);

        // if this is a treatment, do the ratio between this value and the one
        // from the avg of the control sample for the same probe
        if (ratio != null) {
            sampleData.setAttribute("averageRatio", ratio);
            sampleData.setAttribute("averageControl", displayAvgControl);
            if (logRatio != null) { // avgSignal == 0
                sampleData.setAttribute("log2Ratio", getFormat(logRatio, "#.##"));;
            }
        }

        sampleData.setReference("sample", sampleIdRef);
        sampleData.setReference("probe", probeRefId);
        store(sampleData);
        return sampleData.getIdentifier();
    }

    private Double getLog(Double avgSignal, Double avgControl) {
        if (avgSignal == 0) {
            return null;
        }

        Double log2Ratio = Math.log(avgSignal/avgControl)/Math.log(2);
        return log2Ratio;
    }




    /**
     * Returns a string representation of the Double rounded and formatted
     * according to format
     * If signal and/or control is not a number, returns null
     *
     * @param signal Double
     * @param control Double
     * @param format String
     */
    private String getRatio(Double signal, Double control, String format)
    {
        if (control == null) {
            return null;
        }
        if (signal == null) {
            return null;
        }
        if (signal.isNaN()) {
            return null;
        }

        DecimalFormat df = new DecimalFormat(format);
        Double ratio = signal/control;
        if (ratio.isInfinite() || ratio.isNaN()) {
            return null;
        }
        return Double.valueOf(df.format(ratio)).toString();
    }

    /**
     * Returns a string representation of the Double rounded and formatted
     * according to format
     * If Double is not a number, returns null
     *
     * @param signal Double
     * @param format String
     */
    private String getFormat(Double signal, String format) {
        if (signal == null) {
            return null;
        }
        if (signal.isNaN()) {
            return null;
        }

        DecimalFormat df = new DecimalFormat(format);
        return Double.valueOf(df.format(signal)).toString();
    }


    /**
     * Checks if average available and returns it. Otherwise (single replicate) returns the signal
     *
     * @param signal Double
     * @param avgSignal Double
     */
    private Double getAvgSignal(Double signal, Double avgSignal) {
        if (avgSignal == null) {
            return signal;
        }
        // average available
        return avgSignal;
    }

    /**
     * Return the expressions from the bar-expressions table
     * This is a protected method so that it can be overridden for testing.
     * @param connection the bar database connection
     * @throws SQLException if there is a problem while querying
     * @return the expressions
     */
    protected ResultSet getExperiments(Connection connection) throws SQLException {
        String query =
                "SELECT p.proj_id, p.proj_res_area, p.proj_pi, "
                + " p.proj_pi_inst, p.proj_pi_addr "
                + " FROM proj_info p;";
//                + " FROM proj_info p WHERE proj_id not Like 'G%';";
        return doQuery(connection, query, "getExperiments");
    }

    /**
     * Return the samples from the bar-expressions table
     * This is a protected method so that it can be overridden for testing.
     * @param connection the bar database connection
     * @throws SQLException if there is a problem while querying
     * @return the samples
     */
    protected ResultSet getSamples(Connection connection) throws SQLException {
        String query =
                "SELECT p.proj_id, sb.sample_id, sb.sample_bio_name, sb.sample_alias, "
                        + "sg.sample_desc, sg.sample_ctrl, sg.sample_repl, sg.sample_file_name "
                        + "FROM sample_biosource_info sb, sample_general_info sg, proj_info p "
                        + "WHERE sb.sample_id=sg.sample_id "
                        + "AND p.proj_id=sb.proj_id;";
                        // + "AND p.proj_id=sb.proj_id AND sb.sample_id <245;";
        return doQuery(connection, query, "getSamples");
    }

    /**
     * Return the samples properties from the bar-expressions table
     * This is a protected method so that it can be overridden for testing.
     * @param connection the bar database connection
     * @param source depending on the expression database the field growth condition
     *               is present or absent.
     * @throws SQLException if there is a problem while querying
     * @return the samples properties
     */
    protected ResultSet getSampleProperties(Connection connection, String source)
        throws SQLException {
        String query = null;
        if (SOURCES_ALT_HEADER.contains(source)) {
            query = "SELECT sample_id, sample_stock_code, sample_genetic_var, "
                    + "sample_tissue, sample_diseased, sample_growth_condition, "
                    + "sample_growth_stage,sample_time_point "
                    + "FROM sample_biosource_info;";
            return doQuery(connection, query, "getSampleProperties");
        }
        query = "SELECT sample_id, sample_stock_code, sample_genetic_var, "
                + "sample_tissue, sample_diseased, sample_growth_cond, "
                + "sample_growth_stage,sample_time_point "
                + "FROM sample_biosource_info;";
        return doQuery(connection, query, "getSampleProperties");
    }

    /**
     * Return the expressions from the bar-expressions table
     * This is a protected method so that it can be overridden for testing.
     * @param connection the bar database connection
     * @throws SQLException if there is a problem while querying
     * @return the expressions
     */
    protected ResultSet getSampleData(Connection connection) throws SQLException {
        String query =
                "SELECT sample_id, data_probeset_id, data_signal, "
                + "data_call, data_p_val "
                + "FROM sample_data "
                + "WHERE data_probeset_id is not null;";  // added for pathogen
//        + "FROM sample_data limit 1000;";
        return doQuery(connection, query, "getSampleData");
    }

    /**
     * Return the average expressions from the bar-expressions table given a group
     * samples.
     * This is a protected method so that it can be overridden for testing.
     * @param connection the bar database connection
     * @param inClause String the in clause string for the query
     * @throws SQLException if there is a problem while querying
     * @return the expressions
     */
    protected ResultSet getAverages(Connection connection, String inClause)
        throws SQLException {
        String query =
                "SELECT data_probeset_id, avg(data_signal) "
                + "FROM sample_data "
                + "WHERE sample_id in (" + inClause + ") "
                + "GROUP BY data_probeset_id;";
//        + "FROM sample_data limit 1000;";
        return doQuery(connection, query, "getAverages");
    }

    /**
     * Default implementation that makes a data set title based on the data source name.
     * {@inheritDoc}
     */
    @Override
    public String getDataSetTitle(int taxonId) {
        return getDataSourceName() + " expressions data set";
    }

    /**
     * method to wrap the execution of a query with log info
     * @param connection
     * @param query
     * @param comment for not logging
     * @return the result set
     * @throws SQLException
     */
    private ResultSet doQuery(Connection connection, String query, String comment)
        throws SQLException {
        // see ModEncodeMetaDataProcessor
        LOG.info("executing: " + query);
        long bT = System.currentTimeMillis();
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(query);
        LOG.info("QUERY TIME " + comment + ": " + (System.currentTimeMillis() - bT) + " ms");
        return res;
    }

}
