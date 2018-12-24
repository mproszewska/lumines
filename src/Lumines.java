import javax.security.auth.login.AccountExpiredException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public class Lumines extends JFrame {
// main class with GUI

    private final int SQUARE_WIDTH = 20; // width of one block (in pixels)
    private final int BOARD_WIDTH = 16; // width of the board in blocks
    private final int BOARD_HEIGHT = 12; // height of the board in blocks
    private final int FRAME_RATE = 6; // frames per second

    private Keyboard keyboard = new Keyboard(this);
    private Board board; // grid 12x16 of cells
    private Square currentSquare; // block 2x2 of cells
    private Line line; // line that clears matched cells
    private long startTime; // time the new game started
    private int score = 0; // current score
    private boolean pause = false; // game paused
    private int highestScore = 0; // highest score
    private boolean start = false;
    private long ticks = 0;
    private boolean help = false;
    private boolean gameover = false;

    private JPanel titlePanel; // title
    private JPanel scorePanel; // score
    private JPanel timePanel; // time
    private JPanel highestScorePanel; // highest score
    private JPanel instructionPanel; // keys functions
    private JPanel luminesGrid; // board

    public Lumines(){

        setTitle( "Lumines" );
        setSize( 640, 600 );
        setResizable( false );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setIconImage( Toolkit.getDefaultToolkit().getImage("img/icon.jpg") );
        getContentPane().setBackground(Color.black);

        setLayout( new GridBagLayout() );
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;

        titlePanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Toolkit.getDefaultToolkit().getImage("img/logo.jpg"),170,20,this);
            }
        };

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0.2;
        constraints.weightx = 0.4;
        constraints.gridwidth = 2;
        titlePanel.setBackground(Color.black);
        titlePanel.setOpaque(true);
        titlePanel.setVisible(true);
        add(titlePanel,constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        timePanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Toolkit.getDefaultToolkit().getImage("img/time.jpg"),0,0,this);

            }
        };
        timePanel.setOpaque(true);
        timePanel.setBackground(Color.black);
        add(timePanel,constraints);

        constraints.gridy = 2;
        scorePanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Toolkit.getDefaultToolkit().getImage("img/score.jpg"),0,0,this);
            }
        };
        scorePanel.setOpaque(true);
        scorePanel.setBackground(Color.black);
        add(scorePanel, constraints);

        constraints.gridy = 3;
        highestScorePanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Toolkit.getDefaultToolkit().getImage("img/highscore.jpg"),0,0,this);
            }
        };
        highestScorePanel.setOpaque(true);
        highestScorePanel.setBackground(Color.black);
        add(highestScorePanel,constraints);

        constraints.gridy = 1;
        constraints.gridx = 0;
        constraints.weightx = 0.6;
        constraints.weighty = 0.2;
        constraints.gridheight = 3;
        luminesGrid = new JPanel();
        luminesGrid.setOpaque(true);
        luminesGrid.setVisible(true);
        add( luminesGrid, constraints);

        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        instructionPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Toolkit.getDefaultToolkit().getImage("img/help.jpg"),100,0,this);
            }
        };
        instructionPanel.setOpaque(true);
        instructionPanel.setBackground(Color.black);
        add(instructionPanel,constraints);

        newGame();
        setVisible(true);

    }

    public void tick(){
        int row = currentSquare.getRow();
        int column = currentSquare.getColumn();
        boolean newSquare = false;
        int key = keyboard.getKeyPressed();
        if(gameover && key!=0){
            gameover = false;
            pause = false;
            keyboard.reset();
            return;
        }
        switch (key){
            case KeyEvent.VK_P:
                pause = !pause;
                break;
            case KeyEvent.VK_Q:
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            case KeyEvent.VK_H:
                help = !help;
        }
        if (!start) {
            ticks++;
            if (key == KeyEvent.VK_S){
                start = true;
                keyboard.reset();
                return;
            }
            else{
                keyboard.reset();
                return;
            }
        }
        if(!pause && !help && !gameover) {
            switch (key) {
                case KeyEvent.VK_UP:
                    currentSquare.rotateRight();
                    break;
                case KeyEvent.VK_DOWN:
                    currentSquare.rotateLeft();
                    break;
                case KeyEvent.VK_LEFT:
                    if (board.canMove(row, column - 1)) currentSquare.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    if (board.canMove(row, column + 2)) currentSquare.moveRight();
                    break;
                case KeyEvent.VK_SPACE:
                    board.downSquare(currentSquare);
                    newSquare = true;
                    break;
            }
        }
        keyboard.reset();
        if(pause || help || gameover) return;
        matchSquares();
        line.move();
        if((line.getX()/20)-1==16) line.reset();
        if(line.getX()>0 && line.getX()%20==0){
            doClear((line.getX()/20)-1);
        }
        if(!newSquare && board.canMove(currentSquare.getRow()+2,currentSquare.getColumn()) && board.canMove(currentSquare.getRow()+2,currentSquare.getColumn()+1)){
            if(!currentSquare.moveDown()) newSquare = true;
        }
        else if(!newSquare){
            if(currentSquare.getRow()==0){
                pause = true;
                gameover = true;
                newGame();
            }
            else board.downSquare(currentSquare);
            newSquare = true;
        }

        if(newSquare) currentSquare = new Square();
    }

    public void matchSquares(){
        for(int i=2;i<11;i++){
            for(int j=0;j<15;j++){
                Cell cell = board.getCell(i,j);

                if(cell.equalColor(board.getCell(i,j+1)) && cell.equalColor(board.getCell(i+1,j))
                        && cell.equalColor(board.getCell(i+1,j+1))) {
                    board.setCell(i, j, cell.match());
                    board.setCell(i+1, j, cell.match());
                    board.setCell(i, j+1, cell.match());
                    board.setCell(i+1, j+1, cell.match());
                }
            }
        }
    }

    public void doClear(int column){
        List<Cell> left = new ArrayList<>();
        int matched = 0;
        for(int i=11;i>=2;i--){
            Cell cell = board.getCell(i,column);
            if(cell.isMatched()){
                matched++;
            }
            else left.add(cell);
            board.setCell(i,column,Cell.EMPTY);
        }
        for(int i = 0;i<left.size();i++){
            board.setCell(11-i,column,left.get(i));
        }
        score += matched;
        if(matched>0 && board.isEmpty()) score += 15;
        matchSquares();
    }

    private void draw(){
        //Area around the grid to make room for new pieces
        int xPlus = SQUARE_WIDTH * 2;
        int yPlus = SQUARE_WIDTH * 2;

        BufferedImage gridImage = new BufferedImage( luminesGrid.getWidth(), luminesGrid.getHeight(), BufferedImage.TYPE_INT_RGB );
        Graphics grid = gridImage.getGraphics();
        grid.setColor( Color.black );
        grid.clearRect( 0, 0, gridImage.getWidth(), gridImage.getHeight() );

        BufferedImage gridPanelImage = new BufferedImage( luminesGrid.getWidth(), luminesGrid.getHeight(), BufferedImage.TYPE_INT_RGB );
        Graphics gridPanel = gridPanelImage.getGraphics();
        gridPanel.setColor( Color.black );
        gridPanel.clearRect( 0, 0, gridPanelImage.getWidth(), gridPanelImage.getHeight() );


        addSquareToBoard(currentSquare);

        for( int i = 0; i < BOARD_HEIGHT; i++ ) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                grid.setColor(new Color(0, 0, 0));
                grid.drawRect((j * SQUARE_WIDTH), (i * SQUARE_WIDTH), SQUARE_WIDTH, SQUARE_WIDTH);
                if (board.getCell(i, j) == Cell.EMPTY) {
                    grid.setColor(new Color(0, 0, 0));
                    grid.fillRect(1 + (j * SQUARE_WIDTH), 1 + (i * SQUARE_WIDTH), SQUARE_WIDTH - 1, SQUARE_WIDTH - 1);
                } else if (board.getCell(i, j) == Cell.COLOR_ONE ) {
                    grid.setColor(new Color(255, 63, 47));
                    grid.fillRect(1 + (j * SQUARE_WIDTH), 1 + (i * SQUARE_WIDTH), SQUARE_WIDTH - 1, SQUARE_WIDTH - 1);
                }else if (board.getCell(i, j) == Cell.COLOR_ONE_MATCHED) {
                    grid.setColor(new Color(189, 36, 33));
                    grid.fillRect(1 + (j * SQUARE_WIDTH), 1 + (i * SQUARE_WIDTH), SQUARE_WIDTH - 1, SQUARE_WIDTH - 1);
                } else if (board.getCell(i, j) == Cell.COLOR_TWO ) {
                    grid.setColor(new Color(19, 112, 184));
                    grid.fillRect(1 + (j * SQUARE_WIDTH), 1 + (i * SQUARE_WIDTH), SQUARE_WIDTH - 1, SQUARE_WIDTH - 1);
                }
                else if (board.getCell(i, j) == Cell.COLOR_TWO_MATCHED) {
                    grid.setColor(new Color(13, 68, 112));
                    grid.fillRect(1 + (j * SQUARE_WIDTH), 1 + (i * SQUARE_WIDTH), SQUARE_WIDTH - 1, SQUARE_WIDTH - 1);
                }
                }
        }
        if(!start && !help && ticks%3==1){
            grid.drawImage(Toolkit.getDefaultToolkit().getImage("img/press.jpg"),10,90,null);
        }
        else if(help){
            grid.drawImage(Toolkit.getDefaultToolkit().getImage("img/use.jpg"),70,90,null);
        }
        else if(gameover){
            grid.drawImage(Toolkit.getDefaultToolkit().getImage("img/gameover.jpg"),70,90,null);
        }
        // board frame
        grid.setColor(new Color(255,255,255));
        grid.drawLine(0,40,20*16,40);
        grid.drawLine(0,12*20,16*20,12*20);
        grid.drawLine(16*20,40,16*20,240);
        grid.drawLine(0,40,0,240);
        if(line.getX()<20*16) grid.drawLine(line.getX(),40,line.getX(),240);
        if(line.getX()>20) grid.drawLine(line.getX()-20,40,line.getX()-20,240);
        addSquareToBoard(Square.emptySquare(currentSquare.getRow(),currentSquare.getColumn()));
        gridPanel.drawImage( gridImage, xPlus, yPlus, this );
        luminesGrid.getGraphics().drawImage( gridPanelImage, 0, 0, this );
    }

    private void addSquareToBoard(Square currentSquare) {
        int row = currentSquare.getRow();
        int column = currentSquare.getColumn();
        Cell[][] cells = currentSquare.getCells();
        for(int i=row;i<row+2;i++){
            for(int j=column;j<column+2;j++){
                board.setCell(i,j,cells[i-row][j-column]);
            }
        }
    }

    public void newGame(){
        if(highestScore < score) highestScore = score;
        board = new Board();
        currentSquare = new Square();
        line = new Line();
        score = 0;
        startTime = System.currentTimeMillis();
    }

    public void run() throws InterruptedException{
        while(true) {
            long start = System.currentTimeMillis();
            tick();
            draw();
            // wait if the frame was faster then expected
            long interval = System.currentTimeMillis() - start;
            if( interval < (1000 / FRAME_RATE) )
                Thread.sleep( (long)( 1000 / FRAME_RATE ) - interval );
        }
    }


    public static void main(String[] args) throws InterruptedException{
        Lumines game = new Lumines();
        game.run();
    }

    public void resume() {
        pause = false;
    }
}
