package flow;
import java.io.BufferedWriter;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;

import org.json.JSONObject;
import java.util.Base64;
import java.util.Date;

import com.avaya.sce.runtimecommon.IVariableField;
import com.sun.istack.internal.logging.Logger;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * A basic servlet which allows a user to define their code, generate
 * any output, and to select where to transition to next.
 * Last generated by Orchestration Designer at: 2019-FEB-11  04:55:47 PM
 */
public class GoogleASR extends com.avaya.sce.runtime.BasicServlet {

	//{{START:CLASS:FIELDS
	//}}END:CLASS:FIELDS

	/**
	 * Default constructor
	 * Last generated by Orchestration Designer at: 2019-FEB-11  04:55:47 PM
	 */
	public GoogleASR() {
		//{{START:CLASS:CONSTRUCTOR
		super();
		//}}END:CLASS:CONSTRUCTOR
	}

	/**
	 * This method allows for custom integration with other Java components.
	 * You may use Java for sophisticated logic or to integrate with custom
	 * connectors (i.e. JMS, custom web services, sockets, XML, JAXB, etc.)
	 *
	 * Any custom code added here should work as efficiently as possible to prevent delays.
	 * It's important to design your callflow so that the voice browser (Experienve Portal/IR)
	 * is not waiting too long for a response as this can lead to a poor caller experience.
	 * Additionally, if the response to the client voice browser exceeds the configured
	 * timeout, the platform may throw an "error.badfetch". 
	 *
	 * Using this method, you have access to all session variables through the 
	 * SCESession object.
	 *
	 * The code generator will *** NOT *** overwrite this method in the future.
	 * Last generated by Orchestration Designer at: 2019-FEB-11  04:55:47 PM
	 */
	
	//public static StringBuilder ss = new StringBuilder();
	
	
	public static String MobileNumber ="";
	public static String Ucid=""; 
	
