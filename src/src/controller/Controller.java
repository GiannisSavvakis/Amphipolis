package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import model.*;
import view.*;


/**
 * The <code>Controller</code> class is the actual brain of this game.<br>
 * Handles all the communication between model and view - contains the <code>main</code> function.
 */
public class Controller {
	private Bag bag;
	private Board board;
	
	/**
	 * The array that will contain four instances of the
	 * {@link model.Player Player} class.
	 */
	private Player[] players;
	
	private Player thief;
	private int playersCount, playersCountMemory;
	
	/**
	 * Index for the <code>players</code> array.
	 * 
	 * @see #players
	 */
	private int currentPlayer;
	
	/**
	 * This is an instance of the <code>View</code> class that will handle all of the graphics.
	 * 
	 * @see View
	 */
	private View view;
	String messagesTitle = "Amphipolis";
	
	private boolean canDrawTiles = true;
	private boolean canEndRun = false;
	
	private boolean notInWelcomeScreen = false;
	
	private String currentArea = ModelConstants.NO_AREA_YET;
	private int numOfTakenTiles = 0;
	
	private int mosaics_allowed, statues_allowed, skeletons_allowed, amphoras_allowed;
	private int numOfTakenMosaics, numOfTakenStatues, numOfTakenSkeletons, numOfTakenAmphoras;
	private boolean zeroOtherAreas;
	private boolean usingCharacter = false;
	
	
	private ActionListener[] homeScreenListeners;
	private ActionListener[] pauseScreenListeners;
	private ActionListener[] playingScreenListeners;
	
	
	
	
	public static void main(String[] args) {
		Controller c = new Controller();
		c.play();
	}
	
	
	
	
	public Controller() {
		homeScreenListeners = new ActionListener[3];
		homeScreenListeners[0] = new ContinueListener();
		homeScreenListeners[1] = new NewGameListener();
		homeScreenListeners[2] = new PlayingModesListener();
		
		pauseScreenListeners = new ActionListener[3];
		pauseScreenListeners[0] = new ResumeListener();
		pauseScreenListeners[1] = new NewGameListener();
		pauseScreenListeners[2] = new PlayingModesListener();
		
		playingScreenListeners = new ActionListener[11];
		playingScreenListeners[0] = new MosaicTileListener();
		playingScreenListeners[1] = new StatueTileListener();
		playingScreenListeners[2] = new SkeletonTileListener();
		playingScreenListeners[3] = new AmphoraTileListener();
		playingScreenListeners[4] = new AssistantListener();
		playingScreenListeners[5] = new ÁrchaeologistListener();
		playingScreenListeners[6] = new DiggerListener();
		playingScreenListeners[7] = new ProfessorListener();
		playingScreenListeners[8] = new DrawTilesListener();
		playingScreenListeners[9] = new EndRunListener();
		playingScreenListeners[10] = new PauseListener();
		
		view = new View(new WindowClosingListener());
		
		
		String[] strs = new String[2];
		strs[0] = "W e l c o m e !";
		strs[1] = "Continue";
		view.showOptionsScreen(strs, isLastGameSaved(), homeScreenListeners);
		

		board = new Board();
	}
	
	public void init() {
		bag = new Bag();
		board = new Board();
		
		players = new Player[4];
		players[0] = new Player(Color.YELLOW);
		players[1] = new Player(Color.RED);
		players[2] = new Player(Color.BLUE);
		players[3] = new Player(Color.BLACK);
		
		
		for(int i = 0; i < 4; i++) {
			players[i].setName("Player " + (i+1));
		}
		
		thief = new Player(null);
		thief.setName("Thief");
		
		canDrawTiles = true;
		canEndRun = false;
	}
	
