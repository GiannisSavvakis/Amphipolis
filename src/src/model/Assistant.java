package model;

import java.util.ArrayList;

public class Assistant extends Character {
	public Assistant(Player owner) {
		super(owner);
	}

	@Override
	protected ArrayList<Integer> functionality() {
		ArrayList<Integer> allowedAreas = new ArrayList<>();

		for(int i = 0; i < ModelConstants.AREAS.length; i++) {
			allowedAreas.add(1);
		}
		
		return allowedAreas;
	}
}
