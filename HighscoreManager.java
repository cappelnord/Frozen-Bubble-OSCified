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

import java.net.*;

public class HighscoreManager
{
	private GameApplet applet;
	
	private int levelCompleted;
	private long startTime;
	
	private String stringToSend;
	
	public HighscoreManager(GameApplet applet) {
            this.applet = applet;
		
            stringToSend = new String();
		
            levelCompleted = 0;
	}
	
        public void reset() {
            stringToSend = new String();
        }
        
	public void startLevel() {
            startTime = System.currentTimeMillis();
	}
	
	public void endLevel(int nbBubbles) {
            long levelTime = System.currentTimeMillis() - startTime;
		
            levelCompleted++;
		
            stringToSend += "&lvl"+Integer.toString(levelCompleted);
            stringToSend += "="+Long.toString(levelTime);
            stringToSend += "&bub"+Integer.toString(levelCompleted);
            stringToSend += "="+Integer.toString(nbBubbles);
	}
	
	public void sendRecord(String player) {
            stringToSend = "update.php?player="+convertToHTML(player)+stringToSend;
		
            try {
		applet.getAppletContext().showDocument(new URL(applet.getCodeBase(), stringToSend), "_self");
            }
            catch(Exception e) {
		System.err.println("Unable to send highscore");
            }
	}
	
	private String convertToHTML(String input) {
            String output = new String();
		
            int begin = 0;
            int end = input.indexOf(" ");
		
            while (end != -1) {
		output += input.substring(begin, end);
		output += "%20";
			
		begin = end + 1;
		end = input.indexOf(" ", begin);
            }	
		
            output += input.substring(begin);
		
            return output;
	}
}