package com.github.kill05.essenceapi.core.utils.location;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class LocationUtils {

    public static Location setLocation(Location original, Location finalLoc) {
        original.setX(finalLoc.getX());
        original.setY(finalLoc.getY());
        original.setZ(finalLoc.getZ());
        original.setYaw(finalLoc.getYaw());
        original.setPitch(finalLoc.getPitch());
        original.setWorld(finalLoc.getWorld());

        return original;
    }

    public static Location setYaw(Location location, float yaw) {
        location.setYaw(yaw);
        return location;
    }

    public static Location setPitch(Location location, float pitch) {
        location.setPitch(pitch);
        return location;
    }

    public static Location setYawPitch(Location location, float yaw, float pitch) {
        location.setYaw(yaw);
        location.setPitch(pitch);
        return location;
    }



    public static Location faceTowards(Location location, Location faceTowards) {
        Vector vector = VectorUtils.vectorFromTo(location, faceTowards);

        location.setYaw(VectorUtils.getYaw(vector));
        location.setPitch(VectorUtils.getPitch(vector));

        return location;
    }


    public static Location getOffset(Location location, double forward, double right) {
        return offset(location.clone(), forward, right);
    }

    public static Location offset(Location location, double forward, double right) {
        location.add(location.getDirection().multiply(forward));

        double yaw = Math.toRadians(location.getYaw());
        double deltaX = -Math.cos(yaw) * right;
        double deltaZ = -Math.sin(yaw) * right;

        return location.add(deltaX, 0, deltaZ);
    }

    public static Location getCenter(Location loc1, Location loc2) {
        double centerX = (loc1.getX() + loc2.getX()) / 2;
        double centerY = (loc1.getY() + loc2.getY()) / 2;
        double centerZ = (loc1.getZ() + loc2.getZ()) / 2;

        return new Location(loc1.getWorld(), centerX, centerY, centerZ);
    }
}
