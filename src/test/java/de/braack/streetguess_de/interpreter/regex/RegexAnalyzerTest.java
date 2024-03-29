package de.braack.streetguess_de.interpreter.regex;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the class {@link RegexAnalyzer}
 */
@Slf4j
public class RegexAnalyzerTest {

    private static final String testString = "easy (alter|native)\\p{javaLowerCase}\\p{javaCowerLase}\\t{2}{ad-fG-H}.+?and\\s.*.*?.*a Spac(e|er){9,}";

    private RegexAnalyzer createRegexAnalyzer(final String regex) {
        return new RegexAnalyzer(regex);
    }

    static Stream<Arguments> PROVIDER_cnoko_withValidInput_returnsCorrectNumber() {
        return Stream.of(
                Arguments.of(".*", 1),
                Arguments.of(".*?", 1),
                Arguments.of(".*?.*", 2),
                Arguments.of("\\.*.*?", 1),
                Arguments.of("\\\\.*.*?", 2),
                Arguments.of(testString, 3)
        );
    }

    static Stream<Arguments> PROVIDER_cnoeo_withValidInput_returnsCorrectNumber() {
        return Stream.of(
                Arguments.of(".+", 1),
                Arguments.of(".+?", 1),
                Arguments.of(".+?.+", 2),
                Arguments.of("\\.+.+?", 1),
                Arguments.of("\\\\.+.+?", 2),
                Arguments.of(testString, 1)
        );
    }

    static Stream<Arguments> PROVIDER_cnor_withValidInput_returnsCorrectNumber() {
        return Stream.of(
                Arguments.of("\\s", 1),
                Arguments.of("\\p{InGreek}", 1),
                Arguments.of("\\w\\W", 2),
                Arguments.of(testString, 3),
                Arguments.of("012\\012\\07\\0377\\xDA\\xB0\\t\\T\\\\e", 6), //\\012, \\07, \\0377\, \\xDA, \\xB0, \\t = 6 replacers
                Arguments.of("\\p{IsLatin}\\p{IsAlphabetic}\\S\\p{HahaNope}\\s\\09", 4),
                Arguments.of("n\\n\\Nn\\nt\\ente", 3)
        );
    }

    static Stream<Arguments> PROVIDER_elfr_withGoodRegexInput_returnCorrectLiteralsArrays() {
        return Stream.of(
                Arguments.of("abcde", new String[][]{ new String[]{"abcde"} }),
                Arguments.of("abc def", new String[][]{ new String[]{"abc def"} }),
                Arguments.of("abc\\sdef", new String[][]{ new String[]{"abc"}, new String[]{"def"} }),
                Arguments.of("abc\\\\sdef", new String[][]{ new String[]{"abc\\\\sdef"} }),
                Arguments.of("\\tabc", new String[][]{ new String[]{"abc"} }),
                Arguments.of("abc\\t",  new String[][]{ new String[]{"abc"} }),
                Arguments.of("abc\\tone\\Wtwo\\p{IsLatin}three", new String[][]{ new String[]{"abc"}, new String[]{"one"}, new String[]{"two"}, new String[]{"three"}}),
                Arguments.of("doof(mann|frau|person)", new String[][]{ new String[]{"doof"}, new String[]{"mann", "frau", "person"} }),
                Arguments.of("doofperson()", new String[][]{ new String[]{"doofperson"} }),
                Arguments.of("illusion of choice: (|)", new String[][]{ new String[]{"illusion of choice: "} }),
                Arguments.of("select wisely [aeiou]", new String[][]{ new String[]{"select wisely "}, new String[]{"a", "e", "i", "o", "u"} }),
                Arguments.of("don't select at all []", new String[][]{ new String[]{"don't select at all "} }),
                Arguments.of("(|||)brackets(|)", new String[][]{ new String[]{"brackets"} }),
                Arguments.of("[]uhhhh[]", new String[][]{ new String[]{"uhhhh"} })
        );
    }

    @Nested
    @DisplayName("Tests for calculateNumberOfKleeneOperators()")
    class CalculateNumberOfKleeneOperators {

