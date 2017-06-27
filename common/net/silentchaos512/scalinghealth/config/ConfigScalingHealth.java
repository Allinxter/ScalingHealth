package net.silentchaos512.scalinghealth.config;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.silentchaos512.lib.config.AdaptiveConfig;
import net.silentchaos512.scalinghealth.ScalingHealth;
import net.silentchaos512.scalinghealth.lib.EnumAreaDifficultyMode;
import net.silentchaos512.scalinghealth.lib.EnumHealthModMode;

public class ConfigScalingHealth extends AdaptiveConfig {

  public static boolean DEBUG_MODE = false;

  // Client
  public static boolean CHANGE_HEART_RENDERING = true;
  public static boolean RENDER_DIFFICULTY_METER = true;
  public static boolean RENDER_DIFFICULTY_METER_ALWAYS = false;
  public static int DIFFICULTY_METER_DISPLAY_TIME = 160;
  public static int DIFFICULTY_METER_POS_X = 5;
  public static int DIFFICULTY_METER_POS_Y = -30;
  public static int[] HEART_COLORS = {//
      0xBF0000, // 0 red
      0xE66000, // 25 orange-red
      0xE69900, // 40 orange
      0xE6D300, // 55 yellow
      0x99E600, // 80 lime
      0x4CE600, // 100 green
      0x00E699, // 160 teal
      0x00E6E6, // 180 aqua
      0x0099E6, // 200 sky blue
      0x0000E6, // 240 blue
      0x9900E6, // 280 dark purple
      0xD580FF, // 280 light purple
      0x8C8C8C, // 0 gray
      0xE6E6E6  // 0 white
  };

  // Player Health
  public static boolean ALLOW_PLAYER_MODIFIED_HEALTH = true;
  public static int PLAYER_STARTING_HEALTH = 20;
  public static int PLAYER_HEALTH_MAX = 0;
  public static int PLAYER_HEALTH_LOST_ON_DEATH = 0;
  // Regen
  public static boolean ENABLE_BONUS_HEALTH_REGEN = true;
  public static int BONUS_HEALTH_REGEN_MIN_FOOD = 10;
  public static int BONUS_HEALTH_REGEN_MAX_FOOD = 20;
  public static int BONUS_HEALTH_REGEN_INITIAL_DELAY = 400;
  public static int BONUS_HEALTH_REGEN_DELAY = 100;

  // Mobs
  public static float DIFFICULTY_DAMAGE_MULTIPLIER = 0.1f;
  // Mob Health
  public static boolean ALLOW_PEACEFUL_EXTRA_HEALTH = true;
  public static boolean ALLOW_HOSTILE_EXTRA_HEALTH = true;
  public static float DIFFICULTY_GENERIC_HEALTH_MULTIPLIER = 0.5F;
  public static float DIFFICULTY_PEACEFUL_HEALTH_MULTIPLIER = 0.25F;
  public static EnumHealthModMode MOB_HEALTH_SCALING_MODE = EnumHealthModMode.MULTI_HALF;
  private static List<String> MOB_HEALTH_BLACKLIST;
  private static String[] MOB_HEALTH_BLACKLIST_DEFAULTS = new String[] {};
  // Blights
  public static float BLIGHT_CHANCE_MULTIPLIER = 0.0625F;
  public static int BLIGHT_AMP_SPEED = 8;
  public static int BLIGHT_AMP_STRENGTH = 2;
  public static boolean BLIGHT_FIRE_RIDES_BLIGHT = false;
  public static boolean BLIGHT_INVISIBLE = false;
  public static boolean BLIGHT_FIRE_RESIST = true;
  public static float BLIGHT_XP_MULTIPLIER = 10f;
  public static boolean BLIGHT_SUPERCHARGE_CREEPERS = true;
  public static boolean BLIGHT_NOTIFY_PLAYERS_ON_DEATH = true;
  private static List<String> BLIGHT_BLACKLIST;
  private static String[] BLIGHT_BLACKLIST_DEFAULTS = new String[] { "WitherBoss", "Villager" };

