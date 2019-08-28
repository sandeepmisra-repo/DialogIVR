package flow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A basic servlet which allows a user to define their code, generate
 * any output, and to select where to transition to next.
 * Last generated by Orchestration Designer at: 2019-FEB-14  02:23:13 PM
 */
public class LogFileIn extends com.avaya.sce.runtime.BasicServlet {

	//{{START:CLASS:FIELDS
	//}}END:CLASS:FIELDS

	/**
	 * Default constructor
	 * Last generated by Orchestration Designer at: 2019-FEB-14  02:23:13 PM
	 */
	public LogFileIn() {
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
	 * Last generated by Orchestration Designer at: 2019-FEB-14  02:23:13 PM
	 */
	public void createFolder()
	{
		try
		{
//		    File file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5_Tomcat_test\\webapps\\logs\\");
			File file = new File("D:\\officedocument\\personal\\Task By Suvijoy Sir\\Server\\AAOD-7.2.1\\");
	        if (!file.exists()) {
	            if (file.mkdir()) {
	                System.out.println("Folder/Directory is created successfully");
	            } else {
	                System.out.println("Directory/Folder creation failed!!!");
	            }
	        }
		}
		catch(Exception ex)
		{
			System.out.println("[Exception]"+ex.toString());
		}
	}
	public static BufferedWriter out = null;
	public static StringBuilder stringLogBuilder;
	public static int count = 0;
	public static double _execucationTimeIn= 0.0;
	public void servletImplementation(com.avaya.sce.runtimecommon.SCESession mySession) {

		// TODO: Add your code here!
		
		createFolder();
		Date date = new Date();//yyddMMhhmmss
		SimpleDateFormat sdate = new SimpleDateFormat("dd-MM-yy");
		SimpleDateFormat stime = new SimpleDateFormat("HH:mm:ss");
		String formattedDate = sdate.format(date);
		String formattedTime = stime.format(date);
		String TimeStamp=formattedDate + "\t"+ formattedTime;
		
		String MobileNumber =  mySession.getVariableField(IProjectVariables.SESSION,IProjectVariables.SESSION_FIELD_ANI).getStringValue();
		String Ucid =  mySession.getVariableField(IProjectVariables.SESSION,IProjectVariables.SESSION_FIELD_UCID).getStringValue();
		String FileName = MobileNumber+"_"+Ucid;
//		String logFilePath="C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5_Tomcat_test\\webapps\\logs\\ASR_Demo\\";
		String logFilePath="D:\\officedocument\\personal\\Task By Suvijoy Sir\\Server\\Demo\\";
		
		String filepath=logFilePath;
		String realFile=filepath+FileName+".txt";
		mySession.getTraceOutput().writeln(4, "sql Exception ---->writing to file");
		
		try
	    {
			out = new BufferedWriter(new FileWriter(realFile,true));
			out.write("----------------------------------------------Start-----------------------------------------------");
			out.write("\r\n"+TimeStamp+"\t"+ "[Mobile Number , Ucid]     --->");
			out.write(MobileNumber + "\t" + Ucid);
			out.write("\r\n");
		    out.close();
		    stringLogBuilder = new StringBuilder();
	    }
		catch(Exception ex)
		{
			System.out.println("Error :" + ex.toString());
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

		aGoto = new com.avaya.sce.runtime.Goto("WelcomeAnnounce", 0, true, "Default");
		list.add(aGoto);

		return list;
	}
}