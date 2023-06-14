package com.legendsayantan.fabric.bookmacros.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;

import java.util.Objects;

public class BookMacrosClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Initialised Client");

    }

    public static void putIntoChat(String message){
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().setScreen(new ChatScreen(message));
    }
    public static void sendToChat(String message, MinecraftClient client) {
        if (message.startsWith("/")) {
            Objects.requireNonNull(client.getNetworkHandler()).sendChatCommand(message.length() > 256 ? message.substring(1, 256) : message.substring(1));
        } else {
            Objects.requireNonNull(client.getNetworkHandler()).sendChatMessage(message.length() > 256 ? message.substring(0, 256) : message);
        }
    }

    public static Text getPageContents(ItemStack book, int pageIndex) {
        NbtCompound bookTag = book.getNbt();
        if (bookTag != null)
            if (bookTag.contains("pages")) {
                NbtList pagesTag = bookTag.getList("pages", 8); // 8 represents string tag type
                if (pageIndex >= 0 && pageIndex < pagesTag.size()) {
                    String pageContents = pagesTag.getString(pageIndex);
                    if (book.getItem() instanceof WrittenBookItem) {
                        pageContents = pageContents.substring(9, pageContents.length() - 2);
                    }
                    return Text.of(pageContents);
                }
                return Text.of("");
            } else return Text.of("");
        else return Text.of("");
    }
}
