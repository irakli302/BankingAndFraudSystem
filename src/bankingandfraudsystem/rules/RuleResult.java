package bankingandfraudsystem.rules;

public class RuleResult {
    private final Decision decision;
    private final String reason;

    private RuleResult(Decision dec, String reas) {
        if(dec == null) throw new IllegalArgumentException("Decision cannot be null!");

        if(dec == Decision.ALLOW) this.reason = null;
        else {
            if(reas == null || reas.isEmpty()) throw new IllegalArgumentException("Reason cannot be empty!");
            this.reason = reas;
        }
        this.decision = dec;
    }

    public static RuleResult allow() {
        return new RuleResult(Decision.ALLOW, null);
    }

    public static RuleResult review(String reason) {
        return new RuleResult(Decision.REVIEW, reason);
    }

    public static RuleResult block(String reason) {
        return  new RuleResult(Decision.BLOCK, reason);
    }

    public boolean isAllow() {
        return this.decision == Decision.ALLOW;
    }

    public boolean isReview() {
        return this.decision == Decision.REVIEW;
    }

    public boolean isBlock() {
        return this.decision == Decision.BLOCK;
    }

    public Decision getDecision() {
        return this.decision;
    }

    public String getReason() {
        return this.reason;
    }
}
