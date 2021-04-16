package com.thecowking.wrought.init;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.recipes.IWroughtRecipe;
import com.thecowking.wrought.recipes.WroughtRecipe;
import com.thecowking.wrought.recipes.WroughtSerializer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public class RecipeSerializerInit {




    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Wrought.MODID);

    // Coke Oven
    public static final ResourceLocation HONEY_COMB_OVEN_RECIPE_TYPE_ID  = new ResourceLocation(Wrought.MODID, "honey_comb_coke_oven");
    public static IRecipeSerializer<WroughtRecipe> HONEY_COMB_OVEN_RECIPE_SERIALIZER = new WroughtSerializer(1, 2, 0, 1, false, false, HONEY_COMB_OVEN_RECIPE_TYPE_ID);;
    public static final IRecipeType<IWroughtRecipe> HONEY_COMB_OVEN_TYPE = registerType(HONEY_COMB_OVEN_RECIPE_TYPE_ID);
    public static RegistryObject<IRecipeSerializer<?>> HONEY_COMB_SERIALIZER = RECIPE_SERIALIZERS.register("honey_comb_coke_oven",
            () -> HONEY_COMB_OVEN_RECIPE_SERIALIZER);

    // Blast Furnace
    public static final ResourceLocation BLAST_FURNACE_RECIPE_TYPE_ID = new ResourceLocation(Wrought.MODID, "blast_furnace");
    public static IRecipeSerializer<WroughtRecipe> BLAST_FURNACE_RECIPE_SERIALIZER = new WroughtSerializer(3, 3, 0, 2, true, true, BLAST_FURNACE_RECIPE_TYPE_ID);
    public static final IRecipeType<IWroughtRecipe> BLAST_FURNACE_TYPE = registerType(BLAST_FURNACE_RECIPE_TYPE_ID);
    public static RegistryObject<IRecipeSerializer<?>> BLAST_FURNACE_SERIALIZER = RECIPE_SERIALIZERS.register("blast_furnace",
            () -> BLAST_FURNACE_RECIPE_SERIALIZER);

    public static final ResourceLocation BLOOMERY_RECIPE_TYPE_ID = new ResourceLocation(Wrought.MODID, "bloomery");
    public static IRecipeSerializer<WroughtRecipe> BLOOMERY_RECIPE_SERIALIZER = new WroughtSerializer(2, 2, 0, 0, true, true, BLOOMERY_RECIPE_TYPE_ID);
    public static final IRecipeType<IWroughtRecipe> BLOOMERY_TYPE = registerType(BLOOMERY_RECIPE_TYPE_ID);
    public static RegistryObject<IRecipeSerializer<?>> BLOOMERY_SERIALIZER = RECIPE_SERIALIZERS.register("bloomery",
            () -> BLOOMERY_RECIPE_SERIALIZER);


    public static final ResourceLocation FUEL_RECIPE_TYPE_ID = new ResourceLocation(Wrought.MODID, "fuel");
    public static IRecipeSerializer<WroughtRecipe> FUEL_RECIPE_SERIALIZER = new WroughtSerializer(1, 0, 0, 0, false, true, FUEL_RECIPE_TYPE_ID);
    public static final IRecipeType<IWroughtRecipe> FUEL_TYPE = registerType(FUEL_RECIPE_TYPE_ID);
    public static RegistryObject<IRecipeSerializer<?>> FUEL_SERIALIZER = RECIPE_SERIALIZERS.register("fuel",
            () -> FUEL_RECIPE_SERIALIZER);






    private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
        @Override
        public String toString() {
            return Registry.RECIPE_TYPE.getKey(this).toString();
        }
    }

    private static <T extends IRecipeType> T registerType(ResourceLocation recipeTypeId) {
        return (T) Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
    }

}
