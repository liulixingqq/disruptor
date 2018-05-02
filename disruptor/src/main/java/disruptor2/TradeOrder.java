package disruptor2;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/5/2
 */
public class TradeOrder {
    private String orderId;
    private double amount;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
