module at.htlleonding.watch {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens at.htlleonding.hexcalc to javafx.fxml;
    exports at.htlleonding.hexcalc;
    exports at.htlleonding.hexcalc.model;
    opens at.htlleonding.hexcalc.model to javafx.fxml;
    exports at.htlleonding.hexcalc.controller;
    opens at.htlleonding.hexcalc.controller to javafx.fxml;
}
