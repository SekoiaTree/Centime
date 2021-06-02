package com.sekoia.centime.raw;

public class RawCuboid {
    public int[] textureOffset;
    public float[] coordinates;
    public float sizeAdd;

    public RawCuboid(int[] textureOffset, float[] coordinates, float sizeAdd) {
        this.textureOffset = textureOffset;
        this.coordinates = coordinates;
        this.sizeAdd = sizeAdd;
    }
}
