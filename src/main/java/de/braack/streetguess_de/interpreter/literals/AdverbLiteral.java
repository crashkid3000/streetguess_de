package de.braack.streetguess_de.interpreter.literals;

public class AdverbLiteral extends Literal {

    private boolean hasArticle;

    public AdverbLiteral(String value, boolean hasArticle) {
        super(value);
        this.hasArticle = hasArticle;
    }

    public boolean hasArticle() {
        return hasArticle;
    }
}
