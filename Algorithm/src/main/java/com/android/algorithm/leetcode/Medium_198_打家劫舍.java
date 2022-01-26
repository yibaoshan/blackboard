package com.android.algorithm.leetcode;

import org.junit.Test;

public class Medium_198_打家劫舍 {

    /**
     * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
     * <p>
     * 给定一个代表每个房屋存放金额的非负整数数组，计算你 不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/house-robber
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */
    @Test
    public void main() {
//        int[] nums = new int[]{2, 1, 1, 2};
        int[] nums = new int[]{2, 7, 9, 3, 1};
        System.out.println(rob(nums));
        System.out.println(rob2(nums));
    }

    /**
     * 动态规划，参考评论区
     * 保留上一次prev和上上次prevPrev的结果
     * 当前节点+上上次结果和上次结果比较，取最大值作为当前结果
     * 同时，上上次结果改为上次prev的值，上次prev更改为当前result值
     * 执行结果：通过
     * 执行用时：0 ms, 在所有 Java 提交中击败了100.00%的用户
     * 内存消耗：35.5 MB, 在所有 Java 提交中击败了97.03%的用户
     */
    public int rob(int[] nums) {
        if (nums == null) return -1;
        if (nums.length == 1) return nums[0];
        if (nums.length == 2) return Math.max(nums[0], nums[1]);
        int result, prevPrev = nums[0], prev;
        prev = Math.max(prevPrev, nums[1]);
        result = prev;
        for (int i = 2; i < nums.length; i++) {
            result = Math.max(prev, prevPrev + nums[i]);
            prevPrev = prev;
            prev = result;
        }
        return result;
    }

    /**
     * 动态规划
     * 执行结果：通过
     * 执行用时：0 ms, 在所有 Java 提交中击败了100.00%的用户
     * 内存消耗：39.1 MB, 在所有 Java 提交中击败了5.50%的用户
     */
    public int rob2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        if (nums.length == 2) return Math.max(nums[0], nums[1]);
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        return dp[dp.length - 1];
    }

}
