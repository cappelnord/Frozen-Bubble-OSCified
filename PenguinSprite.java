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
import java.util.Random;

public class PenguinSprite extends Sprite
{	
	public final static int STATE_TURN_LEFT = 0;
	public final static int STATE_TURN_RIGHT = 1;
	public final static int STATE_FIRE = 2;
	public final static int STATE_VOID = 3;
	public final static int STATE_GAME_WON = 4;
	public final static int STATE_GAME_LOST = 5;
	
	public final static int[][] LOST_SEQUENCE = {{1,0}, {2,8}, {3,9}, {4,10}, {5,11}, {6,12}, {7,13}, {5,14}};
	public final static int[][] WON_SEQUENCE = {{1,0}, {2,7}, {3,6}, {4,15}, {5,16}, {6,17}, {7,18}, {4,19}};
	
	private Image spritesImage;
	
	private int currentPenguin, displayedPenguin;
	
	private int count;
	
	private Random rand;
	
	private int finalState;
	private int nextPosition;
	
	public PenguinSprite(Image sprites, Random rand)
	{
		super(new Rectangle(360, 435, 57, 45));
		
		this.spritesImage = sprites;	
		this.rand = rand;
		
		currentPenguin = 0;
		displayedPenguin = -1;		
		
		finalState = STATE_VOID;
		nextPosition = 0;
	}
		
	public void updateState(int state)
	{
		if (finalState != STATE_VOID)
		{
			count++;
			
			if (count % 6 == 0)
			{
				if (finalState == STATE_GAME_LOST)
				{
					currentPenguin = LOST_SEQUENCE[nextPosition][1];
					nextPosition = LOST_SEQUENCE[nextPosition][0];
				}
				else if (finalState == STATE_GAME_WON)
				{
					currentPenguin = WON_SEQUENCE[nextPosition][1];
					nextPosition = WON_SEQUENCE[nextPosition][0];
				}
			}
		}
		else
		{
			count++;
		
			switch(state)
			{
				case STATE_TURN_LEFT :
					count = 0;
					currentPenguin = 3;
				break;
				case STATE_TURN_RIGHT :
					count = 0;
					currentPenguin = 2;
				break;
				case STATE_FIRE :
					count = 0;
					currentPenguin = 1;
				break;
				case STATE_VOID :
					if (currentPenguin<4 || currentPenguin>7)
					{
						currentPenguin = 0;
					}
				break;
				case STATE_GAME_WON :
				case STATE_GAME_LOST :
					count = 0;
					finalState = state;
					currentPenguin = 0;
					return;
			}

			if (count>100)
			{
				currentPenguin = 7;
			}
			else if (count % 15 == 0 && count>25)
			{
				currentPenguin = (rand.nextInt() % 3)+4;
				if (currentPenguin < 4)
				{
					currentPenguin = 0;
				}
			}
		}
	}
	
	public void paint(Graphics g, GameApplet applet)
	{
			Rectangle r = this.getSpriteArea();
		
			g.setClip(r);
			g.drawImage(spritesImage, 360-(currentPenguin%4)*57, 435-(currentPenguin/4)*45, applet);
			
			displayedPenguin = currentPenguin;
	}	
}