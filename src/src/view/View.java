package view;


import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;


public class View extends JFrame {
	private JLayeredPane panel;
	private JLayeredPane board;
	private JPanel takenTilesArea;
	private JScrollPane takenTilesScrollFrame;
	private JPanel controls;
	private JLabel playingNow;
	
	private Area entrance, mosaics, statues, skeletons, amphoras;
	private JButton assistant, digger, archaeologist, professor, drawTiles, endRun, pause;
	
	private ClassLoader cl;
	
	
	public View(WindowListener windowClosingListener) {
		panel = new JLayeredPane();
		board = new JLayeredPane();
		takenTilesArea = new JPanel();
		takenTilesScrollFrame = new JScrollPane(takenTilesArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		controls = new JPanel();
		drawTiles = new JButton();
		
		cl = this.getClass().getClassLoader();
		
		
		this.setIconImage(new ImageIcon(this.getClass().getClassLoader().getResource("images_2020/tile_back.png")).getImage());
		this.setResizable(false);
		this.setTitle("Code_4234 - Phase A");
		this.setPreferredSize(new Dimension(1250, 800));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.add(panel);
		this.pack();
		this.addWindowListener(windowClosingListener);
		
		panel.setBounds(0, 0, 720, 720);
		
		this.setVisible(true);
	}
	
	
	private void updateTakenTiles(String[] takenTiles) {
		if(takenTiles == null)	return;
		
		int ttwh = ViewConstants.TAKEN_TILE_WIDTH_HEIGHT;
		
		takenTilesArea.removeAll();
		takenTilesArea.setPreferredSize(new Dimension(ttwh, takenTiles.length*(ttwh+5) + 5));
		
		for(int i = 0; i < takenTiles.length; i++) {
			JLabel t = new JLabel();
			t.setPreferredSize(new Dimension(ttwh, ttwh));
			URL url = cl.getResource(takenTiles[i]);
			ImageIcon imageIcon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(ttwh, ttwh, Image.SCALE_SMOOTH));
			t.setIcon(imageIcon);
			takenTilesArea.add(t);
		}
		
		takenTilesArea.setBounds(0, 0, ttwh, this.getHeight()-35);
		takenTilesArea.setAutoscrolls(true);
		takenTilesScrollFrame.setBounds(0, 0, ttwh+30, this.getContentPane().getHeight());
		
		panel.remove(takenTilesScrollFrame);
		panel.add(takenTilesScrollFrame);
	}
	
	public void addToTakenTiles(String takenTile) {
		int ttwh = ViewConstants.TAKEN_TILE_WIDTH_HEIGHT;
		
		JLabel t = new JLabel();
		t.setPreferredSize(new Dimension(ttwh, ttwh));
		URL url = cl.getResource(takenTile);
		ImageIcon imageIcon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(ttwh, ttwh, Image.SCALE_SMOOTH));
		t.setIcon(imageIcon);
		takenTilesArea.add(t);
		
		Dimension d = new Dimension(ttwh, (int)(takenTilesArea.getPreferredSize().getHeight()+ttwh+5));
		takenTilesArea.setPreferredSize(d);
		
		takenTilesArea.setBounds(0, 0, ttwh, this.getHeight()-35);
		takenTilesArea.setAutoscrolls(true);
		takenTilesScrollFrame.setBounds(0, 0, ttwh+30, this.getHeight()-35);
		
