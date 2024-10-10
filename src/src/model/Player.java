package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Player {
	private Color color;
	private String name;
	private String lastUsedArea;
	private ArrayList<Tile> takenTiles;

	public Assistant Assistant;
	public Archaeologist Archaeologist;
	public Digger Digger;
	public Professor Professor;
	
	int caryatidPoints, sphinxPoints;
	
	
	public Player(Color color) {
		this.color = color;
		this.takenTiles = new ArrayList<>();
		this.lastUsedArea = ModelConstants.AREAS[(new Random()).nextInt(ModelConstants.AREAS.length)];
		
		Assistant = new Assistant(this);
		Archaeologist = new Archaeologist(this);
		Digger = new Digger(this);
		Professor = new Professor(this);
		
		caryatidPoints = ModelConstants.Points.STATUE_FEWEST;
		sphinxPoints = ModelConstants.Points.STATUE_FEWEST;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	/**
	 * @return The value of field <code>lastUsedArea</code>.
	 * <p>
	 * @see #lastUsedArea
	 */
	public String getLastUsedArea() {
		return this.lastUsedArea;
	}
	
	
	public void takeTile(Tile t) {
		takenTiles.add(t);
		
		if(t.toString().equals(ModelConstants.MOSAIC_TILE)) {
			lastUsedArea = ModelConstants.MOSAICS_AREA;
		}
		else if(		t.toString().equals(ModelConstants.SPHINX_TILE) 		||
						t.toString().equals(ModelConstants.CARYATID_TILE) 			)
		{
			lastUsedArea = ModelConstants.STATUES_AREA;
		}
		else if(t.toString().equals(ModelConstants.AMPHORA_TILE)) {
			lastUsedArea = ModelConstants.AMPHORAS_AREA;
		}
		else if(		t.toString().equals(ModelConstants.SKELETON_BIG_TOP_TILE) 		||
						t.toString().equals(ModelConstants.SKELETON_BIG_BOTTOM_TILE) 	||
						t.toString().equals(ModelConstants.SKELETON_SMALL_TOP_TILE) 	||
						t.toString().equals(ModelConstants.SKELETON_SMALL_BOTTOM_TILE)		)
		{
			lastUsedArea = ModelConstants.SKELETONS_AREA;
		}
	}
	
	public void takeTiles(ArrayList<FindingTile> tiles) {
		for(int i = 0; i < tiles.size(); i++) {
			takeTile(tiles.get(i));
		}
	}
	
	public String[] getTakenTiles() {
		String[] ret = new String[takenTiles.size()];
		
		for(int i = 0; i < ret.length; i++) {
			ret[i] = takenTiles.get(i).getIconPath();
		}
		
		return ret;
	}
	
	public int calculatePoints() {
		return	calculateMosaicsPoints()		+
				caryatidPoints + sphinxPoints	+
				calculateSkeletonsPoints()		+
				calculateAmphorasPoints()		;
	}
	
	public void setCaryatidPoints(int caryatidPoints) {
		this.caryatidPoints = caryatidPoints;
	}
	
	public void setSphinxPoints(int sphinxPoints) {
		this.sphinxPoints = sphinxPoints;
	}
	
	private int calculateMosaicsPoints() {
		int points = 0;
		int greenCounter = 0;
		int redCounter = 0;
		int yellowCounter = 0;
		
		for(int i = 0; i < takenTiles.size(); i++) {
			if(takenTiles.get(i).toString().equals(ModelConstants.MOSAIC_TILE)) {
				MosaicTile t = (MosaicTile)takenTiles.get(i);
				
				if(t.getColor().equals(ModelConstants.Colors.GREEN)) {
					greenCounter++;
				}
				else if(t.getColor().equals(ModelConstants.Colors.RED)) {
					redCounter++;
				}
				else if(t.getColor().equals(ModelConstants.Colors.YELLOW)) {
					yellowCounter++;
				}
			}
		}
		
		
		points += (greenCounter/4 + redCounter/4 + yellowCounter/4 ) * ModelConstants.Points.MOSAIC_COMPLETE_SAME_COLOR;
		
		greenCounter = greenCounter%4;
		redCounter = redCounter%4;
		yellowCounter = yellowCounter%4;
		
		points += ((greenCounter + redCounter + yellowCounter) / 4) * ModelConstants.Points.MOSAIC_COMPLETE_DIFFERENT_COLORS;
		
		
		return points;
	}
	
	
	public int getCaryatidCount() {
		int count = 0;
		
		for(int i = 0; i < takenTiles.size(); i++) {
			if(takenTiles.get(i).toString().equals(ModelConstants.CARYATID_TILE)) {
				count++;
			}
		}
		
		return count;
	}
	
	public int getSphinxCount() {
		int count = 0;
		
		for(int i = 0; i < takenTiles.size(); i++) {
			if(takenTiles.get(i).toString().equals(ModelConstants.SPHINX_TILE)) {
				count++;
			}
		}
		
		return count;
	}
	
	private int calculateSkeletonsPoints() {
		int points = 0;
		int bigBottomCounter = 0;
		int bigTopCounter = 0;
		int smallBottomCounter = 0;
		int smallTopCounter = 0;
		int bigCounter, smallCounter, parentPairCounter;
		
		for(int i = 0; i < takenTiles.size(); i++) {
			FindingTile t = (FindingTile)takenTiles.get(i);
			
			if(t.toString().equals(ModelConstants.SKELETON_BIG_BOTTOM_TILE)) {
				bigBottomCounter++;
			}
			else if(t.toString().equals(ModelConstants.SKELETON_BIG_TOP_TILE)) {
				bigTopCounter++;
			}
			else if(t.toString().equals(ModelConstants.SKELETON_SMALL_BOTTOM_TILE)) {
				smallBottomCounter++;
			}
			else if(t.toString().equals(ModelConstants.SKELETON_SMALL_TOP_TILE)) {
				smallTopCounter++;
			}
		}
		
		
		bigCounter = Math.min(bigBottomCounter, bigTopCounter);
		smallCounter = Math.min(smallBottomCounter, smallTopCounter);
		parentPairCounter = bigCounter / 2;
		
		if(parentPairCounter >= smallCounter) {
			points += smallCounter * ModelConstants.Points.SKELETON_FAMILY;
			
			bigCounter -= smallCounter*2;
			smallCounter = 0;;
		}
		else if(parentPairCounter < smallCounter) {
			points += parentPairCounter * ModelConstants.Points.SKELETON_FAMILY;
			
			bigCounter -= parentPairCounter*2;
			smallCounter -= parentPairCounter;
		}
		
		points += (bigCounter + smallCounter) * ModelConstants.Points.SKELETON_COMPLETE_NO_FAMILY;
		
		
		return points;
	}
	
	private int calculateAmphorasPoints() {
		int points = 0;
		int blueCounter = 0;
		int brownCounter = 0;
		int redCounter = 0;
		int greenCounter = 0;
		int yellowCounter = 0;
		int purpleCounter = 0;
		int differentColorsCounter = 0;
		
		for(int i = 0; i < takenTiles.size(); i++) {
			if(takenTiles.get(i).toString().equals(ModelConstants.AMPHORA_TILE)) {
				String c = ((AmphoraTile)takenTiles.get(i)).getColor();
	
					 if(c.equals(ModelConstants.Colors.BLUE))		blueCounter++;
				else if(c.equals(ModelConstants.Colors.BROWN))		brownCounter++;
				else if(c.equals(ModelConstants.Colors.RED))		redCounter++;
				else if(c.equals(ModelConstants.Colors.GREEN))		greenCounter++;
				else if(c.equals(ModelConstants.Colors.YELLOW))		yellowCounter++;
				else if(c.equals(ModelConstants.Colors.PURPLE))		purpleCounter++;
			}
		}
		
		
		while(true) {
			if(blueCounter > 0) {
				blueCounter--;
				differentColorsCounter++;
			}
			if(brownCounter > 0) {
				brownCounter--;
				differentColorsCounter++;
			}
			if(redCounter > 0) {
				redCounter--;
				differentColorsCounter++;
			}
			if(greenCounter > 0) {
				greenCounter--;
				differentColorsCounter++;
			}
			if(yellowCounter > 0) {
				yellowCounter--;
				differentColorsCounter++;
			}
			if(purpleCounter > 0) {
				purpleCounter--;
				differentColorsCounter++;
			}
			
			
			if(differentColorsCounter <= 2) {
				break;
			}
			
			
			points += ModelConstants.Points.AMPHORA_DIFFERENT_COLORS_TO_POINTS[differentColorsCounter];
			
			differentColorsCounter = 0;
		}
		
		
		return points;
	}
}
