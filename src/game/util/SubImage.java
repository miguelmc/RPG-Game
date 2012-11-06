package game.util;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import game.structure.MapManager;
import game.structure.Slot;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

// TODO adjust this class to use Util.render and make more animation friendly
@Deprecated
public class SubImage
{

	Texture spriteSheet;
	Point pos;
	Dimension size;

	public SubImage(Texture texture, Point pos, Dimension size)
	{
		spriteSheet = texture;
		this.pos = pos;
		this.size = size;
	}

	public void render(int x, int y, int flipClockWise)
	{
		float w = spriteSheet.getImageWidth();
		float h = spriteSheet.getImageHeight();

		Point flips[][] = new Point[4][4];

		switch (flipClockWise)
		{
		case 0:
			flips[0][0] = new Point(0, 0);
			flips[0][1] = new Point(size.getWidth(), 0);
			flips[0][2] = new Point(size.getWidth(), size.getHeight());
			flips[0][3] = new Point(0, size.getHeight());
			break;
		case 1:
			flips[1][3] = new Point(0, 0);
			flips[1][0] = new Point(size.getWidth(), 0);
			flips[1][1] = new Point(size.getWidth(), size.getHeight());
			flips[1][2] = new Point(0, size.getHeight());
			break;
		case 2:
			flips[2][2] = new Point(0, 0);
			flips[2][3] = new Point(size.getWidth(), 0);
			flips[2][0] = new Point(size.getWidth(), size.getHeight());
			flips[2][1] = new Point(0, size.getHeight());
			break;
		case 3:
			flips[3][1] = new Point(0, 0);
			flips[3][2] = new Point(size.getWidth(), 0);
			flips[3][3] = new Point(size.getWidth(), size.getHeight());
			flips[3][0] = new Point(0, size.getHeight());
			break;
		}

		glEnable(GL_TEXTURE_2D);
		spriteSheet.bind();
		glLoadIdentity();
		glTranslatef(x - MapManager.getMap().getOffSet().getX() * Slot.SIZE, y - MapManager.getMap().getOffSet().getY()
				* Slot.SIZE, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(pos.getX() / w, pos.getY() / h);
		glVertex2f(flips[flipClockWise][0].getX(), flips[flipClockWise][0].getY());

		glTexCoord2f((pos.getX() + size.getWidth()) / w, pos.getY() / h);
		glVertex2f(flips[flipClockWise][1].getX(), flips[flipClockWise][1].getY());

		glTexCoord2f((pos.getX() + size.getWidth()) / w, (pos.getY() + size.getHeight()) / h);
		glVertex2f(flips[flipClockWise][2].getX(), flips[flipClockWise][2].getY());

		glTexCoord2f(pos.getX() / w, (pos.getY() + size.getHeight()) / h);
		glVertex2f(flips[flipClockWise][3].getX(), flips[flipClockWise][3].getY());

		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
	}

}