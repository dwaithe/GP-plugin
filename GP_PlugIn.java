import ij.*;
import ij.ImagePlus.*;
import ij.gui.*;
import ij.measure.*;
import ij.measure.CurveFitter;
import ij.plugin.*;
import ij.plugin.ZProjector;
import ij.plugin.frame.*;
import ij.process.*;
import ij.process.ImageProcessor.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.IOException;
import java.io.File;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.*;
import java.util.*;
import java.nio.*;
import java.net.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * This plugin was written by Dominic Waithe for Erdinc Sezgin.
 * The contents are copyrighted to Dominic Waithe 2015.
 * For permission to edit or you the code please contact: dominic.waithe@imm.ox.ac.uk
 * 
 */
public class GP_PlugIn implements PlugIn, ActionListener, ChangeListener, ItemListener, ImageListener, PropertyChangeListener, WindowListener {
	/**
	 * This method gets called by ImageJ / Fiji.
	 *
	 * @param arg can be specified in plugins.config
	 * @see ij.plugin.PlugIn#run(java.lang.String)
	 */
	
	JFrame frame,dialogTab;
	JPanel superPanel, subTab,subTab1,subTab2;
	JComboBox menu1, menu2;
	JCheckBox back_checkBox,output_checkBox;
	
	JButton calculateBtn, editBtn, saveChangesBtn,cancelChangesBtn,exportFileBtn,importFileBtn;
	JSpinner noiseTol, thresholdText,wavelengthHighJS,wavelengthLowJS,weightLowJS,weightHighJS;
	ImagePlus imp;
	int nbImg =0,numSlices;
	String[] fileList ={};
	double[] nmArraySort;
	JTextField[] mainWvBoxes;

	 
	

	String item;
	String imgName;

	private static int wavelengthHigh = 1;
	private static int wavelengthLow = 2;
	private static String[] fitStrings = {  "Gamma Variate","Gaussian", "None"};
	private static byte[] channel1 =new byte[]{0, 46, 45, 44, 42, 41, 40, 39, 37, 36, 35, 33, 32, 30, 29, 27, 26, 24, 23, 21, 19, 18, 16, 14, 12, 10, 9, 7, 5, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 3, 4, 5, 6, 7, 8, 8, 9, 10, 11, 12, 13, 14, 15, 16, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 22, 22, 22, 27, 32, 37, 42, 48, 53, 58, 63, 69, 74, 79, 85, 90, 96, 101, 107, 112, 118, 123, -127, -122, -116, -111, -108, -105, -103, -100, -97, -95, -92, -90, -87, -84, -82, -79, -76, -74, -71, -68, -66, -63, -60, -58, -55, -52, -50, -46, -43, -40, -36, -34, -34, -34, -34, -34, -33, -33, -33, -33, -33, -33, -32, -32, -32, -32, -32, -31, -31, -31, -31, -31, -31, -30, -30, -30, -30, -30, -29, -29, -29, -29, -29, -28, -28, -28, -28, -28, -28, -27, -27, -27, -27, -27, -26, -26, -26, -26, -26, -26, -25, -25, -25, -25, -25, -24, -24, -24, -24, -24, -23};
	private static byte[] channel2 =new byte[]{0, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 3, 5, 7, 9, 11, 13, 15, 18, 20, 23, 26, 28, 31, 33, 36, 39, 41, 44, 47, 49, 52, 55, 58, 60, 63, 66, 69, 71, 74, 77, 80, 82, 85, 88, 91, 94, 96, 99, 102, 105, 108, 111, 114, 116, 119, 122, 125, -128, -125, -122, -119, -116, -113, -110, -107, -104, -101, -98, -95, -92, -89, -86, -83, -80, -77, -74, -71, -68, -65, -62, -58, -58, -58, -58, -58, -57, -57, -57, -57, -57, -56, -56, -56, -56, -56, -55, -55, -55, -55, -54, -54, -54, -53, -53, -53, -52, -52, -52, -51, -51, -51, -50, -50, -50, -49, -49, -49, -48, -48, -48, -47, -47, -47, -46, -46, -46, -45, -45, -45, -44, -44, -44, -43, -43, -43, -42, -42, -42, -41, -41, -41, -40, -40, -40, -39, -39, -39, -39, -39, -38, -38, -38, -38, -38, -38, -37, -37, -37, -37, -37, -37, -37, -36, -36, -36, -36, -36, -36, -35, -35, -35, -35, -35, -35, -36, -39, -42, -44, -47, -50, -53, -56, -59, -62, -65, -68, -71, -74, -78, -81, -84, -87, -90, -93, -96, -99, -102, -105, -108, -111, -115, -118, -121, -124, -127, 126, 122, 119, 116, 113, 110, 106, 103, 100, 97, 94, 90, 87, 84, 80, 77, 74, 71, 67, 64, 61, 57, 54, 51, 47, 44, 41, 37, 34};
	private static byte[] channel3 =new byte[]{0, 120, 122, 123, 124, 125, 126, 127, -128, -127, -125, -124, -123, -122, -121, -120, -119, -118, -117, -115, -114, -113, -112, -111, -110, -109, -108, -107, -105, -104, -103, -102, -101, -100, -99, -98, -96, -95, -94, -93, -92, -91, -90, -89, -88, -87, -87, -86, -85, -84, -83, -82, -81, -80, -79, -78, -77, -76, -75, -74, -73, -72, -71, -70, -69, -68, -67, -66, -65, -65, -65, -65, -64, -64, -64, -64, -64, -63, -63, -63, -63, -63, -62, -62, -62, -62, -62, -61, -61, -61, -61, -61, -60, -60, -60, -60, -60, -59, -59, -59, -59, -59, -58, -61, -64, -66, -69, -72, -75, -78, -80, -83, -86, -89, -91, -94, -97, -100, -103, -106, -108, -113, -118, -123, -127, 124, 119, 114, 109, 104, 99, 94, 89, 84, 79, 74, 69, 64, 59, 54, 49, 44, 39, 33, 28, 23, 22, 22, 22, 22, 22, 22, 22, 22, 22, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16};
	
