/*
 * Copyright (c) 2018. Aberic - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.aberic.fabric.utils.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 开启一个线程池
 * 
 * @author 杨毅
 *
 * @date 2017年10月18日 - 下午7:40:27
 * @email abericyang@gmail.com
 */
public class ThreadFNSPool {

	/** 总共多少任务（根据CPU个数决定创建活动线程的个数,这样取的好处就是可以让手机承受得住） */
	// private static final int count =
	// Runtime.getRuntime().availableProcessors() *
	// 3 + 2;

	/** 每次只执行一个任务的线程池 */
	// private static ExecutorService singleTaskExecutor = null;

	/** 每次执行限定个数个任务的线程池 */
	// private static ExecutorService limitedTaskExecutor = null;

	/** 所有任务都一次性开始的线程池 */
	// private static ExecutorService allTaskExecutor = null;

	/** 创建一个可在指定时间里执行任务的线程池，亦可重复执行 */
	// private static ExecutorService scheduledTaskExecutor = null;

	/** 创建一个可在指定时间里执行任务的线程池，亦可重复执行（不同之处：使用工程模式） */
	// private static ExecutorService scheduledTaskFactoryExecutor =
	// Executors.newFixedThreadPool(3, new ThreadFactoryTest());

	private final static int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();// 核心线程数为当前设备CPU核心数+1
	// private final static int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 10 + 1;// 最大线程数为当前设备CPU核心数的5N+1
	// private final static long KEEP_ALIVE_TIME = 60L;// 设置线程的存活时间（闲置超时时长）
	/**
	 * 核心线程池.线程数量固定,且只有核心线程.当线程处于闲置状态时,它们不会被回收,除非线程池关闭.
	 * 当所有线程都处于活动状态时,新任务就会处于等待状态,直到有线程空闲出来. 该线程池因只有核心线程且核心线程不会被回收,因此可以快速响应外部请求.
	 * 另:核心线程没有超时机制,队列也没有大小限制.
	 **/
	// private ExecutorService mFixedThreadPool;
	/**
	 * 非核心线程池.线程数量不固定,且只有非核心线程,并且最大线程数为 Integer.MAX_VALUE ,即任意大.
	 * 当线程池中所有线程都处于活动状态时,会创建新的线程来处理新任务,否则就会利用空闲线程来处理新任务.
	 * 线程中的闲置线程都有超时机制,超时时长为60s,超时则回收.
	 * 该线程适合执行大量的耗时较少的任务,当整个线程池被闲置下来时,线程池中的线程都会因超时而停止,此时没有任何线程,几乎不占用任何系统资源.
	 **/
	private ScheduledExecutorService mScheduledThreadPool;
	/** 无界线程池，可以进行自动线程回收 */
	private ExecutorService mCacheThreadPool;
	private ThreadFactory mFixedFactory;

	private static ThreadFNSPool instance = null;

	public static ThreadFNSPool obtain() {
		if (null == instance) {
			synchronized (ThreadFNSPool.class) {
				if (null == instance) {
					instance = new ThreadFNSPool();
				}
			}
		}
		return instance;
	}

	private ThreadFNSPool() {
		// 创建核心线程池工厂
		mFixedFactory = new ThreadFNSFactory("fixed_thread_pool");
		// mFixedThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 1000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), mFixedFactory);
		mCacheThreadPool = Executors.newCachedThreadPool();
		if (null == mScheduledThreadPool) {
			mScheduledThreadPool = Executors.newScheduledThreadPool(CORE_POOL_SIZE, mFixedFactory);
		}
	}

	/**
	 * 执行定长核心线程池
	 *
	 * @param runnable
	 *            执行线程
	 */
	public void submitFixed(Runnable runnable) {
		mCacheThreadPool.execute(runnable);
	}

	/**
	 * 执行定长,定时线程池
	 *
	 * @param runnable
	 *            执行线程
	 * @param initialDelay
	 *            初始化延时
	 * @param period
	 *            前一次执行结束到下一次执行开始的间隔时间（间隔执行延迟时间）
	 * @param unit
	 *            计时单位
	 */
	public void submitScheduled(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
		if (null == mScheduledThreadPool) {
			throw new RuntimeException("ScheduledExecutorService is not create");
		}
		mScheduledThreadPool.scheduleAtFixedRate(runnable, initialDelay, period, unit);
	}

	/**
	 * 核心线程数为当前设备CPU核心数+1
	 * 
	 * @return
	 */
	public int getCoolPoolSize() {
		return CORE_POOL_SIZE;
	}

	/**
	 * 获取当前线程池的线程数量
	 * 
	 * @return
	 */
	public int getFixedThreadPoolSize() {
		if (null != mCacheThreadPool) {
			return ((ThreadPoolExecutor) mCacheThreadPool).getPoolSize();
		} else {
			return 0;
		}
	}

	/**
	 * 获取线程池中活动线程的数量
	 * 
	 * @return
	 */
	public int getFixedThreadActiveCount() {
		if (null != mCacheThreadPool) {
			return ((ThreadPoolExecutor) mCacheThreadPool).getActiveCount();
		} else {
			return 0;
		}
	}

	/**
	 * 线程池需要执行的任务数量
	 * 
	 * @return
	 */
	public long getFixedThreadTaskCount() {
		if (null != mCacheThreadPool) {
			return ((ThreadPoolExecutor) mCacheThreadPool).getTaskCount();
		} else {
			return 0;
		}
	}

	/**
	 * 获取线程池中完成的任务数
	 * 
	 * @return
	 */
	public long getFixedThreadCompleteCount() {
		if (null != mCacheThreadPool) {
			return ((ThreadPoolExecutor) mCacheThreadPool).getCompletedTaskCount();
		} else {
			return 0;
		}
	}

	/**
	 * 线程池曾经创建过的最大线程数量。通过这个数据可以知道线程池是否满过。如等于线程池的最大大小，则表示线程池曾经满了
	 * 
	 * @return
	 */
	public long getFixedThreadLargestPoolSize() {
		if (null != mCacheThreadPool) {
			return ((ThreadPoolExecutor) mCacheThreadPool).getLargestPoolSize();
		} else {
			return 0;
		}
	}

}
