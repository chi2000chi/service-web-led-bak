package com.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.framework.util.GpsDistanceUtils;

public class ThreadsTest {

	public static String listToStr(List<String> list) {
		StringBuffer str = new StringBuffer();
		Random random = new Random(10);
		Map<Integer, List<String>> group = list.parallelStream()
				.collect(Collectors.groupingBy(e -> random.nextInt(100)));
		group.values().parallelStream().forEach(e -> e.forEach(str::append));
		System.out.println("method1: " + str.toString());
		return str.toString();
	}

	public static String list2Str(List<String> list, final int nThreads) throws Exception {
		if (list == null || list.isEmpty()) {
			return null;
		}

		/**
		 * 用在 limit(n) 前面时，先去除前 m 个元素再返回剩余元素的前 n 个元素
			limit(n) 用在 skip(m) 前面时，先返回前 n 个元素再在剩余的 n 个元素中去除 m 个元素
		 * list = list.stream()
            .limit(2)
            .skip(1)
            .collect(toList());

打印输出 [Person{name='mike', age=25}]
		 */
		StringBuffer ret = new StringBuffer();
		int size = list.size();
		ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
		List<Future<String>> futures = new ArrayList<Future<String>>(nThreads);

		for (int i = 0; i < nThreads; i++) {
			final List<String> subList = list.subList(size / nThreads * i, size / nThreads * (i + 1));
			Callable<String> task = new Callable<String>() {
				@Override
				public String call() throws Exception {
					StringBuffer sb = new StringBuffer();
					for (String str : subList) {
						sb.append(str);
					}
					return sb.toString();
				}
			};
			futures.add(executorService.submit(task));
		}

		for (Future<String> future : futures) {
			ret.append(future.get());
		}
		executorService.shutdown();
		System.out.println("method2: " + ret.toString());
		return ret.toString();
	}

	public static void main(String[] args) throws Exception {
//		  int len=15/10;//平均分割List 
//		List<String> list = new ArrayList<>();
//		for (int i = 0; i < 15; i++) {
//			list.add(String.valueOf(i));
//		}
//		list2Str(list, 10);
//		listToStr(list);
		GpsDistanceUtils utils  = new GpsDistanceUtils();	
		double  a = utils.earthDis(Double.valueOf("45.777073"),Double.valueOf ("126.617695"),Double.valueOf("45.768888"), Double.valueOf("126.716016"));
		System.out.println("method2: " + a);
	}
}
