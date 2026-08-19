package net.yigitguven.chymistry.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.yigitguven.chymistry.Chymistry;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> TWO_HANDED = createTag("two_handed");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Chymistry.MODID, name));
        }
    }
}
