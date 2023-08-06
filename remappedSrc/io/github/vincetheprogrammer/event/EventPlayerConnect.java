package io.github.vincetheprogrammer.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class EventPlayerConnect {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {

        });
    }

}
