package model;

public abstract class Tile {
	private String iconPath;
	
	public Tile(String iconPath) {
		this.iconPath = iconPath;
	}
	
	public String getIconPath() {
		return iconPath;
	}
}