        @ParameterizedTest
        @MethodSource("de.braack.streetguess_de.interpreter.regex.RegexAnalyzerTest#PROVIDER_cnoko_withValidInput_returnsCorrectNumber")
        @DisplayName("Good case test: Finding right number of kleene operators")
        void cnoko_withValidInput_returnsCorrectNumber(final String regex, final int expectedKleeneOperators) {
            //Arrange
            final RegexAnalyzer regexAnalyzer = createRegexAnalyzer(regex);

            //Act
            final int calculatedKleenes = regexAnalyzer.calculateNumberOfKleeneOperators();

            //Assert
            assertEquals(expectedKleeneOperators, calculatedKleenes);
        }

        @Test
        @DisplayName("Using an invalid regex, the number of kleene operators are still found")
        void cnoko_withInvalidRegex_returnsCorrectNumber() {
            //Arrange
            final String badRegex = "addnw.*fw{veve=3.*?..*";
            final int expectedKleenes = 3;
            final RegexAnalyzer regexAnalyzer = createRegexAnalyzer(badRegex);

            //Act
            final int calculatedKleenes = regexAnalyzer.calculateNumberOfKleeneOperators();

            //Assert
            assertEquals(expectedKleenes, calculatedKleenes);
        }

        @Test
        @DisplayName("Using an empty string, the method returns 0")
        void cnoko_withEmptyRegexString_returnsZero(){
            //Arrange
            final String badRegex = "";
            final int expectedKleenes = 0;
            final RegexAnalyzer regexAnalyzer = createRegexAnalyzer(badRegex);

            //Act
            final int calculatedKleenes = regexAnalyzer.calculateNumberOfKleeneOperators();

            //Assert
            assertEquals(expectedKleenes, calculatedKleenes);
        }

        @Test
        @DisplayName("Using a regex without Kleene operators, the method returns 0")
        void cnoko_withoutKleeneOperators_returnsZero(){
            //Arrange
            final String badRegex = "this string has no (Kleene|kleene) Operators";
            final int expectedKleenes = 0;
            final RegexAnalyzer regexAnalyzer = createRegexAnalyzer(badRegex);

            //Act
            final int calculatedKleenes = regexAnalyzer.calculateNumberOfKleeneOperators();

            //Assert
            assertEquals(expectedKleenes, calculatedKleenes);
        }
    }

    @Nested
    @DisplayName("Tests for calculateNumberOfExistenceOperators()")
    class CalculateNumberOfExistenceOperators {

        @ParameterizedTest
        @MethodSource("de.braack.streetguess_de.interpreter.regex.RegexAnalyzerTest#PROVIDER_cnoeo_withValidInput_returnsCorrectNumber")
        @DisplayName("Good case test: Finding right number of existence operators")
        void cnoeo_withValidInput_returnsCorrectNumber(final String regex, final int expectedKleeneOperators) {
            //Arrange
            final RegexAnalyzer regexAnalyzer = createRegexAnalyzer(regex);

            //Act
            final int calculatedExistenceOperators = regexAnalyzer.calculateNumberOfExistenceOperators();

            //Assert
            assertEquals(expectedKleeneOperators, calculatedExistenceOperators);
        }

        @Test
        @DisplayName("Using an invalid regex, the number of existence operators are still found")
        void cnoeo_withInvalidRegex_returnsCorrectNumber() {
            //Arrange
            final String badRegex = "addnw.+fw{veve=3.+?..+";
            final int expectedExistenceOperators = 3;
            final RegexAnalyzer regexAnalyzer = createRegexAnalyzer(badRegex);

            //Act
            final int calculatedExistenceOperators = regexAnalyzer.calculateNumberOfExistenceOperators();

            //Assert
            assertEquals(expectedExistenceOperators, calculatedExistenceOperators);
        }

        @Test
        @DisplayName("Using an empty string, the method returns 0")
        void cnoeo_withEmptyRegexString_returnsZero(){
            //Arrange
            final String badRegex = "";
            final int expectedExistenceOperators = 0;
            final RegexAnalyzer regexAnalyzer = createRegexAnalyzer(badRegex);

            //Act
            final int calculatedExistenceOperators = regexAnalyzer.calculateNumberOfExistenceOperators();

            //Assert
            assertEquals(expectedExistenceOperators, calculatedExistenceOperators);
        }

