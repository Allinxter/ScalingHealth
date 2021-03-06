/*
 * Scaling Health
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.scalinghealth.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.silentchaos512.lib.util.ChatHelper;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.scalinghealth.ScalingHealth;
import net.silentchaos512.scalinghealth.config.Config;
import net.silentchaos512.scalinghealth.entity.EntityBlightFire;
import net.silentchaos512.scalinghealth.init.ModItems;
import net.silentchaos512.scalinghealth.network.NetworkHandler;
import net.silentchaos512.scalinghealth.network.message.MessageMarkBlight;

import java.util.List;

public final class BlightHandler {
    public static final BlightHandler INSTANCE = new BlightHandler();

    public static final String NBT_BLIGHT = ScalingHealth.MOD_ID_OLD + ".IsBlight";

    public static final int UPDATE_DELAY = 200;
    public static final int UPDATE_DELAY_SALT = 5 + ScalingHealth.random.nextInt(10);

    private BlightHandler() {
    }

    // ******************
    // * Blight marking *
    // ******************

    public static boolean isBlight(EntityLivingBase entityLiving) {
        return entityLiving != null && entityLiving.getEntityData().getBoolean(NBT_BLIGHT);
    }

    public static void markBlight(EntityLivingBase entityLiving) {
        if (entityLiving != null)
            entityLiving.getEntityData().setBoolean(NBT_BLIGHT, true);
    }

    public static void spawnBlightFire(EntityLivingBase blight) {
        if (blight.world.isRemote)
            return;

        EntityBlightFire fire = new EntityBlightFire(blight);
        fire.setPosition(blight.posX, blight.posY, blight.posZ);
        blight.world.spawnEntity(fire);
        if (Config.BLIGHT_FIRE_RIDES_BLIGHT)
            fire.startRiding(blight);
    }

    public static EntityBlightFire getBlightFire(EntityLivingBase blight) {
        World world = blight.world;
        List<EntityBlightFire> fireList = world.getEntities(EntityBlightFire.class, e -> true);

        for (EntityBlightFire fire : fireList)
            if (fire.getParent() != null && fire.getParent().equals(blight))
                return fire;

        return null;
    }

    public static void applyBlightPotionEffects(EntityLivingBase entityLiving) {
        int duration = Config.BLIGHT_POTION_DURATION;
        if (duration < 0) {
            duration = Integer.MAX_VALUE;
        } else if (duration == 0) {
            return;
        }

        // TODO: Replace specific potion effects with a list where users can add specific effects.

        // Invisibility
        if (Config.BLIGHT_INVISIBLE)
            entityLiving
                    .addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, duration, 0, true, false));
        // Fire Resistance
        if (Config.BLIGHT_FIRE_RESIST)
            entityLiving
                    .addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, duration, 0, true, false));
        // Speed
        if (Config.BLIGHT_AMP_SPEED > -1)
            entityLiving.addPotionEffect(new PotionEffect(MobEffects.SPEED, duration,
                    Config.BLIGHT_AMP_SPEED, true, false));
        // Strength
        if (Config.BLIGHT_AMP_STRENGTH > -1)
            entityLiving.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, duration,
                    Config.BLIGHT_AMP_STRENGTH, true, false));
    }

    // **********
    // * Events *
    // **********

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlightKilled(LivingDeathEvent event) {
        if (event.getSource() == null || !isBlight(event.getEntityLiving()) || event.getEntity().world.isRemote)
            return;

        LocalizationHelper loc = ScalingHealth.localizationHelper;

        Entity entitySource = event.getSource().getTrueSource();
        boolean isTamedAnimal = entitySource instanceof EntityTameable && ((EntityTameable) entitySource).isTamed();
        if (entitySource instanceof EntityPlayer || isTamedAnimal) {
            // Killed by a player or a player's pet.
            EntityLivingBase blight = event.getEntityLiving();
            EntityPlayer player;
            EntityLivingBase actualKiller;
            if (isTamedAnimal) {
                player = (EntityPlayer) ((EntityTameable) entitySource).getOwner();
                actualKiller = (EntityLivingBase) entitySource;
            } else {
                actualKiller = player = (EntityPlayer) entitySource;
            }

            // Tell all players that the blight was killed.
            if (Config.BLIGHT_NOTIFY_PLAYERS_ON_DEATH) {
                String message = loc.getLocalizedString("blight", "killedByPlayer", blight.getName(), actualKiller.getName());
                ScalingHealth.logHelper.info("{}", message);
                for (EntityPlayer p : player.world.getPlayers(EntityPlayer.class, e -> true))
                    ChatHelper.sendMessage(p, message);
            }

            // Drop hearts!
            final boolean canGetHearts = !(player instanceof FakePlayer) || Config.FAKE_PLAYERS_CAN_GENERATE_HEARTS;
            final int min = Config.HEARTS_DROPPED_BY_BLIGHT_MIN;
            final int max = Config.HEARTS_DROPPED_BY_BLIGHT_MAX;
            final int heartCount = ScalingHealth.random.nextInt(max - min + 1) + min;

            if (canGetHearts && heartCount > 0) {
                Item itemToDrop = Config.HEART_DROP_SHARDS_INSTEAD ? ModItems.crystalShard : ModItems.heart;
                blight.dropItem(itemToDrop, heartCount);
            }
        } else {
            // Killed by something else.
            EntityLivingBase blight = event.getEntityLiving();

            // Tell all players that the blight died.
            if (Config.BLIGHT_NOTIFY_PLAYERS_ON_DEATH) {
                String message = event.getSource().getDeathMessage(blight).getFormattedText();
                String blightName = loc.getLocalizedString("blight", "name", blight.getName());
                message = message.replaceFirst(blight.getName(), blightName);

                if (message.contains("drowned")) {
                    if (message.startsWith("Blight Squid"))
                        message += "... again";
                    else
                        message += "... gg";
                } else if (message.contains("suffocated in a wall")) {
                    message += " *slow clap*";
                }

                ScalingHealth.logHelper.info("{}", message);
                for (EntityPlayer p : blight.world.getPlayers(EntityPlayer.class, e -> true))
                    ChatHelper.sendMessage(p, message);
            }
        }
    }

    @SubscribeEvent
    public void onBlightUpdate(LivingUpdateEvent event) {
        EntityLivingBase entityLiving = event.getEntityLiving();
        if (entityLiving != null && !entityLiving.world.isRemote && isBlight(entityLiving)) {
            World world = entityLiving.world;

            // Add in entity ID so not all blights update on the same tick
            if ((world.getTotalWorldTime() + entityLiving.getEntityId()) % UPDATE_DELAY == 0) {
                // Send message to clients to make sure they know the entity is a blight.
                MessageMarkBlight message = new MessageMarkBlight(entityLiving);
                NetworkHandler.INSTANCE.sendToAllAround(message, new TargetPoint(entityLiving.dimension,
                        entityLiving.posX, entityLiving.posY, entityLiving.posZ, 128));

                // Effects
                // Assign a blight fire if necessary.
                if (getBlightFire(entityLiving) == null)
                    spawnBlightFire(entityLiving);

                // Refresh potion effects
                applyBlightPotionEffects(entityLiving);
            }
        }
    }
}
