package com.github.kill05.essenceapi.core.utils.cooldown;

public class SysTimeCooldownController<T> extends CooldownControllerAdapter<T> {

    public SysTimeCooldownController(long defaultDelay) {
        super(defaultDelay);
    }

    public SysTimeCooldownController() {
    }

    @Override
    public boolean isOnCooldown(T thing, long delay) {
        return System.currentTimeMillis() <= lastExecutionMap.getOrDefault(thing, Long.MIN_VALUE) + delay;
    }

    @Override
    public void restartCooldown(T thing) {
        lastExecutionMap.put(thing, System.currentTimeMillis());
    }
}
