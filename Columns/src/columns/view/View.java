package columns.view;

import columns.Columns;
import columns.model.Figure;
import columns.model.Model;

public class View {
	private Model model;
	private GameGraphics gr;

	public View(Model model, GameGraphics gr) {
		this.model = model;
		this.gr = gr;
	}

	void drawBox(int x, int y, int c) {
		if (c == 0) {
			gr.setColor(Color.black);
			gr.fillRect(Columns.LeftBorder + x * Columns.SL - Columns.SL, Columns.TopBorder + y * Columns.SL - Columns.SL, Columns.SL,
					Columns.SL);
			gr.drawRect(Columns.LeftBorder + x * Columns.SL - Columns.SL, Columns.TopBorder + y * Columns.SL - Columns.SL, Columns.SL,
					Columns.SL);
		} else if (c == 8) {
			gr.setColor(Color.white);
			gr.drawRect(Columns.LeftBorder + x * Columns.SL - Columns.SL + 1, Columns.TopBorder + y * Columns.SL - Columns.SL
					+ 1, Columns.SL - 2, Columns.SL - 2);
			gr.drawRect(Columns.LeftBorder + x * Columns.SL - Columns.SL + 2, Columns.TopBorder + y * Columns.SL - Columns.SL
					+ 2, Columns.SL - 4, Columns.SL - 4);
			gr.setColor(Color.black);
			gr.fillRect(Columns.LeftBorder + x * Columns.SL - Columns.SL + 3, Columns.TopBorder + y * Columns.SL - Columns.SL
					+ 3, Columns.SL - 6, Columns.SL - 6);
		} else {
			gr.setColor(c);
			gr.fillRect(Columns.LeftBorder + x * Columns.SL - Columns.SL, Columns.TopBorder + y * Columns.SL - Columns.SL, Columns.SL,
					Columns.SL);
			gr.setColor(Color.black);
			gr.drawRect(Columns.LeftBorder + x * Columns.SL - Columns.SL, Columns.TopBorder + y * Columns.SL - Columns.SL, Columns.SL,
					Columns.SL);
		}
		// g.setColor (Color.black);
	}

	public void drawField() {
		for (int i = 1; i <= Model.Depth; i++) {
			for (int j = 1; j <= Model.Width; j++) {
				drawBox(j, i, model.getFieldNew()[j][i]);
			}
		}
	}

	public void drawFigure(Figure f) {
		drawBox(f.x, f.y, f.c[1]);
		drawBox(f.x, f.y + 1, f.c[2]);
		drawBox(f.x, f.y + 2, f.c[3]);
	}

	public void hideFigure(Figure f) {
		drawBox(f.x, f.y, 0);
		drawBox(f.x, f.y + 1, 0);
		drawBox(f.x, f.y + 2, 0);
	}

	public void drawWhiteTriple(int a, int b, int c, int d, int i, int j) {
		drawBox(a, b, 8);
		drawBox(j, i, 8);
		drawBox(c, d, 8);
	}

	public void drawAll(GameGraphics g) {
		showLevel(g);
		showScore(g);
		drawField();
		drawFigure(model.getFigure());
	}

	void showHelp(GameGraphics g) {
		g.setColor(Color.black);
	
		g.drawString(" Keys available:", 200 - Columns.LeftBorder, 102);
		g.drawString("Roll Box Up:     ", 200 - Columns.LeftBorder, 118);
		g.drawString("Roll Box Down:   ", 200 - Columns.LeftBorder, 128);
		g.drawString("Figure Left:     ", 200 - Columns.LeftBorder, 138);
		g.drawString("Figure Right:    ", 200 - Columns.LeftBorder, 148);
		g.drawString("Level High/Low: +/-", 200 - Columns.LeftBorder, 158);
		g.drawString("Drop Figure:   space", 200 - Columns.LeftBorder, 168);
		g.drawString("Pause:           P", 200 - Columns.LeftBorder, 180);
		g.drawString("Quit:     Esc or Q", 200 - Columns.LeftBorder, 190);
	}

	public void showLevel(GameGraphics g) {
		g.setColor(Color.black);
		g.clearRect(Columns.LeftBorder + 100, 390, 100, 20);
		g.drawString("Level: " + model.getLevel(), Columns.LeftBorder + 100, 400);
	}

	public void showScore(GameGraphics g) {
		g.setColor(Color.black);
		g.clearRect(Columns.LeftBorder, 390, 100, 20);
		g.drawString("Score: " + model.getTotalScore(), Columns.LeftBorder, 400);
	}
}
