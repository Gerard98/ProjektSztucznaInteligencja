package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class MineLine extends Line {

    private int startIndex;
    private int endIndex;

    public MineLine(){
        super();
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
