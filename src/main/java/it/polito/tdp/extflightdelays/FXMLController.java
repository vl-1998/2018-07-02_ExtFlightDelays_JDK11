package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.ConnectedAirport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare ai branch master_turnoB o master_turnoC per turno B o C

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField distanzaMinima;

    @FXML
    private Button btnAnalizza;

    @FXML
    private ComboBox<Airport> cmbBoxAeroportoPartenza;

    @FXML
    private Button btnAeroportiConnessi;

    @FXML
    private TextField numeroVoliTxtInput;

    @FXML
    private Button btnCercaItinerario;

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	txtResult.clear();
    	
    	String dm = this.distanzaMinima.getText();
    	Double distanzaMinima ;
    	
    	if(dm == null) {
    		txtResult.appendText("Inserire valore distanza minima!");
    		return;
    	}
    	
    	try {
    		distanzaMinima = Double.parseDouble(dm);
    		
    	} catch (IllegalArgumentException e) {
        	txtResult.appendText("Inserire valore numerico!");
        	return;
        }
    	
    	this.model.creaGrafo(distanzaMinima);
    	this.cmbBoxAeroportoPartenza.getItems().clear();
    	this.cmbBoxAeroportoPartenza.getItems().addAll(this.model.getVertex());
    	txtResult.appendText("Grafo creato!\n#Vertici = "+this.model.getVertex().size()+" #Archi = "+this.model.getEdge().size());

    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {
    	txtResult.clear();
    	
    	Airport a = this.cmbBoxAeroportoPartenza.getValue();
    	
    	if (a == null) {
    		txtResult.appendText("Scegliere un aeroporto!");
    	}
    	
    	for(ConnectedAirport c: this.model.direttamenteConnessi(a) ) {
    		txtResult.appendText(c.toString()+"\n");
    	}

    }

    @FXML
    void doCercaItinerario(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Airport a = this.cmbBoxAeroportoPartenza.getValue();
    	
    	if (a == null) {
    		txtResult.appendText("Scegliere un aeroporto!");
    	}
    	
    	String dm = this.numeroVoliTxtInput.getText();
    	Double migliaDisponibili ;
    	
    	if(dm == null) {
    		txtResult.appendText("Inserire valore distanza minima!");
    		return;
    	}
    	
    	try {
    		migliaDisponibili = Double.parseDouble(dm);
    		
    	} catch (IllegalArgumentException e) {
        	txtResult.appendText("Inserire valore numerico!");
        	return;
        }
    	
    	for(Airport a1 : this.model.ricorsiva(migliaDisponibili, a)) {
    		txtResult.appendText(a1.toString()+"\n");
    	}
    	txtResult.appendText("\nMiglia totali percorse = "+this.model.getMiglia());
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
