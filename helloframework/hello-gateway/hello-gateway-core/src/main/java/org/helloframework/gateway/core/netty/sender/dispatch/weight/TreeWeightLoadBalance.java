package org.helloframework.gateway.core.netty.sender.dispatch.weight;

import org.helloframework.gateway.common.definition.base.LoadBalance;
import org.helloframework.gateway.common.definition.base.MethodInfo;
import org.helloframework.gateway.core.exception.LoadBalanceException;
import org.helloframework.gateway.core.exception.ServiceNoProviderException;

import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lanjian
 */
public class TreeWeightLoadBalance<T> implements LoadBalance<T> {
    private TreeMap<Integer, T> treeMap;
    private int total;
    protected final Map<T, Integer> data;
    protected volatile Random ran;
    private MethodInfo methodInfo;
    private AtomicInteger working = new AtomicInteger(0);


    public AtomicInteger working() {
        return working;
    }

    public void updateMethod(MethodInfo methodInfo) {
        this.methodInfo = methodInfo;
    }

    public TreeWeightLoadBalance(Map<T, Integer> data) {
        this.data = data;
        this.ran = new Random();
        init();
    }


    public void init() {
        TreeMap<Integer, T> treeMap = new TreeMap<Integer, T>();
        int total = 0;
        for (Map.Entry<T, Integer> each : this.data.entrySet()) {
            final T k = each.getKey();
            final Integer v = each.getValue();
            if (null == v || v.equals(0)) {
                continue;
            }
            total += v;
            treeMap.put(total, k);
        }
        this.treeMap = treeMap;
        this.total = total;
    }

    public Map<T, Integer> data() {
        return data;
    }

    public int timeout() {
        return methodInfo.getTimeout();
    }


    public int max() {
        return methodInfo.getMax();
    }

    public int size() {
        return treeMap.size();
    }

    public T random() {
        if (size() == 0) {
            throw new ServiceNoProviderException("服务无提供者");
        }
        try {
            int s = this.ran.nextInt(total);
            return random(s);
        } catch (Exception ex) {
            throw new LoadBalanceException("服务调度异常", ex);
        }
    }

    private T random(int ran) {
        if (ran < 0) {
            ran = -ran;
        }
        ran = ran % total;
        ran++;
        SortedMap<Integer, T> map = treeMap.tailMap(ran);
        Integer k = map.firstKey();
        if (null != k) {
            return map.get(k);
        }
        return treeMap.get(treeMap.firstKey());
    }
}
