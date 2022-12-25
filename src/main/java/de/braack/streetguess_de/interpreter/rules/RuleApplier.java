package de.braack.streetguess_de.interpreter.rules;

import lombok.NonNull;

public interface RuleApplier<T> extends RuleChecker {

    T applyRule(@NonNull String input);
}
