package com.legendsayantan.fabric.bookmacros.mixin;

import com.legendsayantan.fabric.bookmacros.client.BookMacrosClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WritableBookItem;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static com.legendsayantan.fabric.bookmacros.client.BookMacrosClient.getPageContents;

@Mixin(BookEditScreen.class)
public class BookEditScreenMixin extends Screen {

    @Shadow
    private int currentPage;

    protected BookEditScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addExecuteButton(CallbackInfo info){
        assert this.client != null;
        assert this.client.player != null;

        if (this.client.player.canModifyBlocks()) {
            this.addDrawableChild(ButtonWidget.builder(Text.of("Copy to chat"), (button) -> {
                this.close();
                BookMacrosClient.putIntoChat(getPageContents(getBook(),this.currentPage).getString());
            }).dimensions(this.width/2 - 100, 220, 98, 20).build());
            this.addDrawableChild(ButtonWidget.builder(Text.of("Send to chat"), (button) -> {
                this.close();
                Text data = getPageContents(getBook(),this.currentPage);
                BookMacrosClient.sendToChat(data.getString(), (MinecraftClient) (Object) this.client);
            }).dimensions(this.width/2 + 2, 220, 98, 20).build());
        }
    }
    ItemStack getBook(){
        assert Objects.requireNonNull(this.client).player != null;
        assert this.client.player != null;
        ItemStack bookItem = this.client.player.getInventory().getMainHandStack();
        if(!(bookItem.getItem() instanceof WritableBookItem)) bookItem = this.client.player.getOffHandStack();
        return bookItem;
    }

}
