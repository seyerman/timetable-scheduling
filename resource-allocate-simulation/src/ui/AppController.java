package ui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Day;
import model.LogMessages;
import model.Resource;
import model.ResourceAllocateSimulation;
import model.Schedule;
import model.Session;

public class AppController {
	
	public static final String EXT_REQ = ".csv";
	public static final String EXT_EXC = "_ALLOCATION_SIMULATED.csv";
	public static final String EXT_DIR = "_output";

	public void process(String[] args) throws IOException {
		LogMessages.print(Arrays.toString(args));
		if(args.length<1) {
			LogMessages.print("Empty input filename. Please write the name of the input file.");
		}else {
			String inputfileName = args[0];
			File f = new File(inputfileName);
			
			List<String> inFileNames  = new ArrayList<>();
			List<String> outFileNames = new ArrayList<>();
			
			String dateStr = getDate();
			
			if(f.isDirectory()) {
				String outDirStr;
				if(args.length>1) {
					outDirStr = args[1];
				}else {
					outDirStr = getOutputDirectoryName(f);
				}
				File[] files = f.listFiles();
				for (int i = 0; i < files.length; i++) {
					String fileName = files[i].getName();
					if(fileName.length()>4 
						&& EXT_REQ.equals(fileName.substring(fileName.length()-EXT_REQ.length(), fileName.length()))
						&& (fileName.length()<EXT_EXC.length() || !EXT_EXC.equals(fileName.substring(fileName.length()-EXT_EXC.length(), fileName.length())))){
						
						inFileNames.add(files[i].getAbsolutePath());
						outFileNames.add(getOutputFileName(files[i], dateStr,outDirStr));		
					}
				}
				//LogMessages.print(Arrays.toString(files));
				File outputDir = new File(outDirStr);
				if(!outputDir.exists()) {
					outputDir.mkdir();
				}
			}else {
				String singleOutputFileName;
				if(args.length>1) {
					singleOutputFileName = args[1];
				}else {
					singleOutputFileName = getOutputFileName(f);
				}
				inFileNames.add(inputfileName);
				outFileNames.add(singleOutputFileName);
			}				
			for(int i=0;i<inFileNames.size();i++) {
				String inputName = inFileNames.get(i);
				String outputName = outFileNames.get(i);
				
				Schedule s = ScheduleFileReader.readSchedule(inputName, ScheduleFileReader.LINE_SEPARATOR);
				
				if(s==null) {
					LogMessages.print(inputName+" has no data");
				}else {
					ResourceAllocateSimulation ra = new ResourceAllocateSimulation(s);
					ra.simulate();
					List<Resource> resources = ra.getResources();
					
					String output = toString(resources,s.getShortAcademicUnit());
					
					PrintWriter pw = new PrintWriter(outputName);
					pw.write(output);
					pw.close();
				}
				if(progressBar!=null) {
					progressBar.setProgress((double)i/inFileNames.size());
				}
			}
			if(progressBar!=null) {
				progressBar.setProgress(1);
			}
		}
	}
	
	private String toString(List<Resource> resources,String auShortName) {
		String[] msg = new String[Day.values().length];
		for (int i = 0; i < msg.length; i++) {
			msg[i] = "";
		}
		msg[0] = "DAY;ZOOM-ID;"+auShortName;
		for(Resource res:resources) {
			Day day = null;
			for(Session s:res.getSessions()) {
				if(s.getDay().getId()!=0) {
					if(day!=s.getDay()) {
						day = s.getDay();
						
						if(!msg[day.getId()-1].equals("")) {
							msg[day.getId()-1] += "\n";
						}
						
						msg[day.getId()-1] += day.getShortName() + ScheduleFileReader.LINE_SEPARATOR + res.getId();	
					}
					msg[day.getId()-1] += ScheduleFileReader.LINE_SEPARATOR + s;
				}
			}			
		}
		return String.join("\n", msg);
	}
	
