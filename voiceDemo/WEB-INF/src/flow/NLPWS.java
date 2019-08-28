package flow;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.*;
import org.json.JSONException;
import org.json.JSONObject;
import com.avaya.sce.runtimecommon.IVariableField;

/**
 * A basic servlet which allows a user to define their code, generate
 * any output, and to select where to transition to next.
 * Last generated by Orchestration Designer at: 2019-FEB-11  05:04:49 PM
 */
public class NLPWS extends com.avaya.sce.runtime.BasicServlet {

	//{{START:CLASS:FIELDS
	//}}END:CLASS:FIELDS

	/**
	 * Default constructor
	 * Last generated by Orchestration Designer at: 2019-FEB-11  05:04:49 PM
	 */
	public NLPWS() {
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
	 * Last generated by Orchestration Designer at: 2019-FEB-11  05:04:49 PM
	 */
	 public static String systemReplys ="" ;
	 public static  String asrReplys ="" ;
	 public static  String UserData="";
	 public static  String mobileNumbers ="" ;
	 public static  String id ="" ;
	 public static  String inputTextin ="" ;
	 public static  String inputTextout ="" ;
	 public static  Double sttTime =0.0 ;
	 public static  String _serverStatus="";
	 public static int count = 2;
	 public static String MobileNumber = "";
	 public static String Ucid = "";
	 
	
	public void servletImplementation(com.avaya.sce.runtimecommon.SCESession mySession) {
		MobileNumber =  mySession.getVariableField(IProjectVariables.SESSION,IProjectVariables.SESSION_FIELD_ANI).getStringValue();
		Ucid =  mySession.getVariableField(IProjectVariables.SESSION,IProjectVariables.SESSION_FIELD_UCID).getStringValue();
		count ++ ;
		IVariableField serverStatus =  mySession.getVariableField(IProjectVariables.SERVER_STATUS_NLPWS);
	    java.text.SimpleDateFormat formatDate = new java.text.SimpleDateFormat("HH");
		java.util.Date date = new java.util.Date(); 
//		String dates=formatDate.format(date);
//		IVariableField startdata = mySession.getVariableField(IProjectVariables.STT_TIME);//system time
//		startdata.setValue(dates);
	    
	    id = mySession.getVariableField(IProjectVariables.SESSION,IProjectVariables.SESSION_FIELD_UCID).getStringValue();
		inputTextin = mySession.getVariableField(IProjectVariables.OUT_GOOGLE_ASR).getStringValue();
		System.out.println(inputTextin);
		try {
		GetData(id,inputTextin);
		}
		catch(JSONException js)
		{
			System.out.println(js);
		}
		 IVariableField systemReply = mySession.getVariableField(IProjectVariables.OUT_NLPWS);
		 if(asrReplys == "null") 
		 {
		 systemReply.setValue(systemReplys);
		 }
		 else 
		 {
			 systemReply.setValue(asrReplys);
		 }
		 serverStatus.setValue(_serverStatus);
		 if(count == 20)
			 count = 3;

		// TODO: Add your code here!

	}
	public static void GetData(String id, String inputText) throws JSONException
	{
		Date systemdates = new Date();//yyddMMhhmmss
		SimpleDateFormat sdate = new SimpleDateFormat("dd-MM-yy");
		SimpleDateFormat stime = new SimpleDateFormat("HH:mm:ss");
		String formattedDate = sdate.format(systemdates);
		String formattedTime = stime.format(systemdates);
		String TimeStamp=formattedDate + "\t"+ formattedTime;
		
		SimpleDateFormat currentTime= new SimpleDateFormat("ss.SS");
		String execucationCurrentTime = currentTime.format(systemdates);
		Double currentTimes = Double.parseDouble(execucationCurrentTime);
//		System.out.println(currentTimes +"\t"+ LogFileIn._execucationTimeIn);
		Double sttOut=currentTimes - LogFileIn._execucationTimeIn;
		sttTime = Double.parseDouble(new DecimalFormat("##.##").format(sttOut)) ;
//		System.out.println(sttTime);
			 try
	            {
				 
				// out = new BufferedWriter(new FileWriter(realFile,true));
				 String UrlString = "http://182.72.46.252:8080/mainlayer/userRequest";
                 String ParamString = "id="+id+"&inputText="+java.net.URLEncoder.encode(inputText,"UTF-8")+"&sttTime="+sttTime+"";
                 String wsurl = UrlString + "?" + ParamString;
	            

	                System.out.println("URL str: " + wsurl + " *********\n");

	                java.net.URL urlObj = new java.net.URL(wsurl);
	                LogFileIn.stringLogBuilder.append("\r\n" + TimeStamp +"\t" +"[Request To NLP Web Service]     -->");
	                LogFileIn.stringLogBuilder.append(wsurl);
	                java.net.HttpURLConnection urlConn = (java.net.HttpURLConnection) urlObj.openConnection();
	                urlConn.setDoOutput(true);
	                urlConn.setRequestMethod("GET");
	                urlConn.setRequestProperty("Content-Type", "application/json");
	                urlConn.setConnectTimeout(60 * 1000);
	                urlConn.setReadTimeout(60 * 1000);
	                urlConn.setRequestProperty("Accept", "application/json");

	                int respcode = urlConn.getResponseCode();
	                System.out.println("Reponsecode: " + respcode);

	                if (urlConn.getResponseCode() != java.net.HttpURLConnection.HTTP_OK)
	                {
	                    System.out.println("********* Unable to connect to the URL *********\n");
	                    _serverStatus = "1";
	                    LogFileIn.stringLogBuilder.append("\r\n"+TimeStamp+ "\t"+"[Response From Google NLP Web Service]     -->");
	                    LogFileIn.stringLogBuilder.append(String.valueOf( respcode));
	                    LogFileIn.stringLogBuilder.append("\n");
	    				
	                }
	                else
	                {
	                    _serverStatus = "0";
	                 

	                    java.io.InputStream is = urlConn.getInputStream();
	                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader((is)));
	                    
	                    
	                    JSONObject myResponse = null;
	                    String tmpStr = null;
	                    while ((tmpStr = reader.readLine()) != null)
	                    {
	                       // sbout.append(tmpStr);
	                    	myResponse = new JSONObject(tmpStr.toString());
	                    }    
	                    
	                    systemReplys = myResponse.getString("systemReply");
	                    try
	                    {
	               	        asrReplys = myResponse.getString("asrReply");
	               	    }
	               	    catch(JSONException ex)
	               	    {
	               	       	asrReplys ="null";
	               	    }
	                   
//	                    System.out.println("Output from Server:  \n" + " "+ inputTextout);
	                    LogFileIn.stringLogBuilder.append("\r\n"+TimeStamp+"\t"+ "[Response From NLP Web Service]     -->");
	                    LogFileIn.stringLogBuilder.append(String.valueOf( respcode)+"."+"\t"+ myResponse);
	                    LogFileIn.stringLogBuilder.append("\n");
	                    

	                    is.close();
	                    reader.close();
	                    urlConn.disconnect();
	                }
 
          } catch (java.net.MalformedURLException ml) {

        	  _serverStatus = "1";
        	  System.out.println("Error Code :"+ ml.toString());
        	  LogFileIn.stringLogBuilder.append("\r\n"+TimeStamp+"\t"+ "[Malformed URL Exception]     -->");
        	  LogFileIn.stringLogBuilder.append(String.valueOf( ml.toString()));
        	  LogFileIn.count = 0;
        	  
 
          } catch (java.io.IOException ex) {
 
        	  _serverStatus = "1";
        	  System.out.println("Error Code :"+ ex.toString());
        	  LogFileIn.stringLogBuilder.append("\r\n"+TimeStamp+"\t"+ "[IO Exception]     -->");
        	  LogFileIn.stringLogBuilder.append(String.valueOf( ex.toString()));
        	  LogFileIn.count = 0;
          }
	     finally
	     {

         }
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

		aGoto = new com.avaya.sce.runtime.Goto("CheckNLPWSError", 0, true, "Default");
		list.add(aGoto);

		return list;
	}
}