package com.github.kill05.essenceapi.core.utils.cooldown;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class CooldownControllerAdapter<T> implements CooldownController<T> {

    protected final Map<T, Long> lastExecutionMap;
    protected long defaultDelay;

    public CooldownControllerAdapter(long defaultDelay) {
        this.lastExecutionMap = new WeakHashMap<>();
        this.defaultDelay = defaultDelay;
    }

    public CooldownControllerAdapter() {
        this(-1);
    }

    @Override
    public boolean executeCooldown(Runnable runnable, T thing, long delay) {
        if(isOnCooldown(thing, delay)) return false;

        try {
            runnable.run();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "There was an error while running a cooldown task", e);
        }

        restartCooldown(thing);
        return true;
    }

    @Override
    public boolean executeCooldown(Runnable runnable, T thing) {
        return executeCooldown(runnable, thing, defaultDelay);
    }

    @Override
    public boolean isOnCooldown(T thing) {
        return isOnCooldown(thing, defaultDelay);
    }

    @Override
    public void setDefaultDelay(long defaultDelay) {
        this.defaultDelay = defaultDelay;
    }

}
