import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
    private int keyPressed;
    private long lastChange;

    Keyboard(Component component){
        component.addKeyListener(this);
        this.keyPressed = 0;
        lastChange = 0;
    }

    int getKeyPressed() {
        return keyPressed;
    }

    void reset(){
        keyPressed = 0;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(System.currentTimeMillis() - lastChange > 100){
            keyPressed = keyEvent.getKeyCode();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
