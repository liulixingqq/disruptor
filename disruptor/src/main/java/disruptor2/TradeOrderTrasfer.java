package disruptor2;

import com.lmax.disruptor.EventTranslator;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/5/2
 */
public class TradeOrderTrasfer implements EventTranslator<TradeOrder> {
    @Override
    public void translateTo(TradeOrder event, long sequence) {
        event.setAmount(Math.random()*99);
        System.out.println("设置订单金额:"+event.getOrderId());
    }
}
