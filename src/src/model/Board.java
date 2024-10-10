package model;

import java.util.ArrayList;

public class Board {
	private ArrayList<LandslideTile> entranceArea;
	private ArrayList<MosaicTile> mosaicsArea;
	private ArrayList<StatueTile> statuesArea;
	private ArrayList<AmphoraTile> amphorasArea;
	private ArrayList<SkeletonTile> skeletonsArea;
	
	private boolean notEnoughSpace;
	private int landslidesMax, mosaicsMax, statuesMax, skeletonsMax, amphorasMax;
	
	public Board() {
		entranceArea = new ArrayList<>();
		mosaicsArea = new ArrayList<>();
		statuesArea = new ArrayList<>();
		amphorasArea = new ArrayList<>();
		skeletonsArea = new ArrayList<>();
		
		notEnoughSpace = false;
		setMax(16, 16, 16, 16, 16);
	}
	
	public void setMax(int landslidesMax, int mosaicsMax, int statuesMax, int skeletonsMax, int amphorasMax) {
		this.landslidesMax = landslidesMax;
		this.mosaicsMax = mosaicsMax;
		this.statuesMax = statuesMax;
		this.skeletonsMax = skeletonsMax;
		this.amphorasMax = amphorasMax;
	}
	
	
	/**
	 * Adds the tiles inside <code>t</code> to their corresponding areas.
	 * 
	 * @param t Array with the <code>Tile</code> Objects to be added.
	 * <p>
	 * @throws NotEnoughSpaceForNewTilesException If there was no available space
	 * for at least one tile.<br>
	 * The tiles that couldn't be placed are just gone without further prospects.
	 */
	public void placeTiles(Tile[] t) throws NotEnoughSpaceForNewTilesException {
		for(int i = 0; i < t.length; i++) {
			if(t[i].toString().equals(ModelConstants.LANDSLIDE_TILE)) {
				if(entranceArea.size() < landslidesMax) {
					entranceArea.add((LandslideTile)t[i]);
				}
			}
			else if(t[i].toString().equals(ModelConstants.MOSAIC_TILE)) {
				if(mosaicsArea.size() == mosaicsMax) {
					notEnoughSpace = true;
				}
				else {
					mosaicsArea.add((MosaicTile)t[i]);
				}
			}
			else if(		t[i].toString().equals(ModelConstants.SPHINX_TILE) 		||
							t[i].toString().equals(ModelConstants.CARYATID_TILE) 		)
			{
				if(statuesArea.size() == statuesMax) {
					notEnoughSpace = true;
				}
				else {
					statuesArea.add((StatueTile)t[i]);
				}
			}
			else if(t[i].toString().equals(ModelConstants.AMPHORA_TILE)) {
				if(amphorasArea.size() == amphorasMax) {
					notEnoughSpace = true;
				}
				else {
					amphorasArea.add((AmphoraTile)t[i]);
				}
			}
			else if(		t[i].toString().equals(ModelConstants.SKELETON_BIG_TOP_TILE) 		||
							t[i].toString().equals(ModelConstants.SKELETON_BIG_BOTTOM_TILE) 	||
							t[i].toString().equals(ModelConstants.SKELETON_SMALL_TOP_TILE) 		||
							t[i].toString().equals(ModelConstants.SKELETON_SMALL_BOTTOM_TILE)		)
			{
				if(skeletonsArea.size() == skeletonsMax) {
					notEnoughSpace = true;
				}
				else {
					skeletonsArea.add((SkeletonTile)t[i]);
				}
			}
		}
		
		if(notEnoughSpace == true) {
			notEnoughSpace = false;
			throw new NotEnoughSpaceForNewTilesException();
		}
	}
	
	
	public MosaicTile removeMoasicTile(String iconPath) {
		for(int i = 0; i < mosaicsArea.size(); i++) {
			if(mosaicsArea.get(i).getIconPath().equals(iconPath)) {
				return mosaicsArea.remove(i);
			}
		}
		
		System.err.println("\treturning null!");
		return null;
	}
	
	public StatueTile removeStatueTile(String iconPath) {
		for(int i = 0; i < statuesArea.size(); i++) {
			if(statuesArea.get(i).getIconPath().equals(iconPath)) {
				return statuesArea.remove(i);
			}
		}
		
		System.err.println("\treturning null!");
		return null;
	}
	
	public SkeletonTile removeSkeletonTile(String iconPath) {
		for(int i = 0; i < skeletonsArea.size(); i++) {
			if(skeletonsArea.get(i).getIconPath().equals(iconPath)) {
				return skeletonsArea.remove(i);
			}
		}
		
		System.err.println("\treturning null!");
		return null;
	}
	
	public AmphoraTile removeAmphoraTile(String iconPath) {
		for(int i = 0; i < amphorasArea.size(); i++) {
			if(amphorasArea.get(i).getIconPath().equals(iconPath)) {
				return amphorasArea.remove(i);
			}
		}
		
		System.err.println("\treturning null!");
		return null;
	}
	
	public ArrayList<FindingTile> removeAllFindingTiles() {
		ArrayList<FindingTile> ret = new ArrayList<>();
		
		while(mosaicsArea.size() > 0) {
			ret.add(mosaicsArea.remove(0));
		}
		while(statuesArea.size() > 0) {
			ret.add(statuesArea.remove(0));
		}
		while(skeletonsArea.size() > 0) {
			ret.add(skeletonsArea.remove(0));
		}
		while(amphorasArea.size() > 0) {
			ret.add(amphorasArea.remove(0));
		}
		
		return ret;
	}
	
	public int getLandslideTilesCount() {
		return entranceArea.size();
	}
	
	public ArrayList<ArrayList<String>> getTiles() {
		ArrayList<ArrayList<String>> ret = new ArrayList<>();

		ret.add(new ArrayList<>());
		for(int i = 0; i < entranceArea.size(); i++) {
			ret.get(0).add(entranceArea.get(i).getIconPath());
		}
		ret.add(new ArrayList<>());
		for(int i = 0; i < mosaicsArea.size(); i++) {
			ret.get(1).add(mosaicsArea.get(i).getIconPath());
		}
		ret.add(new ArrayList<>());
		for(int i = 0; i < statuesArea.size(); i++) {
			ret.get(2).add(statuesArea.get(i).getIconPath());
		}
		ret.add(new ArrayList<>());
		for(int i = 0; i < skeletonsArea.size(); i++) {
			ret.get(3).add(skeletonsArea.get(i).getIconPath());
		}
		ret.add(new ArrayList<>());
		for(int i = 0; i < amphorasArea.size(); i++) {
			ret.get(4).add(amphorasArea.get(i).getIconPath());
		}
		
		return ret;
	}
}
