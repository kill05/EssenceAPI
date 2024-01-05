package com.github.kill05.essenceapi.core.scheduler;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SchedulerListener implements Listener {

    private final Map<Class<? extends Event>, Collection<Predicate<Event>>> taskPredicates;

    public SchedulerListener(JavaPlugin plugin) {
        this.taskPredicates = new HashMap<>();

        RegisteredListener registeredListener = new RegisteredListener(this, (listener, event) -> onEvent(event), EventPriority.NORMAL, plugin, false);
        for (HandlerList handler : HandlerList.getHandlerLists()) {
            handler.register(registeredListener);
        }
    }


    public void onEvent(Event event) {
        Class<?> clazz = event.getClass();
        Collection<Predicate<Event>> predicates = taskPredicates.get(clazz);
        if(predicates == null) return;

        for(Predicate<Event> predicate : predicates) {
            predicate.test(event);
        }
    }
}
