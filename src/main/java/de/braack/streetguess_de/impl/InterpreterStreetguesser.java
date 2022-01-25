package de.braack.streetguess_de.impl;

import de.braack.streetguess_de.api.Streetguesser;

/** {@inheritDoc} */
public class InterpreterStreetguesser implements Streetguesser {

    public String[] guessStreetNameAndAddress(String street) {
        return new String[0];
    }

    public String guessStreetName(String street) {
        return null;
    }

    public String guessHouseNumber(String street) {
        return null;
    }

    public String guessAppendix(String street) {
        return null;
    }
}
