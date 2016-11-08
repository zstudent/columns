package columns;

import java.awt.HeadlessException;

import columns.model.Figure;

@SuppressWarnings("serial")
public class Model implements GameEventListener {

	protected static final int Depth = 15;
	public static final int Width = 7;
	protected static final int MaxLevel = 7;
	private Figure Fig;
	private int fieldNew[][];
	private int fieldOld[][];
	private int Level;

	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		Level = level;
	}

	public Model() throws HeadlessException {
		super();
	}

	public Figure getFigure() {
		return Fig;
	}

	public void setFigure(Figure fig) {
		Fig = fig;
	}

	public int[][] getFieldNew() {
		return fieldNew;
	}

	public void setFieldNew(int fieldNew[][]) {
		this.fieldNew = fieldNew;
	}

	public int[][] getFieldOld() {
		return fieldOld;
	}

	public void setFieldOld(int fieldOld[][]) {
		this.fieldOld = fieldOld;
	}

	@Override
	public void moveLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveRight() {
		// TODO implement move right
		
	}

	@Override
	public void rotateUp() {
		// FIXME Auto-generated method stub
		
	}

	@Override
	public void rotateDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop() {
		// TODO Auto-generated method stub
		
	}

	void packField() {
		int i, j, n;
		for (i = 1; i <= Model.Width; i++) {
			n = Model.Depth;
			for (j = Model.Depth; j > 0; j--) {
				if (fieldOld[i][j] > 0) {
					fieldNew[i][n] = fieldOld[i][j];
					n--;
				}
			}
			for (j = n; j > 0; j--)
				fieldNew[i][j] = 0;
		}
	}

	void pasteFigure(Columns columns, Figure f) {
		fieldNew[f.x][f.y] = f.c[1];
		fieldNew[f.x][f.y + 1] = f.c[2];
		fieldNew[f.x][f.y + 2] = f.c[3];
	}

}