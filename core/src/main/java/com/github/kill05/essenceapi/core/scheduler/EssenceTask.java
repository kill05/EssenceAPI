package com.github.kill05.essenceapi.core.scheduler;

import com.github.kill05.essenceapi.core.scheduler.context.TaskContext;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

public abstract class EssenceTask {

    private TaskGroup group;
    
    private EssenceTask previous;
    private EssenceTask next;
    protected BukkitTask bukkitTask;
    protected boolean async;
    protected long firstDelay;
    protected int repeatTimes;

    protected boolean running;
    protected int currentIteration;

    protected EssenceTask(@Nullable TaskGroup group) {
        this.group = group;
        this.async = false;
        this.firstDelay = 0L;
        this.repeatTimes = 1;

        this.running = false;
        this.currentIteration = 0;
    }


    public void appendTask(EssenceTask next) {
        Validate.notNull(next, "Next task can't be null!");
        if(this.group == null) throw new IllegalArgumentException("Group is null.");
        if(next.group != group) throw new IllegalStateException("Group mismatch between tasks.");
        if(this.next != null) throw new IllegalStateException("Task already has next task assigned.");
        if(next.previous != null) throw new IllegalStateException("Provided task already has a previous task!");
        if(this == next) throw new IllegalArgumentException("Next task can't be the same as this task.");

        this.next = next;
        next.previous = this;
    }

    protected BukkitRunnable createRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                TaskContext context = new TaskContext(EssenceTask.this, currentIteration++);
                EssenceTask.this.run(context);

                if(context.isCancelled()) {
                    EssenceTask.this.cancel();
                    return;
                }

                boolean skip = (repeatTimes > 0 && currentIteration >= repeatTimes) || context.isSkipped();

                if(skip) {
                    startNext();
                }

                if(context.isPaused()) {
                    group.pause(skip ? next : EssenceTask.this);
                    return;
                }

                if(!skip && !context.isPaused() && !context.isCancelled()) {
                    startTaskUnsafe(context.getWaitTicks());
                }
            }
        };
    }

    public abstract void run(TaskContext context);


    protected void startNext() {
        if(next == null) return;
        if(bukkitTask != null) bukkitTask.cancel();

        next.startTaskUnsafe(next.firstDelay);
        this.running = false;
    }

    protected void startTaskUnsafe(long delay) {
        BukkitRunnable runnable = createRunnable();
        JavaPlugin plugin = group.getPlugin();

        if (async) {
            bukkitTask = runnable.runTaskLaterAsynchronously(plugin, delay);
        } else {
            bukkitTask = runnable.runTaskLater(plugin, delay);
        }

        this.running = true;
    }

    public void startTask() {
        if(group == null) throw new IllegalStateException("Task has no group!");
        if(group.hasStarted()) throw new IllegalStateException("Task Group has already started!");
        if(previous != null) throw new IllegalStateException("This task is not the first task of the group!");

        currentIteration = 0;
        startTaskUnsafe(firstDelay);
    }

    public void cancel() {
        group.cancel();
    }


    public EssenceTask getPreviousTask() {
        return previous;
    }

    public EssenceTask getNextTask() {
        return next;
    }

    public EssenceTask getFirstTask() {
        return group.getFirstTask();
    }

    public EssenceTask getLastTask() {
        return group.getLastTask();
    }

    @Nullable
    public EssenceTask getRunningTask() {
        if(this.isThisRunning()) return this;
        return group.getRunningTask();
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }


    public boolean hasThisStarted() {
        return bukkitTask != null;
    }

    public boolean isThisRunning() {
        return hasThisStarted() && running;
    }

    public boolean hasThisFinished() {
        return hasThisStarted() && !isBukkitTaskRunning();
    }

    protected boolean isBukkitTaskRunning() {
        return Bukkit.getScheduler().isCurrentlyRunning(bukkitTask.getTaskId());
    }

    public boolean isFirst() {
        return previous == null;
    }


    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public int getRepeatTimes() {
        return repeatTimes;
    }

    public void setRepeating(int repeatTimes) {
        if(repeatTimes == 0) throw new IllegalArgumentException("Repeat times can't be 0.");
        this.repeatTimes = repeatTimes;
    }

    public long getFirstDelay() {
        return firstDelay;
    }

    public void setFirstDelay(long firstDelay) {
        if(firstDelay < 0) throw new IllegalArgumentException("First delay must be positive.");
        this.firstDelay = firstDelay;
    }

    public void setRepeating() {
        setRepeating(-1);
    }

    public TaskGroup getGroup() {
        return group;
    }

    public void setTaskGroup(TaskGroup group) {
        if(this.group != null) throw new IllegalStateException("Task already has a group!");
        this.group = group;
    }


}
