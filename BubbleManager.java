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

public class BubbleManager
{
	int bubblesLeft;
	Image[] bubbles;
	int[] countBubbles;
	
	int nextID = 0;
	
	public BubbleManager(Image[] bubbles)
	{	
		this.bubbles = bubbles;
		this.countBubbles = new int[bubbles.length];
		this.bubblesLeft = 0;
	}
	
	public void addBubble(Image bubble)
	{
		countBubbles[findBubble(bubble)]++;
		bubblesLeft++;
	}
	
	public void removeBubble(Image bubble)
	{
		countBubbles[findBubble(bubble)]--;
		bubblesLeft--;
	}
	
	public int countBubbles()
	{
		return bubblesLeft;
	}
	
	public int nextBubbleIndex(Random rand)
	{
		int select = rand.nextInt() % bubbles.length;
		
		if (select < 0)
		{
			select = -select;
		}

		int count = -1;
		int position = -1;
		
		while (count != select)
		{
			position++;
			
			if (position == bubbles.length)
			{
				position = 0;
			}
			
			if (countBubbles[position] != 0)
			{
				count++;
			}
		}
		
		return position;
	}
	
	public Image nextBubble(Random rand)
	{
		return bubbles[nextBubbleIndex(rand)];
	}
	
	private int findBubble(Image bubble)
	{
		for (int i=0 ; i<bubbles.length ; i++)
		{
			if (bubbles[i] == bubble)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public int nextID()
	{
		nextID++;
		return nextID - 1;
	}
}