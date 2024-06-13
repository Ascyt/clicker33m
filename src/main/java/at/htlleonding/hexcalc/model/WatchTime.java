package at.htlleonding.hexcalc.model;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Timer;
import java.util.TimerTask;

public class WatchTime {
    //TODO: Singleton (lazy initialization) goes here.

    private Timer timer;

    private SimpleIntegerProperty hours;
    private SimpleIntegerProperty minutes;
    private SimpleStringProperty dayOfWeekShort;
    private SimpleIntegerProperty tickDelay;
    private DayOfWeek dayOfWeek;

    //TODO: Getters go here.

    private WatchTime() {
        //TODO: Initialize properties etc.

        this.timer = new Timer(true);
        this.tickTock();
    }

    private void tickTock() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    //TODO: Logic for each tick goes here... update hours, minutes, day of week accordingly.

                    //DO NOT remove/change the following lines!
                    tickTock();
                });
            }
        }, tickDelay.get());
    }

    public void setTime(int hours, int minutes, DayOfWeek dayOfWeek) {
        //TODO: Allow setting the time and day from outside.
    }
}
