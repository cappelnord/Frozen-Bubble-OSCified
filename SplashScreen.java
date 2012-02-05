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

import de.sciss.net.*;

public class SplashScreen extends GameScreen
{
        private final static String[] MODE_DESC = {"normal mode", "colorblind mode"};
        private final static String[] SOUND_DESC = {"sound enabled", "sound disabled"};
        private final int MODE_TIMING = 30;
        
        private TextSprite modeMessage;
        private int modeTiming;
        
	private TextSprite[] flashingMessage;
	
	private Random rand;
	
	private ImageSprite penguinEyes;
	private int eyesClosed;
	
	private Image fullBackground;
	private Image splashBackground;
	
	private OSCManager oscManager;
	
	private int flashDelay;
	
        private boolean modeKeyPressed, soundKeyPressed;
        private boolean gameInitialized;
        
        private GameFont font;
        
	public SplashScreen(GameApplet gameApplet)
	{
		super(gameApplet);
		
		GameMedia media = gameApplet.getGameMedia();
		
		fullBackground = media.loadImage("background.jpg");
		splashBackground = media.loadImage("splash.jpg");
		
		font = (GameFont)gameApplet.getGameContext().getObject("bubbleFont");
		
		oscManager = (OSCManager)gameApplet.getGameContext().getObject("oscManager");

		
		flashingMessage = new TextSprite[3];
		flashingMessage[0] = new TextSprite(new Rectangle(289, 383, 100, 22), font, "PRESS");
		flashingMessage[1] = new TextSprite(new Rectangle(300, 407, 100, 22), font, "FIRE");
		flashingMessage[2] = new TextSprite(new Rectangle(276, 430, 100, 22), font, "TO START");
		for (int i=0 ; i<3 ; i++)
		{
			this.addSprite(flashingMessage[i]);
		}
		
		penguinEyes = new ImageSprite(new Rectangle(370, 280, 19, 9), media.loadImage("close_eyes.gif"));
		
		// SOUND BUBBLES MOD
		
		
		String message = "| FROZEN BUBBLE 1UP V 0.9.5 - OSCified {  DESIGN & PROGRAMMING - Guillaume Cottenceau";
		message += "  }  GRAPHICS - Alexis Younes (AYO73)  [  GRAPHICS (BUBBLES) - Amaury Amblard-Ladurantie";
		// message += "  ]  SOUND & MUSIC - Matthias Le Bidan (Matths)  £  JAVA 1.1+ VERSION - Glenn sanson £ OSC-MODIFICATION - Patrick Borgeat ";
		message += "  ]  OSCIFICATION - Patrick Borgeat  £  JAVA 1.1+ VERSION - Glenn sanson";
		message += "  \\ SPECIAL THANKS to Benoit dien for his help & to my wife for her patience";
		message += "  µ  PLEASE VISIT THE FROZEN BUBBLE OFFICIAL WEB SITE AT HTTP://WWW.FROZEN-BUBBLE.ORG  §";
		
		ScrollingTextSprite scrollingMessage = new ScrollingTextSprite(new Rectangle(168, 9, 300, 22), font, message, 1);
		this.addSprite(scrollingMessage);
		
		rand = new Random(System.currentTimeMillis());
                
		// Reset any previously registered data
        
		 ((HighscoreManager)this.getGameApplet().getGameContext().getObject("highscoreManager")).reset();
		
		((LifeManager)gameApplet.getGameContext().getObject("lifeManager")).restart();
                
                modeTiming = -1;
                
        oscManager.send("/fb/state/splash", new Object[] {});

	}
	
	public void initBackground()
	{
		addToBackground(fullBackground, new Point(0, 0));
		addToBackground(splashBackground, new Point(116, 0));	
	}	
	
