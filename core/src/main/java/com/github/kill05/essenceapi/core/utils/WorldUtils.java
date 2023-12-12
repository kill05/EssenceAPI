package com.github.kill05.essenceapi.core.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class WorldUtils {

    public static List<Block> getExposedBlocksWithin(Location start, Location end){
        List<Block> blocks = new ArrayList<>();

        int startX = Math.min(start.getBlockX(), end.getBlockX());
        int startY = Math.min(start.getBlockY(), end.getBlockY());
        int startZ = Math.min(start.getBlockZ(), end.getBlockZ());
        int endX = Math.max(start.getBlockX(), end.getBlockX());
        int endY = Math.max(start.getBlockY(), end.getBlockY());
        int endZ = Math.max(start.getBlockZ(), end.getBlockZ());

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    Location loc = new Location(start.getWorld(), x, y, z);
                    if(loc.getBlock().getType() == Material.AIR || loc.getBlock().getRelative(BlockFace.UP).getType().isSolid()) continue;

                    blocks.add(loc.getBlock());
                }
            }
        }

        return blocks;
    }


    public static void forLocationsWithin(Location loc1, Location loc2, Consumer<Location> consumer) {
        if(!loc1.getWorld().equals(loc2.getWorld())) throw new IllegalArgumentException("World of loc1 is not the same as loc2!");

        Vector min = Vector.getMinimum(loc1.toVector(), loc2.toVector());
        Vector max = Vector.getMaximum(loc1.toVector(), loc2.toVector());

        for (int x = min.getBlockX(); x <= max.getBlockX();x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ();z++) {
                    consumer.accept(new Location(loc1.getWorld(), x, y, z));
                }
            }
        }
    }


    public static Location getNearestBlockUnder(Location loc){
        loc = loc.getBlock().getLocation();

        while(loc.getBlock().getType() == Material.AIR && loc.getBlockY() > 0) {
            loc.subtract(0, 1, 0);
        }

        return loc;
    }

    public static void drawBlockLine(Location loc1, Location loc2, Material material) {
        Validate.notNull(loc1, "Loc1 can't be null!");
        Validate.notNull(loc2, "Loc2 can't be null!");
        Validate.notNull(loc1.getWorld(), "Loc2 world can't be null!");
        Validate.notNull(loc2.getWorld(), "Loc2 world can't be null!");

        if(!loc1.getWorld().equals(loc2.getWorld())) {
            throw new IllegalArgumentException("World of loc1 is not the same as world of loc2!");
        }

        int x1 = loc1.getBlockX();
        int y1 = loc1.getBlockY();
        int z1 = loc1.getBlockZ();
        int x2 = loc2.getBlockX();
        int y2 = loc2.getBlockY();
        int z2 = loc2.getBlockZ();

        loc1.getWorld().getBlockAt(x1, y1, z1).setType(material);
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int dz = Math.abs(z2 - z1);
        int xs;
        int ys;
        int zs;
        if (x2 > x1) {
            xs = 1;
        } else {
            xs = -1;
        }
        if (y2 > y1) {
            ys = 1;
        } else {
            ys = -1;
        }
        if (z2 > z1) {
            zs = 1;
        } else {
            zs = -1;
        }

        // Driving axis is X-axis
        if (dx >= dy && dx >= dz) {
            int p1 = 2 * dy - dx;
            int p2 = 2 * dz - dx;
            while (x1 != x2) {
                x1 += xs;
                if (p1 >= 0) {
                    y1 += ys;
                    p1 -= 2 * dx;
                }
                if (p2 >= 0) {
                    z1 += zs;
                    p2 -= 2 * dx;
                }
                p1 += 2 * dy;
                p2 += 2 * dz;
                loc1.getWorld().getBlockAt(x1, y1, z1).setType(material);
            }

        // Driving axis is Y-axis
        } else if (dy >= dx && dy >= dz) {
            int p1 = 2 * dx - dy;
            int p2 = 2 * dz - dy;
            while (y1 != y2) {
                y1 += ys;
                if (p1 >= 0) {
                    x1 += xs;
                    p1 -= 2 * dy;
                }
                if (p2 >= 0) {
                    z1 += zs;
                    p2 -= 2 * dy;
                }
                p1 += 2 * dx;
                p2 += 2 * dz;
                loc1.getWorld().getBlockAt(x1, y1, z1).setType(material);
            }

        // Driving axis is Z-axis
        } else {
            int p1 = 2 * dy - dz;
            int p2 = 2 * dx - dz;
            while (z1 != z2) {
                z1 += zs;
                if (p1 >= 0) {
                    y1 += ys;
                    p1 -= 2 * dz;
                }
                if (p2 >= 0) {
                    x1 += xs;
                    p2 -= 2 * dz;
                }
                p1 += 2 * dy;
                p2 += 2 * dx;
                loc1.getWorld().getBlockAt(x1, y1, z1).setType(material);
            }
        }
    }

    public static void deleteWorld(World world) throws IOException {
        if(world == null) return;
        Bukkit.unloadWorld(world, false);
        FileUtils.deleteDirectory(world.getWorldFolder());
    }

}
