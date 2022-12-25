package de.braack.streetguess_de.interpreter.rules;

import de.braack.streetguess_de.interpreter.literals.Literal;
import lombok.NonNull;

import java.util.List;

public class LiteralsRuleApplier implements RuleApplier<List<Literal>> {
    @Override
    public List<Literal> applyRule(@NonNull String input) {
        return null;
    }

    @Override
    public boolean checkRuleApplicable(@NonNull String input) {
        return false;
    }

    @Override
    public int getFittingScore() {
        return 0;
    }
}
