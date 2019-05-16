package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MineCircle extends Circle {

    private int index;

    public MineCircle(int radius, Color color){
        super(radius,color);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
