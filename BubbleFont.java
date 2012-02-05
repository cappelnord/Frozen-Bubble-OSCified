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

public class BubbleFont extends GameFont
{
	private final char[] BUBBLE_CHARACTERS = {'!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', 
											  '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', 
											  '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', 
											  '?', '@', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 
											  'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
											  's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '|', '{', 
											  '}', '[', ']', '£', '\\', 'µ', '§'};
	
	private final int[] BUBBLE_POSITION = {0, 9, 16, 31, 39, 54, 69, 73, 80, 88, 96, 116, 121, 131, 
										   137, 154, 165, 175, 187, 198, 210, 223, 234, 246, 259, 
										   271, 276, 282, 293, 313, 324, 336, 351, 360, 370, 381, 
										   390, 402, 411, 421, 435, 446, 459, 472, 483, 495, 508, 
										   517, 527, 538, 552, 565, 578, 589, 602, 616, 631, 645, 
										   663, 684, 700, 716, 732, 748, 764, 780, 796};
	
	public BubbleFont(GameApplet applet)
	{
		super(applet.getGameMedia().loadImage("bubbleFont.gif"), applet);
		setCharMap(BUBBLE_CHARACTERS, BUBBLE_POSITION);
	}
}