	public void play() {
		while(true) {
			view.setDrawTilesButtonEnabled(canDrawTiles);
			view.setEndRunButtonEnabled(canEndRun);
			
			if(usingCharacter && view.getTitle().equals("P l a y i n g !")) {
				view.setTitle("Using a Character Card!");
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			if(isGameOver()) {
				deleteSavedProgress();
				
				int winner = findTheWinner();
				String message;
				
				String firstSentence = players[winner].getName() + ", you are the winner!   :D";
				if(players[winner].getName().equals(thief.getName())) {
					firstSentence = "Oh, no!  " + thief.getName() + " is the winner!   :(";
				}
				
				if(playersCount < 4) {
					message =	firstSentence + "          \n\n"
							+	"Statistics:\n"
							+		"          " + players[0].getName() + ":     " + players[0].calculatePoints() + "\n"
							+		"          " + thief.getName() + "     :     " + thief.calculatePoints() + "\n";
				}
				else {
					message =	firstSentence + "          \n\n"
								+	"Statistics:\n"
								+		"          " + players[0].getName() + ":     " + players[0].calculatePoints() + "\n"
								+		"          " + players[1].getName() + ":     " + players[1].calculatePoints() + "\n"
								+		"          " + players[2].getName() + ":     " + players[2].calculatePoints() + "\n"
								+		"          " + players[3].getName() + ":     " + players[3].calculatePoints() + "\n";
				}
				
				view.showMessage(message, "Game Over!");
				
				playersCount = 0;
				notInWelcomeScreen = false;
				
				String[] strs = new String[2];
				strs[0] = "W e l c o m e !";
				strs[1] = "Continue";
				view.showOptionsScreen(strs, isLastGameSaved(), homeScreenListeners);
				
				init();
			}
		}
	}
	
	
	
	
	private int getFirstPlayingPlayer() {
		Random rand = new Random();
		
		return rand.nextInt(playersCount);
	}
	
	
	private boolean isGameOver() {
		return (board.getLandslideTilesCount() == 16);
	}
	
	
	private int findTheWinner() {
		if(playersCount == 1) {
			players[1] = thief;
			playersCount = 2;
		}
		
		int[] c = new int[playersCount];
		int[] s = new int[playersCount];
		
		for(int i = 0; i < playersCount; i++) {
			c[i] = players[i].getCaryatidCount();
			s[i] = players[i].getSphinxCount();
		}
		
		int c_min = players[min(c)].getCaryatidCount();
		int c_max = players[max(c)].getCaryatidCount();
		int s_min = players[min(s)].getSphinxCount();
		int s_max = players[max(s)].getSphinxCount();
		
		for(int i = 0; i < playersCount; i++) {
			if(players[i].getCaryatidCount() == c_min) {
				players[i].setCaryatidPoints(ModelConstants.Points.STATUE_FEWEST);
			}
			else if(players[i].getCaryatidCount() == c_max) {
				players[i].setCaryatidPoints(ModelConstants.Points.STATUE_MORE);
			}
			else {
				players[i].setCaryatidPoints(ModelConstants.Points.STATUE_NOT_MORE_NOR_FEWEST);
			}
			
			if(players[i].getSphinxCount() == s_min) {
				players[i].setSphinxPoints(ModelConstants.Points.STATUE_FEWEST);
			}
			else if(players[i].getSphinxCount() == s_max) {
				players[i].setSphinxPoints(ModelConstants.Points.STATUE_MORE);
			}
			else {
				players[i].setSphinxPoints(ModelConstants.Points.STATUE_NOT_MORE_NOR_FEWEST);
			}
		}
		
		
		int[] points = new int[playersCount];
		
		for(int i = 0; i < points.length; i++) {
			points[i] = players[i].calculatePoints();
		}
		
		
		return max(points);
	}
	
	
	private int min(int[] elements) {
		int min = 0;
		
		for(int i = 0; i < elements.length; i++) {
			if(elements[i] < elements[min]) {
				min = i;
			}
		}
		
		return min;
	}
	
	
	private int max(int[] elements) {
		int max = 0;
		
		for(int i = 0; i < elements.length; i++) {
			if(elements[i] > elements[max]) {
				max = i;
			}
		}
		
		return max;
	}
	
	
	/**
	 * Saves the current's game progress.
	 * <p>
	 * <b>Precondition:</b> None<br>
	 * <b>Postcondition:</b> Progress is correctly saved if no Exception occurs.
	 * <p>
	 * @see #restoreProgress()
	 * @see #deleteSavedProgress()
	 */
	private void saveProgress() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("LastGameSavedProgress.txt"));
			
