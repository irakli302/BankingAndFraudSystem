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

    public static boolean isAllow(RuleResult result) {
        return result.decision == Decision.ALLOW;
    }

    public static boolean isReview(RuleResult result) {
        return result.decision == Decision.REVIEW;
    }

    public static boolean isBlock(RuleResult result) {
        return result.decision == Decision.BLOCK;
    }

    public Decision getDecision() {
        return this.decision;
    }

    public String getReason() {
        return this.reason;
    }
}
