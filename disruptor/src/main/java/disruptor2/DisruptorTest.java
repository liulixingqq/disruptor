package disruptor2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.*;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/5/2
 */
public class DisruptorTest {
    //定义RingBuffer的大小
    private static final int BUFFER_SIZE = 1024;

    //4.定义事件处理的线程或者线程池
    ExecutorService excutorService = Executors.newFixedThreadPool(5);

    //批处理模式
    public void BatchEventProcessor() throws ExecutionException, InterruptedException {

        final RingBuffer<TradeOrder> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<TradeOrder>() {
            @Override
            public TradeOrder newInstance() {
                return new TradeOrder();
            }
        },BUFFER_SIZE,/**5.指定等待策略**/new YieldingWaitStrategy());

        SequenceBarrier sequenceBarrier =  ringBuffer.newBarrier();
        BatchEventProcessor<TradeOrder> batchEventProcessor = new BatchEventProcessor<TradeOrder>(ringBuffer,sequenceBarrier,new TradeDBHandler());
        ringBuffer.addGatingSequences(batchEventProcessor.getSequence());
        excutorService.submit(batchEventProcessor);

        Future<?> future = excutorService.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                long seq;
//                Gson son = new JSON();
                for(int i=0;i<10000;i++){
                    System.out.println("product:"+i);
                    seq = ringBuffer.next();
                    System.out.println("seq:"+seq);
                    ringBuffer.get(seq).setAmount(Math.random()*99);
                    System.out.println("tradeOrder:"+ JSONObject.toJSONString(ringBuffer.get(seq)));
                    ringBuffer.publish(seq);
                }
                return null;
            }
        });
        future.get();
        Thread.sleep(10000);
        batchEventProcessor.halt();
        excutorService.shutdown();
    }


    //workerPool模式
    public void workerPool() throws InterruptedException {

        final RingBuffer<TradeOrder> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<TradeOrder>() {
            @Override
            public TradeOrder newInstance() {
                return new TradeOrder();
            }
        },BUFFER_SIZE,new YieldingWaitStrategy());
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        WorkHandler<TradeOrder> workHandler = new TradeDBHandler();
        WorkerPool<TradeOrder> workerPool = new WorkerPool<TradeOrder>(ringBuffer,sequenceBarrier, new IgnoreExceptionHandler(),workHandler);
        workerPool.start(executorService);
        long seq;
        for(int i =0;i<8;i++){
            seq = ringBuffer.next();
            ringBuffer.get(seq).setAmount(Math.random()*99);
            ringBuffer.publish(seq);
        }
        Thread.sleep(1000);
        workerPool.halt();
        executorService.shutdown();
    }


    /**
     * 6.通过disruptor处理器组装生产者和消费者
     * 菱形结构的处理过程流
     *
     *                                    /--------->TradeOrderVarConsumer--\
     * TradeOrderProductor--->RingBuffer-->                                   ----->TradeOrderJMSSender
     *                                    \--------->TradeDBHandler--------/
     *
     * @throws InterruptedException
     */
    public void processerFlow() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Disruptor<TradeOrder> disruptor = new Disruptor<TradeOrder>(new EventFactory<TradeOrder>(){
            @Override
            public TradeOrder newInstance() {
                return new TradeOrder();
            }
        },BUFFER_SIZE,executorService, ProducerType.SINGLE,/**5.指定等待策略**/new BusySpinWaitStrategy());

        //指定两个消息处理类分别处理TradeOrderVarConsumer   与   TradeDBHandler
        EventHandlerGroup<TradeOrder> eventHandlerGroup = disruptor.handleEventsWith(new TradeOrderVarConsumer(),new TradeDBHandler());

        eventHandlerGroup.then(new TradeOrderJMSSender());
        disruptor.start();//启动disruptor
        CountDownLatch cdl = new CountDownLatch(1);
        executorService.submit(new TradeOrderProductor(cdl,disruptor));
        cdl.await();//用于让任务完全消费掉
        //8.关闭disruptor业务逻辑处理器
        disruptor.shutdown();
        executorService.shutdown();
    }



    public static void main(String args[]) throws ExecutionException, InterruptedException {
        DisruptorTest disruptor = new DisruptorTest();
//        disruptor.BatchEventProcessor();
//          disruptor.workerPool();
//        disruptor.processerFlow();
    }
}
