package me.five.duels.kit;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class Kit {

    private String name;
    private ItemStack offhand;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private Map<Integer, ItemStack> inventorySlots;

    public Kit(Player player) {
        inventorySlots = new HashMap<>();
        if (player.getInventory().getItemInOffHand() != null) offhand = player.getInventory().getItemInOffHand();
        if (player.getInventory().getHelmet() != null) helmet = player.getInventory().getHelmet();
        if (player.getInventory().getChestplate() != null) chestplate = player.getInventory().getChestplate();
        if (player.getInventory().getLeggings() != null) leggings = player.getInventory().getLeggings();
        if (player.getInventory().getBoots() != null) boots = player.getInventory().getBoots();
        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) != null) inventorySlots.put(i, player.getInventory().getItem(i));
        }
    }

    public Kit(ConfigurationSection kitSection) {

        inventorySlots = new HashMap<>();
        if (kitSection.get("offhand") != null) {
            ItemStack item = getItemFromConfig("offhand", kitSection);
            if (item != null) this.offhand = item;
        }
        if (kitSection.get("helmet") != null) {
            ItemStack item = getItemFromConfig("helmet", kitSection);
            if (item != null) this.helmet = item;
        }
        if (kitSection.get("chestplate") != null) {
            ItemStack item = getItemFromConfig("chestplate", kitSection);
            if (item != null) this.chestplate = item;
        }
        if (kitSection.get("leggings") != null) {
            ItemStack item = getItemFromConfig("leggings", kitSection);
            if (item != null) this.leggings = item;
        }
        if (kitSection.get("boots") != null) {
            ItemStack item = getItemFromConfig("boots", kitSection);
            if (item != null) this.boots = item;
        }
        for (int i = 0; i < 36; i++) {
            if (kitSection.get("slot-" + i) != null) {
                ItemStack item = getItemFromConfig("slot-" + i, kitSection);
                if (item != null) inventorySlots.put(i, item);
            }
        }

    }

    public void saveToConfig(ConfigurationSection kitSection) {

        if (offhand != null) saveItemStack("offhand", kitSection, offhand);
        if (helmet != null) saveItemStack("helmet", kitSection, helmet);
        if (chestplate != null) saveItemStack("chestplate", kitSection, chestplate);
        if (leggings != null) saveItemStack("leggings", kitSection, leggings);
        if (boots != null) saveItemStack("boots", kitSection, boots);
        for (Map.Entry<Integer, ItemStack> inventorySlot : inventorySlots.entrySet()) {
            if (inventorySlot.getValue() == null) continue;
            saveItemStack("slot-" + inventorySlot.getKey(), kitSection, inventorySlot.getValue());
        }

    }

    private void saveItemStack(String itemSlot, ConfigurationSection section, ItemStack item) {

        if (item.getType() == null || item.getType().equals(Material.AIR)) return;
        section.set(itemSlot + ".item", item.getType().name());
        if (item.getItemMeta() != null) {
            if (item.getItemMeta().getDisplayName() != null && !item.getItemMeta().getDisplayName().equals("")) section.set(itemSlot + ".displayname", item.getItemMeta().getDisplayName());
            if (item.getItemMeta().getEnchants() != null) {
                for (Map.Entry<Enchantment, Integer> ec : item.getItemMeta().getEnchants().entrySet()) {
                    section.set(itemSlot + ".enchants." + ec.getKey().getName(), ec.getValue());
                }
            }
        }
        section.set(itemSlot + ".count", item.getAmount());

    }

    public ItemStack getItemFromConfig(String itemSlot, ConfigurationSection section) {

        if (section.getString(itemSlot + ".item") == null) return null;
        if (Material.getMaterial(section.getString(itemSlot + ".item")) == null) return null;

        ItemStack itemStack = new ItemStack(Material.getMaterial(section.getString(itemSlot + ".item")));
        ItemMeta meta = itemStack.getItemMeta();
        if (section.getString(itemSlot + ".displayname") != null) {
            meta.setDisplayName(section.getString(itemSlot + ".displayname"));
        }
        ConfigurationSection enchantSection = section.getConfigurationSection(itemSlot + ".enchants");
        if (enchantSection != null) {
            for (String enchantName : enchantSection.getKeys(false)) {
                Enchantment ec = Enchantment.getByName(enchantName);
                if (ec == null) continue;
                meta.addEnchant(ec, enchantSection.getInt(enchantName), true);
            }
        }
        itemStack.setItemMeta(meta);
        if (section.get(itemSlot + ".count") != null) {
            itemStack.setAmount(section.getInt(itemSlot + ".count"));
        }

        return itemStack;

    }

    public void applyToPlayer(Player player) {

        if (offhand != null) player.getInventory().setItemInOffHand(offhand);
        if (helmet != null) player.getInventory().setHelmet(helmet);
        if (chestplate != null) player.getInventory().setChestplate(chestplate);
        if (leggings != null) player.getInventory().setLeggings(leggings);
        if (boots != null) player.getInventory().setBoots(boots);
        for (Map.Entry<Integer, ItemStack> inventorySlot : inventorySlots.entrySet()) {
            if (inventorySlot.getValue() == null || inventorySlot.getValue().getType() == Material.AIR) continue;
            player.getInventory().setItem(inventorySlot.getKey(), inventorySlot.getValue());
        }

    }

    public String getName() {
        return name;
    }

    public Kit setName(String name) {
        this.name = name;
        return this;
    }
}
