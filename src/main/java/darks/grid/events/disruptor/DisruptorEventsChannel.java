/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package darks.grid.events.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.WorkerPool;

import darks.grid.GridException;
import darks.grid.beans.GridEvent;
import darks.grid.config.EventsConfig.EventsChannelConfig;
import darks.grid.events.EventsChannel;
import darks.grid.utils.GridStatistic;
import darks.grid.utils.GridStatistic.EventStatistic;

public class DisruptorEventsChannel extends EventsChannel
{
	
	private static final Logger log = LoggerFactory.getLogger(DisruptorEventsChannel.class);

	public static final String DEFAULT_CHANNEL = "default";
	public static final String SYSTEM_CHANNEL = "system";
	
    WorkerPool<GridEvent> workerPool = null;
    
    RingBuffer<GridEvent> ringBuffer = null;
	
	ExecutorService threadPool = null;
	
	public DisruptorEventsChannel()
	{
		
	}

    @Override
	public boolean initialize(EventsChannelConfig config)
	{
        super.initialize(config);
		log.info("Initialize disruptor events channel " + config.getName() + " with " + config);
		EventFactory<GridEvent> eventFactory = new DisruptorEventFactory();
        int ringBufferSize = config.getBlockQueueMaxNumber(); 
        int threadSize = config.getEventConsumerNumber();
        int bufferSize = ringBufferSize;
        if (Integer.bitCount(bufferSize) != 1)
        {
            bufferSize = (int) Math.pow(2, (int) (Math.log(ringBufferSize) / Math.log(2)));
            log.warn("Change disruptor events channel " + config.getName() + 
                    " buffer size from " + ringBufferSize + " to " + bufferSize);
        }
        if (bufferSize <= 0)
            throw new GridException("Invalid disruptor ringbuffur size:" + ringBufferSize);
        threadPool = Executors.newFixedThreadPool(threadSize);
        ringBuffer = RingBuffer.createMultiProducer(eventFactory, bufferSize, new BlockingWaitStrategy());  
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();  
        ExecutorService executor = Executors.newFixedThreadPool(10);  
        WorkHandler<GridEvent>[] workHandlers = new WorkHandler[threadSize];  
        for (int i = 0; i < threadSize; i++) {  
            WorkHandler<GridEvent> workHandler = new DisruptorEventsWorkHandler(getName());  
            workHandlers[i] = workHandler;  
        }  
  
        WorkerPool<GridEvent> workerPool = new WorkerPool<GridEvent>(ringBuffer, sequenceBarrier, 
                new IgnoreExceptionHandler(), workHandlers);  
        workerPool.start(executor);  
		return true;
	}

    @Override
	public void destroy()
	{
        workerPool.drainAndHalt();
		threadPool.shutdownNow();
	}

    @Override
    protected boolean offerQueue(GridEvent event, boolean sync) {
        long sequence = ringBuffer.next();
        try {
            GridEvent evt = ringBuffer.get(sequence);
            evt.copy(event);
        } finally{
            ringBuffer.publish(sequence);
        }
        return true;
    }

    @Override
	public String getChannelInfo()
	{
		EventStatistic stat = GridStatistic.getEventStat(name);
		StringBuilder buf = new StringBuilder();
		buf.append(name)
	    	.append("\tcount:").append(stat.getEventCount())
		    .append(" remain:").append(ringBuffer.getBufferSize() - ringBuffer.remainingCapacity())
			.append(" avg-delay:").append(stat.getAvgEventDelay())
			.append(" max-delay:").append(stat.getMaxEventDelay());
		return buf.toString();
	}
}
