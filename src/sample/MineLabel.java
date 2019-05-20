package sample;

import javafx.scene.control.Label;

public class MineLabel extends Label {

    private int circleIndex;

    public MineLabel(String value){
        super(value);
    }

    public MineLabel(){
        super();
    }

    public int getCircleIndex() {
        return circleIndex;
    }

    public void setCircleIndex(int circleIndex) {
        this.circleIndex = circleIndex;
    }
}
