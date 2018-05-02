package disruptor2;

import com.lmax.disruptor.EventHandler;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/5/2
 */
public class TradeOrderJMSSender implements EventHandler<TradeOrder> {
    @Override
    public void onEvent(TradeOrder event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("发送订单JMS:"+event.getOrderId()+",金额:"+event.getAmount());
    }
}
