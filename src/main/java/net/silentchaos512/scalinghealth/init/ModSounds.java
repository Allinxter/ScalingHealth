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

package net.silentchaos512.scalinghealth.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.silentchaos512.lib.registry.IRegistrationHandler;
import net.silentchaos512.lib.registry.SRegistry;
import net.silentchaos512.scalinghealth.ScalingHealth;

public class ModSounds implements IRegistrationHandler<SoundEvent> {

  public static final SoundEvent PLAYER_DIED = create("player_died");

  @Override
  public void registerAll(SRegistry reg) {

    reg.registerSoundEvent(PLAYER_DIED, "player_died");
  }

  private static SoundEvent create(String soundId) {

    ResourceLocation name = new ResourceLocation(ScalingHealth.MOD_ID_LOWER, soundId);
    return new SoundEvent(name);
  }
}
