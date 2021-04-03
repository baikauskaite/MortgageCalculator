/**

 2 laboratorinis darbas
 Būsto paskolos skaičiuoklė
 @author Viktorija Baikauskaitė, 4 gr.

 */

package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main extends Application {

    private Scene mainScene, sceneReport, sceneGraph;
    private GridPane grid;
    private Text sceneTitle;
    private Label loanLabel;
    private Text loanCurrencyText;
    private HBox hbLoan;
    private Label deadlineLabel;
    private ArrayList<Integer> years;
    private ArrayList<String> months;
    private ComboBox comboBoxYear;
    private ObservableList<Integer> options;
    private ObservableList<String> optionsString;
    private ComboBox comboBoxMonth;
    private HBox hbDate;
    private Label loanTypeLabel;
    private ToggleGroup loanTypeGroup;
    private RadioButton rbAnnuity;
    private RadioButton rbLinear;
    private HBox hbLoanType;
    private Label annualPercentLabel;
    private Slider slider;
    private TextField annualPercentTextField;
    private TextField loanTextField;
    private Text annualPercentText;
    private HBox hbAnnualPercent1;
    private HBox hbAnnualPercent2;
    private Button buttonTable;
    private Button buttonGraph;
    private Button buttonDelete;
    private Button buttonReport;
    private HBox hbButton1;
    private HBox hbButton2;
    private Text empty;
    private HBox hbErrorMessage1;
    private HBox hbErrorMessage2;
    private Text errorMessage1;
    private Alert alertReport;
    private Alert alertDeleted;

    private File resultFile;
    private FileWriter myWriter;
    private PrintWriter myPrint;

    // Chart
    private NumberAxis axisX;
    private NumberAxis axisY;
    private LineChart<Number, Number> lineChart;
    private XYChart.Series interest;
    private XYChart.Series totalMonthlyPayment;
    private XYChart.Series monthlyPayment;
    private VBox vbChart;
    private Text textUnderChart;
    private Button buttonReturn;

    private ObservableList<Payment> payments;
    private TableView<Payment> tableView;
    private VBox vbTableView;
    private boolean paymentsAreCreated;

    Record customerRecord = new Record();
    double loan;
    int year, month;
    String loanType;
    double percent;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Title
        paymentsAreCreated = false;
        primaryStage.setTitle("Būsto paskolos skaičiuoklė");

        // Gridpane
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        sceneTitle = new Text("Būsto paskolos skaičiuoklė");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        // 1. Pageidaujama paskolos suma (double skaičius)
        loanLabel = new Label("Pageidaujama paskolos suma");
        grid.add(loanLabel, 0, 1);

        loanCurrencyText = new Text("€");
        loanCurrencyText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        hbLoan = new HBox();
        loanTextField = new TextField();
        hbLoan.setAlignment(Pos.CENTER);
        hbLoan.setSpacing(10);
        hbLoan.getChildren().addAll(loanTextField, loanCurrencyText);
        grid.add(hbLoan, 1, 1);

        // 2. Paskolos terminas (metai ir mėnesiai)
        deadlineLabel = new Label("Paskolos mokėjimo terminas");
        grid.add(deadlineLabel, 0, 3);
        final int currentYear = 2021;
        years = new ArrayList<Integer>();
        for (int i = 0; i < 10; ++i) {
            years.add(currentYear+i);
        }
        options = FXCollections.observableArrayList(years);
        comboBoxYear = new ComboBox(options);
        comboBoxYear.setPromptText("Metai");
        months = new ArrayList<String>();
        months.add("Sausis");
        months.add("Vasaris");
        months.add("Kovas");
        months.add("Balandis");
        months.add("Gegužė");
        months.add("Birželis");
        months.add("Liepa");
        months.add("Rugpjūtis");
        months.add("Rugsėjis");
        months.add("Spalis");
        months.add("Lapkritis");
        months.add("Gruodis");
        optionsString = FXCollections.observableArrayList(months);
        comboBoxMonth = new ComboBox(optionsString);
        comboBoxMonth.setPromptText("Mėnesis");
        hbDate = new HBox(10);
        hbDate.setAlignment(Pos.CENTER);
        hbDate.getChildren().addAll(comboBoxYear, comboBoxMonth);
        grid.add(hbDate, 1, 3);

        // 3. Paskolos grąžinimo grafikas (pasirenkame vieną): Anuiteto arba Linijinis
        loanTypeLabel = new Label("Kredito grąžinimo planas");
        grid.add(loanTypeLabel, 0, 4);
        loanTypeGroup = new ToggleGroup();
        rbAnnuity = new RadioButton("Anuiteto");
        rbAnnuity.setToggleGroup(loanTypeGroup);
        rbLinear = new RadioButton("Linijinis");
        rbLinear.setToggleGroup(loanTypeGroup);
        hbLoanType = new HBox();
        hbLoanType.setAlignment(Pos.CENTER);
        hbLoanType.getChildren().addAll(rbAnnuity, rbLinear);
        hbLoanType.setSpacing(10);
        grid.add(hbLoanType, 1, 4);

        // 4. Metinis procentas
        annualPercentLabel = new Label("Metinis procentas");
        slider = new Slider();
        slider.setMin(0);
        slider.setMax(50);
        slider.setValue(5);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(25);
        slider.setMinorTickCount(5);
        annualPercentTextField = new TextField(Double.toString(slider.getValue()));
        annualPercentTextField.setPrefWidth(50);
        annualPercentText = new Text("%");
        annualPercentText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        hbAnnualPercent1 = new HBox();
        hbAnnualPercent1.getChildren().add(annualPercentLabel);
        hbAnnualPercent1.setAlignment(Pos.CENTER_LEFT);
        hbAnnualPercent2 = new HBox();
        hbAnnualPercent2.getChildren().addAll(slider, annualPercentTextField, annualPercentText);
        hbAnnualPercent2.setAlignment(Pos.CENTER_RIGHT);
        hbAnnualPercent2.setSpacing(10);
        grid.add(hbAnnualPercent2, 1, 5);
        grid.add(annualPercentLabel, 0, 5);

        slider.valueProperty().addListener(
                new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number > observable, Number oldValue, Number newValue) {
                        newValue = newValue.intValue();
                        slider.setValue(newValue.intValue());
                        annualPercentTextField.setText(newValue.toString());
                    }
                });

        // Visible gridlines
        //grid.setGridLinesVisible(true);

        // Alerts
        alertDeleted = new Alert(Alert.AlertType.INFORMATION);
        alertDeleted.setTitle("Būsto paskolos skaičiuoklė");
        alertDeleted.setHeaderText("Sėkmingai išvalėte laukus");
        alertReport = new Alert(Alert.AlertType.INFORMATION);
        alertReport.setTitle("Būsto paskolos skaičiuoklė");
        alertReport.setHeaderText("Sėkmingai sukūrėtė ataskaitą");

        // Button
        buttonTable = new Button("Lentelė");
        buttonGraph = new Button("Grafikas");
        buttonDelete = new Button("Išvalyti");
        buttonReport = new Button("Ataskaita");
        buttonReturn = new Button("Grįžti");
        hbButton1 = new HBox(20);
        hbButton2 = new HBox(20);
        hbButton1.setAlignment(Pos.BOTTOM_LEFT);
        hbButton2.setAlignment(Pos.BOTTOM_RIGHT);
        hbButton1.getChildren().addAll(buttonDelete, buttonReport);
        hbButton2.getChildren().addAll(buttonTable, buttonGraph);
        grid.add(hbButton1, 0, 9);
        grid.add(hbButton2, 1, 9);

        empty = new Text(" ");
        empty.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
        hbErrorMessage1 = new HBox(10);
        hbErrorMessage2 = new HBox(10);
        errorMessage1 = new Text("Laukeliuose reikia įvesti skaičius");
        errorMessage1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
        errorMessage1.setFill(Color.RED);
        hbErrorMessage1.setAlignment(Pos.TOP_RIGHT);
        hbErrorMessage2.setAlignment(Pos.TOP_RIGHT);
        hbErrorMessage1.getChildren().add(empty);
        hbErrorMessage2.getChildren().add(empty);
        grid.add(hbErrorMessage1, 1, 2);
        grid.add(hbErrorMessage2, 1, 6);

        // Button actions
        buttonTable.setOnAction(e -> {
            if (isDouble(loanTextField, loanTextField.getText()) &&
                    isDouble(annualPercentTextField, annualPercentTextField.getText())) {
                if (hbErrorMessage2.getChildren().contains(errorMessage1)) {
                    grid.getChildren().remove(hbErrorMessage2);
                    hbErrorMessage2.getChildren().remove(errorMessage1);
                    hbErrorMessage2.getChildren().add(empty);
                    grid.add(hbErrorMessage2, 1, 6);
                }
                loan = Double.parseDouble(loanTextField.getText());
                customerRecord.setLoan(loan);
                year = (int) comboBoxYear.getSelectionModel().getSelectedItem();
                month = comboBoxMonth.getSelectionModel().getSelectedIndex();
                customerRecord.setDate(year, month);
                percent = Double.parseDouble(annualPercentTextField.getText());
                customerRecord.setAnnualPercent(percent);
                loanType = rbAnnuity.isSelected() ? "Anuiteto" : "Linijinis";
                customerRecord.setLoanType(loanType);

                if (!paymentsAreCreated) {
                    createPayments();
                }
                createTable();
                sceneReport = new Scene(vbTableView, 1000, 600);
                primaryStage.setScene(sceneReport);
            }
            else {
                if (!isDouble(annualPercentTextField, annualPercentTextField.getText()) ||
                        !isDouble(loanTextField, loanTextField.getText())) {
                    if (hbErrorMessage2.getChildren().contains(empty)) {
                        grid.getChildren().remove(hbErrorMessage2);
                        hbErrorMessage2.getChildren().remove(empty);
                        hbErrorMessage2.getChildren().add(errorMessage1);
                        grid.add(hbErrorMessage2, 1, 6);
                    }
                }
            }
        });

        buttonGraph.setOnAction(e -> {
            if (isDouble(loanTextField, loanTextField.getText()) &&
                    isDouble(annualPercentTextField, annualPercentTextField.getText())) {
                if (hbErrorMessage2.getChildren().contains(errorMessage1)) {
                    grid.getChildren().remove(hbErrorMessage2);
                    hbErrorMessage2.getChildren().remove(errorMessage1);
                    hbErrorMessage2.getChildren().add(empty);
                    grid.add(hbErrorMessage2, 1, 6);
                }
                loan = Double.parseDouble(loanTextField.getText());
                customerRecord.setLoan(loan);
                year = (int) comboBoxYear.getSelectionModel().getSelectedItem();
                month = comboBoxMonth.getSelectionModel().getSelectedIndex();
                customerRecord.setDate(year, month);
                percent = Double.parseDouble(annualPercentTextField.getText());
                customerRecord.setAnnualPercent(percent);
                loanType = rbAnnuity.isSelected() ? "Anuiteto" : "Linijinis";
                customerRecord.setLoanType(loanType);

                if (!paymentsAreCreated) {
                    createPayments();
                }
                createGraph(payments);
                sceneGraph = new Scene(vbChart, 1000, 600);
                primaryStage.setScene(sceneGraph);
            }
            else {
                if (!isDouble(annualPercentTextField, annualPercentTextField.getText()) ||
                        !isDouble(loanTextField, loanTextField.getText())) {
                    if (hbErrorMessage2.getChildren().contains(empty)) {
                        grid.getChildren().remove(hbErrorMessage2);
                        hbErrorMessage2.getChildren().remove(empty);
                        hbErrorMessage2.getChildren().add(errorMessage1);
                        grid.add(hbErrorMessage2, 1, 6);
                    }
                }
            }

        });

        buttonDelete.setOnAction( e -> {
            annualPercentTextField.setText(String.valueOf(5.0));
            loanTextField.setText("");
            loanTypeGroup.selectToggle(null);
            Payment.setNumberOfPayments(0);
            paymentsAreCreated = false;
            payments = null;
            alertDeleted.showAndWait();
        });

        buttonReturn.setOnAction( e -> {
            primaryStage.setScene(mainScene);
        });

        buttonReport.setOnAction( e -> {
            if (isDouble(loanTextField, loanTextField.getText()) &&
                    isDouble(annualPercentTextField, annualPercentTextField.getText())) {
                if (hbErrorMessage2.getChildren().contains(errorMessage1)) {
                    grid.getChildren().remove(hbErrorMessage2);
                    hbErrorMessage2.getChildren().remove(errorMessage1);
                    hbErrorMessage2.getChildren().add(empty);
                    grid.add(hbErrorMessage2, 1, 6);
                }
                loan = Double.parseDouble(loanTextField.getText());
                customerRecord.setLoan(loan);
                year = (int) comboBoxYear.getSelectionModel().getSelectedItem();
                month = comboBoxMonth.getSelectionModel().getSelectedIndex();
                customerRecord.setDate(year, month);
                percent = Double.parseDouble(annualPercentTextField.getText());
                customerRecord.setAnnualPercent(percent);
                loanType = rbAnnuity.isSelected() ? "Anuiteto" : "Linijinis";
                customerRecord.setLoanType(loanType);

                if (!paymentsAreCreated) {
                    createPayments();
                }
                createFile(payments);
                alertReport.setHeaderText("Sėkmingai sukūrėtė ataskaitą: " + resultFile.getName());
                alertReport.showAndWait();
            }
            else {
                if (!isDouble(annualPercentTextField, annualPercentTextField.getText()) ||
                        !isDouble(loanTextField, loanTextField.getText())) {
                    if (hbErrorMessage2.getChildren().contains(empty)) {
                        grid.getChildren().remove(hbErrorMessage2);
                        hbErrorMessage2.getChildren().remove(empty);
                        hbErrorMessage2.getChildren().add(errorMessage1);
                        grid.add(hbErrorMessage2, 1, 6);
                    }
                }
            }
        });

        // Setting the scene
        mainScene = new Scene(grid, 1000, 600); // sets scene in a given pixel size
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public void createFile(ObservableList<Payment> payments) {
        resultFile = new File("result_"+ loan + "_" + percent +".txt");

        try {
            myWriter = new FileWriter(resultFile);
            myPrint = new PrintWriter(myWriter);
            myPrint.print("Paskolos dydis: " + loan + "\nMėnesiai: " + payments.get(0).getDurationMonths() +
                    "\nKredito grąžinimo planas: " + loanType + "\nMetinis procentas: " + percent + "%\n\n");
            myPrint.printf("%15s\t%s\t%10s\t%s\t%20s\t%s\t%10s\t%s\t%10s\n\n", "Mokėjimo nr.", "|", "Įmoka", "|",
                    "Paskolos grąžinimas", "|", "Palūkanos", "|", "Likutis");
            payments.forEach( payment -> {
                myPrint.printf("%15d\t%s\t%10s\t%s\t%20s\t%s\t%10s\t%s\t%10s\n", payment.getPaymentNumber(), "|",
                        payment.getTotalMonthlyPayment(), "|", payment.getMonthlyPayment(), "|",
                        payment.getMonthlyInterest(), "|", payment.getThisPaymentLeft());

            });
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        tableView = new TableView<>();
        tableView.setItems(payments);
        tableView.setEditable(true);

        TableColumn<Payment, Integer> numberColumn = new TableColumn<>("Mokėjimo nr.");
        numberColumn.setMinWidth(100);
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("paymentNumber"));

        TableColumn<Payment, String> paymentColumn = new TableColumn<>("Įmoka");
        paymentColumn.setMinWidth(200);
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("totalMonthlyPayment"));

        TableColumn<Payment, String> loanColumn = new TableColumn<>("Paskolos grąžinimas");
        loanColumn.setMinWidth(200);
        loanColumn.setCellValueFactory(new PropertyValueFactory<>("monthlyPayment"));

        TableColumn<Payment, String> interestColumn = new TableColumn<>("Palūkanos");
        interestColumn.setMinWidth(200);
        interestColumn.setCellValueFactory(new PropertyValueFactory<>("monthlyInterest"));

        TableColumn<Payment, String> remainderColumn = new TableColumn<>("Likutis");
        remainderColumn.setMinWidth(200);
        remainderColumn.setCellValueFactory(new PropertyValueFactory<>("thisPaymentLeft"));

        tableView.getColumns().addAll(numberColumn, paymentColumn, loanColumn, interestColumn, remainderColumn);

        textUnderChart = new Text("Paskolos dydis" +
                ": " + loan + "\nMėnesiai: " + payments.get(0).getDurationMonths() +
                "\nKredito grąžinimo planas: " + loanType + "\nMetinis procentas: " + percent + "%");

        vbTableView = new VBox();
        vbTableView.setAlignment(Pos.CENTER_RIGHT);
        vbTableView.setPadding(new Insets(30, 100, 30, 100));
        vbTableView.setSpacing(30);
        vbTableView.getChildren().addAll(tableView, textUnderChart, buttonReturn);
    }

    public void createGraph(ObservableList<Payment> payments) {
        axisX = new NumberAxis();
        axisX.setLabel("Mėnesis");
        axisX.setAutoRanging(false);
        axisY = new NumberAxis();
        axisX.setLowerBound(1);
        axisX.setUpperBound(Payment.getNumberOfPayments());
        lineChart = new LineChart<Number, Number>(axisX, axisY);
        interest = new XYChart.Series();
        interest.setName("Palūkanos");
        totalMonthlyPayment = new XYChart.Series();
        totalMonthlyPayment.setName("Įmoka");
        monthlyPayment = new XYChart.Series();
        monthlyPayment.setName("Paskolos grąžinimas");
        payments.forEach(payment -> {
            interest.getData().add(new XYChart.Data(payment.getPaymentNumber(), payment.getMonthlyInterestNumber()));
            totalMonthlyPayment.getData().add(new XYChart.Data(payment.getPaymentNumber(), payment.getTotalMonthlyPaymentNumber()));
            monthlyPayment.getData().add(new XYChart.Data(payment.getPaymentNumber(), payment.getMonthlyPaymentNumber()));
        });

        lineChart.getData().addAll(interest, totalMonthlyPayment, monthlyPayment);

        textUnderChart = new Text("Paskolos dydis: " + loan + " €\nMėnesiai: " + payments.get(0).getDurationMonths() +
                "\nKredito grąžinimo planas: " + loanType + "\nMetinis procentas: " + percent + "%");

        vbChart = new VBox();
        vbChart.setAlignment(Pos.CENTER_RIGHT);
        vbChart.setPadding(new Insets(30, 100, 30, 100));
        vbChart.setSpacing(30);
        vbChart.getChildren().addAll(lineChart, textUnderChart, buttonReturn);

    }

    public void createPayments() {
        this.payments = FXCollections.observableArrayList();
        this.payments.add(new Payment(loan, year, month, loanType, percent));
        int numberOfPayments = this.payments.get(0).countDurationMonths();
        for (int i = 1; i < numberOfPayments; ++i) {
            this.payments.add(new Payment(loan, year, month, loanType, percent));
        }
        paymentsAreCreated = true;
    }

    public boolean isDouble(TextField field, String message) {
        try {
            double loan = Double.parseDouble(message);
            return true;
        }
        catch (NumberFormatException e) {
            field.setText("");
            return false;
        }
    }

}