	public void reloadList(){
		int selectA=menu1.getSelectedIndex();
		menu1.removeAllItems();
		nbImg=0;
        
        
        
        if (WindowManager.getImageCount()!=0){
            int[] IDList=WindowManager.getIDList();
            for (int i=0;i<IDList.length;i++){
                ImagePlus currImg=WindowManager.getImage(IDList[i]);
                if (currImg.getBitDepth()!=24 && currImg.getBitDepth()!=32 && currImg.getTitle() != "mask"){
                    nbImg++;
                    item = currImg.getTitle();
                    menu1.addItem(item);
                    
                }
            }
        }
        }

	
    public void getParam(){
    	
    	try{
    	//Find the selected item in the menu.
    	imgName = menu1.getSelectedItem().toString();
    	//Find the image in the manager.
    	imp = WindowManager.getImage(imgName);
    	//Get the title of the image.
    	String name=imp.getTitle();
    	//Find the stack
		ImageStack stack = imp.getStack();
		
		//Find the number of channels or slices. Depending which is larger
		if(imp.getNChannels() > imp.getNSlices()){
		numSlices = imp.getNChannels();}else{
		numSlices = imp.getNSlices();}

		//Define loccal array to hold wavelengths.
    	double[] nmArray = new double[numSlices];

    	//Define the array with integers from the image-stack.
		for (int i=1;i<numSlices+1;i++){
			
			String stackLabel = "0.0";
			stackLabel = stack.getSliceLabel(i);
			double metaLabel =0.0;
			
			
			try{
			//First try and get the wavelengths from the labels in the image.
			
			double num =  Double.parseDouble(stackLabel);
			
			
			if (num > 0.0){
			nmArray[i-1] = Double.parseDouble(stackLabel);}else{
			
			//If not in label. Try and read filetype
			String namekey = "Dataset name";
			String filepath = imp.getStringProperty(namekey);
			System.out.println(filepath);
			String filetype ="";
			if (filepath.contains(".")) {
			String[]parts = filepath.split("\\.");
			filetype = parts[parts.length-1];
			}
			
			//If file-type is lsm
			if(filetype =="lsm"){
				String key = null;
			if (i<10){key ="ChannelName #0"+i;}else{key ="ChannelName #"+i;}
			try{
			metaLabel =0.0;
			metaLabel = imp.getNumericProperty(key);
			nmArray[i-1] = metaLabel;
			//System.out.println(metaLabel);
			//If that fails just label as 0.0;
			}catch (NumberFormatException e) {}}
			if(filetype =="czi"){
			String key2 = null;
			if (i<10){key2 ="Information|Image|Channel|EmissionWavelength #0"+i;}else{key2 ="Information|Image|Channel|EmissionWavelength #"+i;}
			try{
			metaLabel =0.0;
			metaLabel = imp.getNumericProperty(key2);
			metaLabel = Math.round(metaLabel*100.0)/100.0;
			nmArray[i-1] = metaLabel;
			//System.out.println(metaLabel);
			//If that fails just label as 0.0;
			}catch (NumberFormatException e4) {}}
			
			}
			}catch (Exception v) {
			//If not in label. Try and read filetype
			String namekey = "Dataset name";
			String filepath = imp.getStringProperty(namekey);
			System.out.println(filepath);
			String filetype ="";
			if (filepath.contains(".")) {
			String[]parts = filepath.split("\\.");
			filetype = parts[parts.length-1];
			}
			System.out.println(filetype);
			//If file-type is lsm
			if(filetype.equals("lsm")){
				String key = null;
			if (i<10){key ="ChannelName #0"+i;}else{key ="ChannelName #"+i;}
			try{
			metaLabel =0.0;
			metaLabel = imp.getNumericProperty(key);
			nmArray[i-1] = metaLabel;
			System.out.println("here");
			System.out.println(metaLabel);
			//If that fails just label as 0.0;
			}catch (NumberFormatException e) {}}
			if(filetype.equals("czi")){
			String key2 = null;
			if (i<10){key2 ="Information|Image|Channel|EmissionWavelength #0"+i;}else{key2 ="Information|Image|Channel|EmissionWavelength #"+i;}
			try{
			metaLabel =0.0;
			metaLabel = imp.getNumericProperty(key2);
			metaLabel = Math.round(metaLabel*100.0)/100.0;
			

			
			nmArray[i-1] = metaLabel;
			//System.out.println(metaLabel);
			//If that fails just label as 0.0;
			}catch (NumberFormatException e4) {}}
			
				
			}}
		//Sort the array.
		Arrays.sort(nmArray);
		
		//Find the minimum value.
		double minArrayValue = nmArray[0];
		//Find the maximum value.
		double maxArrayValue = nmArray[nmArray.length-1];

		
		//Get the values from the spin-boxes
		double wvlnLow = (Double)wavelengthLowJS.getValue();
		double wvlnHigh =(Double)wavelengthHighJS.getValue();
		double updateLow;
		double updateHigh;

		//Evaluate whether spinbox values are within allowed range.
		if (wvlnLow > minArrayValue && wvlnLow < maxArrayValue){
			updateLow =  (Double)wavelengthLowJS.getValue();
			}else{updateLow = minArrayValue;}
			
		if (wvlnHigh < maxArrayValue && wvlnHigh > minArrayValue){
			updateHigh =  (Double)wavelengthHighJS.getValue();}else{
			updateHigh = maxArrayValue;}

		//Update the Global arrray.
		nmArraySort = nmArray;
		
		
		//Update spin-boxes with allowed range.
		SpinnerNumberModel SNMHigh = new SpinnerNumberModel(updateHigh,minArrayValue,maxArrayValue,0.1);
		SpinnerNumberModel SNMLow = new SpinnerNumberModel(updateLow,minArrayValue,maxArrayValue,0.1);
		wavelengthHighJS.setModel(SNMHigh);
		wavelengthLowJS.setModel(SNMLow);
    	}catch (Exception e3) {
    		
    		}
    	
    	}


