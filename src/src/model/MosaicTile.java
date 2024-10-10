package model;

public class MosaicTile extends ColoredFindingTile {
	public MosaicTile(String iconPath, String color) {
		super(iconPath, color);
	}
	
	@Override
	public String toString() {
		return ModelConstants.MOSAIC_TILE;
	}
}
