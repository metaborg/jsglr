package java.lang;

public final class Character2 {
    public static boolean isLetter(int i)
    {
    	return ((i > 64 && i < 91) || (i > 96 && i < 123));
    }
    public static boolean isDigit(int i)
    {
    	return ((i > 47) && (i < 58));
    }
    public static boolean isLetterOrDigit(int i)
    {
    	return isLetter(i) || isDigit(i);
    }
}
