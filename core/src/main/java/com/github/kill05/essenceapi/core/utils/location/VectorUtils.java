package com.github.kill05.essenceapi.core.utils.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public final class VectorUtils {

    public static Vector vectorFromTo(Vector start, Vector end){
        return end.clone().subtract(start);
    }

    public static Vector vectorFromTo(Location start, Location end) {
        return vectorFromTo(start.toVector(), end.toVector());
    }

    public static Vector rotateVector(Vector vector, double yaw, double pitch) {
        if(vector.equals(new Vector(0, 0, 0))) return vector.clone();

        double xz = Math.sqrt(vector.getX() * vector.getX() + vector.getZ() * vector.getZ());
        double radius = vector.length();
        double vecPitch = Math.asin(vector.getY() / xz);
        double vecYaw = Math.atan2(vector.getX(), vector.getZ());

        Bukkit.broadcastMessage("radius: " + radius);
        Bukkit.broadcastMessage("yaw: " + Math.toDegrees(vecYaw));
        Bukkit.broadcastMessage("pitch: " + Math.toDegrees(vecPitch));

        return getSphericalVector(radius, Math.toDegrees(vecYaw) + yaw, Math.toDegrees(vecPitch) + pitch);
    }

    public static Vector getSphericalVector(double radius, double yaw, double pitch) {
        double yawCos = Math.cos(Math.toRadians(yaw));
        double yawSin = Math.sin(Math.toRadians(yaw));

        double pitchCos = Math.cos(Math.toRadians(pitch));
        double pitchSin = Math.sin(Math.toRadians(pitch));

        double x = yawCos * pitchCos * radius;
        double z = yawSin * pitchCos * radius;
        double y = pitchSin * radius;

        return new Vector(x, y, z);
    }

    public static Vector getCylindricalVector(double radius, double yaw, double height){
        return getSphericalVector(radius, yaw, 0).add(new Vector(0, height, 0));
    }


    public static List<Location> positionsBetween(Location start, Location end, int amount){
        start = start.clone();
        end = end.clone();

        Vector vector = vectorFromTo(start, end).multiply(1.0/(amount - 1));
        List<Location> list = new ArrayList<>();
        start.subtract(vector);

        for(int j = 0; j < amount; j++){
            list.add(start.add(vector).clone());
        }

        return list;
    }

    public static Location positionBetween(Location start, Location end, int amount, int number) {
        Vector vector = vectorFromTo(start, end).multiply((1.0/(amount - 1)) * number);
        return start.clone().add(vector);
    }


    public static Vector getVectorRight(Location location, double length) {
        Vector direction = location.getDirection().normalize();
        return new Vector(-direction.getZ(), 0.0, direction.getX()).normalize().multiply(length);
    }

    public static Vector getVectorRight(Location location) {
        return getVectorRight(location, 1);
    }

    public static Vector getVectorLeft(Location location, double length) {
        Vector direction = location.getDirection().normalize();
        return new Vector(direction.getZ(), 0.0, -direction.getX()).normalize().multiply(length);
    }

    public static Vector getVectorLeft(Location location) {
        return getVectorLeft(location, 1);
    }

    public static float getYaw(Vector vector) {
        if (((Double) vector.getX()).equals(0.0) && ((Double) vector.getZ()).equals(0.0)){
            return 0;
        }
        return getPositiveYaw((float) Math.toDegrees(Math.atan2(vector.getZ(), vector.getX())) - 90f);
    }

    public static float getPitch(Vector vector) {
        double xy = Math.sqrt(vector.getX() * vector.getX() + vector.getZ() * vector.getZ());
        return (float) Math.toDegrees(Math.atan(vector.getY() / xy));
    }

    private static float getPositiveYaw(float angle) {
        return angle < 0 ? angle + 360 : angle;
    }
}
