package columns;

import java.awt.Color;

import columns.view.GameGraphics;

public class JavaAppletGraphics implements GameGraphics {

	private java.awt.Graphics gr;

	public JavaAppletGraphics(java.awt.Graphics graphics) {
		this.gr = graphics;
	}

	@Override
	public void setColor(int color) {
		gr.setColor(Columns.MyStyles[color]);
	}

	@Override
	public void fillRect(int x, int y, int w, int h) {
		gr.fillRect(x, y, w, h);
	}

	@Override
	public void drawRect(int x, int y, int w, int h) {
		gr.drawRect(x, y, w, h);
	}

	@Override
	public void drawString(String string, int x, int y) {
		gr.drawString(string, x, y);
	}

	@Override
	public void clearRect(int x, int y, int w, int h) {
		gr.clearRect(x, y, w, h);
	}

}
