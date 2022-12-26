package de.braack.streetguess_de.interpreter.rules;

import de.braack.streetguess_de.interpreter.regex.RegexAnalyzer;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexRuleChecker implements RuleChecker {

    private final Pattern regexPattern;
    private final RegexAnalyzer regexAnalyzer;

    public RegexRuleChecker(String regexRule) {
        this(Pattern.compile(regexRule));
    }

    public RegexRuleChecker(Pattern regexPattern) {
        this.regexPattern = regexPattern;
        this.regexAnalyzer = new RegexAnalyzer(regexPattern);
    }

    @Override
    public boolean checkRuleApplicable(@NonNull String input) {
        return regexPattern.matcher(input).find();
    }

    @Override
    public int getFittingScore() {
        final String regex = regexPattern.pattern();
        //# kleene operators .* or .*?
        final int noOfKleenes = regexAnalyzer.getNumberOfKleeneOperators();
        //# existence operators .+ or .+?
        final int noOfExistenceOps = regexAnalyzer.getNumberOfExistenceOperators();
        //# of replacers \s \w etc (not \\)
        final int noOfReplacers = regexAnalyzer.getNumberOfReplacers();
        //# of specifics: no kleene, no existence ops, no replacers, no braces or brackets

        return 0;
    }


}
