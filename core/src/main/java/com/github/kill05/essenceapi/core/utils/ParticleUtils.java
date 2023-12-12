package com.github.kill05.essenceapi.core.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;

public final class ParticleUtils {

    private ParticleUtils() {

    }

    public static void drawParticle(ParticleBuilder builder, Location start, Location end, int amount, Player... players) {
        Vector delta = end.toVector().subtract(start.toVector()).multiply(1.0 / (double) (amount - 1));

        Location displayLoc = start.clone();
        for(int i = 0; i < amount; i++) {
            builder.setLocation(displayLoc).display(players);
            displayLoc.add(delta);
        }
    }
}
