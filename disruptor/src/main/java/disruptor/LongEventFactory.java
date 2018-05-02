package disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * description
 *需要让disruptor为我们创建事件，我们同时还声明了一个EventFactory来实例化Event对象
 * @author Llx
 * @version v1.0.0
 * @since 2018/5/2
 */
public class LongEventFactory implements EventFactory {

    @Override
    public Object newInstance() {
        return new LongEvent();
    }
}
