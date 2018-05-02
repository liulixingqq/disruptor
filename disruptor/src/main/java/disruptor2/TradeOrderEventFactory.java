package disruptor2;

import com.lmax.disruptor.EventFactory;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/5/2
 */
public class TradeOrderEventFactory implements EventFactory<TradeOrder> {
    @Override
    public TradeOrder newInstance() {
        return new TradeOrder();
    }
}
