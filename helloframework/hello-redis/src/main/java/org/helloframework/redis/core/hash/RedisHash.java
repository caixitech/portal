package org.helloframework.redis.core.hash;

/**
 * Created by lanjian
 */

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.util.MurmurHash;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * * 一致性Hash算法
 */
public class RedisHash {

    /**
     * 虚拟节点，key表示虚拟节点的hash值，value表示虚拟节点的名称
     */
    private final TreeMap<Long, String> virtualNodes = new TreeMap();

    private final static String PREFIX = "##";

    /**
     * 虚拟节点的数目，一个真实结点对应n个虚拟节点
     */
    private static final int VIRTUAL_NODES = 160;

    private static final MurmurHash murmurHash = new MurmurHash();

    public void addSlot(String slot) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            String virtualNode = slot + PREFIX + i;
            Long hash = murmurHash.hash(virtualNode);
            if (virtualNodes.containsKey(hash)) {
                throw new RuntimeException("pls check slot double");
            }
            virtualNodes.put(hash, virtualNode);
        }
    }


    /**
     * 得到应当路由到的结点
     */
//    public String findSlot1(String key) {
//        // 得到带路由的结点的Hash值
//        Long hash = murmurHash.hash(key);
//        if (!virtualNodes.containsKey(hash)) {
//            // 得到大于该Hash值的所有Map 存在找不到的情况 找不到 直接返回map的第一个key
//            SortedMap<Long, String> tailMap = virtualNodes.tailMap(hash);
//            hash = tailMap.isEmpty() ? virtualNodes.firstKey() : tailMap.firstKey();
//        }
//        String node = virtualNodes.get(hash);
//        return node.substring(0, node.indexOf(prefix));
//    }

    public String findSlot(String key) {
        long hash = murmurHash.hash(key);
        String slotName = virtualNodes.get(hash);
        // 如果hash值巧合已有，直接使用此key,否则使用小于此hash值的最近一个key
        if (StringUtils.isEmpty(slotName)) {
            Map.Entry<Long, String> longStringEntry = virtualNodes.lowerEntry(hash);
            if (longStringEntry == null) {
                // 碰巧是最小的hash值，取第一个
                longStringEntry = virtualNodes.firstEntry();
            }
            slotName = longStringEntry.getValue();
        }
        return slotName.substring(0, slotName.indexOf(PREFIX));
    }


    public static void main(String[] args) {
        String[] servers = {"KEY1", "KEY2", "KEY3", "KEY4", "KEY5", "KEY6", "KEY7", "KEY8"};
        RedisHash redisHash = new RedisHash();
        for (String s : servers) {
            redisHash.addSlot(s);
        }
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> count = new HashMap();
        for (int i = 0; i < 100000; i++) {
            String key = redisHash.findSlot(i + "");
            map.put(murmurHash.hash(String.valueOf(i)) + key, "a");
            System.out.println("[" + i + "]的hash值为" + i + ", 被路由到结点[" + key + "]");
            if (count.get(key) == null) {
                count.put(key, 1);
            }
            Integer a = count.get(key);
            count.put(key, ++a);
        }
        System.out.println(count);
        String[] servers1 = {"KEY2", "KEY3", "KEY4", "KEY5", "KEY6", "KEY7", "KEY8"};
        RedisHash redisHash1 = new RedisHash();
        for (String s : servers1) {
            redisHash.addSlot(s);
        }
        int fg = 0;
        for (int i = 0; i < 100000; i++) {
            String key = redisHash1.findSlot(i + "");
            String aaa = murmurHash.hash(i + "") + key;
            if (map.containsKey(aaa)) {
                fg++;
            }
        }
        System.out.println(new Double(fg) / new Double(map.size()));
//        SpringOpRedis SpringOpRedis = new SpringOpRedis();
//        SpringOpRedis.execute("feed", "user:111", new FeedRedisCallback("111"));
    }
}