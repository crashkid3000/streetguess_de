package de.braack.streetguess_de.interpreter.regex;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the class {@link RegexAnalyzer}
 */
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
}
