import junit.framework.TestCase;
import queue.impl.InternalObelixQueue;
import queue.impl.ObelixQueueElement;
import queue.impl.RedisObelixQueue;
import queue.interfaces.ObelixQueue;

import static java.lang.Math.random;

public class TestObelixRedisQueue extends TestCase {

    public void testRedisQueuePushAndPop() {

        ObelixQueue obelixQueue = new RedisObelixQueue("logentries" + random()*10000);

        obelixQueue.push(new ObelixQueueElement("a", "1"));
        obelixQueue.push(new ObelixQueueElement("b", "2"));
        obelixQueue.push(new ObelixQueueElement("c", "3"));

        assertEquals(obelixQueue.getAll().size(), 3);

        assertEquals(obelixQueue.pop(), new ObelixQueueElement("a", "1"));
        assertEquals(obelixQueue.pop(), new ObelixQueueElement("b", "2"));
        assertEquals(obelixQueue.pop(), new ObelixQueueElement("c", "3"));

    }

    public void testRedisQueueBehaveAsTheInternalQueuePopAndPush() {

        ObelixQueue obelixQueue = new InternalObelixQueue();
        ObelixQueue redisObelixQueue = new RedisObelixQueue("randomQueueName" + random() * 10000);

        for (int i = 0; i < 10; i++) {
            obelixQueue.push(new ObelixQueueElement(Integer.toString(i), "element" + Integer.toString(i)));
            redisObelixQueue.push(new ObelixQueueElement(Integer.toString(i), "element" + Integer.toString(i)));
        }

        for (int i = 0; i < 10; i++) {
            assertEquals(obelixQueue.pop(), redisObelixQueue.pop());
        }

    }

    public void testRedisQueueBehaveAsTheInternalQueueGetAll() {

        ObelixQueue obelixQueue = new InternalObelixQueue();
        ObelixQueue redisObelixQueue = new RedisObelixQueue("randomQueueName" + random() * 10000);

        for (int i = 0; i < 10; i++) {
            obelixQueue.push(new ObelixQueueElement(Integer.toString(i), "element" + Integer.toString(i)));
            redisObelixQueue.push(new ObelixQueueElement(Integer.toString(i), "element" + Integer.toString(i)));
        }

        assertEquals(obelixQueue.getAll(), redisObelixQueue.getAll());

    }

    public void testReadJsonDataFormattedAsStringFromQueue() {
        String testData = "\"{\\\"file_format\\\": \\\"page_view\\\", \\\"timestamp\\\": 1431962580.7399549, \\\"item\\\": 2016165, \\\"user\\\": \\\"58335767\\\", \\\"ip\\\": \\\"188.218.111.19\\\", \\\"type\\\": \\\"events.pageviews\\\"}\"";

        ObelixQueue obelixQueue = new RedisObelixQueue("logentries" + random()*10000);
        obelixQueue.push(new ObelixQueueElement(testData));
        obelixQueue.pop();

    }

    public void testReadValidJsonDataFromQueue() {
        String testData = "{\"file_format\": \"page_view\"}";

        ObelixQueue obelixQueue = new RedisObelixQueue("logentries" + random()*10000);
        obelixQueue.push(new ObelixQueueElement(testData));
        obelixQueue.pop();
    }
}
