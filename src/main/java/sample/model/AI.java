package sample.model;

public class AI extends Player {
    private static AI ai;

    private AI() {
    }

    public static AI getInstance() {
        if (ai == null)
            ai = new AI();
        return ai;
    }
}
