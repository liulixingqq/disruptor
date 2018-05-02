package disruptor2;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import java.util.UUID;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/5/2
 */

public class TradeDBHandler implements EventHandler<TradeOrder>,WorkHandler<TradeOrder> {
    @Override
    public void onEvent(TradeOrder tradeOrder, long l, boolean b) throws Exception {
        this.onEvent(tradeOrder);
    }

    @Override
    public void onEvent(TradeOrder tradeOrder) throws Exception {
        tradeOrder.setOrderId(UUID.randomUUID().toString());
        System.out.println("订单号为："+tradeOrder.getOrderId()+"金额:"+tradeOrder.getAmount());
    }
}
