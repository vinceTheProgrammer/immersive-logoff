package io.github.vincetheprogrammer.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import io.github.vincetheprogrammer.ImmersiveLogoff;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;

public class DummyPlayerEntity extends PlayerEntity {
    private static final String CRAFATAR_API_URL = "https://crafatar.com/skins/%s";
    public DummyPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    public static GameProfile createDummyProfile(String username, String uuid, MinecraftSessionService sessionService) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), username);

        final Identifier DEFAULT_SKIN_LOCATION = new Identifier("minecraft", "textures/entity/steve.png");

        String skinUrl = String.format(CRAFATAR_API_URL, uuid);

        MinecraftProfileTexture profileTexture = new MinecraftProfileTexture(skinUrl, null);
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures = sessionService.getTextures(profile, false);
        textures.put(MinecraftProfileTexture.Type.SKIN, profileTexture);

        // Use default skin if the texture was not found
        if (!sessionService.getTextures(profile, false).containsKey(MinecraftProfileTexture.Type.SKIN)) {
            ImmersiveLogoff.LOGGER.info("no skin found. attempting to apply default");
            MinecraftProfileTexture defaultTexture = new MinecraftProfileTexture(DEFAULT_SKIN_LOCATION.toString(), null);
            sessionService.getTextures(profile, false).put(MinecraftProfileTexture.Type.SKIN, defaultTexture);
        }

        return profile;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
