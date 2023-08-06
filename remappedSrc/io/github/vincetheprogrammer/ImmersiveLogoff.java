package io.github.vincetheprogrammer;

import io.github.vincetheprogrammer.command.CommandCreateDummyEntity;
import io.github.vincetheprogrammer.entity.DummyPlayerEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImmersiveLogoff implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("immersive-logoff");

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        EntityRendererRegistry
        registerCommands();
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register(CommandCreateDummyEntity::register);
    }
}