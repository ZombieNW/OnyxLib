package com.zombienw.onyxLib.core;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CMDRegistry {

    // material → next auto-assign value
    private final Map<Material, Integer> counters = new HashMap<>();

    // material → set of all taken values (auto + override)
    private final Map<Material, Set<Integer>> taken = new HashMap<>();

    public int assign(Material material, int override) {
        var takenForMaterial = taken.computeIfAbsent(material, m -> new java.util.HashSet<>());

        if (override != 0) {
            if (takenForMaterial.contains(override)) {
                throw new IllegalStateException(
                        "CMD value " + override + " is already taken for material " + material);
            }
            takenForMaterial.add(override);
            return override;
        }

        // Auto-assign: find the next value not already taken
        int next = counters.getOrDefault(material, 1);
        while (takenForMaterial.contains(next)) {
            next++;
        }
        takenForMaterial.add(next);
        counters.put(material, next + 1);
        return next;
    }
}
