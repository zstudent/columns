package columns;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.security.SecureRandom;
import java.util.Random;

import columns.model.Figure;

@SuppressWarnings("serial")
public class Columns extends Applet implements Runnable, ModelListener {

	static final int SL = 25;

	static final int TimeShift = 250;

	static final int FigToDrop = 33;

	static final int MinTimeShift = 200;

	static final int LeftBorder = 2;

	static final int TopBorder = 2;

	private static final Random Random = new SecureRandom();

	Color MyStyles[] = { Color.black, Color.cyan, Color.blue, Color.red,
			Color.green, Color.yellow, Color.pink, Color.magenta, Color.black };

	// View

	int charPressed;
	long timestamp;
	Font fCourier;
	boolean KeyPressed = false;
	Graphics gr;

	Thread thread = null;

	Model model = new Model(this);

	private void drawWhiteTriple(int a, int b, int c, int d, int i, int j) {
		drawBox(a, b, 8);
		drawBox(j, i, 8);
		drawBox(c, d, 8);
	}

	void delay(long t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	void drawBox(int x, int y, int c) {
		if (c == 0) {
			gr.setColor(Color.black);
			gr.fillRect(LeftBorder + x * SL - SL, TopBorder + y * SL - SL, SL,
					SL);
			gr.drawRect(LeftBorder + x * SL - SL, TopBorder + y * SL - SL, SL,
					SL);
		} else if (c == 8) {
			gr.setColor(Color.white);
			gr.drawRect(LeftBorder + x * SL - SL + 1, TopBorder + y * SL - SL
					+ 1, SL - 2, SL - 2);
			gr.drawRect(LeftBorder + x * SL - SL + 2, TopBorder + y * SL - SL
					+ 2, SL - 4, SL - 4);
			gr.setColor(Color.black);
			gr.fillRect(LeftBorder + x * SL - SL + 3, TopBorder + y * SL - SL
					+ 3, SL - 6, SL - 6);
		} else {
			gr.setColor(MyStyles[c]);
			gr.fillRect(LeftBorder + x * SL - SL, TopBorder + y * SL - SL, SL,
					SL);
			gr.setColor(Color.black);
			gr.drawRect(LeftBorder + x * SL - SL, TopBorder + y * SL - SL, SL,
					SL);
		}
		// g.setColor (Color.black);
	}

	void drawField() {
		for (int i = 1; i <= Model.Depth; i++) {
			for (int j = 1; j <= Model.Width; j++) {
				drawBox(j, i, model.getFieldNew()[j][i]);
			}
		}
	}

	void drawFigure(Figure f) {
		drawBox(f.x, f.y, f.c[1]);
		drawBox(f.x, f.y + 1, f.c[2]);
		drawBox(f.x, f.y + 2, f.c[3]);
	}

	void dropFigure(Figure f) {
		int zz;
		if (f.y < Model.Depth - 2) {
			zz = Model.Depth;
			while (model.getFieldNew()[f.x][zz] > 0)
				zz--;
			model.setDeltaScore((long) ((((model.getLevel() + 1) * (Model.Depth * 2 - f.y - zz) * 2) % 5) * 5));
			f.y = zz - 2;
		}
	}

	boolean isFieldFull() {
		for (int i = 1; i <= Model.Width; i++) {
			if (model.getFieldNew()[i][3] > 0)
				return true;
		}
		return false;
	}

	void hideFigure(Figure f) {
		drawBox(f.x, f.y, 0);
		drawBox(f.x, f.y + 1, 0);
		drawBox(f.x, f.y + 2, 0);
	}

	@Override
	public void init() {
		model.setFieldNew(new int[Model.Width + 2][Model.Depth + 2]);
		model.setFieldOld(new int[Model.Width + 2][Model.Depth + 2]);
		gr = getGraphics();
	}

	@Override
	public boolean keyDown(Event e, int k) {
		KeyPressed = true;
		charPressed = e.key;
		return true;
	}

	@Override
	public boolean lostFocus(Event e, Object w) {
		KeyPressed = true;
		charPressed = 'P';
		return true;
	}

	@Override
	public void paint(Graphics g) {
		// ShowHelp(g);

		g.setColor(Color.black);

		ShowLevel(g);
		ShowScore(g);
		drawField();
		drawFigure(model.getFigure());
		requestFocus();
	}

	@Override
	public void run() {
		setup();
		gr.setColor(Color.black);
		requestFocus();

		do {
			timestamp = System.currentTimeMillis();
			model.setFigure(new Figure(Random));
			drawFigure(model.getFigure());
			while ((model.getFigure().y < Model.Depth - 2)
					&& (model.getFieldNew()[model.getFigure().x][model
							.getFigure().y + 3] == 0)) {
				if ((int) (System.currentTimeMillis() - timestamp) > (Model.MaxLevel - model
						.getLevel()) * TimeShift + MinTimeShift) {
					timestamp = System.currentTimeMillis();
					hideFigure(model.getFigure());
					model.getFigure().y++;
					drawFigure(model.getFigure());
				}
				model.setDeltaScore((long) 0);
				do {
					delay(50);
					if (KeyPressed) {
						KeyPressed = false;
						switch (charPressed) {
						case Event.LEFT:
							if ((model.getFigure().x > 1)
									&& (model.getFieldNew()[model.getFigure().x - 1][model
											.getFigure().y + 2] == 0)) {
								hideFigure(model.getFigure());
								model.getFigure().x--;
								drawFigure(model.getFigure());
							}
							break;
						case Event.RIGHT:
							if ((model.getFigure().x < Model.Width)
									&& (model.getFieldNew()[model.getFigure().x + 1][model
											.getFigure().y + 2] == 0)) {
								hideFigure(model.getFigure());
								model.getFigure().x++;
								drawFigure(model.getFigure());
							}
							break;
						case Event.UP: {
							int i = model.getFigure().c[1];
							model.getFigure().c[1] = model.getFigure().c[2];
							model.getFigure().c[2] = model.getFigure().c[3];
							model.getFigure().c[3] = i;
							drawFigure(model.getFigure());
						}
							break;
						case Event.DOWN: {
							int i = model.getFigure().c[1];
							model.getFigure().c[1] = model.getFigure().c[3];
							model.getFigure().c[3] = model.getFigure().c[2];
							model.getFigure().c[2] = i;
							drawFigure(model.getFigure());
						}
							break;
						case ' ':
							hideFigure(model.getFigure());
							dropFigure(model.getFigure());
							drawFigure(model.getFigure());
							timestamp = 0;
							break;
						case 'P':
						case 'p':
							while (!KeyPressed) {
								hideFigure(model.getFigure());
								delay(500);
								drawFigure(model.getFigure());
								delay(500);
							}
							timestamp = System.currentTimeMillis();
							break;
						case '-':
							if (model.getLevel() > 0)
								model.setLevel(model.getLevel() - 1);
							model.setTriplesCount(0);
							ShowLevel(gr);
							break;
						case '+':
							if (model.getLevel() < Model.MaxLevel)
								model.setLevel(model.getLevel() + 1);
							model.setTriplesCount(0);
							ShowLevel(gr);
							break;
						}
					}
				} while ((int) (System.currentTimeMillis() - timestamp) <= (Model.MaxLevel - model
						.getLevel()) * TimeShift + MinTimeShift);
			}
			model.pasteFigure(this, model.getFigure());
			while (model.testField()) {
				delay(500);
				model.packField();
				drawField();
				model.setTotalScore(model.getTotalScore() + model.getDeltaScore());
				ShowScore(gr);
				if (model.getTriplesCount() >= FigToDrop) {
					model.setTriplesCount(0);
					if (model.getLevel() < Model.MaxLevel)
						model.setLevel(model.getLevel() + 1);
					ShowLevel(gr);
				}
			}
		} while (!isFieldFull());
	}

	private void setup() {
		for (int i = 0; i < Model.Width + 1; i++) {
			for (int j = 0; j < Model.Depth + 1; j++) {
				model.getFieldNew()[i][j] = 0;
				model.getFieldOld()[i][j] = 0;
			}
		}
		model.setLevel(0);
		model.setTotalScore((long) 0);
	}

	void ShowHelp(Graphics g) {
		g.setColor(Color.black);

		g.drawString(" Keys available:", 200 - LeftBorder, 102);
		g.drawString("Roll Box Up:     ", 200 - LeftBorder, 118);
		g.drawString("Roll Box Down:   ", 200 - LeftBorder, 128);
		g.drawString("Figure Left:     ", 200 - LeftBorder, 138);
		g.drawString("Figure Right:    ", 200 - LeftBorder, 148);
		g.drawString("Level High/Low: +/-", 200 - LeftBorder, 158);
		g.drawString("Drop Figure:   space", 200 - LeftBorder, 168);
		g.drawString("Pause:           P", 200 - LeftBorder, 180);
		g.drawString("Quit:     Esc or Q", 200 - LeftBorder, 190);
	}

	void ShowLevel(Graphics g) {
		g.setColor(Color.black);
		g.clearRect(LeftBorder + 100, 390, 100, 20);
		g.drawString("Level: " + model.getLevel(), LeftBorder + 100, 400);
	}

	void ShowScore(Graphics g) {
		g.setColor(Color.black);
		g.clearRect(LeftBorder, 390, 100, 20);
		g.drawString("Score: " + model.getTotalScore(), LeftBorder, 400);
	}

	@Override
	public void start() {
		gr.setColor(Color.black);

		// setBackground (new Color(180,180,180));

		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	@Override
	public void gotTriple(int a, int b, int c, int d, int i, int j) {
		drawWhiteTriple(a, b, c, d, i, j);
	}
}