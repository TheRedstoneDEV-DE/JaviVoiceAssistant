package com.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordRPCStatus implements Runnable{
	public static DiscordRPC lib = DiscordRPC.INSTANCE;
	public DiscordRichPresence presence = new DiscordRichPresence();
	public static Boolean stop = false;
	public void run() {
        String applicationId = "960163266619908196";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        //presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.details = "";
        presence.state = "";
        presence.largeImageKey="image01big";
        lib.Discord_UpdatePresence(presence);
        // in a worker thread
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }
}
