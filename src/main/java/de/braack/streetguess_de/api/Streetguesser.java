package de.braack.streetguess_de.api;

/** <p>Implementations of this interface try to <i>guess</i> a street name, house address and/or address appendix from a
 * given input String that represents a German street address. This does not prove that a given street address actually exists,
 * just that there is a good chance that in some city somewhere in Germany, it could. Street addresses from other
 * countries may work too, however, no promises are given.</p>
 *
 * <p>Common with all offered methods is their input string. They all have to follow the pattern of <code>&lt;street name> &lt;house number> &lt;appendix></code>,
 * with <code>appendix</code> being an optional part. For instance, <code>"Platz der Republik 1"</code> and <code>"Spreeweg 1"</code> are accepted inputs
 * as well as <code>"Unbekanntstraße 43 Hinterhaus"</code>.</p>
 */
public interface Streetguesser {

    String[] guessStreetNameAndAddress(String street);
    String guessStreetName(String street);
    String guessHouseNumber(String street);
    String guessAppendix(String street);
}
