public class Board {
    /*board of 12 x 16 cells
    * rows 0,1 are where square appears */
    private Cell[][] board = new Cell[12][16];
    Board(){
        for(int i = 0;i < 12; i++)
            for (int j = 0; j < 16; j++) board[i][j] = Cell.EMPTY;
    }

    boolean canMove(int row, int column){
        return row>=0 && row<12 && column>=0 && column<16 && board[row][column] == Cell.EMPTY;
    }
    boolean isEmpty(){
        for(int i=2;i<12;i++){
            for(int j=0;j<16;j++){
                if(board[i][j]!=Cell.EMPTY) return false;
            }
        }
        return true;
    }

    void downSquare(Square currentSquare) {
        /* move square as low as possible, you can divide square in half
        * */
        int row = currentSquare.getRow()+2;
        int column = currentSquare.getColumn();
        while(canMove(row,column)) row++;
        board[row-1][column] = currentSquare.getCells()[1][0];
        board[row-2][column] = currentSquare.getCells()[0][0];

        row = currentSquare.getRow()+2;
        column = currentSquare.getColumn()+1;
        while(canMove(row,column)) row++;
        board[row-1][column] = currentSquare.getCells()[1][1];
        board[row-2][column] = currentSquare.getCells()[0][1];
    }

    Cell getCell(int row, int column){
        return board[row][column];
    }
    void setCell(int row, int column, Cell cell){
        board[row][column] = cell;
    }
}