  // Items
  public static float HEART_DROP_CHANCE_HOSTILE = 0.01F;
  public static float HEART_DROP_CHANCE_PASSIVE = 0.001f;
  public static int HEARTS_DROPPED_BY_BOSS_MIN = 3;
  public static int HEARTS_DROPPED_BY_BOSS_MAX = 6;
  public static int HEARTS_DROPPED_BY_BLIGHT_MIN = 0;
  public static int HEARTS_DROPPED_BY_BLIGHT_MAX = 2;

  // Difficulty
  public static float DIFFICULTY_MAX = 250;
  public static float DIFFICULTY_DEFAULT = 0;
  public static int HOURS_TO_MAX_DIFFICULTY = 60; // Not actually loaded, just to make calc below clear.
  public static float DIFFICULTY_PER_SECOND = DIFFICULTY_MAX / (HOURS_TO_MAX_DIFFICULTY * 3600);
  public static float DIFFICULTY_PER_BLOCK = DIFFICULTY_MAX / 100000;
  public static float DIFFICULTY_PER_KILL = 0;
  public static float DIFFICULTY_IDLE_MULTI = 0.7f;
  public static float DIFFICULTY_LOST_ON_DEATH = 0f;
  public static float DIFFICULTY_GROUP_AREA_BONUS = 0.05f;
  public static int DIFFICULTY_SEARCH_RADIUS = 160;
  public static EnumAreaDifficultyMode AREA_DIFFICULTY_MODE = EnumAreaDifficultyMode.WEIGHTED_AVERAGE;

  // Network
  public static int PACKET_DELAY = 20;

  // World
  public static float HEART_CRYSTAL_ORE_VEIN_COUNT = 3f / 7f;
  public static int HEART_CRYSTAL_ORE_VEIN_SIZE = 6;
  public static int HEART_CRYSTAL_ORE_MIN_HEIGHT = 10;
  public static int HEART_CRYSTAL_ORE_MAX_HEIGHT = 35;
  public static float HEART_CRYSTAL_ORE_EXTRA_VEIN_CAP = 3f;
  public static float HEART_CRYSTAL_ORE_EXTRA_VEIN_RATE = HEART_CRYSTAL_ORE_EXTRA_VEIN_CAP / 3125;
  public static int HEART_CRYSTAL_ORE_QUANTITY_DROPPED = 1;

  static final String split = Configuration.CATEGORY_SPLITTER;
  public static final String CAT_MAIN = "main";
  public static final String CAT_CLIENT = CAT_MAIN + split + "client";
  public static final String CAT_PLAYER = CAT_MAIN + split + "player";
  public static final String CAT_PLAYER_HEALTH = CAT_PLAYER + split + "health";
  public static final String CAT_PLAYER_REGEN = CAT_PLAYER + split + "regen";
  public static final String CAT_MOB = CAT_MAIN + split + "mob";
  public static final String CAT_MOB_HEALTH = CAT_MOB + split + "health";
  public static final String CAT_MOB_BLIGHT = CAT_MOB + split + "blights";
  public static final String CAT_ITEMS = CAT_MAIN + split + "items";
  public static final String CAT_DIFFICULTY = CAT_MAIN + split + "difficulty";
  public static final String CAT_NETWORK = CAT_MAIN + split + "network";
  public static final String CAT_WORLD = CAT_MAIN + split + "world";

  public static final ConfigScalingHealth INSTANCE = new ConfigScalingHealth();
  
  public ConfigScalingHealth() {

    super(ScalingHealth.MOD_ID_LOWER, true, ScalingHealth.BUILD_NUM);
  }

  @Override
  public void init(File file) {

    // Re-route to different location.
    String path = file.getPath().replaceFirst("\\.cfg$", "/main.cfg"); 
    super.init(new File(path));
  }

