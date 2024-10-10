package model;

import java.util.ArrayList;

public class Archaeologist extends Character {
	public Archaeologist(Player owner) {
		super(owner);
	}

	@Override
	protected ArrayList<Integer> functionality() {
		String lua = this.getOwner().getLastUsedArea();
		ArrayList<Integer> allowedAreas = new ArrayList<>();

		for(int i = 0; i < ModelConstants.AREAS.length; i++) {
			if(ModelConstants.AREAS[i].equals(lua) && !lua.equals(ModelConstants.NO_AREA_YET)) {
				allowedAreas.add(0);
			}
			else {
				allowedAreas.add(2);
			}
		}
		
		return allowedAreas;
	}
}
