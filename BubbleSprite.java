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
import java.util.Vector;
import java.util.Random;


public class BubbleSprite extends Sprite
{
	private static double FALL_SPEED = 1.;
	private static double MAX_BUBBLE_SPEED = 8.;
	private static double MINIMUM_DISTANCE = 841.;
	
	private Image bubbleFace;
    private Image bubbleBlindFace;
	private Image frozenFace;
	private Image bubbleBlink;
	private Image[] bubbleFixed;
	private FrozenGame frozen;
	private BubbleManager bubbleManager;
	private double moveX, moveY;
	private double realX, realY;
	
	private boolean fixed;
	private boolean blink;
	private boolean released;

	private boolean checkJump;
	private boolean checkFall;

	private int fixedAnim;
	
	private int id;
	
	private int color;
	
	private SoundManager soundManager;
	
	private OSCManager oscManager;
	
	public BubbleSprite(int id, Rectangle area, int color, int direction, Image bubbleFace, Image bubbleBlindFace, Image frozenFace, Image[] bubbleFixed, Image bubbleBlink, BubbleManager bubbleManager, SoundManager soundManager, OSCManager oscManager, FrozenGame frozen)
	{
		super(area);
		
		this.id = id;
		this.bubbleFace = bubbleFace;
                this.bubbleBlindFace = bubbleBlindFace;
		this.frozenFace = frozenFace;
		this.bubbleFixed = bubbleFixed;
		this.bubbleBlink = bubbleBlink;
		this.bubbleManager = bubbleManager;
		this.soundManager = soundManager;
		this.oscManager = oscManager;
		this.frozen = frozen;
		
		this.color = color;
		
		this.moveX = MAX_BUBBLE_SPEED * -Math.cos(direction * Math.PI / 40.);
		this.moveY = MAX_BUBBLE_SPEED * -Math.sin(direction * Math.PI / 40.);
		this.realX = area.x;
		this.realY = area.y;
		
		fixed = false;
		fixedAnim = -1;
		

		oscManager.send("/fb/bubble/spawn", new Object[] {id, color, this.normPosX(), this.normPosY()});

		
	}
		
	public BubbleSprite(int id, Rectangle area, int color, Image bubbleFace, Image bubbleBlindFace, Image frozenFace, Image bubbleBlink, BubbleManager bubbleManager, SoundManager soundManager, OSCManager oscManager, FrozenGame frozen)
	{
		super(area);
		this.id = id;

		this.bubbleFace = bubbleFace;
                this.bubbleBlindFace = bubbleBlindFace;
		this.frozenFace = frozenFace;
		this.bubbleBlink = bubbleBlink;
		this.bubbleManager = bubbleManager;
		this.soundManager = soundManager;
		this.oscManager = oscManager;

		this.color = color;
		
		this.frozen = frozen;
		
		this.realX = area.x;
		this.realY = area.y;
		
		fixed = true;
		fixedAnim = -1;
		bubbleManager.addBubble(bubbleFace);
		

		oscManager.send("/fb/bubble/spawn", new Object[] {id, color, this.normPosX(), this.normPosY()});

	}
	
	Point currentPosition()
	{
		int posY = (int)Math.floor((realY-28.-frozen.getMoveDown())/28.);
		int posX = (int)Math.floor((realX-174.)/32. + 0.5*(posY%2));
		
		if (posX>7) 
		{
			posX = 7;
		}
		
		if (posX<0) 
		{
			posX = 0;
		}
		
		if (posY<0)
		{
			posY = 0;
		}
		
		return new Point(posX, posY);
	}
	
	public void removeFromManager()
	{
		bubbleManager.removeBubble(bubbleFace);
	}
	
	public boolean fixed()
	{
		return fixed;
	}
	
	public boolean checked()
	{
		return checkFall;
	}
	
	public boolean released()
	{
		return released;
	}
	
	public void moveDown()
	{
		if (fixed)
		{
			realY += 28.;
		}
		
		super.absoluteMove(new Point((int)realX, (int)realY));
	}
	