  @Override
  public void load() {

    try {
      //@formatter:off

      DEBUG_MODE = loadBoolean("Debug Mode", CAT_MAIN,
          DEBUG_MODE,
          "Draws random stuffs on the screen! And maybe does some other things.");

      // Client
      RENDER_DIFFICULTY_METER = loadBoolean("Render Difficulty Meter", CAT_CLIENT,
          RENDER_DIFFICULTY_METER,
          "Show the difficulty meter. Usually, it is displayed for a few seconds occasionally.");
      RENDER_DIFFICULTY_METER_ALWAYS = loadBoolean("Render Difficulty Meter Always", CAT_CLIENT,
          RENDER_DIFFICULTY_METER_ALWAYS,
          "Render the difficulty meter at all times.");
      DIFFICULTY_METER_DISPLAY_TIME = loadInt("Difficulty Meter Display Time", CAT_CLIENT,
          DIFFICULTY_METER_DISPLAY_TIME, 0, Integer.MAX_VALUE,
          "The time (in ticks) to show the difficulty meter whenever it pops up.");
      DIFFICULTY_METER_POS_X = loadInt("Position X", CAT_CLIENT,
          DIFFICULTY_METER_POS_X, Integer.MIN_VALUE, Integer.MAX_VALUE,
          "Sets position of the difficulty meter. Negative numbers anchor it to the right side of the screen.");
      DIFFICULTY_METER_POS_Y = loadInt("Position Y", CAT_CLIENT,
          DIFFICULTY_METER_POS_Y, Integer.MIN_VALUE, Integer.MAX_VALUE,
          "Sets position of the difficulty meter. Negative numbers anchor it to the bottom of the screen.");
      CHANGE_HEART_RENDERING = loadBoolean("Custom Heart Rendering", CAT_CLIENT,
          CHANGE_HEART_RENDERING,
          "Replace vanilla heart rendering.");
      loadHeartColors(config);

      // Players
      // Health
      ALLOW_PLAYER_MODIFIED_HEALTH = loadBoolean("Allow Modified Health", CAT_PLAYER_HEALTH,
          ALLOW_PLAYER_MODIFIED_HEALTH,
          "Allow Scaling Health to modify the player's health. Set to false if you have another mod that"
          + " modifies player health, and you would like that mod to handle health modifications instead."
          + " With this option set to false, heart containers will not work, nor will the '/scalinghealth"
          + " health' command.");
      PLAYER_STARTING_HEALTH = loadInt("Starting Health", CAT_PLAYER_HEALTH,
          PLAYER_STARTING_HEALTH, 2, Integer.MAX_VALUE,
          "The amount of health (in half hearts) the player starts with.");
      PLAYER_HEALTH_MAX = loadInt("Max Health", CAT_PLAYER_HEALTH,
          PLAYER_HEALTH_MAX, 0, Integer.MAX_VALUE,
          "The maximum amount of health (in half hearts) the player can reach. Zero means unlimited.");
      PLAYER_HEALTH_LOST_ON_DEATH = (int) loadFloat("Health Lost on Death", CAT_PLAYER_HEALTH,
          PLAYER_HEALTH_LOST_ON_DEATH, Integer.MIN_VALUE, Integer.MAX_VALUE,
          "The amount of health (in half hearts) the player will lose each time they die.");
      // Regen
      ENABLE_BONUS_HEALTH_REGEN = loadBoolean("Enable Bonus Regen", CAT_PLAYER_REGEN,
          ENABLE_BONUS_HEALTH_REGEN,
          "Bonus health regen will be enabled. Vanilla regen is not changed in any way, this just adds extra healing!");
      BONUS_HEALTH_REGEN_MIN_FOOD = loadInt("Food Min", CAT_PLAYER_REGEN,
          BONUS_HEALTH_REGEN_MIN_FOOD, 0, 20,
          "The minimum food level at which bonus regen will be active.");
      BONUS_HEALTH_REGEN_MAX_FOOD = loadInt("Food Max", CAT_PLAYER_REGEN,
          BONUS_HEALTH_REGEN_MAX_FOOD, 0, 20,
          "The maximum food level at which bonus regen will be active.");
      BONUS_HEALTH_REGEN_INITIAL_DELAY = loadInt("Delay (Initial)", CAT_PLAYER_REGEN,
          BONUS_HEALTH_REGEN_INITIAL_DELAY, 0, Integer.MAX_VALUE,
          "The number of ticks after being hurt before bonus regen activates.");
      BONUS_HEALTH_REGEN_DELAY = loadInt("Delay", CAT_PLAYER_REGEN,
          BONUS_HEALTH_REGEN_DELAY, 0, Integer.MAX_VALUE,
          "The number of ticks between each bonus regen tick (a half heart being healed).");

      // Mobs
      DIFFICULTY_DAMAGE_MULTIPLIER = loadFloat("Damage Modifier", CAT_MOB,
          DIFFICULTY_DAMAGE_MULTIPLIER, 0f, Float.MAX_VALUE,
          "A multiplier for extra attack strength all mobs will receive. Set to 0 to disable extra attack strength.");
      // Health
      ALLOW_PEACEFUL_EXTRA_HEALTH = loadBoolean("Allow Peaceful Extra Health", CAT_MOB_HEALTH,
          ALLOW_PEACEFUL_EXTRA_HEALTH,
          "Allow peaceful mobs (such as animals) to spawn with extra health based on difficulty.");
      ALLOW_HOSTILE_EXTRA_HEALTH = loadBoolean("Allow Hostile Extra Health", CAT_MOB_HEALTH,
          ALLOW_HOSTILE_EXTRA_HEALTH,
          "Allow hostile mobs (monsters) to spawn with extra health based on difficulty.");
      DIFFICULTY_GENERIC_HEALTH_MULTIPLIER = loadFloat("Base Health Modifier", CAT_MOB_HEALTH,
          DIFFICULTY_GENERIC_HEALTH_MULTIPLIER, 0f, Float.MAX_VALUE,
          "The minimum extra health a mob will have per point of difficulty. For example, at difficulty 30, "
              + "a mob that normally has 20 health would have at least 50 health.");
      DIFFICULTY_PEACEFUL_HEALTH_MULTIPLIER = loadFloat("Base Health Modifier Peaceful", CAT_MOB_HEALTH,
          DIFFICULTY_PEACEFUL_HEALTH_MULTIPLIER, 0f, Float.MAX_VALUE,
          "The minimum extra health a peaceful will have per point of difficulty. Same as "
              + "\"Base Health Modifier\", but for peaceful mobs!");
      MOB_HEALTH_SCALING_MODE = EnumHealthModMode.loadFromConfig(config, MOB_HEALTH_SCALING_MODE);
      MOB_HEALTH_BLACKLIST = Arrays.asList(config.getStringList("Blacklist", CAT_MOB_HEALTH,
          MOB_HEALTH_BLACKLIST_DEFAULTS,
          "Mobs listed here will never receive extra health, and will not become blights. There is"
          + " also a separate blacklist for blights, if you still want the mob in question to have"
          + " extra health."));
      if (MOB_HEALTH_BLACKLIST == null)
        MOB_HEALTH_BLACKLIST = Lists.newArrayList(MOB_HEALTH_BLACKLIST_DEFAULTS);
      // Blights
      BLIGHT_CHANCE_MULTIPLIER = loadFloat("Blight Chance Multiplier", CAT_MOB_BLIGHT,
          BLIGHT_CHANCE_MULTIPLIER, 0f, Float.MAX_VALUE,
          "Determines the chance of a mob spawning as a blight. Formula is "
              + "blightChanceMulti * currentDifficulty / maxDifficulty");
      BLIGHT_AMP_SPEED = loadInt("Amplifier Speed", CAT_MOB_BLIGHT,
          BLIGHT_AMP_SPEED, 0, 99,
          "The amplifier level on the speed potion effect applied to blights.");
      BLIGHT_AMP_STRENGTH = loadInt("Amplifier Strength", CAT_MOB_BLIGHT,
          BLIGHT_AMP_STRENGTH, 0, 99,
          "The amplifier level on the strength potion effect applied to blights.");
      BLIGHT_FIRE_RIDES_BLIGHT = loadBoolean("Fire Rides Blights", CAT_MOB_BLIGHT,
          BLIGHT_FIRE_RIDES_BLIGHT,
          "Blight's fire will be set to ride the blight. This will make the fire follow the blight"
          + " more smoothly and prevent it from bobbing. Disable if you are having issues.");
      BLIGHT_INVISIBLE = loadBoolean("Invisibility", CAT_MOB_BLIGHT,
          BLIGHT_INVISIBLE,
          "Should blights have the invisibility potion effect?");
      BLIGHT_FIRE_RESIST = loadBoolean("Fire Resist", CAT_MOB_BLIGHT,
          BLIGHT_FIRE_RESIST,
          "Should blights have the fire resistance potion effect?");
      BLIGHT_XP_MULTIPLIER = loadFloat("XP Multiplier", CAT_MOB_BLIGHT,
          BLIGHT_XP_MULTIPLIER, 0f, 1000.0f,
          "The multiplier applied to the amount of XP dropped when a blight is killed.");
      BLIGHT_SUPERCHARGE_CREEPERS = loadBoolean("Supercharge Creepers", CAT_MOB_BLIGHT,
          BLIGHT_SUPERCHARGE_CREEPERS,
          "Blight creepers will also be supercharged (like when they are struck by lightning).");
      BLIGHT_BLACKLIST = Arrays.asList(config.getStringList("Blacklist", CAT_MOB_BLIGHT,
          BLIGHT_BLACKLIST_DEFAULTS,
          "Mobs listed here will never become blights, but can still receive extra health. There is"
          + " also a blacklist for extra health."));

      // Items
      HEART_DROP_CHANCE_HOSTILE = loadFloat("Heart Drop Chance", CAT_ITEMS,
          HEART_DROP_CHANCE_HOSTILE, 0f, 1f,
          "The chance of a hostile mob dropping a heart canister when killed.");
      HEART_DROP_CHANCE_PASSIVE = loadFloat("Heart Drop Chance (Passive)", CAT_ITEMS,
          HEART_DROP_CHANCE_PASSIVE, 0f, 1f,
          "The chance of a passive mob (animals) dropping a heart canister when killed.");

      HEARTS_DROPPED_BY_BOSS_MIN = loadInt("Hearts Dropped by Boss Min", CAT_ITEMS,
          HEARTS_DROPPED_BY_BOSS_MIN, 0, 64,
          "The minimum number of heart canisters that a boss will drop when killed.");
      HEARTS_DROPPED_BY_BOSS_MAX = loadInt("Hearts Dropped by Boss Max", CAT_ITEMS,
          HEARTS_DROPPED_BY_BOSS_MAX, 0, 64,
          "The maximum number of heart canisters that a boss will drop when killed.");
      if (HEARTS_DROPPED_BY_BOSS_MAX < HEARTS_DROPPED_BY_BOSS_MIN)
        HEARTS_DROPPED_BY_BOSS_MAX = HEARTS_DROPPED_BY_BOSS_MIN;

      HEARTS_DROPPED_BY_BLIGHT_MIN = loadInt("Hearts Dropped by Blight Min", CAT_ITEMS,
          HEARTS_DROPPED_BY_BLIGHT_MIN, 0, 64,
          "The minimum number of heart canisters that a blight will drop when killed.");
      HEARTS_DROPPED_BY_BLIGHT_MAX = loadInt("Hearts Dropped by Blight Max", CAT_ITEMS,
          HEARTS_DROPPED_BY_BLIGHT_MAX, 0, 64,
          "The maximum number of heart canisters that a blight will drop when killed.");
      if (HEARTS_DROPPED_BY_BLIGHT_MAX < HEARTS_DROPPED_BY_BLIGHT_MIN)
        HEARTS_DROPPED_BY_BLIGHT_MAX = HEARTS_DROPPED_BY_BLIGHT_MIN;

      // Difficulty
      DIFFICULTY_MAX = loadFloat("Max Value", CAT_DIFFICULTY,
          DIFFICULTY_MAX, 0f, Float.MAX_VALUE,
          "The maximum difficult level that can be reached. Note that values beyond 250 are not"
          + " tested, and extremely high values may produce strange results.");
      DIFFICULTY_DEFAULT = loadFloat("Starting Value", CAT_DIFFICULTY,
          DIFFICULTY_DEFAULT, 0f, Float.MAX_VALUE,
          "The starting difficulty level for new worlds.");
      DIFFICULTY_PER_SECOND = loadFloat("Increase Per Second", CAT_DIFFICULTY,
          DIFFICULTY_PER_SECOND, -1000f, 1000f,
          "The amount of difficulty added each second. In Difficult Life, the option was named per tick, "
          + "but was actually applied each second. Negative numbers will decrease difficulty over time.");
      DIFFICULTY_PER_BLOCK = loadFloat("Difficulty Per Block", CAT_DIFFICULTY,
          DIFFICULTY_PER_BLOCK, -1000f, 1000f,
          "The amount of difficulty added per unit distance from the origin/spanw, assuming \"Area Mode\" "
          + "is set to a distance-based option. Negative numbers will decrease difficulty over distance.");
      DIFFICULTY_PER_KILL = loadFloat("Difficulty Per Kill", CAT_DIFFICULTY,
          DIFFICULTY_PER_KILL, -1000f, 1000f,
          "The difficulty gained for each hostile mob killed. Set to 0 to disable. Negative numbers"
          + " cause difficulty to decrease with each kill.");
      DIFFICULTY_IDLE_MULTI = loadFloat("Idle Multiplier", CAT_DIFFICULTY,
          DIFFICULTY_IDLE_MULTI, 0f, 100f,
          "Difficulty added per second is multiplied by this if the player is not moving.");
      DIFFICULTY_LOST_ON_DEATH = loadFloat("Lost On Death", CAT_DIFFICULTY,
          DIFFICULTY_LOST_ON_DEATH, -1000f, 1000f,
          "The difficulty a player loses on death. Entering a negative number will cause the player"
          + " to gain difficulty instead!");
      DIFFICULTY_GROUP_AREA_BONUS = loadFloat("Group Area Bonus", CAT_DIFFICULTY,
          DIFFICULTY_GROUP_AREA_BONUS, -10f, 10f,
          "Adds this much extra difficulty per additional player in the area. So, area difficulty will"
          + " be multiplied by: 1 + group_bonus * (players_in_area - 1)");
      DIFFICULTY_SEARCH_RADIUS = loadInt("Search Radius", CAT_DIFFICULTY,
          DIFFICULTY_SEARCH_RADIUS, 0, Short.MAX_VALUE,
          "The distance from a newly spawned mob to search for players to determine its difficulty "
          + "level. Set to 0 for unlimited range.");
      AREA_DIFFICULTY_MODE = EnumAreaDifficultyMode.loadFromConfig(config, AREA_DIFFICULTY_MODE);

      // Network
      PACKET_DELAY = loadInt("Packet Delay", CAT_NETWORK,
          PACKET_DELAY, 1, 1200,
          "The number of ticks between update packets. Smaller numbers mean more packets (and more "
          + "bandwidth and processing power used), but also less client-server desynconfig.");

      // World
      String cat = CAT_WORLD + split + "heart_crystal_ore";
      HEART_CRYSTAL_ORE_VEIN_COUNT = loadFloat("Veins Per Chunk", cat,
          HEART_CRYSTAL_ORE_VEIN_COUNT, 0, 10000,
          "The number of veins per chunk. The fractional part is a probability of an extra vein in each chunk.");
      HEART_CRYSTAL_ORE_VEIN_SIZE = loadInt("Vein Size", cat,
          HEART_CRYSTAL_ORE_VEIN_SIZE, 0, 10000,
          "The size of each vein.");
      HEART_CRYSTAL_ORE_MIN_HEIGHT = loadInt("Min Height", cat,
          HEART_CRYSTAL_ORE_MIN_HEIGHT, 0, 255,
          "The lowest y-level the ore can be found at. Must be less than Max Height");
      HEART_CRYSTAL_ORE_MAX_HEIGHT = loadInt("Max Height", cat,
          HEART_CRYSTAL_ORE_MAX_HEIGHT, 0, 255,
          "The highest y-level the ore can be found at. Must be greater than Min Height");
      if (HEART_CRYSTAL_ORE_MAX_HEIGHT <= HEART_CRYSTAL_ORE_MIN_HEIGHT) {
        HEART_CRYSTAL_ORE_MAX_HEIGHT = 35;
        HEART_CRYSTAL_ORE_MIN_HEIGHT = 10;
      }
      HEART_CRYSTAL_ORE_EXTRA_VEIN_RATE = loadFloat("Extra Vein Rate", cat,
          HEART_CRYSTAL_ORE_EXTRA_VEIN_RATE, 0f, 1f,
          "The number of extra possible veins per chunk away from spawn. The default value will reach the cap at 50,000 blocks from spawn.");
      HEART_CRYSTAL_ORE_EXTRA_VEIN_CAP = loadFloat("Extra Vein Cap", cat,
          HEART_CRYSTAL_ORE_EXTRA_VEIN_CAP, 0f, 1000f,
          "The maximum number of extra veins created by distance from spawn.");
      HEART_CRYSTAL_ORE_QUANTITY_DROPPED = loadInt("Quantity Dropped", cat,
          HEART_CRYSTAL_ORE_QUANTITY_DROPPED, 1, 64,
          "The base number of heart crystal shards dropped by the ore. Fortune III can double this value at most.");

      //@formatter:on
    } catch (Exception ex) {
      ScalingHealth.logHelper.severe("Could not load configuration file!");
      ex.printStackTrace();
    }
  }

