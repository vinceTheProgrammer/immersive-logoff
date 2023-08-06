package io.github.vincetheprogrammer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.vincetheprogrammer.ImmersiveLogoff;
import io.github.vincetheprogrammer.entity.DummyPlayerEntity2;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandCreateDummyEntity2 {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("testdummy2")
                .executes(CommandCreateDummyEntity2::run));
    }

    private static int run(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        ServerCommandSource source = commandContext.getSource();
        MinecraftServer server = source.getServer();
        ServerWorld world = server.getWorld(World.OVERWORLD);
        if (world == null) {
            commandContext.getSource().sendFeedback(Text.of("world is null"), false);
            return -1;
        }
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            commandContext.getSource().sendFeedback(Text.of("player is null"), false);
            return -1;
        }
        BlockPos blockPos = player.getBlockPos();
        float yaw = player.getYaw();
        float headYaw = player.getHeadYaw();
        float bodyYaw = player.getBodyYaw();
        float pitch = player.getPitch();

        DummyPlayerEntity2 dummy = new DummyPlayerEntity2(ImmersiveLogoff.DUMMY_PLAYER_2, world);

        dummy.refreshPositionAndAngles(blockPos, yaw, pitch);

        world.spawnEntity(dummy);

        commandContext.getSource().sendFeedback(Text.of("spawned"), false);
        return 1;
    }
}
