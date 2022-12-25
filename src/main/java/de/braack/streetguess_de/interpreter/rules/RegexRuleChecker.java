package de.braack.streetguess_de.interpreter.rules;

import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexRuleChecker implements RuleChecker {

    private final Pattern regexPattern;

    public RegexRuleChecker(String regexRule) {
        this(Pattern.compile(regexRule));
    }

    public RegexRuleChecker(Pattern regexPattern) {
        this.regexPattern = regexPattern;
    }

    @Override
    public boolean checkRuleApplicable(@NonNull String input) {
        return regexPattern.matcher(input).find();
    }

    @Override
    public int getFittingScore() {
        final String regex = regexPattern.pattern();
        //# kleene operators .* or .*?
        final int noKleenes = regex.
        //# existence operators .+ or .+?
        //# of replacers \s \w etc (not \\)
        //# of specifics: no kleene, no existence ops, no replacers, no braces or brackets





        regexPattern
        return 0;
    }

    private int getNumberOfKleeneOperators(final String regex){
        return getNumberOfAnyMatchingRegexes(regex, "\\.\\*");
    }

    /**
     * Checks how many time any of the given regexes matches the given string
     * @param searchTarget
     * @param regexes
     * @return
     */
    private int getNumberOfAnyMatchingRegexes(final String searchTarget, final String... regexes) {
        int count = 0;
        final List<Matcher> matchers = new LinkedList<>();
        for(String regex: regexes){
            matchers.add(Pattern.compile(regex).matcher(searchTarget));
        }

        for(Matcher matcher: matchers) {
            while(matcher.find()){
                count++;
            }
        }

        return count;
    }
}
