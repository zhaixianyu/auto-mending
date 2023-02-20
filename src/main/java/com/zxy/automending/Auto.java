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
    static Auto auto ;
    MinecraftClient mc;
    ClientPlayerEntity player;
    int temp = -1;
    boolean runing = false;
    World world = null;
    int tick = 0;
    int toukui = -1;
    int xiongjia = -1;
    int kuzi = -1;
    int xiezi = -1;

    public void autoMenDing(PlayerEntity player){
        ScreenHandler sc =  player.currentScreenHandler;
        if(runing && !sc.slots.get(45).getStack().isDamaged()) reSwitch();
        tick = 0;
        if(mc==null || !player.equals(mc.player)) return;
        if(player.getWorld().equals(world)) {
            temp = -1;
            tick = 0;
            runing = false;
        }

        for(int i=0 ; i < sc.slots.size() ; i++){
            ItemStack item = sc.slots.get(i).getStack();
            if(runing){
                xiejia(i,sc);
                continue;
            }
            if(
                    item.isOf(Items.AIR)||
                    EnchantmentHelper.getLevel(Enchantments.MENDING,item)<=0 ||
                    !item.isDamaged()||
                    item.equals(player.getMainHandStack())||
                    i==45 || i==5|| i==6|| i==7|| i==8
            )continue;
            autoSwitch(i,45);
            temp = i;
            runing = true;
            break;
        }
    }
    public void tick(){
        if(!runing)return;
        if(0 == ++tick % 10 ) reSwitch();
    }

    public void autoSwitch(int a, int b) {
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

    public void reSwitch(){
        if(runing) {
           if(temp!=-1) autoSwitch(temp, 45);
           if(toukui!=-1) {
               autoSwitch(toukui, 5);
               toukui = -1;
           }
           if(xiongjia!=-1) {
               autoSwitch(xiongjia, 6);
               xiongjia = -1;
           }
           if(kuzi!=-1){
               autoSwitch(kuzi, 7);
               kuzi = -1;
           }
           if(xiezi!=-1) {
               autoSwitch(xiezi, 8);
               xiezi = -1;
           }
            tick = 0;
            temp = -1;
            runing = false;
        }
    }
    public void xiejia(int i ,ScreenHandler sc){
        ItemStack item = sc.slots.get(i).getStack();
        if(item.isEmpty() || item.isDamaged() || EnchantmentHelper.getLevel(Enchantments.MENDING,item)<=0) return;
        if(i==5 ){
            for(; i < sc.slots.size();i++){
                if(sc.slots.get(i).getStack().isOf(Items.AIR)){
                    if(i==5||i==6||i==7||i==8||i==45) continue;
                    toukui = i;
                    autoSwitch(5,i);
                    return;
                }
            }
        }
        if(i==6 ){
            for(; i < sc.slots.size();i++){
                if(sc.slots.get(i).getStack().isOf(Items.AIR)){
                    if(i==5||i==6||i==7||i==8||i==45) continue;
                    xiongjia = i;
                    autoSwitch(6,i);
                    return;
                }
            }
        }
        if(i==7 ){
            for(; i < sc.slots.size();i++){
                if(sc.slots.get(i).getStack().isOf(Items.AIR)){
                    if(i==5||i==6||i==7||i==8||i==45) continue;
                    kuzi = i;
                    autoSwitch(7,i);
                    return;
                }
            }
        }
        if(i==8 ){
            for( ; i < sc.slots.size();i++){
                if(sc.slots.get(i).getStack().isOf(Items.AIR)){
                    if(i==5||i==6||i==7||i==8||i==45) continue;
                    xiezi = i;
                    autoSwitch(8,i);
                    return;
                }
            }
        }
    }





    public Auto(MinecraftClient client){
        this.mc = client;
        this.player = client.player;
        auto = this;
    }
    public static Auto getAuto(){
        return auto;
    }
}