	private String getDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
		String format = formatter.format(date);
		return format;
	}
	
	private String getOutputFileName(File file) {
		String dateStr = getDate();
		String outDirStr = file.getParentFile().getAbsolutePath();
		return getOutputFileName(file, dateStr, outDirStr);
	}
	
	private String getOutputFileName(File file, String dateStr, String outDirStr) {
		String fileName = file.getName();
		
		String baseName = fileName.substring(0,fileName.length()-EXT_REQ.length());
		String outFName = outDirStr+File.separator+baseName+"_"+dateStr+EXT_EXC;
		
		return outFName;
	}
	
	private String getOutputDirectoryName(File file) {
		return file.getAbsolutePath()+EXT_DIR;
	}
	
    @FXML
    private Label labInputFile;

    @FXML
    private Label labOutputFile;
    
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label labStateBar;
    
    private Stage thisStage;   
    
    public AppController(Stage st) {
    	thisStage = st;
    }
    
    public AppController() {
    	thisStage = null;
    }
    
    public void initialize() {
    	//progressBar.setMinWidth(Double.MAX_VALUE);
    	
    	getWindow().widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				progressBar.setMinWidth(getWindow().getWidth());
				progressBar.setMaxWidth(getWindow().getWidth());
			}
		});
    }
    
    private Window getWindow() {
    	return thisStage;
    }

    @FXML
    public void chooseAInputFile(ActionEvent event) {
    	FileChooser fc = new FileChooser();
    	fc.getExtensionFilters().addAll(
     	         new FileChooser.ExtensionFilter("Separado por "+ScheduleFileReader.LINE_SEPARATOR, "*.csv")
    	         );
    	File inputFile = fc.showOpenDialog(getWindow());
    	if(inputFile!=null) {
    		labInputFile.setText(inputFile.getAbsolutePath());
    		if(labOutputFile.getText().isEmpty()) {
    			labOutputFile.setText(getOutputFileName(inputFile));
    		}
    		getWindow().sizeToScene();
    	}
    }

    @FXML
    public void chooseAInputFolder(ActionEvent event) {
    	DirectoryChooser dc = new DirectoryChooser();
    	File inputDirectory = dc.showDialog(getWindow());
    	if(inputDirectory!=null) {
    		labInputFile.setText(inputDirectory.getAbsolutePath());
    		if(labOutputFile.getText().isEmpty()) {
    			labOutputFile.setText(getOutputDirectoryName(inputDirectory));
    		}
    		getWindow().sizeToScene();
    	}
    }

    @FXML
    public void chooseAOutputFile(ActionEvent event) {
    	FileChooser fc = new FileChooser();
    	fc.getExtensionFilters().addAll(
      	         new FileChooser.ExtensionFilter("Separado por "+ScheduleFileReader.LINE_SEPARATOR, "*.csv")
      	         );
    	File outputFile = fc.showSaveDialog(getWindow());
    	if(outputFile!=null) {
    		labOutputFile.setText(outputFile.getAbsolutePath());
    		getWindow().sizeToScene();
    	}
    }

    @FXML
    public void chooseAOutputFolder(ActionEvent event) {
    	DirectoryChooser dc = new DirectoryChooser();
    	File outputDirectory = dc.showDialog(getWindow());
    	if(outputDirectory!=null) {
    		labOutputFile.setText(outputDirectory.getAbsolutePath());
    		getWindow().sizeToScene();
    	}
    }
	
    @FXML
    public void allocateResources(ActionEvent event) {
		new Thread() {
			public void run() {
		    	try {
		    		process(new String[] {labInputFile.getText(),labOutputFile.getText()});
		    		Platform.runLater(new Thread() {
		    			public void run() {
							setStatusBar("El proceso de asignación ha terminado");
		    			}
		    		});		    		
		    	}catch (IOException e) {
		    		Platform.runLater(new Thread() {
		    			public void run() {
							new Alert(Alert.AlertType.ERROR,"No fue posible leer el archivo de entrada");
		    				
		    			}
		    		});
				}
			}
		}.start();
    }
    
    public void setStatusBar(String message) {
    	labStateBar.setText(message);
    }
}
