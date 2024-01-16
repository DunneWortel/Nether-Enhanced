package com.dunnewortel;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class DNE implements ModInitializer {

	public static final String MODID = "dne";

	// Items
	public static Item NETHERITE_HORSE_ARMOR;

    public static final Logger LOGGER = LoggerFactory.getLogger("dne");

	@Override
	public void onInitialize() {

		// Create Items
		NETHERITE_HORSE_ARMOR = new HorseArmorItem(15, "netherite");

		// Register Items
		Registry.register(Registries.ITEM, new Identifier(MODID, "netherite_horse_armor"), NETHERITE_HORSE_ARMOR);

		// Add Netherite Horse Armor to loot tables...
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {

			if (!source.isBuiltin()) return;

			switch (id.toString()) {

				// Minecraft's Bastion Treasure
				case "minecraft:chests/bastion_treasure" -> {
					LootPool.Builder poolBuilder = LootPool.builder()
							.with(ItemEntry.builder(NETHERITE_HORSE_ARMOR))
							.rolls(BinomialLootNumberProvider.create(3,0.25f));
					tableBuilder.pool(poolBuilder);
				}

				// Minecraft's Ruined Portal
				case "minecraft:chests/ruined_portal" -> {
					LootPool.Builder poolBuilder = LootPool.builder()
							.with(ItemEntry.builder(NETHERITE_HORSE_ARMOR))
							.rolls(BinomialLootNumberProvider.create(2, 0.1f));
					tableBuilder.pool(poolBuilder);
				}

			}

		});

		LOGGER.info("Dunne's Nether Enhanced Initialized");
	}

	private static class HorseArmorItem extends net.minecraft.item.HorseArmorItem {

		private final String texturePath;

		public HorseArmorItem(int bonus, String materialName) {
			super(bonus, null, new Item.Settings().maxCount(1).fireproof());
			ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> content.add(this));
			texturePath = String.format("textures/entity/horse/armor/horse_armor_%s.png", materialName);
		}

		@Override
		public Identifier getEntityTexture() {
			return new Identifier(DNE.MODID, texturePath);
		}

	}
}