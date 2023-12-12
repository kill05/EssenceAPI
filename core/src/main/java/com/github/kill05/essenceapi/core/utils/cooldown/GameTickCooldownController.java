package com.github.kill05.essenceapi.core.utils.cooldown;

import com.github.kill05.essenceapi.core.utils.EssenceUtils;

public class GameTickCooldownController<T> extends CooldownControllerAdapter<T> {

    public GameTickCooldownController(long defaultDelay) {
        super(defaultDelay);
    }

    public GameTickCooldownController() {
    }

    @Override
    public boolean isOnCooldown(T thing, long delay) {
        return EssenceUtils.getCurrentTick() <= lastExecutionMap.getOrDefault(thing, Long.MIN_VALUE) + delay;
    }

    @Override
    public void restartCooldown(T thing) {
        lastExecutionMap.put(thing, (long) EssenceUtils.getCurrentTick());
    }
}
