package de.braack.streetguess_de.interpreter.regex;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Performs analysis on regexes
 */
@Slf4j
public class RegexAnalyzer {

    private static final String[] KLEENE_OPERATOR_LIST = {"(^\\.|(?<=\\\\)\\\\\\.|(?<!\\\\)\\.)\\*\\??"};
    private static final String[] EXISTENCE_OPERATOR_LIST = {"(^\\.|(?<=\\\\)\\\\\\.|(?<!\\\\)\\.)\\+\\??"};
    private static final String[] REPLACERS_LIST = createReplacersListArray();

    private final String searchTarget;

    public RegexAnalyzer(@NonNull String regexString) {
        this.searchTarget = regexString;
    }

    public RegexAnalyzer(@NonNull Pattern pattern) {
        this.searchTarget = pattern.pattern();
    }

    private static String[] createReplacersListArray(){
        final String start = "(^\\\\|(?<=\\\\)\\\\\\\\|(?<!\\\\)\\\\)";
        return new String[]{ start + "0([0-3])?[0-7]{1,2}", //octal numbers
                start + "x[0-9a-fA-F]{2}", //hexadecimal numbers
                start + "u[0-9a-fA-F]{4}", //hexadecimal numbers too
                start + "[tnrfaedDsSwW]", //other control characters
                start + "c[a-zA-Z]", //"The control character corresponding to [[a-zA-Z]]" -- ??? ... but it's here regardless, so we gotta consider it
                start + "p\\{(Lower|Upper|ASCII|Alpha|Digit|Alnum|Punct|Graph|Print|Blank|Cntrl|XDigit|Space|" + //Posix characters...
                        "javaLowerCase|javaUpperCase|javaWhitespace|javaMirrored|" + //...and java.lang.Character classes...
                        "IsLatin|InGreek|Lu|IsAlphabetic|Sc)\\}", //...and classes for Unicode scripts, blocks, categories and binary properties
                start + "P\\{InGreek\\}"};
    }

    /**
     * Checks how many times any of the given regexes matches the given string
     * @param regexes The list of regex strings that are applied on the string one after another
     * @return How many times any of the given <code>regexes</code> could be applied
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
                log.trace("Found matched: " + matcher.group());
            }
        }

        return count;
    }

    /**
     * Checks how many Kleene operators are within the given string: .* or .*?
     * @return The number of Kleene operators
     */
    public int calculateNumberOfKleeneOperators(){
        return getNumberOfAnyMatchingRegexes(KLEENE_OPERATOR_LIST);
    }

    /**
     * Returns hoe many character existence operators exist: .+ or .+?
     * @return The number of existence operators
     */
    public int calculateNumberOfExistenceOperators() {
        return getNumberOfAnyMatchingRegexes(EXISTENCE_OPERATOR_LIST);
    }

    /**
     * Returns how many replacer characters exist: \s, \S, \0<i>nnn</i> etc.
     * @return The number of replacer chars
     */
    public int calculateNumberOfReplacers() {
        //List of regexes manually compiled, taken from https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
        return getNumberOfAnyMatchingRegexes(REPLACERS_LIST); //Any character except one in the Greek block (negation)
    }

    /**
     * <p>Returns the literals that could be found inside the string.</p>
     * <p>The first index represents different "groups" of literals ("group" as <i>an assortment of something</i>, a set,
     * a collection – not as in a capturing group). The second index represents the literals within a "group"</p>
     * <p>Such a "group" is consists of more than one element if one uses the "alternate"-expression of Regex: Either
     * <code>{...}</code> for single character alternatives, or the <code>(...|...|...)</code> notation when creating an
     * "alternative" selection for Strings.</p>
     * <p><u>Example 1:</u><br/>
     * Input: <code>a{3}b+?word</code><br>
     * Return value: <code>{{a}, {b}, {word}}</code></p>
     * <p><u>Example 2:</u><br/>
     * Input: <code>(a|alpha){3}b+?{word}</code><br/>
     * Return value: <code>{{a, alpha}, {w, o, r, d}, {b}}</code>
     * </p>
     * <p>Finally, beware that the literals in the return value will likely <b>not</b> be in the order that they appeared
     * in the original regex String (as shown in Example 2)</p>
     * @return The literals that could be found inside the string. They won't be in the order they appeared in the
     * original regex.
     */
    public String[][] extractLiteralsFromRegex() {
        return new LiteralsExtractor().extractLiterals();
    }

    private class LiteralsExtractor {

