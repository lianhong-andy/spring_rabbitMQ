package com.andy.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataUtils {

	/**
	 * List和List取交集
	 * @param list1
	 * @param list2
	 * @param <E>
	 * @return
	 */
	public static <E> List<E> getIntersection(List<E> list1, List<E> list2) {
		List<E> src1;
		List<E> src2;
		if (list1.size() < list2.size()) {
			src1 = list1;
			src2 = list2;
		} else {
			src1 = list2;
			src2 = list1;
		}
		//用长度小的来初始化HashSet，效率高
		Set<E> hashSet = new HashSet<>(src1);
		List<E> result = new ArrayList<>(src1.size() >> 1);

		for(E item : src2) {
			//Set的contains方法比List的效率高很多
			if (hashSet.contains(item)) {
				result.add(item);
			}
		}

		return result;
	}

	/**
	 * List和Set取交集
	 * @param list1
	 * @param list2
	 * @param <E>
	 * @return
	 */
	public static <E> Set<E> getIntersection(List<E> list1, Set<E> list2) {
		int initialCapacity = list1.size() < list2.size() ? list1.size() : list2.size();
		Set<E> hashSet = new HashSet<>(initialCapacity >> 1);

		for(E item : list1) {
			//Set的contains方法比List的效率高很多
			if (list2.contains(item)) {
				hashSet.add(item);
			}
		}
		return hashSet;
	}

	/**
	 * List取差集
	 * @param list1 原集合
	 * @param list2 原集合中要除去的集合
	 * @return
	 */
	public static <E> List<E> getDifference(List<E> list1, List<E> list2) {
		List<E> src1;
		List<E> src2;
		if (list1.size() < list2.size()) {
			src1 = list1;
			src2 = list2;
		} else {
			src1 = list2;
			src2 = list1;
		}
		Set<E> hashSet = new HashSet<>(src1);
		List<E> result = new ArrayList<>(src2.size() - src1.size());

		for(E item : src2) {
			//Set的contains方法比List的效率高很多
			if (!hashSet.contains(item)) {
				result.add(item);
			}
		}

		return result;
	}

    /**
     * 集合分组，但数据量大时可以对数据进行分组，进行分批处理
	 * @param srcList 数据源
	 * @param divideFactor 切割尺寸
	 * @param <T> 泛型
     * @return
     */
	public static <T> List<List<T>> divideList(List<T> srcList, Integer divideFactor) {
		List<T> tmpList = new ArrayList<>();
		List<List<T>> rstList = new ArrayList<>();

		tmpList.addAll(srcList);

		int size = tmpList.size();

		//如果divideFactory为空，根据数据大小设置默认值
		if (divideFactor == null) {
			if (size > 100){
				divideFactor = 10;
			}
			if (size <30 ) {
				divideFactor = 5;
			}
		}

		int i = 0;
		//如果数据源比切割尺寸还小,不分组
		if (size < divideFactor) {
			rstList.add(tmpList);
			return rstList;
		}

		while (i < size) {
			List<T> subList = srcList.subList(i, i+divideFactor);
			rstList.add(subList);
			i += divideFactor;

			//最后一份不足一组，当做一组算
			if ( i >= size - divideFactor) {
				rstList.add(tmpList.subList(i,size));
				break;
			}
		}

		return rstList;
	}

}