        @Test
        @DisplayName("Using a regex without existence operators, the method returns 0")
        void cnoeo_withoutExistenceOperators_returnsZero(){
            //Arrange
            final String badRegex = "this string has no (x6tnce|existence) Operators";
            final int expectedExsistenceOperators = 0;
            final RegexAnalyzer regexAnalyzer = createRegexAnalyzer(badRegex);

            //Act
            final int calculatedExistenceOperators = regexAnalyzer.calculateNumberOfExistenceOperators();

            //Assert
            assertEquals(expectedExsistenceOperators, calculatedExistenceOperators);
        }
    }

    @Nested
    @DisplayName("Tests for calculateNumberOfReplacers()")
    class CalculateNumberOfReplacers {

        @ParameterizedTest
        @MethodSource("de.braack.streetguess_de.interpreter.regex.RegexAnalyzerTest#PROVIDER_cnor_withValidInput_returnsCorrectNumber")
        @DisplayName("Good case test: Finding right number of replacers")
        void cnor_withValidInput_returnsCorrectNumber(final String regex, final int expectedKleeneOperators) {
            //Arrange
            final RegexAnalyzer regexAnalyzer = createRegexAnalyzer(regex);

            //Act
            final int calculatedReplacers = regexAnalyzer.calculateNumberOfReplacers();

            //Assert
            assertEquals(expectedKleeneOperators, calculatedReplacers);
        }

        @Test
        @DisplayName("Using an invalid regex, the number of replacers are still found")
        void cnor_withInvalidRegex_returnsCorrectNumber() {
            //Arrange
            final String badRegex = "add\\s\\tnw.+fw{\\svev\\\\ne=3.+?..+";
            final int expectedReplacers = 3;
            final RegexAnalyzer regexAnalyzer = createRegexAnalyzer(badRegex);

            //Act
            final int calculatedReplacers = regexAnalyzer.calculateNumberOfReplacers();

            //Assert
            assertEquals(expectedReplacers, calculatedReplacers);
        }

        @Test
        @DisplayName("Using an empty string, the method returns 0")
        void cnor_withEmptyRegexString_returnsZero(){
            //Arrange
            final String badRegex = "";
            final int expectedReplacers = 0;
            final RegexAnalyzer regexAnalyzer = createRegexAnalyzer(badRegex);

            //Act
            final int calculatedReplacers = regexAnalyzer.calculateNumberOfReplacers();

            //Assert
            assertEquals(expectedReplacers, calculatedReplacers);
        }

        @Test
        @DisplayName("Using a regex without replacers, the method returns 0")
        void cnor_withoutReplacers_returnsZero(){
            //Arrange
            final String badRegex = "this string has no (re|placer) Operators";
            final int expectedReplacers = 0;
            final RegexAnalyzer regexAnalyzer = createRegexAnalyzer(badRegex);

            //Act
            final int calculatedReplacers = regexAnalyzer.calculateNumberOfReplacers();

            //Assert
            assertEquals(expectedReplacers, calculatedReplacers);
        }
    }

    @Nested
    @DisplayName("Tests for extractLiteralsFromRegex()")
    class ExtractLiteralsFromRegex {

        @ParameterizedTest
        @MethodSource("de.braack.streetguess_de.interpreter.regex.RegexAnalyzerTest#PROVIDER_elfr_withGoodRegexInput_returnCorrectLiteralsArrays")
        void elfr_withGoodRegexInput_returnCorrectLiteralsArrays(final String inputRegex, final String[][] expectedLiterals) {
            final RegexAnalyzer regexAnalyzer = new RegexAnalyzer(inputRegex);

            final String[][] actualLiterals = regexAnalyzer.extractLiteralsFromRegex();

            printActualResult(actualLiterals);

            assertArrayEquals(expectedLiterals, actualLiterals);
        }

        private void printActualResult(final String[][] literals){
            StringBuilder printable = new StringBuilder("The actual result is: { ");

            for(String[] literalArray: literals){
                printable.append(Arrays.toString(literalArray)).append(", ");
            }

            printable.replace(printable.length() - 2, printable.length(), "");
            printable.append(" }");

            log.info(printable.toString());
        }
    }
}