		panel.remove(takenTilesScrollFrame);
		panel.add(takenTilesScrollFrame);
	}
	
	
	public void setPlayingNow(String str) {
		if(playingNow == null) return;
		
		playingNow.setText("<html><pre><span style='font-size:13px'>Playing now: <b>" + str + "</b></span></pre></html>");
	}
	
	public void setDrawTilesButtonEnabled(boolean b) { 
		if(drawTiles != null) {
			drawTiles.setEnabled(b);
		}
	}
	public void setEndRunButtonEnabled(boolean b) {
		if(endRun != null) {
			endRun.setEnabled(b);
		}
	}
	
	
	private void initControlsPanel(String pn, boolean[] characterUsage) {
		controls.removeAll();
		
		
		int cw = ViewConstants.CHARACTERS_WIDTH, ch = ViewConstants.CHARACTERS_HEIGHT;
		
		
		URL url;
		Image assistantImage, archaeologistImage, diggerImage, professorImage;
		
		url = cl.getResource("images_2020/assistant.png");
		assistantImage = (new ImageIcon(url)).getImage().getScaledInstance(cw, ch, Image.SCALE_SMOOTH);
		if(characterUsage[0]) {
			assistantImage = GrayFilter.createDisabledImage(assistantImage);
		}

		url = cl.getResource("images_2020/archaeologist.png");
		archaeologistImage = (new ImageIcon(url)).getImage().getScaledInstance(cw, ch, Image.SCALE_SMOOTH);
		if(characterUsage[1]) {
			archaeologistImage = GrayFilter.createDisabledImage(archaeologistImage);
		}

		url = cl.getResource("images_2020/digger.png");
		diggerImage = (new ImageIcon(url)).getImage().getScaledInstance(cw, ch, Image.SCALE_SMOOTH);
		if(characterUsage[2]) {
			diggerImage = GrayFilter.createDisabledImage(diggerImage);
		}

		url = cl.getResource("images_2020/professor.png");
		professorImage = (new ImageIcon(url)).getImage().getScaledInstance(cw, ch, Image.SCALE_SMOOTH);
		if(characterUsage[3]) {
			professorImage = GrayFilter.createDisabledImage(professorImage);
		}
		
		
		
		
		GridBagConstraints c = new GridBagConstraints();
		playingNow = new JLabel();
		setPlayingNow(pn);
		playingNow.setFont(playingNow.getFont().deriveFont(18f));
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		controls.add(playingNow, c);
		
		JLabel useCharacter = new JLabel("<html><pre><span style='font-size:15px'>Use a Character</span></pre></html>");
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weighty = 1.0;
		controls.add(useCharacter, c);
		
		c = new GridBagConstraints();
		assistant = new JButton();
		assistant.setPreferredSize(new Dimension(cw, ch));
		assistant.setIcon(new ImageIcon(assistantImage));
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 2;
		controls.add(assistant, c);
		
		c = new GridBagConstraints();
		archaeologist = new JButton();
		archaeologist.setPreferredSize(new Dimension(cw, ch));
		archaeologist.setIcon(new ImageIcon(archaeologistImage));
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 2;
		controls.add(archaeologist, c);

		c = new GridBagConstraints();
		digger = new JButton();
		digger.setPreferredSize(new Dimension(cw, ch));
		digger.setIcon(new ImageIcon(diggerImage));
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1.0;
		c.weighty = 1.0;
		controls.add(digger, c);

		c = new GridBagConstraints();
		professor = new JButton();
		professor.setPreferredSize(new Dimension(cw, ch));
		professor.setIcon(new ImageIcon(professorImage));
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 1.0;
		c.weighty = 1.0;
		controls.add(professor, c);

		c = new GridBagConstraints();
		drawTiles = new JButton("Draw Tiles");
		drawTiles.setPreferredSize(new Dimension(controls.getWidth(), 30));
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		controls.add(drawTiles, c);

		c = new GridBagConstraints();
		endRun = new JButton("Ånd Run");
		endRun.setPreferredSize(new Dimension(controls.getWidth(), 30));
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		controls.add(endRun, c);
		
		c = new GridBagConstraints();
		pause = new JButton("Pause");
		pause.setPreferredSize(new Dimension(80, 27));
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.weighty = 1.0;
		controls.add(pause, c);
	}
	
	
	

	public void removeMosaicTile(String iconPath) {
		mosaics.removeTile(iconPath);
	}
	public void removeStatueTile(String iconPath) {
		statues.removeTile(iconPath);
	}
	public void removeSkeletonTile(String iconPath) {
		skeletons.removeTile(iconPath);
	}
	public void removeAmphoraTile(String iconPath) {
		amphoras.removeTile(iconPath);
	}
	
	
	
	public void showPlayingScreen(String[] takenTiles, ArrayList<ArrayList<String>> tilesOfAreas, String pn, boolean[] characterUsage, ActionListener[] listeners) {
		panel.removeAll();
		board.removeAll();
		controls.removeAll();
		this.repaint();
		
		this.updateTakenTiles(takenTiles);
		
		this.add(board);
		int h = this.getContentPane().getHeight();
		board.setBounds(140, 0, h, h);
		panel.setBounds(0, 0, this.getWidth(), this.getHeight());
		URL url = cl.getResource("images_2020/background.png");
		
		h = board.getHeight();
		JLabel bi = new JLabel();
		ImageIcon imageIcon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(h, h, Image.SCALE_SMOOTH));
		bi.setIcon(imageIcon);
		bi.setBounds(0, 0, h, h);
		board.add(bi);
		


		entrance = new Area(4, 4);
		entrance.setBounds(270, 330, ViewConstants.AREA_W_H, ViewConstants.AREA_W_H);
		board.add(entrance);
		entrance.repaint();

		mosaics = new Area(4, 4);
		mosaics.setBounds(20, 20, ViewConstants.AREA_W_H, ViewConstants.AREA_W_H);
		board.add(mosaics);
		mosaics.setActionListener(listeners[0]);
		mosaics.repaint();

		statues = new Area(4, 4);
		statues.setBounds(515, 20, ViewConstants.AREA_W_H, ViewConstants.AREA_W_H);
		board.add(statues);
		statues.setActionListener(listeners[1]);
		statues.repaint();

		skeletons = new Area(4, 4);
		skeletons.setBounds(515, 510, ViewConstants.AREA_W_H, ViewConstants.AREA_W_H);
		board.add(skeletons);
		skeletons.setActionListener(listeners[2]);
		skeletons.repaint();

		amphoras = new Area(4, 4);
		amphoras.setBounds(20, 510, ViewConstants.AREA_W_H, ViewConstants.AREA_W_H);
		board.add(amphoras);
		amphoras.setActionListener(listeners[3]);
		amphoras.repaint();
		
		
		board.moveToBack(bi);
		
		
		if(tilesOfAreas != null) {
			entrance.placeTiles(tilesOfAreas.get(0));
			mosaics.placeTiles(tilesOfAreas.get(1));
			statues.placeTiles(tilesOfAreas.get(2));
			skeletons.placeTiles(tilesOfAreas.get(3));
			amphoras.placeTiles(tilesOfAreas.get(4));
		}
		
		
		controls.setBounds(920, 0, 300, this.getHeight()-50);
		controls.setLayout(new GridBagLayout());
		this.initControlsPanel(pn, characterUsage);
		assistant.addActionListener(listeners[4]);
		archaeologist.addActionListener(listeners[5]);
		digger.addActionListener(listeners[6]);
		professor.addActionListener(listeners[7]);
		drawTiles.addActionListener(listeners[8]);
		endRun.addActionListener(listeners[9]);
		pause.addActionListener(listeners[10]);
		
		
		panel.add(controls);
		

		this.setTitle("P l a y i n g !");
	}

	
	/**
	 * Draws the <b>Welcome screen</b> or the <b>Pause screen</b>, depending on its parameters.
	 * 
	 * @param strs Array containing the title of the screen and the first button's text.
	 * @param continueOrResumeEnabled Whether or not first button will be shown as enabled.
	 * @param listeners Array containing the <code>ActionListener</code> Objects for the buttons and the combo box.
	 */
	public void showOptionsScreen(String[] strs, boolean continueOrResumeEnabled, ActionListener[] listeners) {
		panel.removeAll();
		board.removeAll();
		this.repaint();
		JLabel label = new JLabel("<html><pre><span style='font-size:18px'>" + strs[0] + "</span></pre></html>");
		label.setHorizontalAlignment(JLabel.CENTER);
		JButton continueOrResume = new JButton(strs[1]);
		JButton newGame = new JButton("New Game");
		String[] pm = {"Select Playing Mode...", "1-Player", "4-player"};
		JComboBox playingModes = new JComboBox(pm);
		
		int w = 200;
		int x = (this.getWidth() - w) / 2;
		label		.setBounds(  0, 190, this.getWidth(), 30);
		continueOrResume		.setBounds(x, 290, w, 30);
		newGame		.setBounds(x, 360, w, 30);
		
		playingModes.setBounds(x, 400, w, 20);
		playingModes.setSelectedIndex(0);
		
		continueOrResume.addActionListener(listeners[ViewConstants.CONTINUE]);
		newGame.addActionListener(listeners[ViewConstants.NEW_GAME]);
		playingModes.addActionListener(listeners[ViewConstants.PLAYING_MODES]);
		playingModes.setSelectedIndex(0);
		
		
		panel.add(label);
		panel.add(continueOrResume);
		panel.add(newGame);
		panel.add(playingModes);
		
		panel.setBounds(0, 0, this.getWidth(), this.getHeight());
		
		continueOrResume.setEnabled(continueOrResumeEnabled);

		this.setTitle(strs[0]);
	}
	
	
	public void showMessage(String message, String title) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public int confirmAction(String text, String title, int type) {
		return JOptionPane.showConfirmDialog(this, text, title, JOptionPane.YES_NO_OPTION, type);
	}
}
