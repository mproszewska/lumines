class Line {
    // vertical that clears cells
    private int x;
    private int speed;
    Line(){
        x = 0;
        speed = 5;
    }
    void move(){
        x += speed;
    }
    void reset(){
        x = 0;
    }
    int getX(){
        return x;
    }
}
