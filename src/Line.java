public class Line {
    // vertical that clears cells
    private int x;
    private int speed;
    public Line(){
        x = 0;
        speed = 5;
    }
    public void move(){
        x += speed;
    }
    public void reset(){
        x = 0;
    }
    public int getX(){
        return x;
    }
}
