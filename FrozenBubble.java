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

public class FrozenBubble extends GameApplet
{
	/* public final static int SOUND_WON = 0;
	public final static int SOUND_LOST = 1;
	public final static int SOUND_LAUNCH = 2;
	public final static int SOUND_DESTROY = 3;
	public final static int SOUND_REBOUND = 4;
	public final static int SOUND_STICK = 5;
	public final static int SOUND_HURRY = 6;
	public final static int SOUND_NEWROOT = 7;
	public final static int SOUND_NOH = 8;
	public final static int SOUND_TYPEWRITER = 9; */
	
	/* private final static String[] SOUND_FILES = {"applause.au", "lose.au", "launch.au", 
												 "destroy_group.au", "rebound.au", "stick.au", 
												 "hurry.au", "newroot_solo.au", "noh.au",
												 "typewriter.au"}; */
	
        public final static int GAME_NORMAL = 0;
        public final static int GAME_COLORBLIND = 1;
                                                                                                 
        private static int gameMode;
        
        private String host;
        private int port;
        private int listenPort;
        
    public FrozenBubble(String aHost, int aPort, int aListenPort)
    {
    	super();
    	host = aHost;
    	port = aPort;
    	listenPort = aListenPort;
    	    	
    }
                                                                                                 
	public void gameInit()
	{
		LevelManager levelManager = new LevelManager(this.getGameMedia().loadData("levels.txt"));
		this.getGameContext().addObject("levelManager", levelManager);
		
		/* SoundManager soundManager = new SoundManager(this, SOUND_FILES);
		this.getGameContext().addObject("soundManager", soundManager); */
		
		BubbleFont bubbleFont = new BubbleFont(this);
		this.getGameContext().addObject("bubbleFont", bubbleFont);
		
		LifeManager lifeManager = new LifeManager(this);
		this.getGameContext().addObject("lifeManager", lifeManager);
		
		 HighscoreManager highscoreManager = new HighscoreManager(this);
		this.getGameContext().addObject("highscoreManager", highscoreManager); 
		
		OSCManager oscManager = new OSCManager(host, port, listenPort);
		this.getGameContext().addObject("oscManager", oscManager);
		
                
		// Init current mode
                this.setMode(GAME_NORMAL);
                
                // Used to precalc. bubbleLauncher images
		new LaunchBubbleSprite(this, 0, 0);
	}
        
        public static void switchMode() {
            if (gameMode == GAME_COLORBLIND) {
                gameMode = GAME_NORMAL;
            }
            else {
                gameMode = GAME_COLORBLIND;
            }
        }
        
        public static void setMode(int newMode) {
            gameMode = newMode;
        }
        
        public static int getMode() {
            return gameMode;
        }
	
        
	public GameScreen getInitialScreen()
	{
		return new SplashScreen(this);
	}
	
	public boolean needsMouseEvents()
	{
		return false;
	}	

        public static void main(String[] args) {
        	
        	String host = "127.0.0.1";
        	int port = 57120;
        	int listenPort = 57180;
        	
        	for(int i = 0;i < args.length;i++)
        	{
        		if(args[i].equals("-host"))
        		{
        			if(args[i+1] != null) // missing check if it is a reasonable host
        			{
        				host = args[i+1];
        			}
        		}
        		
        		if(args[i].equals("-port"))
        		{
        			if(args[i+1] != null)
        			{
        				port = Integer.parseInt(args[i+1]);
        			}
        		}
        		
        		if(args[i].equals("-listen"))
        		{
        			if(args[i+1] != null)
        			{
        				listenPort = Integer.parseInt(args[i+1]);
        			}
        		}
        		
        	}
        	
            new AppletFrame("Frozen Bubble OSCified v0.9.5-OSC 3", new FrozenBubble(host,port, listenPort), 640, 480);

            

            
        }
}