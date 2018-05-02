package disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/5/2
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b)
            throws Exception {
        System.out.println(longEvent.getValue());
    }

//    EventTranslator
}
