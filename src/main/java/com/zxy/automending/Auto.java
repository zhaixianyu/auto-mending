package com.zxy.automending;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.world.World;

import java.util.ArrayList;


public class Auto {
    static Auto auto;
    MinecraftClient mc;
    ClientPlayerEntity player;
    int temp = -1;
    boolean runing = false;
    ArrayList<Integer> zhuangbei;
    int tick = 0;

    public void autoMenDing(PlayerEntity player) {
        ScreenHandler sc = player.currentScreenHandler;
        if (runing && !sc.slots.get(45).getStack().isDamaged()) reSwitch();
        tick = 0;
        if (mc == null || !player.equals(mc.player)) return;

        for (int i = 0; i < sc.slots.size(); i++) {
            ItemStack item = sc.slots.get(i).getStack();
            if (runing) {
                xiejia(sc);
                break;
            }
            if (
                    item.isOf(Items.AIR) ||
                            EnchantmentHelper.getLevel(Enchantments.MENDING, item) <= 0 ||
                            !item.isDamaged() ||
                            item.equals(player.getMainHandStack()) ||
                            i == 45 || i == 5 || i == 6 || i == 7 || i == 8
            ) continue;
            autoSwitch(i, 45);
            temp = i;
            runing = true;
            break;
        }
    }

    public void tick() {
        if (!runing) return;
        if (0 == ++tick % 10) reSwitch();
    }

    public synchronized void autoSwitch(int a, int b) {
        if (mc.interactionManager == null) {
            return;
        }
        if (player.currentScreenHandler != player.playerScreenHandler) {
            player.closeHandledScreen();
        }
        ScreenHandler screenHandler = player.currentScreenHandler;
        mc.interactionManager.clickSlot(screenHandler.syncId, a, 0, SlotActionType.PICKUP, player);
        mc.interactionManager.clickSlot(screenHandler.syncId, b, 0, SlotActionType.PICKUP, player);
        mc.interactionManager.clickSlot(screenHandler.syncId, a, 0, SlotActionType.PICKUP, player);

    }

    public void reSwitch() {
        if (runing) {
            if (temp != -1) autoSwitch(temp, 45);
            for (int i = 0; i < 4; i++) {
                System.out.println(zhuangbei.get(i));
                if (zhuangbei.get(i) != -1) {
                    autoSwitch(zhuangbei.get(i), i + 5);
                    zhuangbei.set(i, -1);
                }
            }
            tick = 0;
            temp = -1;
            runing = false;
        }
    }

    public void xiejia(ScreenHandler sc) {
        for (int q = 5; q < 9; q++) {
            ItemStack item = sc.slots.get(q).getStack();
            if (item.isEmpty() || item.isDamaged() || EnchantmentHelper.getLevel(Enchantments.MENDING, item) <= 0)
                continue;
            a:
            for (int air = q; air < sc.slots.size(); air++) {
                if (air == 5 || air == 6 || air == 7 || air == 8 || air == 45) continue;
                if (sc.slots.get(air).getStack().isOf(Items.AIR)) {
                    for (Integer zb : zhuangbei) {
                        if (zb == -1) {
                            for (Integer zby : zhuangbei) {
                                if (zby == air) break a;
                            }
//                            System.out.println("头盔：" + zhuangbei.get(0));
//                            System.out.println("胸甲：" + zhuangbei.get(1));
//                            System.out.println("裤子：" + zhuangbei.get(2));
//                            System.out.println("鞋子：" + zhuangbei.get(3));
//                            System.out.println("空位：" + air);
                            autoSwitch(q, air);
                            zhuangbei.set(q - 5, air);
                            break a;
                        }
                    }
                }
            }
        }
    }


    public Auto(MinecraftClient client) {
        this.mc = client;
        this.player = client.player;
        zhuangbei = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            zhuangbei.add(i, -1);
        }
        auto = this;
    }

    public static Auto getAuto() {
        return auto;
    }
}
