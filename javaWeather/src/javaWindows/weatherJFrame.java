package javaWindows;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class weatherJFrame extends JPanel {

	
	private static JLabel tempLabel;
	private static JLabel dewLabel;
	private static JLabel humLabel;
	private static JLabel timLabel;
	private static JLabel heatLabel;
	private static JLabel reftimeLabel;

	private static String tempString = "Temperature: ";
	private static String dewString = "Dewpoint: ";
	private static String humString = "Humidity";
	private static String timString = "Date: ";
	private static String heatString = "Heat Index: ";
	private static String reftimeString = "Last refresh: ";
	
	private static JFormattedTextField tempField;
	private static JFormattedTextField dewField;
	private static JFormattedTextField humField;
	private static JFormattedTextField timField;
	private static JFormattedTextField heatField;
	private static JFormattedTextField reftimeField;
	
	private static NumberFormat tempFormat;
	private static NumberFormat dewFormat;
	private static NumberFormat humFormat;
	private static String timFormat;
	private static String reftimeFormat;
	private static NumberFormat heatFormat;
	
	public weatherJFrame () {
		super(new BorderLayout());
		setUpFormats();
	}
	public static void main(String[] args) {
		for (int times=0; times<=60; times++) {
			
		Object output;
			
			try {
	            
				tempLabel = new JLabel(tempString);
				dewLabel = new JLabel(dewString);
				humLabel = new JLabel(humString);
				timLabel = new JLabel(timString);
				heatLabel	= new JLabel(heatString);
				reftimeLabel = new JLabel(reftimeString);
	
				JFrame frame = new JFrame("Current Weather Conditions");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				URL url = new URL("https://api.weather.gov/stations/KAUS/observations/current");
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("GET");
	            conn.setRequestProperty("Accept", "application/json");
	            BufferedReader object = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            JSONParser jsonParser = new JSONParser();
	            if (conn.getResponseCode() != 200) {
	            	
	                throw new RuntimeException("Failed : HTTP error code : "
	                        + conn.getResponseCode());
	            }
	
	            try {
	            	
		            	output = jsonParser.parse(object);
						JSONObject jsonObject = (JSONObject) output;
						
						JSONObject properties = (JSONObject) jsonObject.get("properties");
						JSONObject dewpoint = (JSONObject) properties.get("dewpoint");
						JSONObject humidity = (JSONObject) properties.get("relativeHumidity");
						JSONObject temperature = (JSONObject) properties.get("temperature");
						JSONObject heatindex = (JSONObject) properties.get("heatIndex");
						String timestamp = (String) properties.get("timestamp");
						String timest = timestamp.substring(0, 10);
						Calendar curtim = Calendar.getInstance();
				        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				        String currenttime=sdf.format(curtim.getTime());
						double dewpointc = (double) dewpoint.get("value");
						double dewpointf = (dewpointc*(9.0/5.0))+32.0;
						double tempvaluec = (double) temperature.get("value");
						double tempvaluef = tempvaluec*(9.0/5.0)+32.0;
						double heatvaluec = (double) heatindex.get("value");
						double heatvaluef = heatvaluec*(9.0/5.0)+32.0;
						DecimalFormat df2 = new DecimalFormat("###.##");
						String tempvaluef1 = df2.format(tempvaluef);
						DecimalFormat df5 = new DecimalFormat ("###.##");
						String heatvaluef1 = df5.format(heatvaluef);
						double hum = (double) humidity.get("value");
						DecimalFormat df3 = new DecimalFormat("##.##");
						String hum1 = df3.format(hum);
						DecimalFormat df4 = new DecimalFormat("##.##");
						String dewvalue = df4.format(dewpointf);
			            
						conn.disconnect();
						
						timField = new JFormattedTextField(timFormat);
						timField.setValue(timest);
						timField.setColumns(10);
						
						reftimeField = new JFormattedTextField(reftimeFormat);
						reftimeField.setValue(currenttime);
						reftimeField.setColumns(10);
						
						tempField = new JFormattedTextField(tempFormat);
						tempField.setValue(new Double(tempvaluef1));
						tempField.setColumns(10);
						
						dewField = new JFormattedTextField(dewFormat);
						dewField.setValue(new Double(dewvalue));
						dewField.setColumns(10);
						
						humField = new JFormattedTextField(humFormat);
						humField.setValue(new Double(hum1));
						humField.setColumns(10);
						
						heatField = new JFormattedTextField(heatFormat);
						heatField.setValue(new Double(heatvaluef1));
						heatField.setColumns(10);
						
						tempLabel.setLabelFor(tempField);
						dewLabel.setLabelFor(dewField);
						humLabel.setLabelFor(humField);
						timLabel.setLabelFor(timField);
						reftimeLabel.setLabelFor(reftimeField);
						heatLabel.setLabelFor(heatField);
						
						JPanel labelPane = new JPanel(new GridLayout(0,1));
						labelPane.add(timLabel);
						labelPane.add(tempLabel);
						labelPane.add(heatLabel);
						labelPane.add(dewLabel);
						labelPane.add(humLabel);
						labelPane.add(reftimeLabel);
						
						
						JPanel fieldPane = new JPanel(new GridLayout(0,1));
						fieldPane.add(timField);
						fieldPane.add(tempField);
						fieldPane.add(heatField);
						fieldPane.add(dewField);
						fieldPane.add(humField);
						fieldPane.add(reftimeField);
						
						BorderFactory.createEmptyBorder(20, 20, 20, 20);
						frame.add(labelPane,BorderLayout.CENTER);
						frame.add(fieldPane,BorderLayout.LINE_END);
						frame.pack();
						frame.setVisible(true);
						try {
							TimeUnit.MINUTES.sleep(2);
						}
						catch(InterruptedException ex) {
							Thread.currentThread().interrupt();
						}
					}
	            finally {
	            //
	            }
				} catch (ParseException e) {
					e.printStackTrace();
				}	
	        //} 
		catch (MalformedURLException e) {
	            e.printStackTrace();
	        }
	         catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
	
	public static void setUpFormats() {
		tempFormat = NumberFormat.getNumberInstance();
		dewFormat = NumberFormat.getNumberInstance();
		humFormat = NumberFormat.getNumberInstance();
	}
}