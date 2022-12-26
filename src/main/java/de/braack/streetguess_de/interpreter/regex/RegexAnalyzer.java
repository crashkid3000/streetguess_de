package de.braack.streetguess_de.interpreter.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Performs analysis on regexes
 */
public class RegexAnalyzer {

    private static final String[] KLEENE_OPERATOR_LIST = {"\\.\\*", "\\.\\*\\?"};
    private static final String[] EXISTENCE_OPERATOR_LIST = {"\\.\\+", "\\.\\+\\?"};
    private static final String[] REPLACERS_LIST = {"\\\\0\\([0-3])?[0-7]{1,2}", //octal numbers
            "\\\\x\\[0-9a-fA-F]{2}", //hexadecimal numbers
            "\\\\u\\[0-9a-fA-F]{4}", //hexadecimal numbers too
            "\\\\{tnrfaedDsSwW}", //other control characters
            "\\\\c[a-zA-Z]", //"The control character corresponding to [[a-zA-Z]]" -- ??? ... but it's here regardless, so we gotta consider it
            "\\\\p\\{(Lower|Upper|ASCII|Alpha|Digit|Alnum|Punct|Graph|Print|Blank|Cntrl|XDigit|Space|" + //Posix characters...
                    "javaLowerCase|javaUpperCase|javaWhitespace|javaMirrored|" + //...and java.lang.Character classes...
                    "IsLatin|InGreek|Lu|IsAlphabetic|Sc)\\}", //...and classes for Unicode scripts, blocks, categories and binary properties
            "\\\\P\\{InGreek\\}"};

    private final String searchTarget;

    public RegexAnalyzer(String regexString) {
        this.searchTarget = regexString;
    }

    public RegexAnalyzer(Pattern pattern) {
        this.searchTarget = pattern.pattern();
    }

    /**
     * Checks how many times any of the given regexes matches the given string
     * @param regexes The list of regex strings that are applied on the string one after another
     * @return How many times any of the givn <code>regexes</code> could be applied
     */
    public int getNumberOfAnyMatchingRegexes(final String... regexes) {
        int count = 0;
        final List<Matcher> matchers = new LinkedList<>();
        for(String regex: regexes){
            matchers.add(Pattern.compile(regex).matcher(this.searchTarget));
        }

        for(Matcher matcher: matchers) {
            while(matcher.find()){
                count++;
            }
        }

        return count;
    }

    /**
     * Checks how many Kleene operators are within the given string: .* or .*?
     * @return The number of Kleene operators
     */
    public int getNumberOfKleeneOperators(){
        return getNumberOfAnyMatchingRegexes(KLEENE_OPERATOR_LIST);
    }

    /**
     * Returns hoe many character existence operators exist: .+ or .+?
     * @return The number of existence operators
     */
    public int getNumberOfExistenceOperators() {
        return getNumberOfAnyMatchingRegexes(EXISTENCE_OPERATOR_LIST);
    }

    /**
     * Returns how many replacer characters exist: \s, \S, \0<i>nnn</i> etc.
     * @return The number of replacer chars
     */
    public int getNumberOfReplacers() {
        //List of regexes manually compiled taken from https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
        return getNumberOfAnyMatchingRegexes(REPLACERS_LIST); //Any character except one in the Greek block (negation)
    }

    /**
     * <p>Returns the literals that could be found inside the string.</p>
     * <p>The first index represents different "groups" of literals ("group" as <i>an assortment of something</i>, a set,
     * a collection – not as in a capturing group). The second index represents the literals within a "group"</p>
     * <p>Such a "group" is consists of more than one element if ne uses the "alternate"-expression of Regex: Either
     * <code>{...}</code> for single character alternatives, or the <code>(...|...|...)</code> notation when </p>
     * <p><u>Example 1:</u><br/>
     * Input: <code>a{3}b+?word</code><br>
     * Return value: <code>{{a}, {b}, {word}}</code></p>
     * <p><u>Example 2:</u><br/>
     * Input: <code>(a|alpha){3}b+?{word}</code><br/>
     * Return value: {{a, alpha}, {b}, {w, o, r, d}}
     * </p>
     * @return the literals that could be found inside the string
     */
    public String[][] getLiteralsFromRegex() {
        final Matcher group1Regex = Pattern.compile("\\(.+?(\\|.+?)*?\\)").matcher(this.searchTarget);
        final Matcher group2Regex = Pattern.compile("\\{(\\w(-\\w)?)+?\\}").matcher(this.searchTarget);
        final List<String[]> retVal = new ArrayList<>();

        List<String> groups = new ArrayList<>();
        while(group1Regex.find()) {
            String group1MatchingResult = group1Regex.group();
            group1MatchingResult = group1MatchingResult.substring(1, group1MatchingResult.length() - 1); //remove surrounding ()
            retVal.add(group1MatchingResult.split("\\|\\s*?"));
        }
        while(group2Regex.find()) {
            String group2MatchingResult = group2Regex.group();
            group2MatchingResult = group2MatchingResult.substring(1, group2MatchingResult.length() - 1);

            final String[] group2atchingResultArray = new String[group2MatchingResult.length()];
            for(int i = 0; i < group2atchingResultArray.length; i++){
                group2atchingResultArray[i] = group2MatchingResult.charAt(i) + "";
            }
            retVal.add(group2atchingResultArray);
        }

        //todo: filter out kleenes, existence ops, replaces, repetitions ({x,n} etc)

        return retVal.toArray(String[][]::new);
    }
}
