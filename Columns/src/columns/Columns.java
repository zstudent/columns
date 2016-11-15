package columns;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.security.SecureRandom;
import java.util.Random;

import columns.model.Figure;
import columns.model.Model;
import columns.model.ModelListener;

@SuppressWarnings("serial")
public class Columns extends Applet implements Runnable, ModelListener {

	static final int SL = 25;

	static final int TimeShift = 250;

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

	public Model model = new Model(this);

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

	void hideFigure(Figure f) {
		drawBox(f.x, f.y, 0);
		drawBox(f.x, f.y + 1, 0);
		drawBox(f.x, f.y + 2, 0);
	}

	@Override
	public void init() {
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

		drawAll(g);

		requestFocus();
	}

	public void drawAll(Graphics g) {
		showLevel(g);
		showScore(g);
		drawField();
		drawFigure(model.getFigure());
	}

	@Override
	public void run() {
		model.setup();
		gr.setColor(Color.black);
		requestFocus();

		do {
			timestamp = System.currentTimeMillis();
			model.setFigure(new Figure(Random));
			drawFigure(model.getFigure());
			while (model.isFigureAbleToMoveDown()) {
				if (isTimeForMoveFigureOnLineDown()) {
					timestamp = System.currentTimeMillis();
					moveFigureOneLineDown();
				}
				model.setDeltaScore((long) 0);
				do {
					delay(50);
					if (KeyPressed) {
						processKeyPressed();
					}
				} while (!isTimeForMoveFigureOnLineDown());
			}
			model.pasteFigure(this, model.getFigure());
			processTriplets();
		} while (!model.isFieldFull());
	}

	public void moveFigureOneLineDown() {
		hideFigure(model.getFigure());
		model.getFigure().y++;
		drawFigure(model.getFigure());
	}

	public void processTriplets() {
		while (model.testField()) {
			delay(500);
			model.packField();
			drawField();
			model.setTotalScore(model.getTotalScore()
					+ model.getDeltaScore());
			showScore(gr);
			if (model.getTriplesCount() >= Model.FigToDrop) {
				model.setTriplesCount(0);
				if (model.getLevel() < Model.MaxLevel)
					model.setLevel(model.getLevel() + 1);
				showLevel(gr);
			}
		}
	}

	public void processKeyPressed() {
		KeyPressed = false;
		switch (charPressed) {
		case Event.LEFT:
			model.moveLeft();
			break;
		case Event.RIGHT:
			model.moveRight();
			break;
		case Event.UP: {
			model.rotateUp();
		}
			break;
		case Event.DOWN: {
			model.rotateDown();
		}
			break;
		case ' ':
			model.drop();
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
			model.descreaseLevel();
			showLevel(gr);
			break;
		case '+':
			model.increaseLevel();
			showLevel(gr);
			break;
		}
	}

	public boolean isTimeForMoveFigureOnLineDown() {
		return (int) (System.currentTimeMillis() - timestamp) > (Model.MaxLevel - model
				.getLevel()) * TimeShift + MinTimeShift;
	}

	void showHelp(Graphics g) {
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

	void showLevel(Graphics g) {
		g.setColor(Color.black);
		g.clearRect(LeftBorder + 100, 390, 100, 20);
		g.drawString("Level: " + model.getLevel(), LeftBorder + 100, 400);
	}

	void showScore(Graphics g) {
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
	
	@Override
	public void changed(Model model) {
		drawAll(gr);
	}
	
}