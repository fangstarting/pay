package com.ff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class Algorithm {
    public static void main(String[] args) {
//        Algorithm algorithm = new Algorithm();
        takeCruiseShip1();
    }

    /**
     * 题目描述-简单
     * 每个句子由多个单词组成，句子中的每个单词的长度都可能不一样，我们假设每个单词的长度Ni为该单词的重量，你需要做的就是给出整个句子的平均重量V。
     * <p>
     * 解答要求
     * 时间限制：1000ms, 内存限制：100MB
     * 输入
     * 输入只有一行，包含一个字符串S(长度不会超过100)，代表整个句子，句子中只包含大小写的英文字母，每个单词之间有一个空格。
     * <p>
     * 输出
     * 输出句子S的平均重量V(四舍五入保留两位小数)。
     * <p>
     * 样例
     * 输入样例 1 复制
     * <p>
     * Who Love Solo
     * 输出样例 1
     * <p>
     * 3.67
     */
    public static void word() {
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        String[] strs = str.trim().split(" ");
        int sum = 0;
        for (String s : strs) {
            sum += s.trim().length();
        }
        float res = (float) sum / strs.length;
//        float floatValue = new BigDecimal(res).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        String format = String.format("%.2f", res);
        System.out.println(format);
    }

    /**
     * 题目描述-中等
     * 在紫金山的路两边有好多树，每棵树的高度记作h，h为一个int类的正整数，从路的开头到路的尽头对树依次编号
     * 1，2，3，4…n (n<100,000)。现在求[a,b]从a号树到b号树高度的极差，就是[a,b]中最高树的高度减去最矮树的高度。
     * <p>
     * 解答要求
     * 时间限制：2000ms, 内存限制：100MB
     * 输入
     * 第二行正整数 N，Q，分别表示有 N 棵树，Q 表示有Q(Q<=10000) 次询问。
     * 第三行有 N 个正整数。
     * 下面 Q 行每行有 a，b。分别表示第 a 棵树第 b 棵树(a<b)。
     * <p>
     * 输出
     * 对于每次询问输出高树的高度。
     * <p>
     * 样例
     * 输入样例 1 复制
     * <p>
     * 6 3
     * 1 7 3 4 2 5
     * 1 5
     * 4 6
     * 2 2
     * 输出样例 1
     * <p>
     * 6
     * 3
     * 0
     */
    public static void treeAndForest() {
        Scanner in = new Scanner(System.in);
        int treeSum = in.nextInt();
        int total = in.nextInt();
        int[] trees = new int[treeSum];
        for (int i = 0; i < treeSum; i++) {
            trees[i] = in.nextInt();
        }
        int[][] compute = new int[total][2];
        for (int[] ints : compute) {
            for (int i = 0; i < ints.length; i++) {
                ints[i] = in.nextInt();
            }
        }
        for (int[] ints : compute) {
            int treeMax = 0, treeMin = 0;
            for (int i = ints[0] - 1; i < ints[1]; i++) {
                if (i == ints[0] - 1) {
                    treeMax = trees[i];
                    treeMin = trees[i];
                } else {
                    if (treeMin > trees[i]) treeMin = trees[i];
                    if (treeMax < trees[i]) treeMax = trees[i];
                }
            }
            System.out.println(treeMax - treeMin);
        }
    }

    /**
     * 题目描述-困难
     * solo发现他参加Online Judge的比赛表现很不稳定，于是他翻开历史记录，发现他在每一轮的比赛中他的排名R都能整除参赛人数N(包括solo)，于是他每次参赛都会先预测他的排名情况，以给自己更大的自信。
     * <p>
     * 解答要求
     * 时间限制：15000ms, 内存限制：100MB
     * 输入
     * 输入只有一个整数N(0<N<109)，代表参赛人数。
     * <p>
     * 输出
     * 在一行输出solo参赛可能的获得的排名数S以及由小到大输出各个排名Ri (0<i ≤ S)，用空格隔开。
     * <p>
     * 样例
     * 输入样例 1 复制
     * <p>
     * 10
     * 输出样例 1
     * <p>
     * 4 1 2 5 10
     */
    public static void standings() {

//        Scanner in = new Scanner(System.in);
//        int number = in.nextInt();
//        long start = System.currentTimeMillis();
//        int sum = 0;
//        StringBuffer sb = new StringBuffer();
//        for (int i = 1; i <= number; i++) {
//            if(number % i == 0){
//               sum++;
//               sb.append(" "+i);
//            }
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("执行时长(ms)：" + (end - start));
//        System.out.println(""+sum+sb);


        /** algorithm plus */
        Scanner in = new Scanner(System.in);
        int number = in.nextInt();
        long start = System.currentTimeMillis();
        Set<Integer> set = new HashSet<>();
        if (number == 1) {
            set.add(1);
        } else {
            for (int i = 1; i <= Math.sqrt(number); i++) {
                if (number % i == 0) {
                    set.add(i);
                    set.add(number / i);
                }
            }
        }
        Object[] objects = set.toArray();
        Arrays.sort(objects);
        StringBuffer sb = new StringBuffer(String.valueOf(objects.length));
//        Arrays.stream(objects).forEach(e -> sb.append(" "+e));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (Object object : objects) {
            sb.append(" " + object);
        }
        long end = System.currentTimeMillis();
        System.out.println("执行时长(ms)：" + (end - start));
        System.out.println(sb);
    }

    /**
     * 题目描述-简单
     * Calculate A + B，and give me the answer!
     * <p>
     * 解答要求
     * 时间限制：1000ms, 内存限制：100MB
     * 输入
     * Input two integers A and B, process to the end of the file. (Watch the Sample Input)
     * <p>
     * 输出
     * For each case, output A + B in one line.(Watch the Sample Output)
     * <p>
     * 样例
     * 输入样例 1 复制
     * <p>
     * 1 1
     * 输出样例 1
     * <p>
     * 2
     */
    public static void ABProblem() {
        Scanner in = new Scanner(System.in);
        List<Integer> list = new ArrayList<>();
        while (in.hasNextInt()) {
            list.add(in.nextInt() + in.nextInt());
        }
        list.forEach(System.out::println);
    }

    /**
     * 题目描述-简单
     * 有 n 个一起去乘游轮。聚会中，如果两个人之间互相认识，那么他们就可以回去了同一艘游轮。
     * <p>
     * 这里假设如果 a 认识 b 且 b 认识 c ，那么 a 就认识 c 。
     * <p>
     * 那么在这种情况下，至少需要摆多少艘游轮可以使得所有人都乘上游轮。
     * <p>
     * 解答要求
     * 时间限制：1000ms, 内存限制：256MB
     * 输入
     * 第一行两个整数 n 和 m 表示一起游轮的人数和认识关系的数量，每个人的标号为 1 到 n 。
     * <p>
     * 接下来 m 行每行两个整数 a,b 表示第 a 个人和第 b 个人认识。
     * <p>
     * 1 <= n, m <= 10^6 。
     * <p>
     * 输出
     * 输出一个数表示最少需要的游轮数量。
     * <p>
     * 样例
     * 输入样例 1 复制
     * <p>
     * 5 2
     * 1 3
     * 2 4
     * 输出样例 1
     * <p>
     * 3
     */
    public static void takeCruiseShip() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(); // 人数
        int m = in.nextInt(); // 关联数
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < m; i++) {
            sb.append(in.nextInt()).append(" ").append(in.nextInt()).append(",");
        }
        Map<Integer, Set<Integer>> ship = new HashMap<>();
        final int[] key = {1};
        for (String s : sb.substring(0, sb.length() - 1).split(",")) {
            ArrayList<Integer> integers = new ArrayList<>();
            for (String s1 : s.split(" ")) {
                integers.add(Integer.valueOf(s1));
            }

//            boolean flag = true;
//            for (Map.Entry<Integer, Set<Integer>> ins : ship.entrySet()) {
//                flag = true;
//                for (Integer ones : ins.getValue()) {
//                    for (Integer tows : integers) {
//                        if(ones.equals(tows) && flag) {//当前ins.ones这条船 tows当中有至少一个人 认识船上的朋友
//                            ins.getValue().addAll(integers);//将两个人添加进当前这一条船
//                            flag = false;
//                            break;
//                        }
//                    }
//                }
//            }
            //添加一条船
//            if(flag){
////                ship.put(key++,new HashSet<>(integers));
//            }else { // 判断这两人是否与其他船只朋友有关联

            Map<Integer, List<Integer>> map = new HashMap<>();
            final int[] index = {0};
            ship.forEach((k, ones) -> { // 遍历已存在的所有船只
                index[0] = k;
                ones.forEach(o -> {  // 遍历当前船 所以人物
                    integers.forEach(t -> {
                        if (t.equals(o)) { // 判断当前这条船 是否与 tows 两人关联
                            if (map.get(index[0]) != null) {
                                List<Integer> integers1 = map.get(index);
                                integers1.add(t);
                                map.put(index[0], integers1);
                            } else {
                                map.put(index[0], new ArrayList<>(t));
                            }
                        }
                    });

                });


            });
            // 是否合并船只
            if (map.isEmpty()) {// 无关联
                //添加一条船
                ship.put(key[0]++, new HashSet<>(integers));
            } else if (map.size() == 1) {// 关系一致 两人均在当前ones这条船
                ship.get(index[0]).addAll(integers);
            } else if(map.size()==2){// 两人关联两条船，执行合并
                Integer k1 = null,k2 = null;
                int var = 1;
                for (Integer k : map.keySet()) {
                    if(var++ == 1) {
                        k1 = k;
                    }else {
                        k2 = k;
                    }
                }
                Set<Integer> set = ship.get(k2);
                ship.remove(k2);
                ship.get(k1).addAll(set);
            }

//            }


            System.out.println("----------");
        }
        final int[] sum = {0};
        ship.forEach((k,v)-> v.forEach(e->sum[0]++));
        System.out.println(n - sum[0] + ship.size());
    }

    //并集--乘游轮
    private static long[] fa =new long [1000001];
    //查出根节点
    private static long find (long x) {
        if (x != fa[Math.toIntExact(x)])
            fa[Math.toIntExact(x)] = find(fa[Math.toIntExact(x)]);
        return fa[Math.toIntExact(x)];
    }
    private static void meger(long x ,long y ){
        x = find(x);
        y = find(y);
        fa[Math.toIntExact(x)]= y;
    }
    public static void takeCruiseShip1() {
        Scanner sc = new Scanner(System.in);
        long n = sc.nextLong();
        long m = sc.nextLong();
        //初始化
        for (long i = 0; i <n ; i++) {
            fa[Math.toIntExact(i)]=i;
        }
        //合并a和b
        while(m--!=0) {
            long a = sc.nextLong();
            long b = sc.nextLong();
            meger(a, b);
        }
        long res=0;
        for (long k = 0; k <n; k++) {
            //找出跟节点
            if (k==fa[Math.toIntExact(k)]){
                ++res;
            }
        }
        System.out.print(res);
    }

}
