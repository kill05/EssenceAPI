package com.github.kill05.essenceapi.core.scheduler.context;

import com.github.kill05.essenceapi.core.scheduler.EssenceTask;
import org.apache.commons.lang3.Validate;
import org.bukkit.event.Event;

import java.util.function.Predicate;

public class TaskContext {

    private final EssenceTask task;
    private final int iteration;
    private long waitTicks;
    private boolean cancelled;
    private boolean skipped;
    private boolean paused;
    private Predicate<Event> pausePredicate;

    public TaskContext(EssenceTask task, int iteration) {
        Validate.notNull(task, "Task can't be null.");
        this.task = task;
        this.iteration = iteration;
    }


    public EssenceTask getTask() {
        return task;
    }

    public EssenceTask getPrevious() {
        return task.getPreviousTask();
    }

    public EssenceTask getNext() {
        return task.getNextTask();
    }

    public int getIteration() {
        return iteration;
    }


    public void cancel() {
        this.cancelled = true;
    }

    public void skip() {
        this.skipped = true;
    }

    public void waitTicks(long ticks) {
        this.waitTicks = ticks;
    }

    public void pause() {
        this.paused = true;
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void pauseAndResumeWhen(Class<T> event, Predicate<T> listener) {
        this.pausePredicate = (Predicate<Event>) listener;
        pause();
    }


    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public long getWaitTicks() {
        return waitTicks;
    }

    public boolean isPaused() {
        return paused;
    }
}
