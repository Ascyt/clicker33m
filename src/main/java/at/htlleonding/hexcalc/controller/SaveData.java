package at.htlleonding.hexcalc.controller;

public class SaveData {
    public int id;
    public long points = 0;
    public int upgradeAmount = 0;
    public boolean hasWon = false;

    @Override
    public String toString() {
        return "Save " + id + " [" + GameController.getPercentage(points) + "]";
    }
}
