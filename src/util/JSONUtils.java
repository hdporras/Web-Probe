package util;

public class JSONUtils
{
	public static String toValidJSONString(String input)
	{
		if(input != null)
		{
			String regex="\\\\";
			String replaceWith = "\\\\\\\\";
			System.out.println(input);
			input = input.replaceAll(regex, replaceWith);
			System.out.println(input);
			input = input.replaceAll("\"", "\\\\\"");
			System.out.println(input);

			return input;
		}
		else
			return "null";

	}
}
