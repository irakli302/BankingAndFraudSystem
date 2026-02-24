package bankingandfraudsystem.rules.impl;

import bankingandfraudsystem.rules.Decision;
import bankingandfraudsystem.util.Money;

public class VelocitySumRule {
    private final Money threshold;
    private final int windowMinutes;

    public VelocitySumRule(Money money, int windowMnts) {
        if(money == null) throw new IllegalArgumentException("Money cannot be null!");

        if(!money.isPositive()) throw new IllegalArgumentException("Money must be positive!");

        if(windowMnts < 1) throw new IllegalArgumentException("WindowMinutes should be greater then 0!");

        this.threshold = money;
        this.windowMinutes = windowMnts;
    }
}
