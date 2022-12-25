package de.braack.streetguess_de.interpreter.rules;

import lombok.NonNull;

public interface RuleChecker {

    boolean checkRuleApplicable(@NonNull final String input);

    /**
     * <p>Returns a scoring that tells how specific this rule is.</p>
     * <p>Basically speaking, a low score means that this rule is very broad and fits many strings. A high score means that
     * the rule is very selective and only fits few strings.</p>
     * <p>Also not that teh score itself doesn't mean a lot. What's important is how it compares to other scores.</p>
     * <p>For example:
     * <table>
     *     <tr>
     *         <th>Ruletype</th>
     *         <th>Rule</th>
     *         <th>Score</th>
     *     </tr>
     *     <tr>
     *         <td>Regex</td>
     *         <td>.*</td>
     *         <td>extremely low</td>
     *     </tr>
     *     <tr>
     *         <td>Regex</td>
     *         <td>.+</td>
     *         <td>low</td>
     *     </tr>
     *     <tr>
     *         <td>Literals</td>
     *         <td>(article) (freetext)</td>
     *         <td>medium</td>
     *     </tr>
     *     <tr>
     *         <td>Regex</td>
     *         <td>\d+((-|/)\d+|[a-z]((/|-)[a-z])?)?</td>
     *         <td>high</td>
     *     </tr>
     * </table></p>
     * @return The fittingness score, as outlined above
     */
    int getFittingScore();


}
