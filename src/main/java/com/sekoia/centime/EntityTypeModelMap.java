package com.sekoia.centime;

import com.sekoia.centime.model.CustomModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class EntityTypeModelMap {
    private static final HashMap<EntityType<? extends Entity>, CustomModel<? extends Entity>> models = new HashMap<>();

    public static void addEntry(Identifier identifier, CustomModel<? extends Entity> model) {
        // Janky shit bc ENTITY_TYPE is a defaulted registry.
        if (Registry.ENTITY_TYPE.get(identifier) == EntityType.PIG && !identifier.toString().equals("minecraft:pig")) {
            CentimeInit.LOGGER.warn("Attempted to do CEM on non-existing/unloaded entity type {}", identifier);
            return;
        }
        models.put(Registry.ENTITY_TYPE.get(identifier), model);
        CentimeInit.LOGGER.info("Loading model for entity type {}.", Registry.ENTITY_TYPE.get(identifier));
    }

    public static boolean isModelOverridden(EntityType<? extends Entity> entityType) {
        return models.containsKey(entityType);
    }

    @Nullable
    public static CustomModel<? extends Entity> getModel(EntityType<? extends Entity> entityType) {
        return models.get(entityType);
    }

    public static void clearEntries() {
        models.clear();
    }
}
