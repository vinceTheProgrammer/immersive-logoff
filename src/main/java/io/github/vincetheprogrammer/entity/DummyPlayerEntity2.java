package io.github.vincetheprogrammer.entity;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import io.github.vincetheprogrammer.ImmersiveLogoff;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class DummyPlayerEntity2 extends PassiveEntity implements PolymerEntity {
    public DummyPlayerEntity2(EntityType<DummyPlayerEntity2> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    @Override
    public List<Pair<EquipmentSlot, ItemStack>> getPolymerVisibleEquipment(List<Pair<EquipmentSlot, ItemStack> > oldList, ServerPlayerEntity player) {
        List<Pair<EquipmentSlot, ItemStack>> list = Lists.newArrayListWithCapacity(2);
        list.add(Pair.of(EquipmentSlot.MAINHAND, Items.DIAMOND.getDefaultStack()));
        return list;
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.PLAYER;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return PolymerEntityUtils.createPlayerSpawnPacket(this);
    }

    @Override
    public void onBeforeSpawnPacket(Consumer<Packet<?>> packetConsumer) {
        var packet = PolymerEntityUtils.createMutablePlayerListPacket(EnumSet.of(PlayerListS2CPacket.Action.ADD_PLAYER, PlayerListS2CPacket.Action.UPDATE_LISTED));
        var gameprofile = new GameProfile(this.getUuid(), "Test NPC");
        gameprofile.getProperties().put("textures", new Property("textures",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4NDk2MDEzNzEzOCwKICAicHJvZmlsZUlkIiA6ICI1ZWYxMmQ3NmNjOWQ0OTEzOGZiNWZlMGE4YmYyM2U5YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaW5jZVRoZU1pbmVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M3YjhmNDM4YWEzZjc4Nzc1ODkxNzBiZWVjYjlmYTRhM2Q4M2E5ZmExOGQwMTkwMTRmMDY0NDk4ZWJlMWU4YmQiCiAgICB9CiAgfQp9",
                "o/OqfdaF8SZy7UiH1TPW8fFsF9lSDGDAFMNOzYHoNDw1fGlJpET0KkZFFtWWbmDqP0KBbOWDfsRNd5njfiEHwCj2Li1DyHO9IBO5sMt1RyY1kGvT/GVkQT2NRevkNdq8oKwDiu+VnSvF8DegVWGtXGJsL2QJrE+QfY2QeNSZdcxXSLTH+/xdyV3ud+BgjeW8pNnU9qIsWIRcyp90ZUoLRLZwe+fdXrl98C6T0JA9wBmBKwTplOSigsfOHINI8deRM+L18jHu6YgZpwIesQf3IG74ruFRxIPxJVwAvGQyOfmYYdXqYqzWFpkTuVngDRFior7i3Hw6lHJofKnOmnTA8Be24aDFwgt2NkylTnmY1ee7BZjq5ork3uyMO8Y1RtNWRM+2zsh8kc0ciAfrkYL18MW472DcvAjjdJ1l2D9kQ4j54xGlsH4YM0F8ppeFWLuPyozkVLekW5hRYNcpAV0ZMQchp37NvwtOPgl4vN5a5NgaC60R3RUvh0R6J9zevKPNMsobrmB8Z0Ze4JRI+Qo/Hg/FMv6PUeXbmzQFL2XMuvA9y+/dRVCHtQu8JagIHP41TLyFWAmAHPLpGT99aStF3CBNQhh33p5sfIich/fkhm6E29pCp1GVVizyaCaq/DDW7Gqxq26hc1pQZjq7Wha9GRqfOp1lKo+0skawHkVCR0o="
        ));
        packet.getEntries().add(new PlayerListS2CPacket.Entry(this.getUuid(), gameprofile, false, 0, GameMode.ADVENTURE, null, null));
        packetConsumer.accept(packet);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        player.networkHandler.sendPacket(new PlayerRemoveS2CPacket(List.of(this.getUuid())));
        super.onStartedTrackingBy(player);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return new DummyPlayerEntity2(ImmersiveLogoff.DUMMY_PLAYER_2, world);
    }

    public void updateSkin(Property texture, MinecraftServer server) {
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        var gameprofile = new GameProfile(this.getUuid(), "Test NPC");
        gameprofile.getProperties().put("textures", new Property("textures",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4NDk2MDEzNzEzOCwKICAicHJvZmlsZUlkIiA6ICI1ZWYxMmQ3NmNjOWQ0OTEzOGZiNWZlMGE4YmYyM2U5YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaW5jZVRoZU1pbmVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M3YjhmNDM4YWEzZjc4Nzc1ODkxNzBiZWVjYjlmYTRhM2Q4M2E5ZmExOGQwMTkwMTRmMDY0NDk4ZWJlMWU4YmQiCiAgICB9CiAgfQp9",
                "o/OqfdaF8SZy7UiH1TPW8fFsF9lSDGDAFMNOzYHoNDw1fGlJpET0KkZFFtWWbmDqP0KBbOWDfsRNd5njfiEHwCj2Li1DyHO9IBO5sMt1RyY1kGvT/GVkQT2NRevkNdq8oKwDiu+VnSvF8DegVWGtXGJsL2QJrE+QfY2QeNSZdcxXSLTH+/xdyV3ud+BgjeW8pNnU9qIsWIRcyp90ZUoLRLZwe+fdXrl98C6T0JA9wBmBKwTplOSigsfOHINI8deRM+L18jHu6YgZpwIesQf3IG74ruFRxIPxJVwAvGQyOfmYYdXqYqzWFpkTuVngDRFior7i3Hw6lHJofKnOmnTA8Be24aDFwgt2NkylTnmY1ee7BZjq5ork3uyMO8Y1RtNWRM+2zsh8kc0ciAfrkYL18MW472DcvAjjdJ1l2D9kQ4j54xGlsH4YM0F8ppeFWLuPyozkVLekW5hRYNcpAV0ZMQchp37NvwtOPgl4vN5a5NgaC60R3RUvh0R6J9zevKPNMsobrmB8Z0Ze4JRI+Qo/Hg/FMv6PUeXbmzQFL2XMuvA9y+/dRVCHtQu8JagIHP41TLyFWAmAHPLpGT99aStF3CBNQhh33p5sfIich/fkhm6E29pCp1GVVizyaCaq/DDW7Gqxq26hc1pQZjq7Wha9GRqfOp1lKo+0skawHkVCR0o="
        ));
        for (ServerPlayerEntity player : players) {
            player.networkHandler.sendPacket(new PlayerListS2CPacket(ACTION).getEntries().add(new PlayerListS2CPacket(this.getUuid(), gameprofile, false, 0, GameMode.ADVENTURE, null, null)));
        }
    }

    private static String getSkin(String uuid) {
        final String CRAFATAR_API_URL = "https://crafatar.com/skins/%s?default=MHF_Steve";
        String base64Skin = "";
        try {
            // Create URL object and open connection
            URL url = new URL(String.format(CRAFATAR_API_URL, uuid));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method and headers, if needed
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Check if the request was successful
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the skin image data
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // Convert image data to Base64
                byte[] imageBytes = outputStream.toByteArray();
                base64Skin = Base64.getEncoder().encodeToString(imageBytes);

                // Close streams
                outputStream.close();
                inputStream.close();

                // Use the Base64 representation of the skin as needed
                System.out.println("Base64 Skin: " + base64Skin);
            } else {
                System.out.println("Error: Response code: " + responseCode);
            }

            // Disconnect the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return base64Skin;
    }
}

