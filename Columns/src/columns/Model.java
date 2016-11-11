package columns;

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
	private long totalScore;
	private long deltaScore;
	private int triplesCount;
	private ModelListener listener;

	public Model(ModelListener listener)  {
		this.listener = listener;
	}
	
	
	public int getTriplesCount() {
		return triplesCount;
	}

	public void setTriplesCount(int triplesCount) {
		this.triplesCount = triplesCount;
	}

	public long getDeltaScore() {
		return deltaScore;
	}

	public void setDeltaScore(long deltaScore) {
		this.deltaScore = deltaScore;
	}

	public long getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(long totalScore) {
		this.totalScore = totalScore;
	}

	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		Level = level;
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

	boolean checkNeighbours(int a, int b, int c, int d, int i, int j) {
		if ((getFieldNew()[j][i] != getFieldNew()[a][b])
				|| (getFieldNew()[j][i] != getFieldNew()[c][d])) {
			return false;
		}
		getFieldOld()[a][b] = 0;
		getFieldOld()[j][i] = 0;
		getFieldOld()[c][d] = 0;
		setTotalScore(getTotalScore() + (getLevel() + 1) * 10);
		setTriplesCount(getTriplesCount() + 1);
		listener.gotTriple(a,b,c,d,i,j);
		return true;
	}


	boolean testField() {
		boolean changed = false;
		int i, j;
		for (i = 1; i <= Model.Depth; i++) {
			for (j = 1; j <= Model.Width; j++) {
				getFieldOld()[j][i] = getFieldNew()[j][i];
			}
		}
		for (i = 1; i <= Model.Depth; i++) {
			for (j = 1; j <= Model.Width; j++) {
				if (getFieldNew()[j][i] > 0) {
					changed |= checkNeighbours(j, i - 1, j, i + 1, i, j);
					changed |= checkNeighbours(j - 1, i, j + 1, i, i, j);
					changed |= checkNeighbours(j - 1, i - 1, j + 1, i + 1, i, j);
					changed |= checkNeighbours(j + 1, i - 1, j - 1, i + 1, i, j);
				}
			}
		}
		return changed;
	}


}