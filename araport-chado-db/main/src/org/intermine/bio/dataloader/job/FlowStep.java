package org.intermine.bio.dataloader.job;

/*
 * Copyright (C) 2002-2015 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */


import org.apache.log4j.Logger;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.item.domain.database.ParseException;
import org.intermine.item.domain.database.UnexpectedInputException;

public class FlowStep<I, O> extends AbstractStep
{
    protected static final Logger LOG = Logger.getLogger(FlowStep.class);
//    private static final StopWatch TIMER = new StopWatch();
    private DatabaseItemReader<? extends I> reader;
    private ItemProcessor<? super I, ? extends O> processor;
    private Step postProcessor;
    private TaskExecutor taskExecutor;

    @Override
    protected void open() throws Exception {
        if (reader != null) {
            reader.open();
            LOG.debug("Successfully Opened Cursor in FlowStep: " + this.getName());
        } else {
            LOG.error("Error Opening Cursor in FlowStep");
        }
    }

    @Override
    protected void doExecute(StepExecution stepExecution) throws Exception {
        taskExecutor.execute(new Runnable() {
            public void run() {
                LOG.info("Running Flow Step " + getName());
                I item = null;
                try {
                    while (reader.hasNext()) {
                        item = reader.read();
                        LOG.debug("SQL" + reader.getSql());
                        LOG.debug("Current Item = " + item);
                        LOG.debug("Parameter values:" + reader.getParameterMap());
                        if (item != null) {
                            O processedItem = processor.process(item);
                            LOG.debug("Item has been created: " + processedItem);
                        }
                    }
                } catch (UnexpectedInputException e1) {
                    LOG.error("Error: " + e1.getCause());
                    e1.printStackTrace();
                } catch (ParseException e1) {
                    LOG.error("Error:  " + e1.getCause());
                    e1.printStackTrace();
                } catch (Exception e1) {
                    LOG.error("Error: " + e1.getCause());
                    e1.printStackTrace();
                }
                LOG.info(getName() + " has been completed.");
            }
        });
    }

    @Override
    protected void doPostProcess(StepExecution stepExecution) throws Exception {
        if (postProcessor != null) {
            LOG.info("Running PostProcessing..");
            postProcessor.execute(stepExecution);
        } else {
            LOG.info("PostProcessor is Null");
        }
    }

    public void setItemReader(DatabaseItemReader<? extends I> reader) {
        this.reader = reader;
    }

    public void setItemProcessor(ItemProcessor<? super I, ? extends O> processor) {
        this.processor = processor;
    }

    protected DatabaseItemReader<? extends I> getItemReader() {
        return reader;
    }

    protected ItemProcessor<? super I, ? extends O> getItemProcessor() {
        return processor;
    }

    public void setStepPostProcessor(Step postProcessor) {
        this.postProcessor = postProcessor;
    }

    protected Step getStepProcessor() {
        return postProcessor;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    protected TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }
}
