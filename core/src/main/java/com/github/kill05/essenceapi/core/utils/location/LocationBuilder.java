package com.github.kill05.essenceapi.core.utils.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class LocationBuilder implements Cloneable {

    private final Location location;


    public LocationBuilder(Location location, boolean clone) {
        this.location = clone ? location.clone() : location;
    }

    public LocationBuilder(Location location) {
        this(location, false);
    }


    public LocationBuilder forwards(double amount, boolean usePitch) {
        location.add(VectorUtils.getSphericalVector(amount, location.getYaw() + 90, usePitch ? location.getPitch() : 0));
        return this;
    }

    public LocationBuilder forwards(double amount) {
        return forwards(amount, false);
    }

    public LocationBuilder forwardsPitch(double amount) {
        return forwards(amount, true);
    }


    public LocationBuilder backwards(double amount, boolean usePitch) {
        return forwards(-amount, usePitch);
    }

    public LocationBuilder backwards(double amount) {
        return backwards(amount, false);
    }

    public LocationBuilder backwardsPitch(double amount) {
        return backwards(amount, true);
    }


    public LocationBuilder right(double amount) {
        double yaw = Math.toRadians(location.getYaw());
        double deltaX = -Math.cos(yaw) * amount;
        double deltaZ = -Math.sin(yaw) * amount;

        location.add(deltaX, 0, deltaZ);
        return this;
    }

    public LocationBuilder left(double amount) {
        return right(-amount);
    }


    public LocationBuilder setLocation(double x, double y, double z) {
        location.setX(x);
        location.setY(y);
        location.setZ(z);
        return this;
    }

    public LocationBuilder add(double x, double y, double z) {
        location.add(x, y, z);
        return this;
    }

    public LocationBuilder subtract(double x, double y, double z) {
        location.subtract(x, y, z);
        return this;
    }


    public LocationBuilder setYawPitch(float yaw, float pitch) {
        return setYaw(yaw).setPitch(pitch);
    }

    public LocationBuilder setYaw(float yaw) {
        location.setYaw(yaw);
        return this;
    }

    public LocationBuilder setPitch(float pitch) {
        location.setPitch(pitch);
        return this;
    }

    public LocationBuilder setWorld(World world) {
        location.setWorld(world);
        return this;
    }


    public LocationBuilder setLocation(Location from) {
        setLocation(from.getX(), from.getY(), from.getZ());
        setYawPitch(from.getYaw(), from.getPitch());
        return setWorld(from.getWorld());
    }

    public LocationBuilder setFacing(Location faceTowards) {
        Vector vector = VectorUtils.vectorFromTo(location, faceTowards);
        return setYawPitch(VectorUtils.getYaw(vector), VectorUtils.getPitch(vector));
    }

    public double getX() {
        return location.getX();
    }

    public double getY() {
        return location.getY();
    }

    public double getZ() {
        return location.getZ();
    }

    public float getYaw() {
        return location.getYaw();
    }

    public float getPitch() {
        return location.getPitch();
    }

    public World getWorld() {
        return location.getWorld();
    }


    public Location get() {
        return location;
    }

    public Location getCloned() {
        return get().clone();
    }

    @Override
    public LocationBuilder clone() {
        return new LocationBuilder(getCloned());
    }
}
