
/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Risultato;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnGiocatoreMigliore"
    private Button btnGiocatoreMigliore; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMatch"
    private ComboBox<Match> cmbMatch; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Match m = this.cmbMatch.getValue();
    	if(m==null) {
    		this.txtResult.appendText("Seleziona una partita!");
    		return;
    	}
    	
    	this.model.creaGrafo(m);
    	this.txtResult.appendText("Grafo creato!\n");
    	this.txtResult.appendText("# Vertici : " + this.model.nVertici() + "\n");
    	this.txtResult.appendText("# Archi : " + this.model.nArchi() + "\n");
    	
    }

    @FXML
    void doGiocatoreMigliore(ActionEvent event) {    	
    	this.txtResult.clear();
    	
    	if(!this.model.grafoCreato()) {
    		this.txtResult.appendText("Crea prima il grafo\n");
    		return;
    	}
    	
    	Player migliore = this.model.giocatoreMigliore();
    	this.txtResult.appendText("Giocatore migliore:\n" + migliore.toString());
    	this.txtResult.appendText("\nDelta efficienza = " + this.model.deltaComplessivo(migliore));
    	
    }
    
    @FXML
    void doSimula(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	if(!this.model.grafoCreato()) {
    		this.txtResult.appendText("Crea prima il grafo\n");
    		return;
    	}
    	
    	Match m = this.cmbMatch.getValue();
    	if(m==null) {
    		this.txtResult.appendText("Seleziona una partita!");
    		return;
    	}
    	
    	int nAzioni= -1;
    	try {
    		nAzioni = Integer.parseInt(this.txtN.getText());
    		
    		this.model.simula(nAzioni, m);
    		
    		for(Risultato ris : this.model.getMappaRis().values()) {
    			if(ris.getSquadraId()==m.getTeamHomeID()) {
    				this.txtResult.appendText(m.getTeamHomeNAME() + "  " + ris.toString()+ "\n");
    			}
    			else {
    				this.txtResult.appendText(m.getTeamAwayNAME() + "  " + ris.toString()+ "\n");
    			}
    		}
    		
    	}catch(NumberFormatException ex) {
    		txtResult.appendText("Errore: il numero di azioni N deve essere un intero positivo\n");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGiocatoreMigliore != null : "fx:id=\"btnGiocatoreMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMatch != null : "fx:id=\"cmbMatch\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbMatch.getItems().clear();
    	this.cmbMatch.getItems().addAll(this.model.getAllMatches());
    }
}
