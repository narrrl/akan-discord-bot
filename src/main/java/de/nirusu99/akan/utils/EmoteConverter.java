package de.nirusu99.akan.utils;

public class EmoteConverter {
    public static String convertRegionalIndicators(final String input) {
        char[] chars = input.toLowerCase().replace("ä","ae")
                .replace("ö","oe")
                .replace("ü","ue")
                .replace("ß","ss")
                .toCharArray();
        String[] output = new String[chars.length];
        for (int i = 0; i < output.length; i++) {
            switch (chars[i]) {
                case '0':
                    output[i] = ":zero:";
                    break;
                case '1':
                    output[i] = ":one:";
                    break;
                case '2':
                    output[i] = ":two:";
                    break;
                case '3':
                    output[i] = ":three:";
                    break;
                case '4':
                    output[i] = ":four:";
                    break;
                case '5':
                    output[i] = ":five:";
                    break;
                case '6':
                    output[i] = ":six:";
                    break;
                case '7':
                    output[i] = ":seven:";
                    break;
                case '8':
                    output[i] = ":eight:";
                    break;
                case '9':
                    output[i] = ":nine:";
                    break;
                case ' ':
                    output[i] = " ";
                    break;
                default:
                    output[i] = ":regional_indicator_" + chars[i] + ":";
            }
        }
        StringBuilder out = new StringBuilder();
        for (String str : output) {
            out.append(str);
        }
        return out.toString();
    }
}
