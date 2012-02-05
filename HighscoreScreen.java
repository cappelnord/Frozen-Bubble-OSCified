/*
 *                 [[ Frozen Bubble OSCified ]]
 *
 * Copyright (c) 2000-2003 Guillaume Cottenceau.
 * Java sourcecode - Copyright (c) 2003 Glenn Sanson.
 * OSC Modification - Copyright (c) 2008 Patrick Borgeat.
 *
 * This code is distributed under the GNU General Public License 
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2, as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *
 * Artwork:
 *    Alexis Younes <73lab at free.fr>
 *      (everything but the bubbles)
 *    Amaury Amblard-Ladurantie <amaury at linuxfr.org>
 *      (the bubbles)
 *
 * Soundtrack:
 *    Matthias Le Bidan <matthias.le_bidan at caramail.com>
 *      (the three musics and all the sound effects)
 *      (Excluded from OSCified Version)
 *
 * Design & Programming:
 *    Guillaume Cottenceau <guillaume.cottenceau at free.fr>
 *      (design and manage the project, whole Perl sourcecode)
 *
 * Java version:
 *    Glenn Sanson <glenn.sanson at free.fr>
 *      (whole Java sourcecode, including JIGA classes 
 *             http://glenn.sanson.free.fr/jiga/)
 *
 *          [[ http://glenn.sanson.free.fr/fb/ ]]
 *          [[ http://www.frozen-bubble.org/   ]]
 *          
 * OSC Modification:
 *    Patrick Borgeat <patrick@borgeat.de>
 *    
 *    		[[http://www.cappel-nord.de]]
 *    
 *    Using the formidable NetUtil OSC Library by Hanns Holger Rutz
 *    
 *    		[[http://www.sciss.de/netutil/]]
 */


import net.library.jiga.*;

import java.awt.*;

public class HighscoreScreen extends GameScreen
{
	private final static String[] comment = {"read the rules", "no comment", "time to wake up", 
											 "try pong", "too hard?", "tired?", "stop coffee!", 
											 "could do better", "not too bad", "well done", 
											 "hard gamer", "keybord master", "genius", "a.i.", 
											 "did you write this game?", "killer", "serial winner", 
											 "incredible", "unbelivable", "perfect"};
		
	private Image fullBackground;
	private Image splashBackground;
	private Image emptyBoard;
	
	private boolean initKeyboard;
	
	private String levelString;
	private String commentString;
	private String starString;
	
	private OSCManager oscManager;
	
	public HighscoreScreen(GameApplet gameApplet)
	{
		super(gameApplet);
		
		GameMedia media = gameApplet.getGameMedia();
		
		fullBackground = media.loadImage("background.jpg");
		splashBackground = media.loadImage("splash.jpg");
		emptyBoard = media.loadImage("void_panel.jpg");
		
		GameFont font = (GameFont)gameApplet.getGameContext().getObject("bubbleFont");
		
		oscManager = (OSCManager)gameApplet.getGameContext().getObject("oscManager");

				
		ScrollingTextSprite scrollingMessage = new ScrollingTextSprite(new Rectangle(168, 9, 300, 22), font, "| Frozen bubble { offline highscores |", 1);
		this.addSprite(scrollingMessage);
		
		LevelManager levelManager = (LevelManager)gameApplet.getGameContext().getObject("levelManager");
				
		levelString = "Level "+Integer.toString(levelManager.getLevelIndex()-1);
		commentString = comment[(levelManager.getLevelIndex()-2) / 4];
		starString = new String("]£§µ").substring(0, (levelManager.getLevelIndex()-2)%4+1);
		
		this.addSprite(new TextSprite(new Rectangle(190, 100, 280, 22), font, "Last level completed"));
		this.addSprite(new TextSprite(new Rectangle((640-getTextSize(levelString, font))>>1, 125, 280, 22), font, levelString));
		this.addSprite(new TextSprite(new Rectangle(190, 155, 280, 22), font, "Final Comment"));
		this.addSprite(new TextSprite(new Rectangle((640-getTextSize(commentString, font))>>1, 180, 280, 22), font, commentString));
		this.addSprite(new TextSprite(new Rectangle(190, 205, 280, 22), font, "Points"));
		this.addSprite(new TextSprite(new Rectangle((640-getTextSize(starString, font))>>1, 226, 280, 22), font, starString));
		
		initKeyboard = false;
	}
	
	public void initBackground()
	{
		addToBackground(fullBackground, new Point(0, 0));
		addToBackground(splashBackground, new Point(116, 0));	
		addToBackground(emptyBoard, new Point(180, 98));	
	}	
	
	public void play(int[] keyCodes, Point mouseCoord, boolean leftButton, boolean rightButton) 
	{
		
		// merges keyCodes from game API with keyCodes from OSC
		keyCodes = oscManager.mergeWithKeyCodes(keyCodes);
		
		if (keyCodes.length == 0 && !initKeyboard)
		{
			initKeyboard = true;
		}
		
		if (initKeyboard)
		{
			for (int i=0 ; i<keyCodes.length ; i++)
			{
				int current = keyCodes[i];
									
				if (current == FrozenGame.KEY_UP || current == FrozenGame.KEY_SHIFT)
				{
					getGameApplet().setCurrentScreen(new SplashScreen(getGameApplet()));
				}
			}
		}		
	}
	
	public int getTextSize(String text, GameFont font) {
		int size = 0;
		
		for (int i=0 ; i<text.length() ; i++) {
			size += font.charSize(text.charAt(i));
			size += font.SEPARATOR_WIDTH;
		}
		
		return size - font.SEPARATOR_WIDTH;
	}	
}