package dice.chess.ui;

public class Settings {

    private static boolean highlightPieceMoves = true;
    private static boolean soundEffects = true;
    private static boolean challenge1 = false;
    private static boolean challenge2 = false;
    private static boolean animatesMoves = false;

    public static boolean isHighlightPieceMoves() {

        return highlightPieceMoves;
    }
    public static boolean isAddSoundEffects() {

        return soundEffects;
    }

    public static boolean isChallenge1() {

        return challenge1;
    }

    public static boolean isChallenge2() {

        return challenge2;
    }
    public static boolean isAnimateMoves() {
        return animatesMoves;
    }

    public static void setHighlightPieceMoves(boolean isHighlighted) {

        highlightPieceMoves = isHighlighted;
    }
    public static void setSoundEffects(boolean isSoundEffect) {

        soundEffects = isSoundEffect;
    }
    public static void setChallenge1(boolean isChallenge1) {

        challenge1 = isChallenge1;
    }

    public static void setChallenge2(boolean isChallenge2) {

        challenge2 = isChallenge2;
    }
    public static void setAnimatesMoves(boolean isAnimate) {

        animatesMoves = isAnimate;
    }
    
}
