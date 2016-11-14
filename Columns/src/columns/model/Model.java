package columns.model;

import columns.Columns;
import columns.GameEventListener;

@SuppressWarnings("serial")
public class Model implements GameEventListener {

	public static final int Depth = 15;
	public static final int Width = 7;
	public static final int MaxLevel = 7;
	private Figure fig;
	private int fieldNew[][];
	private int fieldOld[][];
	private int level;
	private long totalScore;
	private long deltaScore;
	private int triplesCount;
	private ModelListener listener;
	public static final int FigToDrop = 33;

	public Model(ModelListener listener)  {
		this.listener = listener;
		this.fieldNew = new int[Model.Width + 2][Model.Depth + 2];
		this.fieldOld = new int[Model.Width + 2][Model.Depth + 2];
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
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Figure getFigure() {
		return fig;
	}

	public void setFigure(Figure fig) {
		this.fig = fig;
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

	public void packField() {
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

	public void pasteFigure(Columns columns, Figure f) {
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


	public boolean testField() {
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


	public boolean isFieldFull() {
		for (int i = 1; i <= Model.Width; i++) {
			if (getFieldNew()[i][3] > 0)
				return true;
		}
		return false;
	}


	public void setup() {
		for (int i = 0; i < Model.Width + 1; i++) {
			for (int j = 0; j < Model.Depth + 1; j++) {
				fieldNew[i][j] = 0;
				fieldOld[i][j] = 0;
			}
		}
		setLevel(0);
		setTotalScore((long) 0);
	}


	public boolean isFigureAbleToMoveDown() {
		return (fig.y < Model.Depth - 2)
				&& (fieldNew[fig.x][fig.y + 3] == 0);
	}


	public boolean canFigureMoveLeft() {
		return (fig.x > 1)
				&& (fieldNew[fig.x - 1][fig.y + 2] == 0);
	}


	public boolean canFigureMoveRight() {
		return (fig.x < Model.Width)
				&& (fieldNew[fig.x + 1][fig.y + 2] == 0);
	}


	@Override
	public void moveLeft() {
		if (canFigureMoveLeft()) {
			getFigure().x--;
			listener.changed(this);
		}
	}


	@Override
	public void moveRight() {
		if (canFigureMoveRight()) {
			getFigure().x++;
			listener.changed(this);
		}
	}


	@Override
	public void rotateUp() {
		int i = getFigure().c[1];
		getFigure().c[1] = getFigure().c[2];
		getFigure().c[2] = getFigure().c[3];
		getFigure().c[3] = i;
		listener.changed(this);
	}


	@Override
	public void rotateDown() {
		int i = getFigure().c[1];
		getFigure().c[1] = getFigure().c[3];
		getFigure().c[3] = getFigure().c[2];
		getFigure().c[2] = i;
		listener.changed(this);
	}


	public void increaseLevel() {
		if (level < Model.MaxLevel)
			level++;
		this.triplesCount = 0;
		listener.changed(this);
	}


	public void descreaseLevel() {
		if (level > 0)
			level--;
		this.triplesCount = 0;
		listener.changed(this);
	}


	@Override
	public void drop() {
		int zz;
		if (fig.y < Model.Depth - 2) {
			zz = Model.Depth;
			while (getFieldNew()[fig.x][zz] > 0)
				zz--;
			setDeltaScore((long) ((((getLevel() + 1)
					* (Model.Depth * 2 - fig.y - zz) * 2) % 5) * 5));
			fig.y = zz - 2;
			listener.changed(this);
		}
	}


}