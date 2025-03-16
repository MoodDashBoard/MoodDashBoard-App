package model.Enums;

import javafx.scene.paint.Color;

public enum MoodState {
    DEPRIMIDO(1, Color.rgb(28, 44, 64)),
    TRISTE(2, Color.rgb(166, 179, 193)),
    NEUTRAL(3, Color.rgb(224, 224, 224)),
    CONTENTO(4, Color.rgb(39, 174, 96)),
    EUFORICO(5, Color.rgb(243, 156, 18));

    private final int value;
    private final Color color;

    MoodState(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}