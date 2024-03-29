package me.Neoblade298.MLMCCustomFoods;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FileManager
{
  File save;
  
  public FileManager()
  {
    this.save = new File(MLMCCustomFoodsMain.getMain().getDataFolder(), "foods.yml");
    if (!this.save.exists()) {
      MLMCCustomFoodsMain.getMain().saveResource("foods.yml", false);
    }
  }
  
  public HashMap<String, Food> loadFoods()
  {
    this.save.mkdirs();
    
    FileConfiguration foodConfig = YamlConfiguration.loadConfiguration(this.save);
    HashMap<String, Food> foods = new HashMap<String, Food>();
    for (String s : foodConfig.getKeys(false)) {
      if (!s.equalsIgnoreCase("valid-worlds"))
      {
        String name = foodConfig.getString(s + ".name").replaceAll("&", "§");
        ArrayList<String> lore = new ArrayList<String>();
        for (String loreLine : foodConfig.getStringList(s + ".lore")) {
          lore.add(loreLine.replaceAll("&", "§"));
        }
        ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
        PotionEffectType type;
        for (String potion : foodConfig.getStringList(s + ".effects"))
        {
          String[] split = potion.split(",");
          type = PotionEffectType.getByName(split[0]);
          int amp = Integer.parseInt(split[1]);
          int duration = Integer.parseInt(split[2]) * 20;
          PotionEffect effect = new PotionEffect(type, duration, amp);
          effects.add(effect);
        }
        Object attribs = new ArrayList<Object>();
        String attribName;
        for (String potion : foodConfig.getStringList(s + ".attributes"))
        {
          String[] split = potion.split(",");
          attribName = split[0];
          int amp = Integer.parseInt(split[1]);
          int duration = Integer.parseInt(split[2]) * 20;
          AttributeEffect attrib = new AttributeEffect(attribName, amp, duration);
          ((ArrayList<AttributeEffect>)attribs).add(attrib);
        }
        Object sounds = new ArrayList<Object>();
        for (String sname : foodConfig.getStringList(s + ".sound-effects"))
        {
          Sound sound = Sound.valueOf(sname.toUpperCase());
          if (sound != null) {
            ((ArrayList<Sound>)sounds).add(sound);
          }
        }
        double sat = foodConfig.getDouble(s + ".saturation");
        int hung = foodConfig.getInt(s + ".hunger");
        Food food = new Food(name, hung, sat, lore, effects, (ArrayList<AttributeEffect>)attribs, (ArrayList<Sound>)sounds);
        food.setHealth(foodConfig.getInt(s + ".health.amount"));
        food.setHealthDelay(foodConfig.getInt(s + ".health.delay"));
        food.setHealthTime(foodConfig.getInt(s + ".health.repetitions"));
        food.setMana(foodConfig.getInt(s + ".mana.amount"));
        food.setManaDelay(foodConfig.getInt(s + ".mana.delay"));
        food.setManaTime(foodConfig.getInt(s + ".mana.repetitions"));
        food.setEatable(foodConfig.getBoolean(s + ".edible-when-not-hungry"));
        food.setCooldown(foodConfig.getInt(s + ".cooldown"));
        food.setCommands(foodConfig.getStringList(s + ".commands"));
        food.setWorlds(foodConfig.getStringList(s + ".worlds"));
        foods.put(name, food);
      }
    }
    return foods;
  }
}
