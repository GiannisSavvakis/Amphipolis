package model;

public class AmphoraTile extends ColoredFindingTile {
	public AmphoraTile(String iconPath, String color) {
		super(iconPath, color);
	}
	
	@Override
	public String toString() {
		return ModelConstants.AMPHORA_TILE;
	}
}
