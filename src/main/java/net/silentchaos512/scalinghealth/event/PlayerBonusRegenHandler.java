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

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.silentchaos512.scalinghealth.config.Config;

import java.util.HashMap;
import java.util.Map;

public class PlayerBonusRegenHandler {

  public static PlayerBonusRegenHandler INSTANCE = new PlayerBonusRegenHandler();

  private Map<String, Integer> timers = new HashMap<>();

  public int getTimerForPlayer(EntityPlayer player) {

    if (player == null || !timers.containsKey(player.getName()))
      return -1;
    return timers.get(player.getName());
  }

  @SubscribeEvent
  public void onPlayerTick(PlayerTickEvent event) {

    if (event.side == Side.CLIENT || !Config.ENABLE_BONUS_HEALTH_REGEN)
      return;

    EntityPlayer player = event.player;
    String name = player.getName();

    // Add player timer if needed.
    if (!timers.containsKey(name))
      timers.put(name, Config.BONUS_HEALTH_REGEN_INITIAL_DELAY);

    int foodLevel = player.getFoodStats().getFoodLevel();
    boolean foodLevelOk = foodLevel >= Config.BONUS_HEALTH_REGEN_MIN_FOOD
        && foodLevel <= Config.BONUS_HEALTH_REGEN_MAX_FOOD;

    if (player.getHealth() < player.getMaxHealth() && foodLevelOk) {
      // Tick timer, heal player and reset on 0.
      int timer = timers.get(name);
      if (--timer <= 0) {
        player.heal(1f);
        player.addExhaustion(Config.BONUS_HEALTH_REGEN_EXHAUSTION);
        timer = Config.BONUS_HEALTH_REGEN_DELAY;
      }
      timers.put(name, timer);
    }
  }

  @SubscribeEvent
  public void onPlayerHurt(LivingHurtEvent event) {

    EntityLivingBase entityLiving = event.getEntityLiving();
    if (entityLiving.world.isRemote || !(entityLiving instanceof EntityPlayer))
      return;
    timers.put(entityLiving.getName(), Config.BONUS_HEALTH_REGEN_INITIAL_DELAY);
  }
}
