package com.github.kill05.essenceapi.core.scheduler;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TaskGroup {

    private final JavaPlugin plugin;
    private EssenceTask first;
    private EssenceTask last;
    private boolean paused;

    public TaskGroup(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void addTask(EssenceTask task) {
        if(first == null) {
            first = task;
            last = task;
            return;
        }

        last.appendTask(task);
        last = task;
    }

    public void start() {
        if(first == null) return;
        first.startTask();
    }

    public void cancel() {
        iterateTasks(task -> {
            BukkitTask bukkitTask = task.getBukkitTask();
            if(bukkitTask != null) bukkitTask.cancel();
            task.bukkitTask = null;
        });
    }

    public void pause() {
        EssenceTask task = getRunningTask();
        if(task == null) return;
        pause(task);
    }

    public void resume() {
        EssenceTask task = getRunningTask();
        if(task == null) return;
        resume(task);
    }

    protected void pause(EssenceTask current) {
        this.paused = true;
        if(current != null) current.cancel();
    }

    protected void resume(EssenceTask current) {
        if(current != null) current.startTaskUnsafe(current.getFirstDelay());
        this.paused = false;
    }


    public void iterateTasks(Consumer<EssenceTask> consumer) {
        EssenceTask task = first;
        if(first == null) return;

        while (task != null) {
            consumer.accept(task);
            task = task.getNextTask();
        }
    }



    public JavaPlugin getPlugin() {
        return plugin;
    }

    public EssenceTask getFirstTask() {
        return first;
    }

    public EssenceTask getLastTask() {
        return last;
    }

    @Nullable
    public EssenceTask getRunningTask() {
        EssenceTask running = first;

        while (running != null) {
            if(running.isThisRunning()) return running;
            running = running.getNextTask();
        }

        return null;
    }

    public boolean hasStarted() {
        return first != null && first.hasThisStarted();
    }

    public boolean isRunning() {
        return getRunningTask() != null;
    }

    public boolean hasFinished() {
        return last != null && last.hasThisFinished();
    }

    public boolean isPaused() {
        return paused;
    }
}
