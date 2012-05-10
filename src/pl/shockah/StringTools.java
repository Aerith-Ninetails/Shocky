package pl.shockah;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools {
	private static HashMap<String,String> htmlEntities;
	public static final Pattern unicodePattern = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
	
	static {
		htmlEntities = new HashMap<String,String>();
		htmlEntities.put("&lt;","<"); htmlEntities.put("&gt;",">");
		htmlEntities.put("&amp;","&"); htmlEntities.put("&quot;","\"");
		htmlEntities.put("&agrave;","a"); htmlEntities.put("&Agrave;","A");
		htmlEntities.put("&acirc;","�"); htmlEntities.put("&auml;","�");
		htmlEntities.put("&Auml;","�"); htmlEntities.put("&Acirc;","�");
		htmlEntities.put("&aring;","a"); htmlEntities.put("&Aring;","A");
		htmlEntities.put("&aelig;","a"); htmlEntities.put("&AElig;","A" );
		htmlEntities.put("&ccedil;","�"); htmlEntities.put("&Ccedil;","�");
		htmlEntities.put("&eacute;","�"); htmlEntities.put("&Eacute;","�" );
		htmlEntities.put("&egrave;","e"); htmlEntities.put("&Egrave;","E");
		htmlEntities.put("&ecirc;","e"); htmlEntities.put("&Ecirc;","E");
		htmlEntities.put("&euml;","�"); htmlEntities.put("&Euml;","�");
		htmlEntities.put("&iuml;","i"); htmlEntities.put("&Iuml;","I");
		htmlEntities.put("&ocirc;","�"); htmlEntities.put("&Ocirc;","�");
		htmlEntities.put("&ouml;","�"); htmlEntities.put("&Ouml;","�");
		htmlEntities.put("&oslash;","o"); htmlEntities.put("&Oslash;","O");
		htmlEntities.put("&szlig;","�"); htmlEntities.put("&ugrave;","u");
		htmlEntities.put("&Ugrave;","U"); htmlEntities.put("&ucirc;","u");
		htmlEntities.put("&Ucirc;","U"); htmlEntities.put("&uuml;","�");
		htmlEntities.put("&Uuml;","�"); htmlEntities.put("&nbsp;"," ");
		htmlEntities.put("&copy;","\u00a9");
		htmlEntities.put("&reg;","\u00ae");
		htmlEntities.put("&euro;","\u20a0");
		htmlEntities.put("&bull;","�");
	}
	
	public static String getFilenameStrippedWindows(String fname) {
		fname = fname.replace('\\','-');
		fname = fname.replace('/','-');
		fname = fname.replace(": "," - ");
		fname = fname.replace(':','-');
		fname = fname.replace("*","");
		fname = fname.replace("?","");
		fname = fname.replace('"','\'');
		fname = fname.replace('<','[');
		fname = fname.replace('>',']');
		fname = fname.replace("|"," - ");
		return fname;
	}
	
	public static String escapeHTML(String s) {
		s = s.replace("&","&amp;");
		for (String key : htmlEntities.keySet()) {
			if (key.equals("&amp;")) continue;
			s = s.replace(htmlEntities.get(key),key);
		}
		return s;
	}
	public static String unescapeHTML(String source) {
		boolean continueLoop;
		int skip = 0;
		do {
			continueLoop = false;
			int i = source.indexOf("&",skip);
			if (i > -1) {
				int j = source.indexOf(";",i);
				if (j > i) {
					String entityToLookFor = source.substring(i,j+1);
					if (entityToLookFor.indexOf("#")==1) {
						char chr = (char)Integer.parseInt(source.substring(i+2,j));
						source = source.substring(0,i)+chr+source.substring(j+1);
						continueLoop = true;
					} else {
						String value = htmlEntities.get(entityToLookFor);
						if (value != null) {
							source = source.substring(0,i)+value+source.substring(j+1);
							continueLoop = true;
						} else if (value == null) {
							skip = i+1;
							continueLoop = true;
						}
					}
				}
			}
		} while (continueLoop);
		return source;
	}
	public static String stripHTMLTags(String s) {
		return s.replaceAll("\\<[^>]*>","");
	}
	public static String unicodeParse(String s) {
		Matcher m = unicodePattern.matcher(s);
		while (m.find()) {
			int count = m.groupCount();
			if (count == 1) {
				short hex = Short.valueOf(m.group(1),16);
				s = s.replaceAll("\\\\u"+m.group(1),Character.toString((char)hex));
			}
			m = unicodePattern.matcher(s);
		}
		return s;
	}
	
	public static boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {return false;}
		return true;
	}
	
	public static boolean isBoolean(String s) {
		return (s.equalsIgnoreCase("true")||s.equalsIgnoreCase("false"));
	}
	
	public static String implode(String[] spl, String separator) {return implode(spl,0,spl.length-1,separator);}
	public static String implode(String[] spl, int a, String separator) {return implode(spl,a,spl.length-1,separator);}
	public static String implode(String[] spl, int a, int b, String separator) {
		StringBuffer sb = new StringBuffer();
		while (a <= b) {
			if (sb.length() != 0) sb.append(separator);
			sb.append(spl[a++]);
		}
		return sb.toString();
	}
}