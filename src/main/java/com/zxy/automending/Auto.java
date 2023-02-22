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

import java.util.ArrayList;


public class Auto {
    static Auto auto;
    MinecraftClient mc;
    ClientPlayerEntity player;
    int fushou = -1;
    boolean run = false;
    ArrayList<Integer> zhuangbei;
    int tick = 0;

    public void autoMenDing(PlayerEntity player) {
        ScreenHandler sc = player.currentScreenHandler;
        if (run && !sc.slots.get(45).getStack().isDamaged()) reSwitch();
        tick = 0;
        if (mc == null || !player.equals(mc.player)) return;

        for (int i = 0; i < sc.slots.size(); i++) {
            ItemStack item = sc.slots.get(i).getStack();
            if (run) {
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
//            System.out.println("修补："+i);
            autoSwitch(i, 45);
            fushou = i;
            run = true;
            break;
        }
    }

    public void tick() {
        if (!run) return;
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
        if (run) {
//            System.out.println("副手归位: " + temp);
            if (fushou != -1) autoSwitch(fushou, 45);
            for (int i = 0; i < 4; i++) {

                if (zhuangbei.get(i) != -1) {
//                    System.out.println("装备归位:" + zhuangbei.get(i) +"  "+ (i+5));
                    autoSwitch(zhuangbei.get(i), i + 5);
                    zhuangbei.set(i, -1);
                }
            }
            tick = 0;
            fushou = -1;
            run = false;
        }
    }

    public void xiejia(ScreenHandler sc) {
        if(player.getHealth() < 20 || player.isFallFlying()) return;
        for (int q = 5; q < 9; q++) {
            ItemStack item = sc.slots.get(q).getStack();
            if (item.isEmpty() || item.isDamaged() || EnchantmentHelper.getLevel(Enchantments.MENDING, item) <= 0)
                continue;
            a:
            for (int air = q; air < sc.slots.size(); air++) {
                if (air == 5 || air == 6 || air == 7 || air == 8 || air == 45 || air == fushou) continue;
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
//                            System.out.println("卸甲：" + air + "  " + q);
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
