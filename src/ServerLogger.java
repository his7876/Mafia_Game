
public class ServerLogger {
	private static final boolean ENABLE_LOGGING = true;
	
	
	public static void printLog(String input) {
		if(ENABLE_LOGGING) {
			System.out.println(input);
		}
	}
	
		
}