	public void play(int[] keyCodes, Point mouseCoord, boolean leftButton, boolean rightButton) 
	{
		
			// merges keyCodes from game API with keyCodes from OSC
			keyCodes = oscManager.mergeWithKeyCodes(keyCodes);
		
       		boolean newModeKeyState = false;
                boolean newSoundKeyState = false;
            
                if (gameInitialized) {            
                    for (int i=0 ; i<keyCodes.length ; i++) {
                            int current = keyCodes[i];
                            
                            if (current == FrozenGame.KEY_M) {
                                newModeKeyState = true;
                            }

                            if (current == FrozenGame.KEY_S) {
                                newSoundKeyState = true;
                            }                        
                            
                            if (current == FrozenGame.KEY_UP || current == FrozenGame.KEY_SHIFT) {
                                    ((LevelManager)getGameApplet().getGameContext().getObject("levelManager")).goToFirstLevel();
                                    gameInitialized = false;
                                    
                                    // OSC-MOD
                                    
                                    try { 
                            			((OSCClient)getGameApplet().getGameContext().getObject("oscManager")).send(new OSCMessage("/fb/state/start", new Object[] {}));
                            			System.out.println("OSC: /fb/state/start");
                            		}
                                    catch (Exception e) {}
                                    
                                    getGameApplet().setCurrentScreen(new FrozenGame(getGameApplet()));

                            }
                    }
                }
                else {
                    if (keyCodes.length == 0) {
                        gameInitialized = true;
                    }
                }
		
                // Add mode information
                if (newModeKeyState != modeKeyPressed) {
                    if (newModeKeyState) {
                        FrozenBubble.switchMode();
                        
                        if (modeTiming != -1) {
                            this.removeSprite(modeMessage);
                        }
                        
                        modeMessage = new TextSprite(new Rectangle((640-getTextSize(MODE_DESC[FrozenBubble.getMode()], font)>>1), 80, 400, 100), font, MODE_DESC[FrozenBubble.getMode()]);
                        this.addSprite(modeMessage);
                        modeTiming = MODE_TIMING;
                    }
                    
                    modeKeyPressed = newModeKeyState;
                }
                
                if (newSoundKeyState != soundKeyPressed) {
                    if (newSoundKeyState) {
			SoundManager sm = (SoundManager)this.getGameApplet().getGameContext().getObject("soundManager");                        

                        sm.setSoundState(!sm.getSoundState());
                        
                        if (modeTiming != -1) {
                            this.removeSprite(modeMessage);
                        }
                        
			if (sm.getSoundState()) {
                            modeMessage = new TextSprite(new Rectangle((640-getTextSize(SOUND_DESC[0], font)>>1), 80, 400, 100), font, SOUND_DESC[0]);
                        }
                        else {
                            modeMessage = new TextSprite(new Rectangle((640-getTextSize(SOUND_DESC[1], font)>>1), 80, 400, 100), font, SOUND_DESC[1]);
                        }
                        this.addSprite(modeMessage);
                        modeTiming = MODE_TIMING;                        
                    }
                    
                    soundKeyPressed = newSoundKeyState;
                }                                
                
                if (modeTiming != -1) {
                    
                    if (modeTiming == 0) {
                        this.removeSprite(modeMessage);
                        modeMessage = null;
                    }
                    
                    modeTiming--;                    
                }
                
                
		flashDelay++;
		if (flashDelay == 30)
		{
			for (int i=0 ; i<3 ; i++)
			{
				this.removeSprite(flashingMessage[i]);
			}
		}
		else if (flashDelay == 50)
		{
			for (int i=0 ; i<3 ; i++)
			{
				this.addSprite(flashingMessage[i]);
			}
			
			flashDelay = 0;
		}
		
		if (rand.nextInt() % 100 == 0 && eyesClosed == 0)
		{
			eyesClosed = 20;
			this.addSprite(penguinEyes);
		}
		else if (eyesClosed != 0)
		{
			eyesClosed--;
			
			if (eyesClosed == 10)
			{
				this.removeSprite(penguinEyes);
			}
		}
	}	
        
	private int getTextSize(String text, GameFont font) {
		int size = 0;
		
		for (int i=0 ; i<text.length() ; i++) {
			size += font.charSize(text.charAt(i));
			size += font.SEPARATOR_WIDTH;
		}
		
		return size - font.SEPARATOR_WIDTH;
	}        
}