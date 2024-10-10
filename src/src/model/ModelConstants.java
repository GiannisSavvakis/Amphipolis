package model;

public class ModelConstants {
	public static final int TILES_TOTAL_COUNT = 135;
	public static final String LANDSLIDE_TILE = "landslide";
	public static final String MOSAIC_TILE = "mosaic";
	public static final String CARYATID_TILE = "caryatid";
	public static final String SPHINX_TILE = "sphinx";
	public static final String AMPHORA_TILE = "amphora";
	public static final String SKELETON_BIG_TOP_TILE = "skeletonBigTop";
	public static final String SKELETON_BIG_BOTTOM_TILE = "skeletonBigBottom";
	public static final String SKELETON_SMALL_TOP_TILE = "skeletonSmallTop";
	public static final String SKELETON_SMALL_BOTTOM_TILE = "skeletonSmallBottom";
	
	
	public static final class Colors {
		public static final String BLUE = "blue";
		public static final String BROWN = "brown";
		public static final String RED = "red";
		public static final String GREEN = "green";
		public static final String YELLOW = "yellow";
		public static final String PURPLE = "purple";
	}
	
	
	public static final String NO_AREA_YET = "noareayet";
	public static final String ENTRANCE_AREA = "entranceArea";
	public static final String MOSAICS_AREA = "mosaicsArea";
	public static final String STATUES_AREA = "statuesArea";
	public static final String SKELETONS_AREA = "skeletonsArea";
	public static final String AMPHORAS_AREA = "amphorasArea";
	public static final String[] AREAS = {
			MOSAICS_AREA, STATUES_AREA, SKELETONS_AREA, AMPHORAS_AREA
	};
	
	
	public static final class Points {
		public static final int MOSAIC_COMPLETE_SAME_COLOR = 4;
		public static final int MOSAIC_COMPLETE_DIFFERENT_COLORS = 2;
		public static final int MOSAIC_INCOMPLETE = 0;
		
		public static final int STATUE_MORE = 6;
		public static final int STATUE_FEWEST = 0;
		public static final int STATUE_NOT_MORE_NOR_FEWEST = 3;
		
		public static final int SKELETON_FAMILY = 6;
		public static final int SKELETON_COMPLETE_NO_FAMILY = 1;
		public static final int SKELETON_INCOMPLETE = 0;
		
		public static final int AMPHORA_DIFFERENT_COLORS_TO_POINTS[] = {0, 0, 0, 1, 2, 4, 6};
	}
}
