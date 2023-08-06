package io.github.vincetheprogrammer.command;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.vincetheprogrammer.entity.DummyPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandCreateDummyEntity {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("testdummy")
                .executes(CommandCreateDummyEntity::run));
    }

    private static int run(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        ServerCommandSource source = commandContext.getSource();
        String uuid = source.getPlayer().getUuidAsString();
        if (uuid == null) return -1;
        MinecraftServer server = source.getServer();
        ServerWorld world = server.getWorld(World.OVERWORLD);
        if (world == null) return -1;
        MinecraftSessionService sessionService = server.getSessionService();
        PlayerEntity player = source.getPlayer();
        BlockPos blockPos = player.getBlockPos();
        float yaw = player.getYaw();
        PlayerEntity dummyPlayer = new PlayerEntity(world, blockPos, yaw, DummyPlayerEntity.createDummyProfile("alph", uuid, sessionService)) {
            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return false;
            }
        };
        boolean spawned = world.spawnEntity(dummyPlayer);
        commandContext.getSource().sendFeedback(Text.of("kek: " + spawned), false);
        return 1;
    }
}