        public String[][] extractLiterals(){
            final List<String[]> retVal = new ArrayList<>();

            this.addStringGroupLiterals(retVal);
            this.addCharacterGroupLiterals(retVal);

            final List<String> operatorsToDelete = generateDeletableOperatorsList();

            String[] joinedSplitResultsArray = new String[]{RegexAnalyzer.this.searchTarget + ""};

            for(final String operatorToDelete: operatorsToDelete){
                int totalLength = 0;
                List<String[]> splitResultList = new ArrayList<>();

                for(String joinedSplitResult: joinedSplitResultsArray){
                    final String[] splitResult = joinedSplitResult.split(operatorToDelete);
                    splitResultList.add(splitResult);
                    totalLength += splitResult.length;
                }

                joinedSplitResultsArray = flattenArrayList(splitResultList, totalLength);

            }

            for(String literal: joinedSplitResultsArray){
                if(literal != null && !literal.equals("")){ //Clean up result already when we are inserting values
                    retVal.add(new String[]{literal});
                }

            }

            return retVal.toArray(String[][]::new);
        }

        private void addStringGroupLiterals(final List<String[]> literalArrays) {
            final Matcher group1Regex = Pattern.compile("\\(.+?(\\|.+?)*?\\)").matcher(RegexAnalyzer.this.searchTarget);
            while(group1Regex.find()) {
                String group1MatchingResult = group1Regex.group();
                group1MatchingResult = group1MatchingResult.substring(1, group1MatchingResult.length() - 1); //remove surrounding ()
                String[] group11MatchingResultArray = group1MatchingResult.split("\\|\\s*?");
                group11MatchingResultArray = cleanUpArray(group11MatchingResultArray);
                literalArrays.add(group11MatchingResultArray);
            }
        }

        private void addCharacterGroupLiterals(final List<String[]> literalArrays) {
            final Matcher group2Regex = Pattern.compile("\\[(\\w(-\\w)?)+?]").matcher(RegexAnalyzer.this.searchTarget);
            while(group2Regex.find()) {
                String group2MatchingResult = group2Regex.group();
                group2MatchingResult = group2MatchingResult.substring(1, group2MatchingResult.length() - 1);

                final String[] group2MatchingResultArray = new String[group2MatchingResult.length()];
                for(int i = 0; i < group2MatchingResultArray.length; i++){
                    group2MatchingResultArray[i] = group2MatchingResult.charAt(i) + "";
                }
                //todo: implement cleanup for group2 matching (similar to group1 in result)
                literalArrays.add(group2MatchingResultArray);
            }
        }

        private List<String> generateDeletableOperatorsList() {
            final List<String> operatorsToDelete = new ArrayList<>(Arrays.asList(KLEENE_OPERATOR_LIST));
            operatorsToDelete.addAll(Arrays.asList(EXISTENCE_OPERATOR_LIST));
            operatorsToDelete.addAll(Arrays.asList(REPLACERS_LIST));
            operatorsToDelete.add("\\{\\d(,(\\d)?)?\\}"); //repetitions such as X{3}, X{3,} or X{3,5}
            operatorsToDelete.add("\\*(\\?)?"); //Kleene star (0+ repetitions)
            operatorsToDelete.add("\\+(\\?)?"); //1+ repetitions
            operatorsToDelete.add("\\(\\?(\\=\\w+)\\)"); //capturing groups
            return operatorsToDelete;
        }

        /**
         * Flattens a list of String arrays to a single String array, preserving the order they appeared in.
         * @param toBeFlattened The list of String arrays that shall be flattened
         * @param finalArraySize The expected array size of the final array (if you already know it)
         * @return The flattened array
         */
        private String[] flattenArrayList(final List<String[]> toBeFlattened, int finalArraySize){
            final String[] retVal = new String[finalArraySize];
            int indexCounter = 0;
            for (final String[] innerArray : toBeFlattened) {
                for (final String splitResult: innerArray) {
                    retVal[indexCounter] = splitResult;
                    indexCounter++;
                }
            }
            return retVal;
        }

        /**
         * Cleans the given array by putting it into a new removing elements that are <code>null</code> or empty.
         * @param toBeCleaned The array that will be cleaned
         * @return The cleaned array
         */
        private String[] cleanUpArray(String[] toBeCleaned) {
            final List<String> temp = new ArrayList<>();
            for(String string: toBeCleaned) {
                if(string != null && !string.equals("")){
                    temp.add(string);
                }
            }

            toBeCleaned = temp.toArray(String[]::new);

            return toBeCleaned;
        }

    }
}
