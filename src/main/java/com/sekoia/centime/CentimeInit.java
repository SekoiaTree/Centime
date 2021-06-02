package com.sekoia.centime;

import com.sekoia.centime.model.animation.AnimationManager;
import com.sekoia.centime.model.animation.AnimationManagers;
import com.sekoia.centime.model.feature.FeatureBuilder;
import com.sekoia.centime.model.feature.FeatureBuilders;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CentimeInit implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "centime";
    @SuppressWarnings("rawtypes")
    public static final SimpleRegistry<AnimationManager> ANIMATION_MANAGER = FabricRegistryBuilder.createSimple(AnimationManager.class, new Identifier(MODID, "animation_manager")).buildAndRegister();
    public static final SimpleRegistry<FeatureBuilder> FEATURE_BUILDER = FabricRegistryBuilder.createSimple(FeatureBuilder.class, new Identifier(MODID, "feature_builder")).buildAndRegister();
    @Override
    public void onInitializeClient() {
        AnimationManagers.registerAnimationManagers();
        FeatureBuilders.registerFeatureBuilders();
    }
}
