package in.lokeshkaushik.to_do_app.service;

public class LexRankService {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int BASE = ALPHABET.length();
    public static final String INITIAL_RANK = "i00000"; // start from mid so we have space on both ends
    private static final int RANK_LENGTH = 6;
    private static final char MIN_RANK_CHAR = ALPHABET.charAt(0);
    private static final char MAX_RANK_CHAR = ALPHABET.charAt(BASE - 1);

    public static final String MIN_RANK = String.valueOf(MIN_RANK_CHAR).repeat(RANK_LENGTH);
    public static final String MAX_RANK = String.valueOf(MAX_RANK_CHAR).repeat(RANK_LENGTH);


    public static String calculateLexMIdPoint(String left, String right) {
        left = padRight(left, MIN_RANK_CHAR);
        right = padRight(right, MAX_RANK_CHAR);

        if(left.compareTo(right) >= 0){
            throw new IllegalArgumentException("Invalid ranks provided");
        }

        char[] result = new char[RANK_LENGTH];
        boolean midPointChosen = false;

        for(int i = 0; i < RANK_LENGTH; i++){
            int L = ALPHABET.indexOf(left.charAt(i));
            int R = ALPHABET.indexOf(right.charAt(i));

            if(!midPointChosen) {
                if(R - L > 1){
                    result[i] = ALPHABET.charAt((L + R) / 2);
                    midPointChosen = true;
                }
                else{
                    result[i] = left.charAt(i);
                }
            } else {
                result[i] = ALPHABET.charAt(BASE / 2);
            }
        }

        String mid = new String(result);

        if(!(left.compareTo(mid) < 0 && mid.compareTo(right) < 0)) {
            throw new IllegalStateException("Failed to generate midpoint for lex rank");
        }

        return mid;
    }

    private static String padRight(String s, char pad) {
        if (s.length() >= LexRankService.RANK_LENGTH) return s;
        return s + String.valueOf(pad).repeat(LexRankService.RANK_LENGTH - s.length());
    }
}
