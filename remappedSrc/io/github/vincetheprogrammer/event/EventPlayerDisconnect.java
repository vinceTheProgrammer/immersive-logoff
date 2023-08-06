package io.github.vincetheprogrammer.event;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import io.github.vincetheprogrammer.ImmersiveLogoff;
import io.github.vincetheprogrammer.entity.DummyPlayerEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class EventPlayerDisconnect {
    public static void register() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ImmersiveLogoff.LOGGER.info("diconnecto");
            ServerWorld world = server.getWorld(World.OVERWORLD);
            if (world == null) return;
            ImmersiveLogoff.LOGGER.info("past return");
            BlockPos blockPos = handler.player.getBlockPos();
            float yaw = handler.player.getYaw();
            String username = handler.player.getGameProfile().getName();
            String uuid = handler.player.getUuidAsString();
            MinecraftSessionService sessionService= server.getSessionService();
            ImmersiveLogoff.LOGGER.info(blockPos.toShortString());
            DummyPlayerEntity dummyPlayer = new DummyPlayerEntity(world, blockPos, yaw, DummyPlayerEntity.createDummyProfile(username, uuid, sessionService));
            dummyPlayer.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());

            Consumer<PlayerEntity> playerEntityConsumer = playerEntity -> {};

            //LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
            //lightning.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());

            ImmersiveLogoff.LOGGER.info(dummyPlayer.getPos().toString());
            world.spawnEntity(dummyPlayer);
            ImmersiveLogoff.LOGGER.info("hmm");
        });
    }
}
