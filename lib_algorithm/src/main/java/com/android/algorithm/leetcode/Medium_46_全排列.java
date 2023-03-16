package com.android.algorithm.leetcode;


import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Medium_46_全排列 {

    /**
     * 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
     * <p>
     * 示例 1：
     * <p>
     * 输入：nums = [1,2,3]
     * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
     * 示例 2：
     * <p>
     * 输入：nums = [0,1]
     * 输出：[[0,1],[1,0]]
     * 示例 3：
     * <p>
     * 输入：nums = [1]
     * 输出：[[1]]
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/permutations
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */

    @Test
    public void main() {
        int[] nums = new int[]{1, 2, 3};
        List<List<Integer>> lists = permute(nums);
        for (int i = 0; i < lists.size(); i++) {
            for (int j = 0; j < lists.get(i).size(); j++) {
                System.out.print(lists.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    /**
     * 执行结果：通过
     * 执行用时：1 ms, 在所有 Java 提交中击败了80.52%的用户
     * 内存消耗：38.6 MB, 在所有 Java 提交中击败了67.11%的用户
     */
    public List<List<Integer>> permute(int[] nums) {
        if (nums == null || nums.length == 0) return res;
        boolean[] visited = new boolean[nums.length];
        backtrack(nums, visited);
        return res;
    }

    private List<List<Integer>> res = new ArrayList<>();
    private Deque<Integer> deque = new ArrayDeque<>();

    private void backtrack(int[] nums, boolean[] visited) {
        if (deque.size() == nums.length) {
            res.add(new ArrayList<>(deque));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) continue;
            visited[i] = true;
            deque.addLast(nums[i]);
            backtrack(nums, visited);
            deque.removeLast();
            visited[i] = false;
        }
    }

}
