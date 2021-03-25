package com.thecowking.wrought.data;

import net.minecraft.item.crafting.Ingredient;

public class MetalData {

    protected String id;
    protected int meltingPoint;
    protected Ingredient bloomeryFlux;
    protected Ingredient bloomeryByProduct;


    public MetalData(String id, int meltingPoint, Ingredient bloomeryFlux, Ingredient bloomeryByProduct)  {
        this.id = id;
        this.meltingPoint = meltingPoint;
        this.bloomeryFlux = bloomeryFlux;
        this.bloomeryByProduct = bloomeryByProduct;
    }

    public String getId() {
        return id;
    }

    public int getMeltingPoint() {
        return meltingPoint;
    }

    public Ingredient getBloomeryFlux() {
        return bloomeryFlux;
    }
    public Ingredient getBloomeryByProduct() {
        return bloomeryByProduct;
    }


}
