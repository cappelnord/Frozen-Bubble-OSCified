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

public class LaunchBubbleSprite extends Sprite
{
        public final static String LAUNCHER_BACK_CONTEXT_KEY = "back_launcher";
        public final static String LAUNCHER_BUBBLES_CONTEXT_KEY = "sprites_launcher";
        public final static String LAUNCHER_BUBBLES_BLIND_CONTEXT_KEY = "sprites_blind_launcher";

        public final static String LAUNCHER_IMG = "launcher.gif";
        public final static String LAUNCHER_ALPHA_IMG = "launcher_alpha.gif";
        
	private int currentColor;
	private int currentDirection;
	
	private Point currentImageArea;
	
        private Image backgroundImage;
	private Image spritesImage;
        private Image spritesBlindImage;
        
        public LaunchBubbleSprite(GameApplet applet, int initialColor, int initialDirection) {
            
            super(new Rectangle(276, 362, 86, 76));
            
            currentColor = initialColor;
            currentDirection = initialDirection;
            
            GameContext context = applet.getGameContext();
            
            backgroundImage = (Image)context.getObject(LAUNCHER_BACK_CONTEXT_KEY);
            spritesImage = (Image)context.getObject(LAUNCHER_BUBBLES_CONTEXT_KEY);
            spritesBlindImage = (Image)context.getObject(LAUNCHER_BUBBLES_BLIND_CONTEXT_KEY);
            
            // Create image
            if (backgroundImage == null || spritesImage == null) {
                backgroundImage = applet.createImage(86, 3116);
                spritesImage = applet.createImage(256, 1312);
                spritesBlindImage = applet.createImage(256, 1312);
                
                createLauncherImages(applet);
                
                context.addObject(LAUNCHER_BACK_CONTEXT_KEY, backgroundImage);
                context.addObject(LAUNCHER_BUBBLES_CONTEXT_KEY, spritesImage);
                context.addObject(LAUNCHER_BUBBLES_BLIND_CONTEXT_KEY, spritesBlindImage);
            }
        }
        
        private void createLauncherImages(GameApplet applet) {

            Graphics gb = backgroundImage.getGraphics();
            Graphics gs = spritesImage.getGraphics();
            Graphics gsb = spritesBlindImage.getGraphics();
            
            GameMedia media = applet.getGameMedia();
            
            // Gets game empty background
            Image imgGame = media.loadImage("background.jpg");

            // prepare background
            for (int i=0 ; i<41 ; i++) {
                gb.setClip(0, i*76, 86, 76);
                gb.drawImage(imgGame, -276, i*76-362, applet);
            }
            
            // prepare sprites
            
            // Retrieve bubbles
            Image[] bubbles = new Image[8];
            for (int i=0 ; i<8 ; i++) {
		bubbles[i] = media.loadImage("bubble-"+Integer.toString(i+1)+".gif");
            }
            
            // Retrieve -colorblind- bubbles
            Image[] colorblindBubbles = new Image[8];
            for (int i=0 ; i<8 ; i++) {
		colorblindBubbles[i] = media.loadImage("bubble-colourblind-"+Integer.toString(i+1)+".gif");
            }

            for (int j=0 ; j<41 ; j++) {
                for (int i=0 ; i<8 ; i++) {
                    // Clip area
                    gs.setClip(i*32, j*32, 32, 32);
                    gsb.setClip(i*32, j*32, 32, 32);
                    // Draw background
                    gs.drawImage(imgGame, i*32-302, j*32-390, applet);
                    gsb.drawImage(imgGame, i*32-302, j*32-390, applet);
                    // Draw bubbles
                    gs.drawImage(bubbles[i], i*32, j*32, applet);
                    gsb.drawImage(colorblindBubbles[i], i*32, j*32, applet);
                }
            }
            
            // Gets launcher image
            EffectImage launcher = new EffectImage(applet, LAUNCHER_IMG, LAUNCHER_ALPHA_IMG);
            
            double rotateAngle = 0.025 * Math.PI;
            
            Image result = null;
            for (int i=0 ; i<41 ; i++) {
                result = launcher.getRotate(rotateAngle*(i-20), 50, 50);
                
                // background
                gb.setClip(0, i*76, 86, 76);
                gb.drawImage(result, -8, i*76-6, applet);
                
                for (int j=0 ; j<8 ; j++) {
                    // Clip area
                    gs.setClip(j*32, i*32, 32, 32);
                    gsb.setClip(j*32, i*32, 32, 32);
                    // draw launcher
                    gs.drawImage(result, j*32-34, i*32-34, applet);
                    gsb.drawImage(result, j*32-34, i*32-34, applet);
                }
            }
        }
        		
	public void changeColor(int newColor) {
		currentColor = newColor;
	}
	
	public void changeDirection(int newDirection) {
            currentDirection = newDirection;
   	}
	
	public void paint(Graphics g, GameApplet applet) {
		Rectangle r = this.getSpriteArea();
		
		g.setClip(r);
		g.drawImage(backgroundImage, 276, 362-currentDirection*76, applet);
                g.setClip(302, 390, 32, 32);
                if (FrozenBubble.getMode() == FrozenBubble.GAME_NORMAL) {
                    g.drawImage(spritesImage, 302-currentColor*32, 390-currentDirection*32, applet);
                }
                else {
                    g.drawImage(spritesBlindImage, 302-currentColor*32, 390-currentDirection*32, applet);
                }
	}
}