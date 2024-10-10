package model;

import java.util.ArrayList;
import java.util.Collections;

public class Bag {
	private ArrayList<Tile> tiles;
	
	public Bag() {
		tiles = new ArrayList<>(ModelConstants.TILES_TOTAL_COUNT);
		
		for(int i = 0; i < 24; i++) {
			tiles.add(new LandslideTile());
		}
		
		for(int i = 0; i < 9; i++) {
			tiles.add(new MosaicTile("images_2020/mosaic_green.png", ModelConstants.Colors.GREEN));
			tiles.add(new MosaicTile("images_2020/mosaic_red.png", ModelConstants.Colors.RED));
			tiles.add(new MosaicTile("images_2020/mosaic_yellow.png", ModelConstants.Colors.YELLOW));
		}
		
		for(int i = 0; i < 12; i++) {
			tiles.add(new CaryatidTile("images_2020/caryatid.png"));
			tiles.add(new SphinxTile("images_2020/sphinx.png"));
		}

		for(int i = 0; i < 10; i++) {
			tiles.add(new SkeletonBigTopTile());
			tiles.add(new SkeletonBigBottomTile());
		}
		for(int i = 0; i < 5; i++) {
			tiles.add(new SkeletonSmallTopTile());
			tiles.add(new SkeletonSmallBottomTile());
		}
		
		for(int i = 0; i < 5; i++) {
			tiles.add(new AmphoraTile("images_2020/amphora_blue.png", ModelConstants.Colors.BLUE));
			tiles.add(new AmphoraTile("images_2020/amphora_brown.png", ModelConstants.Colors.BROWN));
			tiles.add(new AmphoraTile("images_2020/amphora_red.png", ModelConstants.Colors.RED));
			tiles.add(new AmphoraTile("images_2020/amphora_green.png", ModelConstants.Colors.GREEN));
			tiles.add(new AmphoraTile("images_2020/amphora_yellow.png", ModelConstants.Colors.YELLOW));
			tiles.add(new AmphoraTile("images_2020/amphora_purple.png", ModelConstants.Colors.PURPLE));
		}
		
		Collections.shuffle(tiles);
	}
	
	public Tile drawOneTile(String iconPath) {
		for(Tile t : tiles) {
			if(t.getIconPath().equals(iconPath)) {
				tiles.remove(t);
				return t;
			}
		}
		
		System.err.println("Bag.drawOneTile: Returning null!");
		return null;
	}
	
	public Tile[] drawFourTiles() {
		Tile[] ret = new Tile[4];
		
		for(int i = 0; i < 4; i++) {
			ret[i] = this.tiles.remove(0);
		}
		
		return ret;
	}
	
	public LandslideTile[] drawEightLandslideTiles() {
		ArrayList<LandslideTile> ret = new ArrayList<>();
		int counter = 0;
		
		for(int i = 0; true; i++) {
			if(tiles.get(i).toString().equals(ModelConstants.LANDSLIDE_TILE)) {
				ret.add((LandslideTile)tiles.remove(i));
				i--;
				counter++;
				
				if(counter == 8) {
					break;
				}
			}
		}
		
		Collections.shuffle(tiles);	//	gia na mhn einai oles oi katolis8hseis sto telos
		
		return ret.toArray(new LandslideTile[0]);
	}
}
