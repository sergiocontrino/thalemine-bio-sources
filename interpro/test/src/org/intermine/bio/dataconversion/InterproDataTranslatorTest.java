package org.intermine.bio.dataconversion;

/*
 * Copyright (C) 2002-2005 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.Map;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.io.*;

import org.intermine.xml.full.FullParser;
import org.intermine.xml.full.ItemFactory;
import org.intermine.metadata.Model;
import org.intermine.bio.dataconversion.InterproDataTranslator;
import org.intermine.dataconversion.*;

public class InterproDataTranslatorTest extends DataTranslatorTestCase {
  private String tgtNs = "http://www.flymine.org/model/genomic#";
  private ItemFactory interproItemFactory;
  private ItemFactory genomicItemFactory;

  public InterproDataTranslatorTest(String arg) throws Exception {
    super(arg);
    interproItemFactory = new ItemFactory(Model.getInstanceByName("interpro"));
    genomicItemFactory = new ItemFactory(Model.getInstanceByName("genomic"));
  }

  public void setUp() throws Exception {
    super.setUp();
  }

  public void testTranslate() throws Exception {

    Map sourceItemMap = writeItems(getSrcItems());

    DataTranslator translator = new InterproDataTranslator(new MockItemReader(sourceItemMap), mapping, srcModel, getTargetModel(tgtNs));

    MockItemWriter tgtIw = new MockItemWriter(new LinkedHashMap());
    translator.translate(tgtIw);

    // uncomment to write out a new copy of tgt items
    //writeTgtFile(tgtIw);


    String expectedNotActual = "in expected, not actual: " + compareItemSets(new HashSet(getExpectedItems()), tgtIw.getItems());
    String actualNotExpected = "in actual, not expected: " + compareItemSets(tgtIw.getItems(), new HashSet(getExpectedItems()));

    if (expectedNotActual.length() > 27) {
      StringBuffer badMsg = new StringBuffer();
      badMsg.append("InterproDataTranslatorTest - source and target datasets disagree!\n");
      badMsg.append("---------------------------------EXPECTED REPLY--------------------------------\n");
      badMsg.append("ENA LENGTH:" + expectedNotActual.length() + " STRING:" + expectedNotActual);
      badMsg.append("\n---------------------------------ACTUAL REPLY--------------------------------\n");
      badMsg.append("ANE LENGTH:" + actualNotExpected.length() + " STRING:" + actualNotExpected);
      writeMessageToTestLogFile(badMsg.toString());
    } else {
      writeMessageToTestLogFile("InterproDataTranslatorTest - source and target datasets agree!");
    }

    assertEquals(new HashSet(getExpectedItems()), tgtIw.getItems());
  }

  protected Collection getExpectedItems() throws Exception {
    return FullParser.parse(getClass().getClassLoader().getResourceAsStream("test/InterproDataTranslatorFunctionalTest_tgt.xml"));
  }

  protected Collection getSrcItems() throws Exception {
    return FullParser.parse(getClass().getClassLoader().getResourceAsStream("test/InterproDataTranslatorFunctionalTest_src.xml"));
  }

  protected String getSrcModelName() {
    return "interpro";
  }

  protected String getModelName() {
    return "genomic";
  }

  private void writeTgtFile(MockItemWriter targetItemWriter) throws Exception {

    File tgtFile = new File("interpro_tgt.xml");

    if (tgtFile.exists()) {
      tgtFile.delete();
    }

    FileWriter fw = new FileWriter(tgtFile);
    fw.write(targetItemWriter.getItems().toString());
    fw.flush();
    fw.close();
  }

  private void writeMessageToTestLogFile(String message) throws Exception {
      System.err.println(message);
  }
}