  public static List<String> getMobHealthBlacklist() {

    if (MOB_HEALTH_BLACKLIST == null)
      MOB_HEALTH_BLACKLIST = Lists.newArrayList();
    return MOB_HEALTH_BLACKLIST;
  }

  public static List<String> getMobBlightBlacklist() {

    if (BLIGHT_BLACKLIST == null)
      BLIGHT_BLACKLIST = Lists.newArrayList();
    return BLIGHT_BLACKLIST;
  }

  private static void loadHeartColors(Configuration c) {

    // Get hex strings for default colors.
    String[] defaults = new String[HEART_COLORS.length];
    for (int i = 0; i < defaults.length; ++i)
      defaults[i] = String.format("%06x", HEART_COLORS[i]);

    // Load the string list from config.
    String[] list = c.getStringList("Heart Colors", ConfigScalingHealth.CAT_CLIENT, defaults,
        "The colors for each additional row of hearts. The colors will loop back around to the beginning if necessary. Use hexadecimal to specify colors (like HTML color codes).");

    // Convert hex strings to ints.
    try {
      HEART_COLORS = new int[list.length];
      for (int i = 0; i < HEART_COLORS.length; ++i)
        HEART_COLORS[i] = Integer.decode("0x" + list[i]);
    } catch (NumberFormatException ex) {
      ScalingHealth.logHelper.warning(
          "Failed to load heart colors because a value could not be parsed. Make sure all values are valid hexadecimal integers. Try using an online HTML color picker if you are having problems.");
      ex.printStackTrace();
    }
  }
}
