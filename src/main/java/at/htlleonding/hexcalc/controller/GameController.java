package at.htlleonding.hexcalc.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class GameController {
    public SaveData data = new SaveData();

    @FXML
    private Label pointsLabel;

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;
    @FXML
    private Label statusLabel;

    @FXML
    private Label upgradeButtonCost;

    @FXML
    private Label downgradeButtonCost;

    @FXML
    private Button clickButton;

    @FXML
    private ListView<SaveData> savesView;
    private ObservableList<SaveData> saves;

    public int pointsPerClick = 1;
    public int upgradeCost;
    public int downgradeCost;

    @FXML
    private void initialize() {
        progressBar.progressProperty().bind(Bindings.createDoubleBinding(
            () -> getProgress(data.points),
            pointsLabel.textProperty()
        ));
        progressLabel.textProperty().bind(Bindings.createStringBinding(
                () -> getPercentage(data.points),
                progressBar.progressProperty()
        ));
        statusLabel.textProperty().bind(Bindings.createStringBinding(
                () -> data.points == 33000000 ? "You win!" : (data.points < 33000000 ? "Too low!" : "Too high!"),
                pointsLabel.textProperty()
        ));

        saves = FXCollections.observableArrayList();
        saves.addAll(Database.getAllSaves());

        if (saves.isEmpty()) {
            newSave();
        }
        else {
            setData(saves.get(0));
        }

        savesView.setItems(saves);
        savesView.setOnMouseClicked(this::onListViewClick);

        updateTextLabel();
        updateButtonText();
        updateUpgrade();
        updateDowngrade();
    }

    public static String getPercentage(double points) {
        return Math.round(getProgress(points) * 100 * 1000) / 1000.0 + "%";
    }

    private static double getProgress(double points) {
        if (points == 0)
            return 0;

        final double target = 33000000;

        if (points > target)
            return 1 - getProgress(points / target);

        if (points < 0)
            return 0;

        return Math.log(points) / Math.log(target);
    }

    private void onListViewClick(MouseEvent event) {
        saveData();
        setData(savesView.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void newSave() {
        SaveData newSave = new SaveData();
        newSave.id = Database.addSave(newSave);

        saves.add(newSave);

        setData(newSave);
    }
    @FXML
    private void deleteSave() {
        if (saves.size() <= 1)
            return;

        Database.deleteSave(data.id);

        saves.remove(data);

        if (!saves.isEmpty())
            setData(saves.get(saves.size() - 1));
    }

    private void setData(SaveData data) {
        this.data = data;

        updateTextLabel();
        updateButtonText();
        updateUpgrade();
        updateDowngrade();
    }
    private void saveData() {
        Database.updateSave(data);
        savesView.refresh();
    }

    private void updateTextLabel() {
        pointsLabel.setText(data.points + " Point" + (Math.abs(data.points) == 1 ? "": "s"));
    }
    private void updateButtonText() {
        clickButton.setText((pointsPerClick >= 0) ? ("+" + pointsPerClick) : ("-" + -pointsPerClick));
    }
    private void updateUpgradeCostText() {
        upgradeButtonCost.setText("Cost: " + upgradeCost);
    }
    private void updateDowngradeCostText() {
        downgradeButtonCost.setText("Cost: " + downgradeCost);
    }

    @FXML
    private void onClick() {
        if (data.hasWon)
            return;

        data.points += pointsPerClick;
        updateTextLabel();

        if (data.points == 33000000) {
            data.hasWon = true;
        }

        saveData();
    }

    @FXML
    private void onUpgrade() {
        if (data.hasWon)
            return;

        data.points -= upgradeCost;
        updateUpgrade();
        updateDowngrade();

        updateTextLabel();

        data.upgradeAmount++;
        pointsPerClick = getPointsPerClick(data.upgradeAmount);

        updateButtonText();
    }
    private void updateUpgrade() {
        upgradeCost = getUpgradeCost(data.upgradeAmount, false);
        updateUpgradeCostText();
        saveData();
    }

    @FXML
    private void onDowngrade() {
        if (data.hasWon)
            return;

        data.points -= downgradeCost;
        updateUpgrade();
        updateDowngrade();

        updateTextLabel();

        data.upgradeAmount--;
        pointsPerClick = getPointsPerClick(data.upgradeAmount);

        updateButtonText();
    }
    private void updateDowngrade() {
        downgradeCost = getUpgradeCost(data.upgradeAmount, true);
        updateDowngradeCostText();
        saveData();
    }

    private static int getPointsPerClick(int upgradeAmount) {
        return (upgradeAmount < 0) ?
                -getPointsPerClick(-upgradeAmount) :
                ((int)Math.sqrt(Math.exp(upgradeAmount) + upgradeAmount + 1));
    }

    private static int getUpgradeCost(int upgradeAmount, boolean isDowngrade) {
        if (upgradeAmount < 0 && !isDowngrade)
            return 0;

        return (isDowngrade) ?
                -getUpgradeCost(-upgradeAmount, false) :
                ((int)Math.sqrt(Math.exp(upgradeAmount) * upgradeAmount * 2) + 1);
    }
}