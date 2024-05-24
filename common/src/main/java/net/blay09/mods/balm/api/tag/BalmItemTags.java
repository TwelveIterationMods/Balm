package net.blay09.mods.balm.api.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

public class BalmItemTags {
    public static final TagKey<Item> COOKING_OIL = TagKey.create(Registries.ITEM, new ResourceLocation("c", "cooking_oil"));
    public static final TagKey<Item> DIAMONDS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "gems/diamonds"));
    public static final TagKey<Item> EGGS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "eggs"));
    public static final TagKey<Item> EMERALDS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "gems/emerald"));
    public static final TagKey<Item> GEMS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "gems"));
    public static final TagKey<Item> GOLD_NUGGETS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "nuggets/gold"));
    public static final TagKey<Item> INGOTS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "ingots"));
    public static final TagKey<Item> IRON_INGOTS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "ingots/iron"));
    public static final TagKey<Item> IRON_NUGGETS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "nuggets/iron"));
    public static final TagKey<Item> NUGGETS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "nuggets"));
    public static final TagKey<Item> ORES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "ores"));
    public static final TagKey<Item> STONES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "stones"));
    public static final TagKey<Item> WOODEN_CHESTS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "chests/wooden"));
    public static final TagKey<Item> WOODEN_RODS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "rods/wooden"));

    // Dye tags
    public static final TagKey<Item> DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes"));
    public static final TagKey<Item> WHITE_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/white"));
    public static final TagKey<Item> ORANGE_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/orange"));
    public static final TagKey<Item> MAGENTA_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/magenta"));
    public static final TagKey<Item> LIGHT_BLUE_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/light_blue"));
    public static final TagKey<Item> YELLOW_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/yellow"));
    public static final TagKey<Item> LIME_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/lime"));
    public static final TagKey<Item> PINK_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/pink"));
    public static final TagKey<Item> GRAY_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/gray"));
    public static final TagKey<Item> LIGHT_GRAY_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/light_gray"));
    public static final TagKey<Item> CYAN_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/cyan"));
    public static final TagKey<Item> PURPLE_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/purple"));
    public static final TagKey<Item> BLUE_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/blue"));
    public static final TagKey<Item> BROWN_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/brown"));
    public static final TagKey<Item> GREEN_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/green"));
    public static final TagKey<Item> RED_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/red"));
    public static final TagKey<Item> BLACK_DYES = TagKey.create(Registries.ITEM, new ResourceLocation("c", "dyes/black"));

    @SuppressWarnings("unchecked")
    public static final TagKey<Item>[] DYE_TAGS = new TagKey[] {
            WHITE_DYES,
            ORANGE_DYES,
            MAGENTA_DYES,
            LIGHT_BLUE_DYES,
            YELLOW_DYES,
            LIME_DYES,
            PINK_DYES,
            GRAY_DYES,
            LIGHT_GRAY_DYES,
            CYAN_DYES,
            PURPLE_DYES,
            BLUE_DYES,
            BROWN_DYES,
            GREEN_DYES,
            RED_DYES,
            BLACK_DYES
    };
}