			out.write(String.valueOf(playersCountMemory) + "\n");
			out.write(String.valueOf(currentPlayer) + "\n");
			out.write(String.valueOf(canDrawTiles) + "\n");
			out.write(String.valueOf(canEndRun) + "\n");
			out.write(String.valueOf(notInWelcomeScreen) + "\n");
			out.write(currentArea + "\n");
			out.write(String.valueOf(numOfTakenTiles) + "\n");
			out.write(String.valueOf(mosaics_allowed) + "\n");
			out.write(String.valueOf(statues_allowed) + "\n");
			out.write(String.valueOf(skeletons_allowed) + "\n");
			out.write(String.valueOf(amphoras_allowed) + "\n");
			out.write(String.valueOf(numOfTakenMosaics) + "\n");
			out.write(String.valueOf(numOfTakenStatues) + "\n");
			out.write(String.valueOf(numOfTakenSkeletons) + "\n");
			out.write(String.valueOf(numOfTakenAmphoras) + "\n");
			out.write(String.valueOf(usingCharacter) + "\n");
			out.write(String.valueOf(zeroOtherAreas) + "\n");
			
			ArrayList<ArrayList<String>> tiles = board.getTiles();
			int num = 0;
			for(ArrayList<String> area : tiles) {
				num += area.size();
			}
			out.write(String.valueOf(num) + "\n");
			for(ArrayList<String> area : tiles) {
				for(String tile : area) {
					out.write(tile + "\n");
				}
			}
			
			
			savePlayerState(out, players[0]);
			