	public void move()
	{
		realX += moveX;
		
		if (realX>=414.)
		{
			moveX = -moveX;
			realX += (414. - realX);
			// soundManager.playSound(FrozenBubble.SOUND_REBOUND);
			

			oscManager.send("/fb/bubble/rebound", new Object[] {id, 1, this.normPosY()});

			
		}
		else if (realX<=190.)
		{
			moveX = -moveX;
			realX += (190. - realX);
			// soundManager.playSound(FrozenBubble.SOUND_REBOUND);
			

			oscManager.send("/fb/bubble/rebound", new Object[] {id, -1,this.normPosY()});

		}
		
		realY += moveY;
		

		oscManager.send("/fb/bubble/position", new Object[] {id, this.normPosX(), this.normPosY()});

		
		Point currentPosition = currentPosition();
		Vector neighbors = getNeighbors(currentPosition);
		
		if (checkCollision(neighbors) || realY < 44.+frozen.getMoveDown())
		{
			realX = 190.+currentPosition.x*32-(currentPosition.y%2)*16;
			realY = 44.+currentPosition.y*28+frozen.getMoveDown();
			
			fixed = true;
			
			Vector checkJump = new Vector();
			this.checkJump(checkJump, neighbors);
			
			BubbleSprite[][] grid = frozen.getGrid();
			
			if (checkJump.size() >= 3)
			{	
				released = true;
				
				for (int i=0 ; i<checkJump.size() ; i++)
				{
					BubbleSprite current = (BubbleSprite)checkJump.elementAt(i);
					Point currentPoint = current.currentPosition();
					
					frozen.addJumpingBubble(current);
					

					oscManager.send("/fb/bubble/jump", new Object[] {current.getID()});

					
					if (i>0)
					{
						current.removeFromManager();
					}
					grid[currentPoint.x][currentPoint.y] = null;
				}
				
				for (int i=0 ; i<8 ; i++)
				{
					if (grid[i][0] != null)
					{
						grid[i][0].checkFall();
					}
				}
				
				for (int i=0 ; i<8 ; i++)
				{
					for (int j=0 ; j<12 ; j++)
					{
						if (grid[i][j] != null)
						{
							if (!grid[i][j].checked())
							{
								frozen.addFallingBubble(grid[i][j]);
								

								oscManager.send("/fb/bubble/fall", new Object[] {grid[i][j].getID()});

								
								grid[i][j].removeFromManager();
								grid[i][j] = null;
							}
						}
					}
				}
				
				// soundManager.playSound(FrozenBubble.SOUND_DESTROY);
			}
			else
			{
				bubbleManager.addBubble(bubbleFace);
				grid[currentPosition.x][currentPosition.y] = this;
				moveX = 0.;
				moveY = 0.;
				fixedAnim = 0;
				// soundManager.playSound(FrozenBubble.SOUND_STICK);
				

				oscManager.send("/fb/bubble/stick", new Object[] {id});

				
			}
		}
		
		super.absoluteMove(new Point((int)realX, (int)realY));
	}
	
	Vector getNeighbors(Point p)
	{
		BubbleSprite[][] grid = frozen.getGrid();
		
		Vector list = new Vector();
		
		if ((p.y % 2) == 0)
		{
			if (p.x > 0)
			{
				list.addElement(grid[p.x-1][p.y]);
			}
			
			if (p.x < 7)
			{
				list.addElement(grid[p.x+1][p.y]);
				
				if (p.y > 0)
				{
					list.addElement(grid[p.x][p.y-1]);
					list.addElement(grid[p.x+1][p.y-1]);
				}
				
				if (p.y < 12)
				{
					list.addElement(grid[p.x][p.y+1]);
					list.addElement(grid[p.x+1][p.y+1]);
				}
			}
			else
			{
				if (p.y > 0)
				{
					list.addElement(grid[p.x][p.y-1]);
				}
				
				if (p.y < 12)
				{
					list.addElement(grid[p.x][p.y+1]);
				}				
			}
		}
		else
		{
			if (p.x < 7)
			{
				list.addElement(grid[p.x+1][p.y]);
			}
			
			if (p.x > 0)
			{
				list.addElement(grid[p.x-1][p.y]);
				
				if (p.y > 0)
				{
					list.addElement(grid[p.x][p.y-1]);
					list.addElement(grid[p.x-1][p.y-1]);
				}
				
				if (p.y < 12)
				{
					list.addElement(grid[p.x][p.y+1]);
					list.addElement(grid[p.x-1][p.y+1]);
				}
			}
			else
			{
				if (p.y > 0)
				{
					list.addElement(grid[p.x][p.y-1]);
				}
				
				if (p.y < 12)
				{
					list.addElement(grid[p.x][p.y+1]);
				}				
			}			
		}
		
		return list;
	}
	
