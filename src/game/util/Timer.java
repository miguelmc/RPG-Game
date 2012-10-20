package game.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This object executes a method every fixed amount of time. Call start() to
 * start a new thread.
 */
public class Timer extends Thread
{

	// TODO pass the amount of times to execute

	private Method method;
	private int time, repetitions;
	private Object object;

	/**
	 * Calls the method every fixed amount of time until join() is called on <i>this</i>.
	 * <br>
	 * <br>
	 * @param obj
	 *            - The object to which the method is going to be called to.
	 * @param method
	 *            - The method to call. (Use getClass().getMethod("methodName",
	 *            new Class<?>[0]));
	 * @param alertTime
	 *            - The time interval (in miliseconds) between each method call.
	 */
	public Timer(Object obj, String method, int alertTime)
	{
		this(obj, method, alertTime, 0);
	}
	
	/**
	 * Calls the method every fixed amount of time for <i>repetitions</i> times.
	 * <br>
	 * <br>
	 * @param obj
	 *            - The object to which the method is going to be called to.
	 * @param method
	 *            - The method to call. (Use getClass().getMethod("methodName",
	 *            new Class<?>[0]));
	 * @param alertTime
	 *            - The time interval between each method call.
	 */
	public Timer(Object obj, String method, int alertTime, int repetitions)
	{
		object = obj;
		time = alertTime;
		try {
			this.method = obj.getClass().getMethod(method, new Class<?>[0]);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		this.repetitions = repetitions;
	}

	@Override
	public void run()
	{
		try
		{
			for(int i=0; i<repetitions || repetitions == 0; i++)
			{
				Thread.sleep(time); // pauses the thread
				method.invoke(object, new Object[0]); // invokes the method on the object
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				join();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

}
