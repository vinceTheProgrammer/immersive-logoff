package io.github.vincetheprogrammer.type;

import com.google.common.collect.ImmutableSet;
import io.github.vincetheprogrammer.entity.DummyPlayerEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.resource.featuretoggle.FeatureSet;

public class CustomEntityType extends EntityType {
    public CustomEntityType(EntityFactory factory, SpawnGroup spawnGroup, boolean saveable, boolean summonable, boolean fireImmune, boolean spawnableFarFromPlayer, ImmutableSet canSpawnInside, EntityDimensions dimensions, int maxTrackDistance, int trackTickInterval, FeatureSet requiredFeatures) {
        super(factory, spawnGroup, saveable, summonable, fireImmune, spawnableFarFromPlayer, canSpawnInside, dimensions, maxTrackDistance, trackTickInterval, requiredFeatures);
    }

    public static final EntityType<DummyPlayerEntity> DUMMY;

    static {
        DUMMY = register("player", EntityType.Builder.create(SpawnGroup.MISC).disableSaving().disableSummon().setDimensions(0.6F, 1.8F).maxTrackingRange(32).trackingTickInterval(2));
    }

}
