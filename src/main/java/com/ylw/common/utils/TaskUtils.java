package com.ylw.common.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskUtils {
	private static List<ExecutorService> executorServices = new LinkedList<>();

	public static Executor getExcutor() {
		if (executorService == null) {
			executorService = new ThreadPoolExecutor(kDefaultThreadPoolSize, maxThreadPoolSize, kKeepAliveTime,
					kTimeUnit, queue, handler);
			executorServices.add(executorService);
		}
		return executorService;
	}

	public static Executor getSingleExcutor() {
		ExecutorService singleExecutorService = Executors.newSingleThreadExecutor();
		executorServices.add(singleExecutorService);
		return singleExecutorService;
	}

	private static int kDefaultThreadPoolSize = 4;
	private static int maxThreadPoolSize = 40;
	private static int kKeepAliveTime = 30;
	private static TimeUnit kTimeUnit = TimeUnit.SECONDS;

	private final static LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

	static RejectedExecutionHandler handler = new RejectedExecutionHandler() {
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			queue.add(r);
		}
	};

	private static ExecutorService executorService;

	public static void shutdown() {
		executorServices.forEach(action -> {
//			action.shutdown();
			action.shutdownNow();
		});
	}

}
