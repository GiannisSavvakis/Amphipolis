package model;

public abstract class ColoredFindingTile extends FindingTile {
	private String color;
	
	public ColoredFindingTile(String iconPath, String color) {
		super(iconPath);
		this.color = color;
	}
	
	public String getColor() {
		return this.color;
	}
}
