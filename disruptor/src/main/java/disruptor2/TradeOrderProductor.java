package disruptor2;

import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.CountDownLatch;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/5/2
 */
public class TradeOrderProductor implements Runnable  {
    CountDownLatch cdl;
    private final int  count= 100000;
    Disruptor disruptor;
    public  TradeOrderProductor(CountDownLatch cdl,Disruptor disruptor){
        this.disruptor =disruptor;
        this.cdl = cdl;
    }
    @Override
    public void run() {
        TradeOrderTrasfer tof;
        try {
            for (int i = 0; i < count; i++) {
                tof = new TradeOrderTrasfer();
                disruptor.publishEvent(tof);
            }
        }finally {
            cdl.countDown();
        }
    }
}
