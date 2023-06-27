package com.github.et118.betteranvils.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import java.util.Map;
import static net.minecraft.screen.AnvilScreenHandler.getNextCost;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(
            method="updateResult",
            at=@At(value = "INVOKE", target="Lnet/minecraft/screen/Property;get()I"),
            locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void injected(CallbackInfo ci, ItemStack itemStack, int i, int j, int k, ItemStack itemStack2, ItemStack itemStack3, Map map) {
        if (!itemStack2.isEmpty()) {
            int t = itemStack2.getRepairCost();
            if (!itemStack3.isEmpty() && t < itemStack3.getRepairCost()) {
                t = itemStack3.getRepairCost();
            }

            if (k != i || k == 0) {
                t = getNextCost(t);
            }

            itemStack2.setRepairCost(t);
            EnchantmentHelper.set(map, itemStack2);
        }

        output.setStack(0, itemStack2);
        sendContentUpdates();
        ci.cancel();
    }
}