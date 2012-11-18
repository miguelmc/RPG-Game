package game.util;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex2i;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public class Renderer {
	
	private Renderer(){}
	
	public static void render(Builder builder)
	{
		
		Point flips[] = new Point[4];
		flips[0] = new Point((builder.flipX + builder.flipY) % 2, builder.flipY);
		flips[1] = new Point((flips[0].getX() + 1) % 2, flips[0].getY());
		flips[2] = new Point(flips[1].getX(), (flips[1].getY() + 1) % 2);
		flips[3] = new Point((flips[2].getX() + 1) % 2, flips[2].getY());
		
		Point rotates[] = new Point[4];
		rotates[(0 + (builder.rotation*3)) % 4] = new Point(0, 0);
		rotates[(1 + (builder.rotation*3)) % 4] = new Point(builder.size.getWidth(),0);
		rotates[(2 + (builder.rotation*3)) % 4] = new Point(builder.size.getWidth(), builder.size.getHeight());
		rotates[(3 + (builder.rotation*3)) % 4] = new Point(0, builder.size.getHeight());

		glEnable(GL_TEXTURE_2D);
		builder.texture.bind();
		glLoadIdentity();
		glTranslatef(builder.position.getX(), builder.position.getY(), 0);
		glBegin(GL_QUADS);
		for (int i=0; i<4; i++)
		{
			glTexCoord2f((builder.offset.getX() + flips[i].getX() * builder.imageWidth) / builder.texture.getTextureWidth(),
						 ((builder.offset.getY() + flips[i].getY() * builder.imageHeight) / builder.texture.getTextureHeight()));
			glVertex2f(rotates[i].getX(), rotates[i].getY());
		}
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
	}
	
	public static void renderQuad(Point position, Dimension size)
	{
		glTranslatef(position.getX(), position.getY(), 0);
		glBegin(GL_QUADS);
			glVertex2f(0, 0);
			glVertex2f(size.getWidth(), 0);
			glVertex2f(size.getWidth(), size.getHeight());
			glVertex2f(0, size.getHeight());
		glEnd();
		glLoadIdentity();
	}

	public static void renderLines(int width, Point origin, Point...points)
	{
		if(points.length%2 != 0)
			throw new IllegalArgumentException("The points must be in pairs of two.");
		
		glTranslatef(origin.getX(), origin.getY(), 0);
		glLineWidth(width);
		glBegin(GL_LINES);
			for(int i=0; i<points.length; i++)
			{
				glVertex2i(points[i].getX(), points[i].getY());
				glVertex2i(points[(i+1)%(points.length)].getX(), points[(i+1)%(points.length)].getY());
			}
		glEnd();
		glLoadIdentity();
	}
	
	public static void renderBorder(int width, Point position, Dimension size)
	{
		
		renderLines(width, position, new Point(0, 0),
									 new Point(size.getWidth(), 0),
									 new Point(size.getWidth(), size.getHeight()),
									 new Point(0, size.getHeight()));
		
	}
	
	public static class Builder
	{
		private int flipX;
		private int flipY;
		private float imageWidth;
		private float imageHeight;
		private int rotation = 0;
		private Texture texture;
		private Point position;
		private Dimension size;
		private Point offset = new Point(0, 0);
		
		public Builder(Texture texture, Point position, Dimension size)
		{
			this.texture = texture;
			this.position = position;
			this.size = size;
			imageWidth = texture.getTextureWidth();
			imageHeight = texture.getTextureHeight();
		}
		
		public Builder flipX(boolean flipX) {
			this.flipX = flipX ? 1:0;
			return this;
		}

		public Builder flipY(boolean flipY) {
			this.flipY = flipY ? 1:0;
			return this;
		}

		public Builder rotate(int rotation) {
			this.rotation = rotation;
			return this;
		}

		public Builder imageSize(int width, int height) {
			this.imageWidth = width;
			this.imageHeight = height;
			return this;
		}

		public Builder offset(Point offset)
		{
			this.offset.setLocation(offset);
			return this;
		}
		
	}	
}
