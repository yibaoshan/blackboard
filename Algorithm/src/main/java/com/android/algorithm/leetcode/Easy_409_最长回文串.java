package com.android.algorithm.leetcode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Easy_409_最长回文串 {

    public static void main(String[] args) {
        String s = "aaaaaaaaaakk";
        System.out.println(longestPalindrome(s));
        System.out.println(longestPalindrome2(s));
    }

    public int longestPalindrome3(String s) {
        if (s == null) return 0;
        if (s.length() < 2) return s.length();
        HashMap<Character, Integer> hashmap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            if (hashmap.containsKey(s.charAt(i))) hashmap.put(s.charAt(i), hashmap.get(s.charAt(i)) + 1);
            else hashmap.put(s.charAt(i), 1);
        }
        int sum = 0;
        for (int cnt : hashmap.values()) {
            if (cnt < 2) continue;
            if (cnt % 2 != 0 && sum % 2 != 0) sum += cnt - 1;
            else sum += cnt;
        }
        if (sum == s.length() || sum % 2 != 0) return sum;
        return sum + 1;
    }

    public static int longestPalindrome2(String s) {
        char[] chars = s.toCharArray();
        int l = chars.length;
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        Collection<Integer> values = map.values();
        for (char i : chars) {
            if (map.containsKey(i)) {
                map.replace(i, (Integer) map.get(i), (Integer) map.get(i) + 1);
            }
            map.put(i, 1);
        }
        if (map.size() == l) {
            return 1;
        }
        int res = 0;
        for (Integer j : map.values()) {
            if (j > 1 && j % 2 == 0) {
                res += j;
            }
            if (j > 1 && j % 2 != 0) {
                res = res + j - 1;

            }
        }
        if (l > res) {
            res = res + 1;

        }
        return res;

    }

    public static int longestPalindrome(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        if (chars.length < 2) return chars.length;
        int max = 0;
        HashMap<Character, Integer> hashMap = new HashMap<>();
        for (char c : chars) {
            if (hashMap.containsKey(c)) {
                int count = hashMap.get(c);
                hashMap.put(c, ++count);
            } else hashMap.put(c, 1);
        }
        Iterator<Map.Entry<Character, Integer>> iterator = hashMap.entrySet().iterator();
        boolean flag = false;
        while (iterator.hasNext()) {
            int value = iterator.next().getValue();
            if (value < 2) continue;
            if (value % 2 != 0) {
                if (flag) value--;
                else flag = true;
            }
            max += value;
        }
        if (max % 2 == 0 && max < chars.length) max++;
        return max;
    }

}
