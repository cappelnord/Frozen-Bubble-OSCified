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

import de.sciss.net.*;
import java.net.*;
import java.util.*;

public class OSCManager {
	
	private OSCClient osc = null;
	
	final static boolean VERBOSE = false;
	
	private int hMoveKey = 0;
	
	private ArrayList<Integer> keyCodes;
		
	OSCManager(String host, int port, int listenPort)
	{
		
		keyCodes = new ArrayList<Integer>();
		

		
		try {
			
			osc = OSCClient.newUsing(OSCClient.UDP, listenPort, host.equals("127.0.0.1") || host.equals("localhost"));
			osc.setTarget(new InetSocketAddress(host,port));
			
			osc.start();
			
			osc.addOSCListener(new OSCListener() {
				
				public void messageReceived( OSCMessage m, SocketAddress addr, long time)
				{

					if(VERBOSE)
					{
						System.out.println("Reveived: " + m.getName());
					}

					if(m.getName().equals("/fire"))
					{
						addKeyCode(FrozenGame.KEY_UP);
					}
					
					if(m.getName().equals("/stay/left"))
					{
						hMoveKey = FrozenGame.KEY_LEFT;
					}
					
					if(m.getName().equals("/stay/right"))
					{
						hMoveKey = FrozenGame.KEY_RIGHT;
					}
					
					if(m.getName().equals("/stay/release"))
					{
						hMoveKey = 0;
					}
					
					if(m.getName().equals("/step/left"))
					{
						addKeyCode(FrozenGame.KEY_LEFT);
					}
					
					if(m.getName().equals("/step/right"))
					{
						addKeyCode(FrozenGame.KEY_RIGHT);
					}
					

					
					
				}
			});
						
		    // osc.dumpOSC( OSCChannel.kDumpBoth, System.err );
			
			System.out.println("Created OSC Connection to " + host + ":" + port);
			System.out.println("Listening on port " + listenPort);
		}
		catch (Exception e)
		{
			System.out.println("Error creating OSC Connection");
		}
				
		try { 
			osc.send(new OSCMessage("/fb/state/init", new Object[] {}));
			
			if(VERBOSE)
			{
				System.out.println("OSC: /fb/state/init");
			}
		}
		catch (Exception e) {}
		
		// Listener
		
	}
	
	public void send (String message, Object[] args)
	{
		
        try { 
			osc.send(new OSCMessage(message, args));			
		}
        catch (Exception e) {}
		
        if(VERBOSE)
        {
        	System.out.print("OSC: " + message);
        	for(int i = 0;i < args.length;i++)
        	{
        		System.out.print(" " + args[i]);
        	}
        	System.out.println(" ");
        }
        
	}
	
	public int[] mergeWithKeyCodes(int[] gameKeyCodes)
	{
		
		if(hMoveKey != 0)
		{
			this.addKeyCode(hMoveKey);
		}
		
		if(keyCodes.size() == 0) // most cases don't need computation
		{
			return gameKeyCodes;
		}
		else
		{	
			int retInt[] = new int[keyCodes.size() + gameKeyCodes.length];
			
			int i = 0;
			
			Iterator<Integer> oscIt = keyCodes.iterator();
			
			while(oscIt.hasNext())
			{
				retInt[i] = oscIt.next().intValue();
				i++;
			}
			
			for(int e = 0; e < gameKeyCodes.length;e++)
			{
				retInt[i] = gameKeyCodes[e];
				i++;
			}
			
			keyCodes.clear();
			return retInt;
		}
	}
	
	private void addKeyCode(int arg)
	{
		keyCodes.add(new Integer(arg));
	}
	
	public OSCClient getClient()
	{
		return osc;
	}

}