	public void GUI(){
		/**Loads the main GUI interface **/
		frame = new JFrame("GP Calculator v 2.0");
		superPanel = new JPanel(new GridBagLayout());
		superPanel.setSize(400,585);
		frame.add(superPanel);
		frame.setSize(400,585);
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		
		c.gridx = 1;c.gridy = 1;
		menu1= new JComboBox(fileList);
		menu1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
		getParam();
		}});  
		
		
		superPanel.add(new JLabel("input Stack: "),c);
		c.gridx = 2;c.gridy = 1;
		
		superPanel.add(menu1,c);
		menu2=new JComboBox(fitStrings);
		
		c.gridx = 1;c.gridy = 2;
		superPanel.add(new JLabel("Model to fit: "),c);
		
		c.gridx = 2;c.gridy = 2;
		superPanel.add(menu2,c);
		
		
		
		c.gridx = 2;c.gridy = 3;
		editBtn = new JButton("Edit wavelengths");
		editBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){setupDialog();}});      
		
		superPanel.add(editBtn,c);
		c.gridx =1;c.gridy =4;
		
		superPanel.add(new JLabel("sample wavelength low"),c);
		c.gridx =2;
		
		wavelengthLowJS = new  JSpinner(new SpinnerNumberModel(1.0,0.0,1.0,1.0));
		superPanel.add(wavelengthLowJS,c);
		c.gridx =1;c.gridy =5;
		
		superPanel.add(new JLabel("sample wavelength high"),c);
		c.gridx =2;
		
		wavelengthHighJS = new JSpinner(new SpinnerNumberModel(1.0,0.0,1.0,1.0));
		superPanel.add(wavelengthHighJS,c);
		c.gridx =1;c.gridy =6;
		
		superPanel.add(new JLabel("threshold (0-255)"),c);
		c.gridx =2;
		thresholdText = new JSpinner(new SpinnerNumberModel(41,0,255,1));
		superPanel.add(thresholdText,c);
		c.gridx =1;c.gridy =7;
		
		superPanel.add(new JLabel("Noise tolerance (0-1.0)"),c);
		c.gridx =2;
		
		noiseTol = new JSpinner(new SpinnerNumberModel(0.8,0.0,1.0,0.05));
		superPanel.add(noiseTol,c);
		
		calculateBtn = new JButton("calculate");
		calculateBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){processImg();}});      
		
		c.gridx = 2;c.gridy = 8;
		superPanel.add(calculateBtn,c);
		
		c.gridx =1;c.gridy =10;
		superPanel.add(new JLabel("Optional:"),c);
		c.gridx = 1;c.gridy = 11;
		superPanel.add(new JLabel("Remove Background "),c);
		
		c.gridx = 2;
		back_checkBox = new JCheckBox();
		superPanel.add(back_checkBox,c);
		
		c.gridx =1;c.gridy =12;
		superPanel.add(new JLabel("Supplementary Images"),c);
		c.gridx =2;
		output_checkBox = new JCheckBox();
		superPanel.add(output_checkBox,c);

		c.gridx =1;c.gridy =13;
		superPanel.add(new JLabel("Low WL weight"),c);
		c.gridx =2;
		weightLowJS = new  JSpinner(new SpinnerNumberModel(1.00,0.01,5.00,0.01));
		superPanel.add(weightLowJS,c);
		
		
		c.gridx =1;c.gridy =14;
		superPanel.add(new JLabel("High WL weight"),c);
		c.gridx =2;
		weightHighJS = new  JSpinner(new SpinnerNumberModel(1.00,0.01,5.00,0.01));
		superPanel.add(weightHighJS,c);
	

		
		frame.setVisible(true);
		
		}
	
	@Override
	public void run(String arg) {
		
		GUI();
		reloadList();
		
		ImagePlus.addImageListener(this);
		//imgName = menu1.getSelectedItem().toString();
		getParam();
		
		
	}
	public void actionPerformed(ActionEvent e) {}
	public void imageOpened(ImagePlus imp){
		
        reloadList();
    }
    public void imageClosed(ImagePlus imp){
    	
        reloadList();
    }
    public void itemStateChanged(ItemEvent e){
       
    }
    
    
    
    public void imageUpdated(ImagePlus imp){}
    
    public void stateChanged(ChangeEvent e) {
       
    }
    
    public void propertyChange(PropertyChangeEvent e) {
       
    }
    
    public void windowActivated(WindowEvent e) {
        
    }
    
    public void windowClosed(WindowEvent e) {}
    
    public void windowClosing(WindowEvent e) {
        ImagePlus.removeImageListener(this);
       
    }
    
    public void windowDeactivated(WindowEvent e) {}
     
    public void windowDeiconified(WindowEvent e) {}
     
    public void windowIconified(WindowEvent e) {}
     
    public void windowOpened(WindowEvent e) {}
    //public class setupDialog{

	public void setupDialog(){
	dialogTab = new JFrame();
	
	dialogTab.setVisible(true);
	subTab = new JPanel();
	subTab1 = new JPanel(new GridBagLayout());
	dialogTab.setSize(400,700);
	dialogTab.add(subTab);

	subTab.add(subTab1);

	//frame = new JFrame("Channels");
		//superPanel = new JPanel(new GridBagLayout());
		//frame.add(superPanel);
		//frame.setSize(400,585);
		//GridBagConstraints c = new GridBagConstraints();
		//c.fill = GridBagConstraints.HORIZONTAL;

	

	subTab.setLayout(new BoxLayout(subTab, BoxLayout.Y_AXIS));
	subTab1.setLayout(new GridBagLayout());
	
	saveChangesBtn = new JButton("Apply to Stack");
	saveChangesBtn.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e){saveChanges();}});

	cancelChangesBtn = new JButton("Close");
	cancelChangesBtn.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e){cancelChanges();}});

	exportFileBtn = new JButton("Export to File");
	exportFileBtn.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e){exportFile();}});

	importFileBtn = new JButton("Import from File");
	importFileBtn.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e){importFile();}});

	GridBagConstraints d = new GridBagConstraints();
	d.fill = GridBagConstraints.HORIZONTAL;
	d.gridx = 0;
	d.gridy = 0;
	subTab1.add(importFileBtn,d);
	
	d.gridx=1;
	d.gridy=0;
	
	subTab1.add(exportFileBtn,d);
	d.gridx =0;d.gridy=1;
	
	subTab1.add(saveChangesBtn,d);
	d.gridx =1;
	subTab1.add(cancelChangesBtn,d);
	
	
	getParam();
	updateComboBox();
	}
	

	public void updateComboBox(){
		
		subTab2 = new JPanel();
		
		subTab2.setLayout(new GridBagLayout());
		subTab.add(subTab2);
		int ind = -1;
		JTextField[] wvBoxes = new JTextField [numSlices];
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		int numCol = numSlices;
		
		outerloop:
		for(int row=0;row<numCol;row++){
			for(int col=1;col<5;col=col+2){
			ind = ind +1;
			wvBoxes[ind] = new JTextField("",3);
			c.gridx = col;
			c.gridy = row;
			subTab2.add(new JLabel("input CH:"+Integer.toString(ind+1)),c);
			subTab2.add(wvBoxes[ind],c);
			c.gridy = row;
			c.gridx = col+1;
			subTab2.add(wvBoxes[ind],c);
			wvBoxes[ind].setText("");
			if(ind==numSlices-1){
				break outerloop;}
			}
			if(ind==numSlices-1){
				break outerloop;}
			}
		
		
		for (int i=0;i<numSlices;i++){
			
			wvBoxes[i].setText(Double.toString(nmArraySort[i]));
			}
		//Update global array.
		subTab2.setAlignmentY(Component.TOP_ALIGNMENT);
		subTab2.revalidate();
		subTab2.repaint();
		mainWvBoxes = wvBoxes;
		
		
		};
	public void saveChanges(){
		//Save changes to labelled frames.
		imgName = menu1.getSelectedItem().toString();
		ImagePlus imp = WindowManager.getImage(imgName);
		ImageStack stack = imp.getStack();
		for(int i=0;i<numSlices;i++){
			String text =  mainWvBoxes[i].getText();
			
			nmArraySort[i] = Double.parseDouble(text);
			stack.setSliceLabel(text,i+1);
			
			
			}
		
			
		
		};
	public void cancelChanges(){
		dialogTab.setVisible(false);
		
		};
	public void exportFile(){
		JFileChooser chooseFile = new JFileChooser();
		chooseFile.setSelectedFile(new File("stackLabels.txt"));
		int ret = chooseFile.showSaveDialog(null);
		String path ="/";
		
		if (ret == JFileChooser.APPROVE_OPTION){
		path = chooseFile.getSelectedFile().getAbsolutePath();
		IJ.log("path:"+path);
		}
		

		
		try {
           
            File newTextFile = new File(path);

            FileWriter fw = new FileWriter(newTextFile);
            for(int i=0;i<numSlices;i++){
			String text =  mainWvBoxes[i].getText()+"\n";
            fw.write(text);}
            fw.close();

        } catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
        }
    
		
		
		
		}
	public void importFile(){
		JFileChooser chooseFile = new JFileChooser();
		int ret = chooseFile.showOpenDialog(null);
		double[] nmArray = new double[numSlices];
		String path ="/";
		if (ret == JFileChooser.APPROVE_OPTION){
			
		path = chooseFile.getSelectedFile().getAbsolutePath();
		IJ.log("path:"+path);
		}
		try {
			
			File file = new File(path);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			int ind = 0;
			while ((line = bufferedReader.readLine()) != null) {
				
				nmArray[ind] = Double.parseDouble(line);
				ind++;
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
			fileReader.close();
			
			
			
			nmArraySort = nmArray;
			subTab2.removeAll();
			updateComboBox();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		}
		public void  processImg(){
		int selectA=menu1.getSelectedIndex();
		getParam();
		menu1.setSelectedIndex(selectA);
		String imgName1 = menu1.getSelectedItem().toString();
		ImagePlus imp2 = imp;
		for (int i =1;i<numSlices+1;i++){
		imp2.setPosition(i);
		imp2.resetDisplayRange();}
		//Create instance of the Zprojector class.
		ZProjector Zpro = new ZProjector(imp2);
		int method = 3;//SUM_METHOD
		Zpro.setMethod(method);
		Zpro.doProjection();
		//Return the projection
		ImagePlus impS = Zpro.getProjection();

		//Create an instance of the ImageConverter class. Convert to 8-bit.
		ImageConverter imC = new ImageConverter (impS);
		imC.convertToGray8();

		//Read the threshold form the user defined text-field.
		int lowerTHR = (Integer)thresholdText.getValue();

		//Apply the threshold operation.
		impS.getProcessor().threshold(lowerTHR);
		
		
		
		
		ImageStack stack = imp2.getStack();
		//stack.toHyperStack(1, numSlices, 1,"default","Color");
		
		byte[]maskCH = (byte[])impS.getProcessor().getPixels();

		float backgroundArr[] = new float[numSlices];
		
		//background calculation
		if (back_checkBox.isSelected() == true){
		for(int i =0;i<numSlices;i++){
			float c = 0;
			float bacPix = 0;
			ImageProcessor slice = stack.getProcessor(i+1);
			byte[] im = (byte[])slice.getPixels();
			
			for(int b=0;b<im.length;b++){
				
				if(maskCH[b]== 0){
					bacPix += im[b];
					c +=1;
					}}
			backgroundArr[i] = bacPix/c;
			slice.subtract(backgroundArr[i]);
			
			
			
			}}

		double wvlow = (Double)wavelengthLowJS.getValue();
		double wvhgh = (Double)wavelengthHighJS.getValue();
		float out1_pix[] ={};
		float out2_pix[] ={};
		float out_err[]  ={};
		ImageStack wave_stack = new ImageStack(stack.getWidth(),stack.getHeight());
		IJ.log("Filename: "+imgName1+" Fit: "+menu2.getSelectedItem());
		long startTime = System.currentTimeMillis();
		if(menu2.getSelectedItem() == "None"){ 
		//Conventional method.

		double curr_min_l =0;
		int glb_min_l_ind =1;
		double glb_min_l = 100000000;
		
		double curr_min_h = 0;
		int glb_min_h_ind =1;
		double glb_min_h = 100000000;
		
	
		
		for(int h = 0; h<numSlices;h++){
			
			curr_min_h = (nmArraySort[h]-wvhgh)*(nmArraySort[h]-wvhgh);
			
		
			if (curr_min_h<glb_min_h ) {
			glb_min_h = curr_min_h;
			glb_min_h_ind =h +1;
			
			}}
			
		for(int l = 0; l<numSlices;l++){
			
			curr_min_l = (nmArraySort[l]-wvlow)*(nmArraySort[l]-wvlow);
			
			if (curr_min_l<glb_min_l ){
			glb_min_l = curr_min_l;
			glb_min_l_ind = l +1;
			
			}}

		
		
		//Stack calculation.
		ImageStack temp_stack = new ImageStack(stack.getWidth(),stack.getHeight());
		ImageStack float_stack = stack.convertToFloat();

		//Find the float point values.
		ImageProcessor wvlow_temp_im = float_stack.getProcessor(glb_min_l_ind);
		ImageProcessor wvhgh_temp_im = float_stack.getProcessor(glb_min_h_ind);

		
		//Subtract background
		//wvlow_temp_im.subtract(backgroundArr[glb_min_l_ind-1]);
		//wvhgh_temp_im.subtract(backgroundArr[glb_min_h_ind-1]);

		
		//Add wv slices.
		wave_stack.addSlice("wvlow_"+nmArraySort[glb_min_l_ind-1], wvlow_temp_im);
		wave_stack.addSlice("wvhgh_"+nmArraySort[glb_min_h_ind-1], wvhgh_temp_im);	
		
		
		
		
		
		//Fitting
		}else{
		int fitType = 8;
		if(menu2.getSelectedItem() == "Gamma Variate"){fitType= 8; }//GAMMA_VARIATE;
		if(menu2.getSelectedItem() == "Gaussian"){fitType=21;}//GAUSSIAN_NOOFFSET
		
		
		double x_data[] = new double[numSlices];
		for(int i=0; i<numSlices; i++) {
    	x_data[i] = nmArraySort[i];
		}
		double noise_tol= (Double)noiseTol.getValue();

		

		FloatProcessor out1 = new FloatProcessor(stack.getWidth(), stack.getHeight());
		FloatProcessor out2 = new FloatProcessor(stack.getWidth(), stack.getHeight());
		FloatProcessor err = new FloatProcessor(stack.getWidth(), stack.getHeight());
		out1_pix = (float[])out1.getPixels();
		out2_pix = (float[])out2.getPixels();
		out_err = (float[])err.getPixels();
		
			double cou =0;
			double sumErr =0;
			for(int y=0;y<stack.getHeight();y++){
				for(int x=0;x<stack.getWidth();x++){
				int ind= (stack.getWidth()*y)+x;
				if(maskCH[ind]== -1){
					double y_data[] = new double[numSlices];
					for(int sli=0;sli<numSlices;sli++){
					
					y_data[sli] = stack.getVoxel(x,y,sli);
					
					}
					CurveFitter crfit = new CurveFitter(x_data,y_data);
					crfit.doFit(fitType);
					double ERROR = crfit.getRSquared();
					out_err[ind] = (float)(double)ERROR;
					
					if(ERROR > noise_tol){
						cou = cou+1;
						sumErr = sumErr + ERROR;
						out1_pix[ind] = (float)(double)crfit.f(crfit.getParams(),wvlow);
						out2_pix[ind] = (float)(double)crfit.f(crfit.getParams(),wvhgh);
						//If sample is too high or two low, do not include. Gets set to NaN later.
						if (out1_pix[ind] >255 || out2_pix[ind] >255 || out1_pix[ind] <=1|| out2_pix[ind] <=1){
							maskCH[ind]= 0;
						cou = cou-1;
						sumErr = sumErr - ERROR;}}
			
				
		}else{out_err[ind] = Float.NaN;}}}
		
		double averageError = sumErr/cou;
		
		IJ.log("Sum of pixels above noise tolerance:\t"+cou);
		IJ.log("Sum of R2 above noise tolerance:\t"+sumErr);
		IJ.log("Average R2 in pixels above noise tolerance (0-1.0, 1.0 =best)\t: "+averageError);
		
		wave_stack.addSlice("wvlow_"+wvlow, out1_pix);
		wave_stack.addSlice("wvhgh_"+wvhgh, out2_pix);
		
		}

		


		ImageProcessor slice_wvlow = wave_stack.getProcessor(1);
		float[] wvlow_lin = (float[])slice_wvlow.getPixels();
		ImageProcessor slice_wvhigh = wave_stack.getProcessor(2);
		float[] wvhigh_lin = (float[])slice_wvhigh.getPixels();

		
		float wvlow_lin_norm[] = new float[wvlow_lin.length];
		float wvhigh_lin_norm[] = new float[wvhigh_lin.length];

		//Get weights.
		double dob_High_value = (Double)weightHighJS.getValue();
		double dob_Low_value = (Double)weightLowJS.getValue(); 
		float weight_High_value = (float)(double)dob_High_value;
		float weight_Low_value = (float)(double)dob_Low_value;


		
		//Normalise data or make mask. (Background subtraction can make <0). Avoid pixels below 0.5 leads 
		for(int ind=0;ind<wvhigh_lin.length;ind++){
			if(wvlow_lin[ind]>0.5){
			wvlow_lin_norm[ind] = (wvlow_lin[ind]/255)*weight_Low_value;}else{
				maskCH[ind] = 0;
				}
			if(wvhigh_lin[ind]>0.5){
			wvhigh_lin_norm[ind] =(wvhigh_lin[ind]/255)*weight_High_value;
			}else{
				maskCH[ind] =0;
				}
			}

		float out_add[] = new float[wvlow_lin.length];
		float out_sub[] = new float[wvlow_lin.length];
		float out_GP[] = new float[wvlow_lin.length];
		float out_sort[] = new float[wvlow_lin.length];

		for(int ind=0;ind<wvhigh_lin.length;ind++){
			
			//Only scan the unmasked pixels.
			if(maskCH[ind]== -1){
				out_sub[ind] = wvlow_lin_norm[ind]-wvhigh_lin_norm[ind];
				out_add[ind] = wvhigh_lin_norm[ind]+wvlow_lin_norm[ind];
				
			if(out_add[ind]>0){
				
			out_GP[ind]= out_sub[ind]/out_add[ind];
			
			

			

			
			
			}else{out_GP[ind]=Float.NaN;}
			}else{out_GP[ind]=Float.NaN;}
			
			out_sort[ind] = out_GP[ind];
			}


		


		
		LUT col = new LUT(channel1,channel2,channel3);

		
		
		FloatProcessor float_GP = new FloatProcessor(stack.getWidth(), stack.getHeight(),out_GP,col);
		
		
		
		ImagePlus outStack = new ImagePlus(imgName1+"_Input_Fit_"+menu2.getSelectedItem(), wave_stack);
		outStack.setPosition(1);
		outStack.resetDisplayRange();
		outStack.setPosition(2);
		outStack.resetDisplayRange();
		
		
		
		
		

		ImagePlus imp_GP =  new ImagePlus(imgName1+"_GP_MAP_Fit_"+menu2.getSelectedItem(), float_GP );
		imp_GP.setDisplayRange(-1,1);
		
		
		
		if (output_checkBox.isSelected() == true){
		impS.show();
		impS.setTitle("mask");
		outStack.show();	
		if(menu2.getSelectedItem() != "None"){ 
			
		FloatProcessor err_FP = new FloatProcessor(stack.getWidth(), stack.getHeight(),out_err);
		ImagePlus err_imp = new ImagePlus(imgName1+"_GoodnessOfFit_Fit_"+menu2.getSelectedItem(), err_FP);
		err_imp.show();
		}

		}
		imp_GP.show();
		HistogramWindow histo_GP = new HistogramWindow(imgName1+"_Histo_Fit_"+menu2.getSelectedItem(),imp_GP, 512, -1,1);

		//Calculate median in positive domain and negative domain.

		Arrays.sort(out_sort);
		int negativeEnd = 0;
		int positiveStart = out_sort.length;
		int maxInd = 0;
		float maxValue = 0;
		for(int ind=0;ind<out_sort.length;ind++){
			
			
			if (out_sort[ind] < 0.0){
				//negative fraction
				negativeEnd = ind;
				


				
				}
			if (out_sort[ind]>0.0){
				if(ind< positiveStart){
				positiveStart = ind;}
				if(maxValue< out_sort[ind]){
					maxValue = out_sort[ind];
					maxInd = ind;
					
					}
				
				
				}
			
			}
		long endTime = System.currentTimeMillis();
		int pos_ind = ((maxInd - positiveStart)/2)+positiveStart;
		int neg_ind = (negativeEnd)/2;
		IJ.log("Positive phase median: "+out_sort[pos_ind]);
		IJ.log("Negative phase median: "+out_sort[neg_ind]);
		IJ.log("Calculation duration (ms): "+(endTime - startTime));
		//System.out.println("maxInd"+maxInd);
		//System.out.println("negativeEnd"+negativeEnd);
		//System.out.println("positiveStart"+positiveStart);
		
		menu1.setSelectedIndex(selectA);
		
		

			
			
			
		}

//}
	
}