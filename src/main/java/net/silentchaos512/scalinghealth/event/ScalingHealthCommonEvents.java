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
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.silentchaos512.lib.util.ChatHelper;
import net.silentchaos512.scalinghealth.ScalingHealth;
import net.silentchaos512.scalinghealth.config.Config;
import net.silentchaos512.scalinghealth.init.ModItems;
import net.silentchaos512.scalinghealth.init.ModSounds;
import net.silentchaos512.scalinghealth.lib.module.ModuleAprilTricks;
import net.silentchaos512.scalinghealth.utils.ModifierHandler;
import net.silentchaos512.scalinghealth.utils.SHPlayerDataHandler;
import net.silentchaos512.scalinghealth.utils.SHPlayerDataHandler.PlayerData;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Random;

public class ScalingHealthCommonEvents {

  @SubscribeEvent
  public void onLivingDrops(LivingDropsEvent event) {

    // Handle heart drops.
    // Was a player responsible for the death?
    EntityPlayer player = getPlayerThatCausedDeath(event.getSource());
    if (player == null || (player instanceof FakePlayer
        && !Config.FAKE_PLAYERS_CAN_GENERATE_HEARTS)) {
      return;
    }

    EntityLivingBase killedEntity = event.getEntityLiving();
    if (!killedEntity.world.isRemote) {
      Random rand = ScalingHealth.random;
      int stackSize = 0;

      // Different drop rates for hostiles and passives.
      float dropRate = killedEntity instanceof IMob ? Config.HEART_DROP_CHANCE_HOSTILE
          : Config.HEART_DROP_CHANCE_PASSIVE;
      if (killedEntity instanceof EntitySlime) {
        dropRate /= 6f;
      }

      // Basic heart drops for all mobs.
      if (event.isRecentlyHit() && rand.nextFloat() <= dropRate) {
        stackSize += 1;
      }

      // Heart drops for bosses.
      if (!killedEntity.isNonBoss()) {
        int min = Config.HEARTS_DROPPED_BY_BOSS_MIN;
        int max = Config.HEARTS_DROPPED_BY_BOSS_MAX;
        stackSize += min + rand.nextInt(max - min + 1);
      }

      if (stackSize > 0) {
        Item itemToDrop = Config.HEART_DROP_SHARDS_INSTEAD ? ModItems.crystalShard
            : ModItems.heart;
        killedEntity.dropItem(itemToDrop, stackSize);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onXPDropped(LivingExperienceDropEvent event) {

    EntityLivingBase entityLiving = event.getEntityLiving();

    // Additional XP from all mobs.
    short difficulty = entityLiving.getEntityData()
        .getShort(DifficultyHandler.NBT_ENTITY_DIFFICULTY);
    float multi = 1.0f + Config.MOB_XP_BOOST * difficulty;

    float amount = event.getDroppedExperience();
    amount *= multi;

    // Additional XP from blights.
    if (BlightHandler.isBlight(entityLiving)) {
      amount *= Config.BLIGHT_XP_MULTIPLIER;
    }

    event.setDroppedExperience(Math.round(amount));
  }

  /**
   * Get the player that caused a mob's death. Could be a FakePlayer or null.
   *
   * @return The player that caused the damage, or the owner of the tamed animal that caused the damage.
   */
  private @Nullable EntityPlayer getPlayerThatCausedDeath(DamageSource source) {

    if (source == null) {
      return null;
    }

    // Player is true source.
    Entity entitySource = source.getTrueSource();
    if (entitySource instanceof EntityPlayer) {
      return (EntityPlayer) entitySource;
    }

    // Player's pet is true source.
    boolean isTamedAnimal = entitySource instanceof EntityTameable
        && ((EntityTameable) entitySource).isTamed();
    if (entitySource instanceof EntityTameable) {
      EntityTameable tamed = (EntityTameable) entitySource;
      if (tamed.isTamed() && tamed.getOwner() instanceof EntityPlayer) {
        return (EntityPlayer) tamed.getOwner();
      }
    }

    // No player responsible.
    return null;
  }

  @SubscribeEvent
  public void onPlayerDied(LivingDeathEvent event) {

    if (event.getEntity() == null || !(event.getEntity() instanceof EntityPlayer)) {
      return;
    }

    EntityPlayer player = (EntityPlayer) event.getEntity();

    if (ModuleAprilTricks.instance.isEnabled() && ModuleAprilTricks.instance.isRightDay()) {
      ScalingHealth.proxy.playSoundOnClient(player, ModSounds.PLAYER_DIED, 0.6f, 1f);
    }
  }

  @SubscribeEvent
  public void onPlayerRespawn(
      net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {

    // Set player health correctly after respawn.
    if (event.player instanceof EntityPlayerMP) {
      EntityPlayerMP player = (EntityPlayerMP) event.player;
      PlayerData data = SHPlayerDataHandler.get(player);

      // Lose health on death?
      if (Config.PLAYER_HEALTH_LOST_ON_DEATH > 0 && !event.isEndConquered()) {
        float newHealth = data.getMaxHealth() - Config.PLAYER_HEALTH_LOST_ON_DEATH;
        float startHealth = Config.PLAYER_STARTING_HEALTH;
        data.setMaxHealth(newHealth < startHealth ? startHealth : newHealth);
      }

      // Lose difficulty on death?
      if (!event.isEndConquered()) {
        double currentDifficulty = data.getDifficulty();
        double newDifficulty = MathHelper.clamp(
            currentDifficulty - Config.DIFFICULTY_LOST_ON_DEATH,
            Config.DIFFICULTY_MIN, Config.DIFFICULTY_MAX);
        data.setDifficulty(newDifficulty);
      }

      // Apply health modifier
      if (Config.ALLOW_PLAYER_MODIFIED_HEALTH) {
        float health = player.getHealth();
        float maxHealth = data.getMaxHealth();
        ModifierHandler.setMaxHealth(player, maxHealth, 0);
        if (health != maxHealth && maxHealth > 0) {
          player.setHealth(player.getMaxHealth());
        }
      }
    }
  }

  @SubscribeEvent
  public void onPlayerJoinedServer(PlayerLoggedInEvent event) {

    // Sync player data and set health.
    if (event.player instanceof EntityPlayerMP) {
      EntityPlayerMP player = (EntityPlayerMP) event.player;
      PlayerData data = SHPlayerDataHandler.get(player);

      // Resets, based on config?
      Calendar today = Calendar.getInstance();
      Calendar lastTimePlayed = data.getLastTimePlayed();

      if (Config.DIFFFICULTY_RESET_TIME.shouldReset(today, lastTimePlayed)) {
        ScalingHealth.logHelper.info(String.format("Reset player %s's difficulty to %d",
            player.getName(), (int) Config.DIFFICULTY_DEFAULT));
        ChatHelper.sendMessage(player, "[Scaling Health] Your difficulty has been reset.");
        data.setDifficulty(Config.DIFFICULTY_DEFAULT);
      }
      if (Config.PLAYER_HEALTH_RESET_TIME.shouldReset(today, lastTimePlayed)) {
        data.setMaxHealth(Config.PLAYER_STARTING_HEALTH);
        ScalingHealth.logHelper.info(String.format("Reset player %s's health to %d",
            player.getName(), (int) Config.PLAYER_STARTING_HEALTH));
        ChatHelper.sendMessage(player, "[Scaling Health] Your health has been reset.");
      }

      data.getLastTimePlayed().setTime(today.getTime());

      // Apply health modifier
      if (Config.ALLOW_PLAYER_MODIFIED_HEALTH) {
        float health = player.getHealth();
        float maxHealth = data.getMaxHealth();
        ModifierHandler.setMaxHealth(player, maxHealth, 0);
      }
    }

    if (ModuleAprilTricks.instance.isEnabled() && ModuleAprilTricks.instance.isRightDay()) {
      ChatHelper.sendMessage(event.player,
          TextFormatting.RED + "[Scaling Health] It's April Fool's time... hehehe.");
    }
  }

  @SubscribeEvent
  public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {

    if (!event.getEntityPlayer().world.isRemote && event.getResultStatus() == SleepResult.OK
        && Config.WARN_WHEN_SLEEPING
        && Config.DIFFICULTY_FOR_SLEEPING > 0f) {
      ChatHelper.sendStatusMessage(event.getEntityPlayer(),
          TextFormatting.RED + ScalingHealth.localizationHelper.getMiscText("sleepWarning"), false);
    }
  }

  @SubscribeEvent
  public void onPlayerWakeUp(PlayerWakeUpEvent event) {

    ScalingHealth.logHelper.debug(event.getEntityPlayer().world.isRemote, event.updateWorld(),
        event.shouldSetSpawn());
    if (!event.getEntityPlayer().world.isRemote && !event.updateWorld()) {
      EntityPlayer player = event.getEntityPlayer();
      PlayerData data = SHPlayerDataHandler.get(player);
      if (data != null) {
        data.incrementDifficulty(Config.DIFFICULTY_FOR_SLEEPING, false);
      }

      // TODO: World difficulty increase?
    }
  }

  @SubscribeEvent
  public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {

    if (event.getModID().equals(ScalingHealth.MOD_ID_LOWER)) {
      Config.INSTANCE.load();
      Config.INSTANCE.save();
    }
  }
}
