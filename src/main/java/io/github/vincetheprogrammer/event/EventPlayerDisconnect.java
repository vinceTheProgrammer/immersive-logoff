package io.github.vincetheprogrammer.event;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import io.github.vincetheprogrammer.ImmersiveLogoff;
import io.github.vincetheprogrammer.entity.DummyPlayerEntity2;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class EventPlayerDisconnect {

    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    public static void register() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ImmersiveLogoff.LOGGER.info("diconnecto");
            ServerWorld world = server.getWorld(World.OVERWORLD);
            if (world == null) return;
            ImmersiveLogoff.LOGGER.info("past return");
            BlockPos blockPos = handler.player.getBlockPos();
            Vec3d pos = handler.player.getPos();
            float yaw = handler.player.getYaw();
            float headYaw = handler.player.getHeadYaw();
            float bodyYaw = handler.player.getBodyYaw();
            float pitch = handler.player.getPitch();
            String username = handler.player.getGameProfile().getName();
            String uuid = handler.player.getUuidAsString();
            MinecraftSessionService sessionService= server.getSessionService();
            ImmersiveLogoff.LOGGER.info(blockPos.toShortString());

            executorService.schedule(() -> {
                synchronized (world) {
                    DummyPlayerEntity2 dummy = new DummyPlayerEntity2(ImmersiveLogoff.DUMMY_PLAYER_2, world);
                    handler.player.getGameProfile().getProperties().get("textures");
                    class TempDummy extends DummyPlayerEntity2 {

                        public TempDummy(EntityType<DummyPlayerEntity2> entityEntityType, World world) {
                            super(entityEntityType, world);
                        }


                    }
                    dummy.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), yaw, pitch);
                    dummy.setHeadYaw(headYaw);
                    //dummy.setBodyYaw(bodyYaw);
                    world.spawnEntity(dummy);
                }
            }, 30, TimeUnit.MILLISECONDS);

            ImmersiveLogoff.LOGGER.info("hmm");
        });
    }
}