	public void servletImplementation(com.avaya.sce.runtimecommon.SCESession mySession) {
		//createFolder();
		Date date = new Date();//yyddMMhhmmss
		SimpleDateFormat sdate = new SimpleDateFormat("dd-MM-yy");
		SimpleDateFormat stime = new SimpleDateFormat("HH:mm:ss");
		String formattedDate = sdate.format(date);
		String formattedTime = stime.format(date);
		String TimeStamp=formattedDate + "\t"+ formattedTime;
		
		SimpleDateFormat currentTime= new SimpleDateFormat("ss.SS");
		String execucationTimeIn = currentTime.format(date);
		LogFileIn._execucationTimeIn = Double.parseDouble(execucationTimeIn);


		MobileNumber =  mySession.getVariableField(IProjectVariables.SESSION,IProjectVariables.SESSION_FIELD_ANI).getStringValue();
		Ucid =  mySession.getVariableField(IProjectVariables.SESSION,IProjectVariables.SESSION_FIELD_UCID).getStringValue();
		

		try
	    {
		
			IVariableField varOutGoogleTTS = mySession.getVariableField(IProjectVariables.OUT_GOOGLE_TTS); 
			varOutGoogleTTS.setValue("");
			//SSL Certificate
	        TrustManager[] trustAllCerts = new TrustManager[]
	                {
	                     new X509TrustManager()
	                     {
	                          public java.security.cert.X509Certificate[] getAcceptedIssuers()
	                          {
	                               return null;
	                          }

	                          public void checkClientTrusted(X509Certificate[] certs, String authType)
	                          {
	                          }

	                          public void checkServerTrusted(X509Certificate[] certs, String authType)
	                          {
	                          }
	                     }
	                };

	                // Install the all-trusting trust manager
	                SSLContext sc = SSLContext.getInstance("SSL");
	                sc.init(null, trustAllCerts, new java.security.SecureRandom());
	                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	                // Create all-trusting host name verifier
	                HostnameVerifier allHostsValid = new HostnameVerifier()
	                {
	                     public boolean verify(String hostname, SSLSession session)
	                     {
	                          return true;
	                     }
	                };

	                // Install the all-trusting host verifier-----29/9
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	        	        	       
	        
	        String Audio =  mySession.getVariableField(IProjectVariables.URL).toString();	    
	        URL url1 = new URL(Audio); 
	        ReadableByteChannel readableByteChannel = Channels.newChannel(url1.openStream());
	        String FILE_NAME="C:\\RecodinVoice\\GoogleASR.wav";
	        FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
	        FileChannel fileChannel = fileOutputStream.getChannel();
	        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);      
	        fileOutputStream.close();
	        
	        File file = new File(FILE_NAME);	        
	        System.out.println("Orignal file path:  "+Audio+" , New file path: "+FILE_NAME+" , IsExist: "+file.exists());
	        
	        String encode = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
	        String json = "{\"config\":{\"sampleRate\":8000,\"languageCode\":\"en-IN\"},\"audio\":{}}";
	        JSONObject jsonObj = new JSONObject(json);
	        JSONObject chldJson = jsonObj.getJSONObject("audio");
	        chldJson.put("content", encode);
    
	       // String weburl = "https://speech.googleapis.com/v1beta1/speech:syncrecognize?key=AIzaSyAoLUXGkx3UPQ93T2ISL0X_QjZmEXFzmdI";
	        String weburl = "https://speech.googleapis.com/v1beta1/speech:syncrecognize?key=AIzaSyC3PzIEuYMqxFvRDexZh_QSGhMKUbB_4eo";
	        java.net.URL url = new java.net.URL(weburl);
	        LogFileIn.stringLogBuilder.append("\r\n"+TimeStamp+"\t"+ "[Request Google ASR]     -->");
	        LogFileIn.stringLogBuilder.append(weburl);
	        
	        java.net.HttpURLConnection connjava = (java.net.HttpURLConnection) url.openConnection();
	        connjava.setRequestMethod("POST");

	        connjava.setRequestProperty("Content-Type", "application/json");
	        connjava.setDoInput(true);
	        connjava.setDoOutput(true);
	        connjava.setAllowUserInteraction(true);

	        java.io.DataOutputStream dos = new java.io.DataOutputStream(connjava.getOutputStream());
	        System.out.println("json payload: "+jsonObj);
	        dos.writeBytes(jsonObj.toString());
	        dos.flush();
	        dos.close();
	        int respcode = connjava.getResponseCode();
	        System.out.println(respcode);
	        
	        LogFileIn.stringLogBuilder.append("\r\n"+TimeStamp+ "\t"+"[Response From Google ASR]     -->");
	        LogFileIn.stringLogBuilder.append(String.valueOf( respcode)+"."+"\t"+connjava.getResponseMessage());           
            if (connjava.getResponseCode() != java.net.HttpURLConnection.HTTP_OK)
            {
                System.out.println("********* Unable to connect to the URL *********\n");
                System.out.println(connjava.getResponseMessage());
                IVariableField serverStatus = mySession.getVariableField(IProjectVariables.SERVER_STATUS_ASR); 
	        	serverStatus.setValue("1");
            }
            else
            {
            System.out.println("********* Connected *********\n");
	        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(connjava.getInputStream()));
	        StringBuilder sbout = new StringBuilder();
	        String line;
	        while ((line = in.readLine()) != null)
	        {
	            sbout.append(line);
	        }
	        in.close();
	        
//	        LogFileIn.stringLogBuilder.append("\r\n"+TimeStamp+ "\t"+"Response: "+sbout.toString());
//	        LogFileIn.stringLogBuilder.append("\n");

	        JSONObject js = new JSONObject(sbout.toString());
	        if(js.length() == 0)
	        {
	        	IVariableField serverStatus = mySession.getVariableField(IProjectVariables.SERVER_STATUS_ASR); 
	        	serverStatus.setValue("1");
	        	LogFileIn.count++;
	        	LogFileIn.stringLogBuilder.append("\r\n"+TimeStamp+ "\t"+"Response: "+sbout.toString());
	  	        LogFileIn.stringLogBuilder.append("\n");
	 	        IVariableField errorCounts = mySession.getVariableField(IProjectVariables.ASRERROR_COUNT); 
	 	        errorCounts.setValue(Integer.toString(LogFileIn.count));
	        	if(LogFileIn.count == 4)
	        	{
	        		LogFileIn.stringLogBuilder.append("\r\n"+TimeStamp+"\t"+"Response: "+"{You Have Error In Google ASR}");
		 	        LogFileIn.stringLogBuilder.append("\n");
		 	        LogFileIn.count = 0;
	        	}
	        }
	        else {
	        	 LogFileIn.count = 0;
	        	 String jsonString = js.getJSONArray("results").getJSONObject(0).getJSONArray("alternatives").getJSONObject(0).getString("transcript");
	             System.out.println("Out put string"+jsonString);
	             LogFileIn.stringLogBuilder.append("\r\n"+TimeStamp+ "\t"+"Response: "+sbout.toString());
	 	         LogFileIn.stringLogBuilder.append("\n");
	             
	             IVariableField varOutGoogleASR = mySession.getVariableField(IProjectVariables.OUT_GOOGLE_ASR); 
	             varOutGoogleASR.setValue(jsonString); 

	         	 IVariableField serverStatus = mySession.getVariableField(IProjectVariables.SERVER_STATUS_ASR); 
	         	 serverStatus.setValue("0");
	        	
	        }
          }
	    }
		
	    catch (Exception ex )
	    {
	    	
	    	System.out.println("error");
	    	IVariableField serverStatus = mySession.getVariableField(IProjectVariables.SERVER_STATUS_ASR); 
	    	serverStatus.setValue("1");
	    	System.out.println(ex.getMessage());
	    	 System.out.println(ex);
	        ex.printStackTrace();
	        LogFileIn.stringLogBuilder.append("\r\n"+TimeStamp+ "\t"+"[Exception]");
	        LogFileIn.stringLogBuilder.append(ex.toString());
	    }
		// TODO: Add your code here!
		

	}
	
	
	
	
	
	/**
	 * Builds the list of branches that are defined for this servlet object.
	 * This list is built automatically by defining Goto nodes in the call flow editor.
	 * It is the programmer's responsibilty to provide at least one enabled Goto.<BR>
	 *
	 * The user should override updateBranches() to determine which Goto that the
	 * framework will activate.  If there is not at least one enabled Goto item, 
	 * the framework will throw a runtime exception.<BR>
	 *
	 * This method is generated automatically and changes to it may
	 * be overwritten next time code is generated.  To modify the list
	 * of branches for the flow item, override:
	 *     <code>updateBranches(Collection branches, SCESession mySession)</code>
	 *
	 * @return a Collection of <code>com.avaya.sce.runtime.Goto</code>
	 * objects that will be evaluated at runtime.  If there are no gotos
	 * defined in the Servlet node, then this returns null.
	 * Last generated by Orchestration Designer at: 2019-AUG-28  10:13:07 AM
	 */
	public java.util.Collection getBranches(com.avaya.sce.runtimecommon.SCESession mySession) {
		java.util.List list = null;
		com.avaya.sce.runtime.Goto aGoto = null;
		list = new java.util.ArrayList(1);

		aGoto = new com.avaya.sce.runtime.Goto("CheckASRError", 0, true, "Default");
		list.add(aGoto);

		return list;
	}
}
