package com.github.kill05.essenceapi.core.scheduler;

import com.github.kill05.essenceapi.core.scheduler.context.TaskContext;

import java.util.function.Consumer;

public class ContextConsumerTask extends EssenceTask {

    protected final Consumer<TaskContext> consumer;

    protected ContextConsumerTask(TaskGroup group, Consumer<TaskContext> consumer) {
        super(group);
        this.consumer = consumer;
    }

    public Consumer<TaskContext> getConsumer() {
        return consumer;
    }

    @Override
    public void run(TaskContext context) {
        consumer.accept(context);
    }
}
