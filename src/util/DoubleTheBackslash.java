/**
 * @author Hugo Porras
 * 
 * Acknowledgements:
 * This work used resources from Cyber-ShARE Center of Excellence, 
 * which is supported by National Science Foundation grant number 
 * HRD-0734825. Unless otherwise stated, work by Cyber-ShARE is 
 * licensed under a Creative Commons Attribution 3.0 Unported 
 * License. Permissions beyond the scope of this license may be 
 * available at 
 * http://cybershare.utep.edu/content/cyber-share-acknowledgment.
 */

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