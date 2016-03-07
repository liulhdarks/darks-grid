package darks.grid.test.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import darks.grid.network.codec.FSTCodec;
import darks.grid.network.codec.GenericCodec;
import darks.grid.network.codec.GridCodec;
import darks.grid.network.codec.HessianCodec;
import darks.grid.network.codec.KryoCodec;

public class CodecTest
{

	@Test
	public void testCodec() throws Exception 
	{
//		final GridCodec codec = new KryoCodec();
//		final GridCodec codec = new GenericCodec();
		final GridCodec codec = new FSTCodec();
//		final GridCodec codec = new HessianCodec();
		final AtomicInteger byteLen = new AtomicInteger(0);
		final AtomicLong costSum = new AtomicLong(0L);
		int threadCount = 10;
		final CountDownLatch latch = new CountDownLatch(threadCount);
		for (int t = 0; t < threadCount; t++)
		{
			new Thread("thread-"+t)
			{
				public void run()
				{
					try
					{
						boolean first = true;
						for (int i = 0; i < 100000; i++)
						{
							Serializable bean = createTestBean(i);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							long st = System.currentTimeMillis();
							codec.encode(baos, bean);
							long et = System.currentTimeMillis();
							ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
							long st2 = System.currentTimeMillis();
							codec.decode(bais, null);
							long et2 = System.currentTimeMillis();
							if (first)
							{
								first = false;
								continue;
							}
							costSum.getAndAdd(et - st);
							costSum.getAndAdd(et2 - st2);
							byte[] bytes = baos.toByteArray();
							byteLen.getAndSet(bytes.length);
						}
						System.out.println("complete " + this.getName());
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						latch.countDown();
					}
				}
			}.start();
		}
		latch.await();
		System.out.println("cost:" + costSum.get() + " byteLen:" + byteLen.get());
	}
	
	private Serializable createTestBean(int index)
	{
		CodecTestBean bean = new CodecTestBean();
		bean.id = index;
		bean.name = "name" + index;
		bean.skills = new String[]{"k1", "k2"};
		bean.list = new ArrayList<String>();
		bean.list.add("p1");
		bean.list.add("p2");
		bean.subBean = new CodecTestSubBean("test", Double.MAX_VALUE);
		bean.subSet.add(new CodecTestSubBean("sub1", Double.MAX_VALUE));
		bean.subSet.add(new CodecTestSubBean("sub2", Double.MAX_VALUE));
		bean.subSet.add(new CodecTestSubBean("sub3", Double.MAX_VALUE));
		return bean;
	}
	
}
class CodecTestBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4042090818390099919L;

	int id;
	
	String name;
	
	String[] skills;
	
	List<String> list;
	
	CodecTestSubBean subBean = null;
	
	Set<CodecTestSubBean> subSet = new HashSet<CodecTestSubBean>();
	
	public CodecTestBean() {}
}

class CodecTestSubBean implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4980003165697429160L;
	
	String prop;
	
	double value;

	public CodecTestSubBean()
	{
	}

	public CodecTestSubBean(String prop, double value)
	{
		super();
		this.prop = prop;
		this.value = value;
	}
	
	
}