			switch(playersCountMemory) {
			case 1:
				savePlayerState(out, thief);
				break;
			
			case 4:
				for(int i = 1; i < playersCountMemory; i++) {
					savePlayerState(out, players[i]);
				}
				break;
			
			default:
				out.close();
				deleteSavedProgress();
				System.err.println("Controller.saveProgress: Wrong playersCountMemory! - Saving canceled");
			}
			
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			view.dispose();
			System.exit(0);
		}
	}
	
	
	private void savePlayerState(BufferedWriter out, Player p) throws IOException {
		out.write(p.getName() + "\n");
		
		out.write(String.valueOf(p.Assistant.isUsed()) + "\n");
		out.write(String.valueOf(p.Archaeologist.isUsed()) + "\n");
		out.write(String.valueOf(p.Digger.isUsed()) + "\n");
		out.write(String.valueOf(p.Professor.isUsed()) + "\n");
		
		String[] takenTiles = p.getTakenTiles();
		out.write(String.valueOf(takenTiles.length) + "\n");
		for(String t : takenTiles) {
			out.write(t + "\n");
		}
	}
	
	
	private void restorePlayerState(BufferedReader in, Player p) throws Exception {
		p.setName(in.readLine());

		if(in.readLine().equals(String.valueOf(true)))	p.Assistant.use();
		if(in.readLine().equals(String.valueOf(true)))	p.Archaeologist.use();
		if(in.readLine().equals(String.valueOf(true)))	p.Digger.use();
		if(in.readLine().equals(String.valueOf(true)))	p.Professor.use();
		
		int num = Integer.parseInt(in.readLine());
		for(int i = 0; i < num; i++) {
			p.takeTile(bag.drawOneTile(in.readLine()));
		}
	}
	
	
	/**
	 * Restores the saved progress.
	 * <p>
	 * <b>Precondition:</b> Progress must be correctly saved - no error checking is done.<br>
	 * <b>Postcondition:</b> Progress is correctly restored if no Exception occurs.
	 * <p>
	 * @see #saveProgress()
	 */
	private void restoreProgress() {
		if(!isLastGameSaved()) {
			return;
		}
		
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("LastGameSavedProgress.txt"));

			playersCount = Integer.parseInt(in.readLine());
			currentPlayer = Integer.parseInt(in.readLine());
			canDrawTiles = in.readLine().equals(String.valueOf(true));
			canEndRun = in.readLine().equals(String.valueOf(true));
			notInWelcomeScreen = in.readLine().equals(String.valueOf(true));
			currentArea = in.readLine();
			numOfTakenTiles = Integer.parseInt(in.readLine());
			mosaics_allowed = Integer.parseInt(in.readLine());
			statues_allowed = Integer.parseInt(in.readLine());
			skeletons_allowed = Integer.parseInt(in.readLine());
			amphoras_allowed = Integer.parseInt(in.readLine());
			numOfTakenMosaics = Integer.parseInt(in.readLine());
			numOfTakenStatues = Integer.parseInt(in.readLine());
			numOfTakenSkeletons = Integer.parseInt(in.readLine());
			numOfTakenAmphoras = Integer.parseInt(in.readLine());
			usingCharacter = in.readLine().equals(String.valueOf(true));
			zeroOtherAreas = in.readLine().equals(String.valueOf(true));
			
			int num = Integer.parseInt(in.readLine());
			Tile[] t = new Tile[1];
			for(int i = 0; i < num; i++) {
				t[0] = bag.drawOneTile(in.readLine());
				board.placeTiles(t);
			}
			
			
			restorePlayerState(in, players[0]);
			
			switch(playersCount) {
			case 1:
				restorePlayerState(in, thief);
				break;
			
			case 4:
				for(int i = 1; i < playersCount; i++) {
					restorePlayerState(in, players[i]);
				}
				break;
			
			default:
				System.err.println("Controller.restoreProgress: Wrong playersCount!");
			}
			
			
			in.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			view.dispose();
			System.exit(0);
		}
	}
	
	
	private boolean isLastGameSaved() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("LastGameSavedProgress.txt"));
			String firstLine = in.readLine();
			in.close();
			
			return (firstLine != null) && (!firstLine.equals("0"));
			
		}
		catch (IOException e) {
			return false;
		}
	}
	
	
	private void deleteSavedProgress() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("LastGameSavedProgress.txt"));
			
			out.write("");
			
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	public boolean[] characterUsage() {
		boolean[] ret = new boolean[4];

		ret[0] = players[currentPlayer].Assistant.isUsed();
		ret[1] = players[currentPlayer].Archaeologist.isUsed();
		ret[2] = players[currentPlayer].Digger.isUsed();
		ret[3] = players[currentPlayer].Professor.isUsed();
		
		return ret;
	}

	
	
	class VoidListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {}
	}
	
	
	class WindowClosingListener implements WindowListener {
		@Override
		public void windowOpened(WindowEvent e) {}
		@Override
		public void windowIconified(WindowEvent e) {}
		@Override
		public void windowDeiconified(WindowEvent e) {}
		@Override
		public void windowActivated(WindowEvent e) {}
		@Override
		public void windowDeactivated(WindowEvent e) {}
		
		@Override
		public void windowClosing(WindowEvent e) {
			if(isGameOver()) {
				deleteSavedProgress();
				System.exit(0);
			}
			else if(notInWelcomeScreen) {
				int selection = view.confirmAction("If you don't save your current progress, any outdated saved progress will be deleted\n\nWould you like to save your progress?", "Save progress?", JOptionPane.QUESTION_MESSAGE);
				
				if(selection == JOptionPane.YES_OPTION) {
					saveProgress();
					System.exit(0);
				}
				else if(selection == JOptionPane.NO_OPTION) {
					deleteSavedProgress();
					System.exit(0);
				}
			}
			else {
				System.exit(0);
			}
		}
		
		@Override
		public void windowClosed(WindowEvent e) {
			System.exit(0);
		}
	}
	
	
	class ContinueListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			init();
			restoreProgress();
			
			view.showPlayingScreen(players[currentPlayer].getTakenTiles(), board.getTiles(), players[currentPlayer].getName(), characterUsage(), playingScreenListeners);
			view.setPlayingNow(players[currentPlayer].getName());
			
			notInWelcomeScreen = true;
		}
	}
	
	
	class ResumeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			restoreProgress();
			view.showPlayingScreen(players[currentPlayer].getTakenTiles(), board.getTiles(), players[currentPlayer].getName(), characterUsage(), playingScreenListeners);
		}
	}
	
	
	class NewGameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(playersCount == 1 || playersCount == 4) {
				playersCountMemory = playersCount;
				
				int selection = JOptionPane.YES_OPTION;
				
				if(isLastGameSaved()) {
					selection = view.confirmAction("If you start a new game all of your progress will be lost!\n\nAre you sure you want to start new game?", "Attention!", JOptionPane.WARNING_MESSAGE);
				}
				
				if(selection == JOptionPane.YES_OPTION) {
					deleteSavedProgress();
					init();
					
					if(playersCount == 1) {
						try {
							board.placeTiles(bag.drawEightLandslideTiles());
						}
						catch (NotEnoughSpaceForNewTilesException e1) {}
					}
					
					currentPlayer = getFirstPlayingPlayer();
					
					Player p = players[currentPlayer];
					view.showPlayingScreen(p.getTakenTiles(), board.getTiles(), p.getName(), characterUsage(), playingScreenListeners);

					notInWelcomeScreen = true;
				}
			}
			else {
				view.showMessage("You have to select a playing mode.", messagesTitle);
			}
		}
	}

	
	class PlayingModesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox pms = (JComboBox)e.getSource();
			playersCount = 3*pms.getSelectedIndex() - 2;
		}
	}
	
	
	class MosaicTileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(usingCharacter) {
				if(mosaics_allowed == 0) {
					view.showMessage("You can not take tiles from this area!", messagesTitle);
					return;
				}
				if(numOfTakenMosaics == mosaics_allowed) {
					view.showMessage(	"You can not take more than "
										+ mosaics_allowed + " tile" + (mosaics_allowed == 1 ? "" : "s")
										+ " from this area!"
										, messagesTitle
									);
					return;
				}
				
				JButtonWithIconPath t = (JButtonWithIconPath)e.getSource();
				view.removeMosaicTile(t.getIconPath());
				players[currentPlayer].takeTile(board.removeMoasicTile(t.getIconPath()));
				view.addToTakenTiles(t.getIconPath());
				
				numOfTakenMosaics++;
				
				if(zeroOtherAreas == true) {
					statues_allowed = 0;
					skeletons_allowed = 0;
					amphoras_allowed = 0;
				}
			}
			else {
				if(canDrawTiles) {
					view.showMessage("You have to draw tiles first!", messagesTitle);
					return;
				}
				
				if(numOfTakenTiles == 2) {
					view.showMessage("You can not take more than 2 tiles!\n\nIf you want, you can now use a Character!\nOr else, you can just end your run now.", messagesTitle);
				}
				else if(currentArea.equals(ModelConstants.NO_AREA_YET) || currentArea.equals(ModelConstants.MOSAICS_AREA)) {
					JButtonWithIconPath t = (JButtonWithIconPath)e.getSource();
					view.removeMosaicTile(t.getIconPath());
					players[currentPlayer].takeTile(board.removeMoasicTile(t.getIconPath()));
					view.addToTakenTiles(t.getIconPath());
					
					currentArea = ModelConstants.MOSAICS_AREA;
					numOfTakenTiles++;
				}
				else {
					view.showMessage("The tiles you take have to be of the same area!", messagesTitle);
				}
			}
		}
	}
	
	
	class StatueTileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(usingCharacter) {
				if(statues_allowed == 0) {
					view.showMessage("You can not take tiles from this area!", messagesTitle);
					return;
				}
				if(numOfTakenStatues == statues_allowed) {
					view.showMessage(	"You can not take more than "
										+ statues_allowed + " tile" + (statues_allowed == 1 ? "" : "s")
										+ " from this area!"
										, messagesTitle
									);
					return;
				}

				JButtonWithIconPath t = (JButtonWithIconPath)e.getSource();
				view.removeStatueTile(t.getIconPath());
				players[currentPlayer].takeTile(board.removeStatueTile(t.getIconPath()));
				view.addToTakenTiles(t.getIconPath());
				
				numOfTakenStatues++;
				
				if(zeroOtherAreas == true) {
					mosaics_allowed = 0;
					skeletons_allowed = 0;
					amphoras_allowed = 0;
				}
			}
			else {
				if(canDrawTiles) {
					view.showMessage("You have to draw tiles first!", messagesTitle);
					return;
				}
				
				if(numOfTakenTiles == 2) {
					view.showMessage("You can not take more than 2 tiles!\n\nIf you want, you can now use a Character!\nOr else, you can just end your run now.", messagesTitle);
				}
				else if(currentArea.equals(ModelConstants.NO_AREA_YET) || currentArea.equals(ModelConstants.STATUES_AREA)) {
					JButtonWithIconPath t = (JButtonWithIconPath)e.getSource();
					view.removeStatueTile(t.getIconPath());
					players[currentPlayer].takeTile(board.removeStatueTile(t.getIconPath()));
					view.addToTakenTiles(t.getIconPath());
					
					currentArea = ModelConstants.STATUES_AREA;
					numOfTakenTiles++;
				}
				else {
					view.showMessage("The tiles you take have to be of the same area!", messagesTitle);
				}
			}
		}
	}
	
	
	class SkeletonTileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(usingCharacter) {
				if(skeletons_allowed == 0) {
					view.showMessage("You can not take tiles from this area!", messagesTitle);
					return;
				}
				if(numOfTakenSkeletons == skeletons_allowed) {
					view.showMessage(	"You can not take more than "
										+ skeletons_allowed + " tile" + (skeletons_allowed == 1 ? "" : "s")
										+ " from this area!"
										, messagesTitle
									);
					return;
				}
				
				JButtonWithIconPath t = (JButtonWithIconPath)e.getSource();
				view.removeSkeletonTile(t.getIconPath());
				players[currentPlayer].takeTile(board.removeSkeletonTile(t.getIconPath()));
				view.addToTakenTiles(t.getIconPath());
				
				numOfTakenSkeletons++;
				
				if(zeroOtherAreas == true) {
					mosaics_allowed = 0;
					statues_allowed = 0;
					amphoras_allowed = 0;
				}
			}
			else {
				if(canDrawTiles) {
					view.showMessage("You have to draw tiles first!", messagesTitle);
					return;
				}
				
				if(numOfTakenTiles == 2) {
					view.showMessage("You can not take more than 2 tiles!\n\nIf you want, you can now use a Character!\nOr else, you can just end your run now.", messagesTitle);
				}
				else if(currentArea.equals(ModelConstants.NO_AREA_YET) || currentArea.equals(ModelConstants.SKELETONS_AREA)) {
					JButtonWithIconPath t = (JButtonWithIconPath)e.getSource();
					view.removeSkeletonTile(t.getIconPath());
					players[currentPlayer].takeTile(board.removeSkeletonTile(t.getIconPath()));
					view.addToTakenTiles(t.getIconPath());
					
					currentArea = ModelConstants.SKELETONS_AREA;
					numOfTakenTiles++;
				}
				else {
					view.showMessage("The tiles you take have to be of the same area!", messagesTitle);
				}
			}
		}
	}
	
	
	class AmphoraTileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(usingCharacter) {
				if(amphoras_allowed == 0) {
					view.showMessage("You can not take tiles from this area!", messagesTitle);
					return;
				}
				if(numOfTakenAmphoras == amphoras_allowed) {
					view.showMessage(	"You can not take more than "
										+ amphoras_allowed + " tile" + (amphoras_allowed == 1 ? "" : "s")
										+ " from this area!"
										, messagesTitle
									);
					return;
				}
				
				JButtonWithIconPath t = (JButtonWithIconPath)e.getSource();
				view.removeAmphoraTile(t.getIconPath());
				players[currentPlayer].takeTile(board.removeAmphoraTile(t.getIconPath()));
				view.addToTakenTiles(t.getIconPath());
				
				numOfTakenAmphoras++;
				
				if(zeroOtherAreas == true) {
					mosaics_allowed = 0;
					statues_allowed = 0;
					skeletons_allowed = 0;
				}
			}
			else {
				if(canDrawTiles) {
					view.showMessage("You have to draw tiles first!", messagesTitle);
					return;
				}
				
				if(numOfTakenTiles == 2) {
					view.showMessage("You can not take more than 2 tiles!\n\nIf you want, you can now use a Character!\nOr else, you can just end your run now.", messagesTitle);
				}
				else if(currentArea.equals(ModelConstants.NO_AREA_YET) || currentArea.equals(ModelConstants.AMPHORAS_AREA)) {
					JButtonWithIconPath t = (JButtonWithIconPath)e.getSource();
					view.removeAmphoraTile(t.getIconPath());
					players[currentPlayer].takeTile(board.removeAmphoraTile(t.getIconPath()));
					view.addToTakenTiles(t.getIconPath());
					
					currentArea = ModelConstants.AMPHORAS_AREA;
					numOfTakenTiles++;
				}
				else {
					view.showMessage("The tiles you take have to be of the same area!", messagesTitle);
				}
			}
		}
	}
	
	
	class AssistantListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(canDrawTiles) {
				view.showMessage("You have to draw tiles first!", messagesTitle);
				return;
			}
			if(usingCharacter) {
				return;
			}
			
			try {
				ArrayList<Integer> f = players[currentPlayer].Assistant.use();
				
				mosaics_allowed = f.remove(0);
				statues_allowed = f.remove(0);
				skeletons_allowed = f.remove(0);
				amphoras_allowed = f.remove(0);
				
				usingCharacter = true;
				zeroOtherAreas = true;
				
				numOfTakenMosaics = 0;
				numOfTakenStatues = 0;
				numOfTakenSkeletons = 0;
				numOfTakenAmphoras = 0;
				
				view.showPlayingScreen(players[currentPlayer].getTakenTiles(), board.getTiles(), players[currentPlayer].getName(), characterUsage(), playingScreenListeners);
			}
			catch(CharacterIsAlreadyUsedException ciaue) {
				view.showMessage("You can not use your Assistant card again!", messagesTitle);
			}
		}
	}
	
	
	class ÁrchaeologistListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(canDrawTiles) {
				view.showMessage("You have to draw tiles first!", messagesTitle);
				return;
			}
			if(usingCharacter) {
				return;
			}
			
			try {
				ArrayList<Integer> f = players[currentPlayer].Archaeologist.use();
				
				mosaics_allowed = f.remove(0);
				statues_allowed = f.remove(0);
				skeletons_allowed = f.remove(0);
				amphoras_allowed = f.remove(0);
				
				usingCharacter = true;
				zeroOtherAreas = true;
				
				numOfTakenMosaics = 0;
				numOfTakenStatues = 0;
				numOfTakenSkeletons = 0;
				numOfTakenAmphoras = 0;
				
				view.showPlayingScreen(players[currentPlayer].getTakenTiles(), board.getTiles(), players[currentPlayer].getName(), characterUsage(), playingScreenListeners);
			}
			catch(CharacterIsAlreadyUsedException ciaue) {
				view.showMessage("You can not use your Árchaeologist card again!", messagesTitle);
			}
		}
	}
	
	
	class DiggerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(canDrawTiles) {
				view.showMessage("You have to draw tiles first!", messagesTitle);
				return;
			}
			if(usingCharacter) {
				return;
			}
			
			try {
				ArrayList<Integer> f = players[currentPlayer].Digger.use();
				
				mosaics_allowed = f.remove(0);
				statues_allowed = f.remove(0);
				skeletons_allowed = f.remove(0);
				amphoras_allowed = f.remove(0);
				
				usingCharacter = true;
				zeroOtherAreas = players[currentPlayer].getLastUsedArea().equals(ModelConstants.NO_AREA_YET);
				
				numOfTakenMosaics = 0;
				numOfTakenStatues = 0;
				numOfTakenSkeletons = 0;
				numOfTakenAmphoras = 0;
				
				view.showPlayingScreen(players[currentPlayer].getTakenTiles(), board.getTiles(), players[currentPlayer].getName(), characterUsage(), playingScreenListeners);
			}
			catch(CharacterIsAlreadyUsedException ciaue) {
				view.showMessage("You can not use your Digger card again!", messagesTitle);
				return;
			}
		}
	}
	
	
	class ProfessorListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(canDrawTiles) {
				view.showMessage("You have to draw tiles first!", messagesTitle);
				return;
			}
			if(usingCharacter) {
				return;
			}
			
			try {
				ArrayList<Integer> f = players[currentPlayer].Professor.use();
				
				mosaics_allowed = f.remove(0);
				statues_allowed = f.remove(0);
				skeletons_allowed = f.remove(0);
				amphoras_allowed = f.remove(0);
				
				usingCharacter = true;
				zeroOtherAreas = false;
				
				numOfTakenMosaics = 0;
				numOfTakenStatues = 0;
				numOfTakenSkeletons = 0;
				numOfTakenAmphoras = 0;
				
				view.showPlayingScreen(players[currentPlayer].getTakenTiles(), board.getTiles(), players[currentPlayer].getName(), characterUsage(), playingScreenListeners);
				
				view.setDrawTilesButtonEnabled(false);
			}
			catch(CharacterIsAlreadyUsedException ciaue) {
				view.showMessage("You can not use your Professor card again!", messagesTitle);
				return;
			}
		}
	}
	
	
	private boolean landslideTileExist(Tile[] tiles) {
		for(int i = 0; i < tiles.length; i++) {
			if(tiles[i].toString().equals(ModelConstants.LANDSLIDE_TILE)) {
				return true;
			}
		}
		return false;
	}
	
	
	class DrawTilesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Tile[] tiles = bag.drawFourTiles();
			
			try {
				board.placeTiles(tiles);
			} catch (NotEnoughSpaceForNewTilesException nesfnte) {
				if(!isGameOver()) {
					view.showMessage("Some tiles were not placed because there is no space available!", messagesTitle);
				}
			}
			
			if(playersCount == 1 && landslideTileExist(tiles)) {
				thief.takeTiles(board.removeAllFindingTiles());
				
				EndRunListener erl = new EndRunListener();
				erl.actionPerformed(null);
				
				return;
			}
			
			Player p = players[currentPlayer];
			view.showPlayingScreen(p.getTakenTiles(), board.getTiles(), p.getName(), characterUsage(), playingScreenListeners);
			
			canDrawTiles = false;
			canEndRun = true;
		}
	}
	
	
	class EndRunListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			currentPlayer = (currentPlayer + 1) % playersCount;
			currentArea = ModelConstants.NO_AREA_YET;
			numOfTakenTiles = 0;
			
			mosaics_allowed = 0;
			statues_allowed = 0;
			skeletons_allowed = 0;
			amphoras_allowed = 0;
			zeroOtherAreas = false;
			usingCharacter = false;
			canDrawTiles = true;
			canEndRun = false;

			Player p = players[currentPlayer];
			view.showPlayingScreen(p.getTakenTiles(), board.getTiles(), p.getName(), characterUsage(), playingScreenListeners);
		}
	}
	
	
	class PauseListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			saveProgress();
			
			playersCountMemory = playersCount;
			
			String[] strs = new String[2];
			strs[0] = "G a m e   P a u s e d !";
			strs[1] = "Resume";
			view.showOptionsScreen(strs, true, pauseScreenListeners);
		}
	}
}
