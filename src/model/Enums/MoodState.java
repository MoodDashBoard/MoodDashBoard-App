package model.Enums;

public enum MoodState {
    DEPRIMIDO(1),
    TRISTE(2),
    NEUTRAL(3),
    CONTENTO(4),
    EUFORICO(5);

    private final int value;

    MoodState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}