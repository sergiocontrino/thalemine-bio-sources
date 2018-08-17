package org.intermine.bio.dataloader.job;

import org.intermine.bio.item.ItemProcessor;
import org.intermine.item.domain.database.DatabaseItemReader;

public class FlowStepBuilder<I, O>
{

    private final FlowStep<I, O> flowStep;

    public FlowStepBuilder() {
        flowStep = new FlowStep<I, O>();
    }

    public  FlowStepBuilder<I,O> flowStep() {
        return new FlowStepBuilder<I,O>();
    }

    public FlowStep<I, O> build(String name, DatabaseItemReader<? extends I> reader,
            ItemProcessor<? super I, ? extends O> processor, TaskExecutor taskExecutor

    ) {

        flowStep.setName(name);
        flowStep.setItemReader(reader);
        flowStep.setItemProcessor(processor);
        flowStep.setTaskExecutor(taskExecutor);

        return flowStep;

    }

    public FlowStep<I, O> build(DatabaseItemReader<? extends I> reader,
            ItemProcessor<? super I, ? extends O> processor, TaskExecutor taskExecutor

    ) {

        flowStep.setItemReader(reader);
        flowStep.setItemProcessor(processor);
        flowStep.setTaskExecutor(taskExecutor);

        return flowStep;

    }

    public FlowStep<I, O> build(DatabaseItemReader<? extends I> reader,
            ItemProcessor<? super I, ? extends O> processor, TaskExecutor taskExecutor, Step step

    ) {

        flowStep.setItemReader(reader);
        flowStep.setItemProcessor(processor);
        flowStep.setStepPostProcessor(step);
        flowStep.setTaskExecutor(taskExecutor);

        return flowStep;

    }
}
