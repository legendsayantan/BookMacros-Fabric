package com.legendsayantan.fabric.bookmacros.mixin;

import com.legendsayantan.fabric.bookmacros.client.BookMacrosClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.LecternScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.LecternScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.legendsayantan.fabric.bookmacros.client.BookMacrosClient.getPageContents;


@Mixin(LecternScreen.class)
public abstract class LecternScreenMixin extends BookScreen implements ScreenHandlerProvider<LecternScreenHandler> {

    @Shadow @Final private LecternScreenHandler handler;
    int pageIndex = 0;

    @Inject(method = "addCloseButton", at = @At("TAIL"))
    private void addExecuteButton(CallbackInfo info){
        assert this.client != null;
        assert this.client.player != null;
        if (this.client.player.canModifyBlocks()) {
            this.addDrawableChild(ButtonWidget.builder(Text.of("Copy to chat"), (button) -> {
                this.close();
                ItemStack bookItem = this.getScreenHandler().getBookItem();
                BookMacrosClient.putIntoChat(getPageContents(bookItem,this.pageIndex).getString());
            }).dimensions(this.width/2 - 100, 220, 98, 20).build());
            this.addDrawableChild(ButtonWidget.builder(Text.of("Send to chat"), (button) -> {
                this.close();
                ItemStack bookItem = this.getScreenHandler().getBookItem();
                Text data = getPageContents(bookItem,this.pageIndex);
                BookMacrosClient.sendToChat(data.getString(), this.client);
            }).dimensions(this.width/2 + 2, 220, 98, 20).build());
        }
    }

    @Inject(method = "updatePage", at = @At("TAIL"))
    private void setPageIndex(CallbackInfo info){
        pageIndex = handler.getPage();
    }
}
