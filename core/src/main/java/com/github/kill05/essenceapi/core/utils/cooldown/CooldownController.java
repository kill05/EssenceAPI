package com.github.kill05.essenceapi.core.utils.cooldown;

public interface CooldownController<T> {

    boolean executeCooldown(Runnable runnable, T thing, long delay);

    boolean executeCooldown(Runnable runnable, T thing);

    boolean isOnCooldown(T thing, long delay);

    boolean isOnCooldown(T thing);

    void restartCooldown(T thing);

    void setDefaultDelay(long defaultDelay);

}
