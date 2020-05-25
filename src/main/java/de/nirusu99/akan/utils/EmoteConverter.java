package de.nirusu99.akan.utils;

public final class EmoteConverter {

    enum Emote {
        ZERO('0'),ONE('1'),TWO('2'),THREE('3'),FOUR('4'),
        FIVE('5'),SIX('6'),SEVEN('7'),EIGHT('8'),NINE('9'),
        QUESTION('?'),EXCLAMATION('!'),SPACE(' ') {
            @Override
            public String toString() {
                return "\n";
            }
        };

        private final char number;

        Emote(final char number) {
            this.number = number;
        }

        private static Emote getNumber(final char num) {
            for (Emote n : Emote.values()) {
                if (n.number == num) {
                    return n;
                }
            }
            return null;
        }


        @Override
        public String toString() {
            return ":"+ super.toString() + ":";
        }
    }

    private EmoteConverter() { }

    public static String convertRegionalIndicators(final String input) {
        char[] chars = input.toLowerCase().replace("ä","ae")
                .replace("ö","oe")
                .replace("ü","ue")
                .replace("ß","ss")
                .toCharArray();
        String[] output = new String[chars.length];
        for (int i = 0; i < output.length; i++) {
            Emote n = Emote.getNumber(chars[i]);
            if (n != null) {
                output[i] = n.toString().toLowerCase();
            } else {
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
