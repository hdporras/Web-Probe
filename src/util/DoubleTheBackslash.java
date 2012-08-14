package util;

public class DoubleTheBackslash
{
	public static void main(String[] args)
	{
		String[] inputs = {"asdf\\sdfaga\"asdfasd\"cvzxcv", "abc\\def", "abc\\def\\\\ghi"};

		JSONUtils.toValidJSONString(inputs[0]);
		System.out.println();
		JSONUtils.toValidJSONString(inputs[1]);
		System.out.println();
		JSONUtils.toValidJSONString(inputs[2]);
		System.out.println();
		
		showReplacements(inputs, "\"", "\\\\\"");
		showReplacements(inputs, "\\\\", "\\\\\\\\");
		showReplacements(inputs, "\\\\+", "\\\\\\\\");
		showReplacements(inputs, "(?<!\\\\)\\\\(?!\\\\)", "\\\\\\\\");
		showReplacements(inputs, "(?<!\\\\)\\\\(?!\\\\)", "$0$0");
		showReplacements(inputs, "([^\\\\])(\\\\)([^\\\\])", "$1\\\\\\\\$3");
		showReplacements(inputs, "([^\\\\])(\\\\)([^\\\\])", "$1$2$2$3");
	}

	private static void showReplacements(String[] inputs,
			String regex, String replaceWith) {
		for (String input : inputs) {
			System.out.println(input);
			System.out.println(input.replaceAll(regex, replaceWith));
			System.out.println();
		}
	}
}