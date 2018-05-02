package disruptor2;

import com.lmax.disruptor.EventHandler;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/5/2
 */
public class TradeOrderVarConsumer implements EventHandler<TradeOrder> {
    @Override
    public void onEvent(TradeOrder event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("进行订单消费:"+event.getOrderId());
    }
}
