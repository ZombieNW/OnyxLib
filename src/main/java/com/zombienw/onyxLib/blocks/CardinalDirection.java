package com.zombienw.onyxLib.blocks;

import org.bukkit.Rotation;
import org.bukkit.entity.Player;

public enum CardinalDirection {
    NORTH, EAST, SOUTH, WEST;

    // Get the closest direction player is looking at
    public static CardinalDirection fromPlayer(Player player) {
        float yaw = (player.getLocation().getYaw() % 360 + 360) % 360;
        if (yaw >= 315 || yaw < 45)  return SOUTH;
        if (yaw < 135)               return WEST;
        if (yaw < 225)               return NORTH;
        return EAST;
    }

    // Direction to armor stand yaw
    public float toArmorStandYaw() {
        return switch (this) {
            case NORTH -> 0f;
            case EAST  -> 90f;
            case SOUTH -> 180f;
            case WEST  -> 270f;
        };
    }

    public Rotation toFrameRotation() {
        return switch (this) {
            case NORTH -> Rotation.NONE;
            case EAST  -> Rotation.COUNTER_CLOCKWISE;
            case SOUTH -> Rotation.CLOCKWISE_135;
            case WEST  -> Rotation.CLOCKWISE;
        };
    }
}
