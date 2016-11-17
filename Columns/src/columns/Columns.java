package columns;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import columns.model.Figure;
import columns.model.Model;
import columns.model.ModelListener;
import columns.view.GameGraphics;
import columns.view.View;


@SuppressWarnings("serial")
public class Columns extends Applet implements ModelListener {
	
	public static final int SL = 25;

	static final int TimeShift = 250;

	static final int MinTimeShift = 200;

	public static final int LeftBorder = 2;

	public static final int TopBorder = 2;

	private static final Random Random = new SecureRandom();

	public static final Color MyStyles[] = { Color.black, Color.cyan, Color.blue, Color.red,
			Color.green, Color.yellow, Color.pink, Color.magenta, Color.black, Color.white };

	// View

	int charPressed;
	long timestamp;
	Font fCourier;
	boolean KeyPressed = false;
	GameGraphics gr;

	public Model model = new Model(this);
	public View  view;

	private ScheduledExecutorService scheduler;

	void delay(long t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		gr = new JavaAppletGraphics(getGraphics());
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				KeyPressed = true;
				charPressed = e.getKeyCode();
				processKeyPressed();
				checkConditions();
			}
		});
		
		view = new View(model, gr);
		
		scheduler = Executors.newScheduledThreadPool(1);
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

		view.drawAll(gr);

		requestFocus();
	}

	public void launchNewFigure() {
		model.setFigure(new Figure(Random));
		view.drawFigure(model.getFigure());
	}

	public void moveFigureOneLineDown() {
		view.hideFigure(model.getFigure());
		model.getFigure().y++;
		view.drawFigure(model.getFigure());
		model.setDeltaScore((long) 0);
		checkConditions();
	}

	private void checkConditions() {
		if (!model.isFigureAbleToMoveDown()) {
			model.pasteFigure(this, model.getFigure());
			processTriplets();
			launchNewFigure();
		}
		
		if (model.isFieldFull()) {
			// game over!
		}
	}

	public void processTriplets() {
		if (model.testField()) {
			scheduler.schedule(() -> {
				model.packField();
				view.drawField();
				model.setTotalScore(model.getTotalScore() + model.getDeltaScore());
				view.showScore(gr);
				if (model.getTriplesCount() >= Model.FigToDrop) {
					model.setTriplesCount(0);
					if (model.getLevel() < Model.MaxLevel)
						model.setLevel(model.getLevel() + 1);
					view.showLevel(gr);
				}
				processTriplets();
			}, 500, TimeUnit.MILLISECONDS);
		}
	}

	public void processKeyPressed() {
		KeyPressed = false;
		switch (charPressed) {
		case KeyEvent.VK_LEFT:
			model.moveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			model.moveRight();
			break;
		case KeyEvent.VK_UP: {
			model.rotateUp();
		}
			break;
		case KeyEvent.VK_DOWN: {
			model.rotateDown();
		}
			break;
		case KeyEvent.VK_SPACE:
			model.drop();
			timestamp = 0;
			break;
		case KeyEvent.VK_P:
			while (!KeyPressed) {
				view.hideFigure(model.getFigure());
				delay(500);
				view.drawFigure(model.getFigure());
				delay(500);
			}
			timestamp = System.currentTimeMillis();
			break;
		case KeyEvent.VK_MINUS:
			model.descreaseLevel();
			view.showLevel(gr);
			break;
		case KeyEvent.VK_PLUS:
			model.increaseLevel();
			view.showLevel(gr);
			break;
		}
	}

	public boolean isTimeForMoveFigureOnLineDown() {
		return (int) (System.currentTimeMillis() - timestamp) > getMoveOneLineDownDelay();
	}

	public long getMoveOneLineDownDelay() {
		return (Model.MaxLevel - model.getLevel()) * TimeShift + MinTimeShift;
	}

	@Override
	public void start() {
		gr.setColor(columns.view.Color.black);

		model.setup();
		gr.setColor(columns.view.Color.black);
		requestFocus();
		launchNewFigure();

		Thread t = new Thread(new Timer(this::moveFigureOneLineDown,
				this::getMoveOneLineDownDelay));
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void gotTriple(int a, int b, int c, int d, int i, int j) {
		view.drawWhiteTriple(a, b, c, d, i, j);
	}

	@Override
	public void changed(Model model) {
		view.drawAll(gr);
	}

}