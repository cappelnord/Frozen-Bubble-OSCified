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
import java.awt.event.*;

public class EnterNameScreen extends GameScreen
{
	private Image fullBackground;
	private Image splashBackground;
	private Image panelBackground;
	
	private SoundManager soundManager;
	
	private TextSprite playerName;
	private int lastChar;
	
	public EnterNameScreen(GameApplet gameApplet)
	{
		super(gameApplet);
		
		GameMedia media = gameApplet.getGameMedia();
		
		fullBackground = media.loadImage("background.jpg");
		splashBackground = media.loadImage("splash.jpg");
		panelBackground = media.loadImage("void_panel.jpg");
		
		soundManager = (SoundManager)gameApplet.getGameContext().getObject("soundManager");
		
		GameFont font = (GameFont)gameApplet.getGameContext().getObject("bubbleFont");
		this.addSprite(new TextSprite(new Rectangle(226, 380, 184, 22), font, "ENTER YOUR NAME"));
		
		playerName = new TextSprite(new Rectangle(323, 418, 246, 22), font, new String());
		this.addSprite(playerName);
	}
	
	public void initBackground()
	{
		addToBackground(fullBackground, new Point(0, 0));
		addToBackground(splashBackground, new Point(116, 0));	
		addToBackground(panelBackground, new Point(178, 373));
	}	
	
	private boolean isValidChar(int code)
	{
		if (code>=KeyEvent.VK_0 && code<=KeyEvent.VK_9)
		{
			return true;
		}

		if (code>=KeyEvent.VK_A && code<=KeyEvent.VK_Z)
		{
			return true;
		}

		if (code == KeyEvent.VK_SPACE)
		{
			return true;
		}
		
		return false;
	}
	
	public void play(int[] keyCodes, Point mouseCoord, boolean leftButton, boolean rightButton) 
	{
		if (keyCodes.length == 1)
		{
			if (keyCodes[0] != lastChar)
			{
				lastChar = keyCodes[0];				
				
				if (isValidChar(keyCodes[0]))
				{
					if (playerName.getText().length() < 16)
					{
						// soundManager.playSound(FrozenBubble.SOUND_TYPEWRITER);

						playerName.addCharacter((char)keyCodes[0]);
						
						int moveRight = playerName.getFont().charSize(playerName.getText().charAt(playerName.getText().length()-1));
						moveRight >>= 1;
						
						playerName.relativeMove(new Point(-moveRight-1, 0));
					}
				}
				else if (keyCodes[0] == KeyEvent.VK_BACK_SPACE || 
						 keyCodes[0] == KeyEvent.VK_DELETE)
				{
					
					if (playerName.getText().length() > 0)
					{
						// soundManager.playSound(FrozenBubble.SOUND_TYPEWRITER);
	
						int moveRight = playerName.getFont().charSize(playerName.getText().charAt(playerName.getText().length()-1));
						moveRight >>= 1;						
						
						playerName.removeLastChar();
						
						playerName.relativeMove(new Point(moveRight+1, 0));
					}
				}
				else if (keyCodes[0] == KeyEvent.VK_ESCAPE)
				{
					// soundManager.playSound(FrozenBubble.SOUND_TYPEWRITER);
					
					this.getGameApplet().setCurrentScreen(new SplashScreen(getGameApplet()));
				}
				else if (keyCodes[0] == KeyEvent.VK_ENTER)
				{
					// soundManager.playSound(FrozenBubble.SOUND_TYPEWRITER);
					
					String outputName = playerName.getText().toLowerCase().trim();
					if (outputName.length() > 0)
					{
						((HighscoreManager)this.getGameApplet().getGameContext().getObject("highscoreManager")).sendRecord(outputName);
					}
					
					this.getGameApplet().setCurrentScreen(new SplashScreen(getGameApplet()));
				}
			}
		}
		else if (keyCodes.length == 0)
		{
			lastChar = 0;
		}
	}	
}