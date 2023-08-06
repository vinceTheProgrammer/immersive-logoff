package io.github.vincetheprogrammer.entity;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;

public class DummyPlayerEntity extends Entity implements PolymerEntity {
    public DummyPlayerEntity(EntityType<DummyPlayerEntity> entityEntityType, World world) {
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
        //String texture = getSkin("5ef12d76-cc9d-4913-8fb5-fe0a8bf23e9b");
//        ImmersiveLogoff.LOGGER.info(gameprofile.getProperties().get("textures"));
//        gameprofile.getProperties().put("textures", new Property("textures",
//                texture,
//                texture
//        ));
//        ImmersiveLogoff.LOGGER.info(gameprofile.getProperties().get("textures").toString());
        packet.getEntries().add(new PlayerListS2CPacket.Entry(this.getUuid(), gameprofile, false, 0, GameMode.ADVENTURE, null, null));
        packetConsumer.accept(packet);
    }

    /**
     * Initializes data tracker.
     *
     * @apiNote Subclasses should override this and call {@link DataTracker#startTracking}
     * for any data that needs to be tracked.
     */
    @Override
    protected void initDataTracker() {

    }

    /**
     * Reads custom data from {@code nbt}. Subclasses has to implement this.
     *
     * <p>NBT is a storage format; therefore, a data from NBT is loaded to an entity instance's
     * fields, which are used for other operations instead of the NBT. The data is written
     * back to NBT when saving the entity.
     *
     * <p>{@code nbt} might not have all expected keys, or might have a key whose value
     * does not meet the requirement (such as the type or the range). This method should
     * fall back to a reasonable default value instead of throwing an exception.
     *
     * @param nbt
     * @see #writeCustomDataToNbt
     */
    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    /**
     * Writes custom data to {@code nbt}. Subclasses has to implement this.
     *
     * <p>NBT is a storage format; therefore, a data from NBT is loaded to an entity instance's
     * fields, which are used for other operations instead of the NBT. The data is written
     * back to NBT when saving the entity.
     *
     * @param nbt
     * @see #readCustomDataFromNbt
     */
    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        player.networkHandler.sendPacket(new PlayerRemoveS2CPacket(List.of(this.getUuid())));
        super.onStartedTrackingBy(player);
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

