package com.thecowking.wrought.init;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.recipes.BlastFurnace.BlastFurnaceRecipe;
import com.thecowking.wrought.recipes.BlastFurnace.BlastFurnaceRecipeSerializer;
import com.thecowking.wrought.recipes.HoneyCombCokeOven.HoneyCombCokeOvenRecipe;
import com.thecowking.wrought.recipes.HoneyCombCokeOven.HoneyCombCokeOvenRecipeSerializer;
import com.thecowking.wrought.recipes.IWroughtRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerInit {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Wrought.MODID);

    // Honey Comb
    public static final ResourceLocation HONEY_COMB_RECIPE_TYPE_ID = new ResourceLocation( "honey_comb_coke_oven");
    public static final IRecipeSerializer<HoneyCombCokeOvenRecipe> HONEY_COMB_OVEN_RECIPE_SERIALIZER = new HoneyCombCokeOvenRecipeSerializer();
    public static final IRecipeType<IWroughtRecipe> HONEY_COMB_OVEN_TYPE = registerType(HONEY_COMB_RECIPE_TYPE_ID);
    public static final RegistryObject<IRecipeSerializer<?>> HONEY_COMB_SERIALIZER = RECIPE_SERIALIZERS.register("honey_comb_coke_oven",
            () -> HONEY_COMB_OVEN_RECIPE_SERIALIZER);

    // Blast Furnace
    public static final ResourceLocation BLAST_FURNACE_RECIPE_TYPE_ID = new ResourceLocation( "blast_furnace");
    public static final IRecipeSerializer<BlastFurnaceRecipe> BLAST_FURNCE_RECIPE_SERIALIZER = new BlastFurnaceRecipeSerializer();
    public static final IRecipeType<IWroughtRecipe> BLAST_FURNACE_TYPE = registerType(BLAST_FURNACE_RECIPE_TYPE_ID);
    public static final RegistryObject<IRecipeSerializer<?>> BLAST_FURNACE_SERIALIZER = RECIPE_SERIALIZERS.register("blast_furnace",
            () -> BLAST_FURNCE_RECIPE_SERIALIZER);

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