	void checkJump(Vector jump, Image compare)
	{
		if (checkJump)
		{
			return;
		}
		checkJump = true;
		
		if (this.bubbleFace == compare)
		{
			checkJump(jump, this.getNeighbors(this.currentPosition()));	
		}
	}
	
	void checkJump(Vector jump, Vector neighbors)
	{
		jump.addElement(this);
		
		for (int i=0 ; i<neighbors.size() ; i++)
		{
			BubbleSprite current = (BubbleSprite)neighbors.elementAt(i);
			
			if (current != null)
			{
				current.checkJump(jump, this.bubbleFace);
			}
		}		
	}
		
	public void checkFall()
	{
		if (checkFall)
		{
			return;
		}
		checkFall = true;
		
		Vector v = this.getNeighbors(this.currentPosition());
				
		for (int i=0 ; i<v.size() ; i++)
		{
			BubbleSprite current = (BubbleSprite)v.elementAt(i);
			
			if (current != null)
			{
				current.checkFall();
			}
		}
	}
	
	boolean checkCollision(Vector neighbors)
	{
		for (int i=0 ; i<neighbors.size() ; i++)
		{
			BubbleSprite current = (BubbleSprite)neighbors.elementAt(i);
			
			if (current != null)
			{
				if (checkCollision(current))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	boolean checkCollision(BubbleSprite sprite)
	{
		double value = (sprite.getSpriteArea().x - this.realX) * (sprite.getSpriteArea().x - this.realX)
					   + (sprite.getSpriteArea().y - this.realY) * (sprite.getSpriteArea().y - this.realY);
		
		return (value < MINIMUM_DISTANCE);
	}
	
	public void jump()
	{
		if (fixed)
		{
			moveX = -6. + frozen.getRandom().nextDouble() * 12.;
			moveY = -5. - frozen.getRandom().nextDouble() * 10. ;
			
			fixed = false;
		}
		
		moveY += FALL_SPEED;
		realY += moveY;
		realX += moveX;
		
		super.absoluteMove(new Point((int)realX, (int)realY));
		
		if (realY >= 680.)
		{
			frozen.deleteJumpingBubble(this);
			

			oscManager.send("/fb/bubble/delete", new Object[] {id});

			
		}
	}
	
	public void fall()
	{
		if (fixed)
		{
			moveY = frozen.getRandom().nextDouble()* 5.;
		}
		
		fixed = false;
		
		moveY += FALL_SPEED;
		realY += moveY;
		
		super.absoluteMove(new Point((int)realX, (int)realY));
		
		if (realY >= 680.)
		{
			frozen.deleteFallingBubble(this);
			
			oscManager.send("/fb/bubble/delete",  new Object[] {id});
			
		}
	}
	
	public void blink()
	{
		blink = true;
	}
	
	public void frozenify()
	{
		changeSpriteArea(new Rectangle(getSpritePosition().x-1, getSpritePosition().y-1, 34, 42));
		bubbleFace = frozenFace;
		

		oscManager.send("/fb/bubble/frozenify", new Object[] {id});

		
	}
	
	public final void paint(Graphics g, GameApplet applet)
	{
		checkJump = false;
		checkFall = false;
		
		Point p = getSpritePosition();
		
		if (blink && bubbleFace != frozenFace)
		{
			blink = false;
			g.drawImage(bubbleBlink, p.x, p.y, applet);
		}
		else
		{
                        if (FrozenBubble.getMode() == FrozenBubble.GAME_NORMAL || bubbleFace == frozenFace) {
                            g.drawImage(bubbleFace, p.x, p.y, applet);
                        }
                        else {
                            g.drawImage(bubbleBlindFace, p.x, p.y, applet);
                        }
		}
		
		if (fixedAnim != -1)
		{
			g.drawImage(bubbleFixed[fixedAnim], p.x, p.y, applet);
			fixedAnim++;
			if (fixedAnim == 6)
			{
				fixedAnim = -1;
			}
		}
	}
	
	public int getID()
	{
		return id;
	}
	
	public double normPosX()
	{
		return (realX -190) / 224;
	}
	
	public double normPosY()
	{
		return (realY - 38) / 352;
	}
	
	public int getColor()
	{
		return color;
	}
}