package game.util;

import game.Main;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

public class MouseManager{

	public static final int LEFT = 0, RIGHT = 1;
	private static int x, y, dx, dy;
		
	public static boolean mouseReleased() { return !isButtonDown(0) && !mouseMoved(); }
	
	public static boolean mousePressed() { return isButtonDown(0) && !mouseMoved(); }
	
	public static boolean mouseMoved() { return !(getDX() == 0 && getDY() == 0); }
	
	public static boolean isButtonDown(int button) { return Mouse.isButtonDown(button); }
	
	public static boolean hasEvent()
	{ 
		x = Mouse.getX();
		y = Main.DIM.getHeight() - Mouse.getY() - 1;
		dx = Mouse.getDX();
		dy = -Mouse.getDY();
		return Mouse.next();
	}
	
	public static int getDX(){ return dx; }
	
	public static int getDY(){ return dy; }
	
	public static int getX() { return x; }
	
	public static int getY() { return y; }
	
	public static Point getPosition() { return new Point(x, y); } //TODO do not create a new point every time it is called
	
}
