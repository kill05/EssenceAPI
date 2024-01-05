package com.github.kill05.essenceapi.core.scheduler;

import com.github.kill05.essenceapi.core.scheduler.context.TaskContext;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class TaskBuilder {

    private final TaskGroup group;
    private final boolean startImmediately;
    private long nextWait;

    public TaskBuilder(JavaPlugin plugin, boolean startImmediately) {
        this.group = new TaskGroup(plugin);
        this.startImmediately = startImmediately;
    }

    public TaskBuilder(JavaPlugin plugin) {
        this(plugin, true);
    }


    public static TaskBuilder runLater(JavaPlugin plugin, Consumer<TaskContext> consumer) {
        return new TaskBuilder(plugin).thenRun(consumer);
    }


    public TaskBuilder thenWait(long ticks) {
        this.nextWait += ticks;
        return this;
    }


    public TaskBuilder thenRun(Consumer<TaskContext> consumer) {
        EssenceTask task = new ContextConsumerTask(group, consumer);
        return thenRun(task);
    }

    public TaskBuilder thenRepeat(int times, Consumer<TaskContext> consumer) {
        EssenceTask task = new ContextConsumerTask(group, consumer);
        task.setRepeating(times);
        return thenRun(task);
    }

    public TaskBuilder thenRepeat(Consumer<TaskContext> consumer) {
        return thenRepeat(-1, consumer);
    }

    public TaskBuilder thenRun(EssenceTask task) {
        boolean first = group.getFirstTask() == null;
        task.setFirstDelay(nextWait);
        nextWait = 0;

        group.addTask(task);
        if(first && startImmediately) start();
        return this;
    }


    public TaskBuilder thenPause() {
        return thenRun(TaskContext::pause);
    }

    public <T extends Event> TaskBuilder thenPauseAndResumeWhen(Class<T> event, Predicate<T> listener) {
        return thenRun(context -> context.pauseAndResumeWhen(event, listener));
    }


    public TaskGroup start() {
        group.start();
        return group;
    }

    public TaskGroup getGroup() {
        return group;
    }

    public JavaPlugin getPlugin() {
        return group.getPlugin();
    }
}
