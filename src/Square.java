import java.util.Random;
import java.util.stream.IntStream;

class Square {
    // block of 2x2 cells
    private Cell[][] cells = new Cell[2][2];
    private int row; // row of cell [0][0]
    private int column; // column of cell [0][0]
    private long lastChange;

    Square(){
        Random random = new Random();
        for(int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++) cells[i][j] = Cell.values()[random.nextInt(2) + 1];
        column = 7;
        row = 0;
        lastChange = 0;
    }

    public static Square emptySquare(int row, int column) {
        Square square = new Square();
        for(int i=0;i<2;i++){
            for(int j=0;j<2;j++){
                square.cells[i][j] = Cell.EMPTY;
            }
        }
        square.row = row;
        square.column = column;
        return  square;
    }

    public void rotateRight(){
        if(System.currentTimeMillis()-lastChange > 100){
            Cell tmp = cells[0][0];
            cells[0][0] = cells[1][0];
            cells[1][0] = cells[1][1];
            cells[1][1] = cells[0][1];
            cells[0][1] = tmp;
        }
        lastChange = System.currentTimeMillis();
    }
    public void rotateLeft(){
        if(System.currentTimeMillis()-lastChange > 100){
            Cell tmp = cells[0][0];
            cells[0][0] = cells[0][1];
            cells[0][1] = cells[1][1];
            cells[1][1] = cells[1][0];
            cells[1][0] = tmp;
        }
        lastChange = System.currentTimeMillis();
    }
    public void moveRight(){
        if(column<16 ) column++;
    }
    public void moveLeft(){
        if(column>0 ) column--;
    }
    public boolean moveDown(){
        if(row<10 ){
            row++;
            return true;
        }
        return false;
    }
    public Cell[][] getCells() {
        return cells;
    }
    public int getColumn() {
        return column;
    }
    public int getRow() {
        return row;
    }
}
