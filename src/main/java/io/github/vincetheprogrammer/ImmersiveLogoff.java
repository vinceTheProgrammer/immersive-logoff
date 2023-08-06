package io.github.vincetheprogrammer;

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import io.github.vincetheprogrammer.command.CommandCreateDummyEntity;
import io.github.vincetheprogrammer.command.CommandCreateDummyEntity2;
import io.github.vincetheprogrammer.entity.DummyPlayerEntity;
import io.github.vincetheprogrammer.entity.DummyPlayerEntity2;
import io.github.vincetheprogrammer.event.EventPlayerDisconnect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImmersiveLogoff implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("immersive-logoff");
    public static final EntityType<DummyPlayerEntity> DUMMY_PLAYER = EntityType.Builder.create(DummyPlayerEntity::new, SpawnGroup.CREATURE).setDimensions(0.75f, 1.8f).build("dummy_player");
    public static final EntityType<DummyPlayerEntity2> DUMMY_PLAYER_2 = EntityType.Builder.create(DummyPlayerEntity2::new, SpawnGroup.CREATURE).setDimensions(0.75f, 1.8f).build("dummy_player_2");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing...");
        registerEntities();
        registerEvents();
        registerCommands();
        LOGGER.info("Initialized.");
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register(CommandCreateDummyEntity::register);
        CommandRegistrationCallback.EVENT.register(CommandCreateDummyEntity2::register);
    }

    private void registerEvents() {
        EventPlayerDisconnect.register();
    }

    private void registerEntities() {
        Registry.register(Registries.ENTITY_TYPE, new Identifier("immersive-logoff", "dummy_player"), DUMMY_PLAYER);
        Registry.register(Registries.ENTITY_TYPE, new Identifier("immersive-logoff", "dummy_player_2"), DUMMY_PLAYER_2);
        PolymerEntityUtils.registerType(DUMMY_PLAYER, DUMMY_PLAYER_2);
        FabricDefaultAttributeRegistry.register(DUMMY_PLAYER_2, DummyPlayerEntity2.createMobAttributes());
    }
}