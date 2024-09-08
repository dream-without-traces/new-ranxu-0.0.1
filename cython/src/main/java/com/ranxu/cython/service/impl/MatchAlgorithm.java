package com.ranxu.cython.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @Author gaoqiang
 * @Description
 * @Date 2024/9/8 上午10:26
 */

class MatchAlgorithm {

    public static void main(String[] args) {
        MatchAlgorithm solution = new MatchAlgorithm();
        List<String> template = readCsvFileByColumns("/Users/gaoqiang/Desktop/归档/templates.csv");
        List<String> s = readCsvFileByColumns("/Users/gaoqiang/Desktop/归档/no_template_df.csv");
        // 清除读取的前后字符串
        for (int i = 0; i < s.size(); i++) {
            s.set(i, s.get(i).substring(1, s.get(i).length() - 2));
        }
        List<String> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        List<String> strings = solution.matchTemplate(template.toArray(new String[0]), s.toArray(new String[0]));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        for (int i = 0; i < strings.size(); i++) {
            System.out.println(strings.get(i));
        }
    }

    public static List<String> readCsvFileByColumns(String filePath) {
        List<String> columns = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columns;
    }

    /**
     * 字典树+dfs进行模板库匹配，当返回-1的时候表示没有匹配到，否则返回匹配的下标
     *
     * @param model 匹配模版数组
     * @param s     匹配字段数组
     * @return 返回匹配字符串
     */
    public List<String> matchTemplate(String[] model, String[] s) {
        // 按照模版的长度排序
        Arrays.sort(model, (o1, o2) -> o1.length() - o2.length());
        Trie head = new Trie();
        // 树创建
        for (int i = 0; i < model.length; i++) {
            Trie tempTrie = head;
            String[] sub = model[i].split(" ");
            for (int j = 0; j < sub.length; j++) {
                if (!tempTrie.children.containsKey(sub[j])) {
                    tempTrie.children.put(sub[j], new Trie());
                }
                tempTrie = tempTrie.children.get(sub[j]);
            }
            tempTrie.list.add(i);
        }
        // 字符串匹配
        List<String> result = new ArrayList<>();
        for (String str : s) {
            String[] matchedStr = str.split(" ");
            int o = dfsFind(head, matchedStr, 0);
            result.add(o < 0 ? "" : model[o]);
        }
        return result;
    }

    private Integer dfsFind(Trie node, String[] matchedStr, int index) {
        int ans = -1;
        if (index == matchedStr.length) {
            if (!node.list.isEmpty()) ans = node.list.get(0);
            return ans;
        }
        // 模版匹配结束结果记录
        if (!node.list.isEmpty()) {
            ans = Math.max(ans, node.list.get(0));
        }
        for (Map.Entry<String, Trie> entry : node.children.entrySet()) {
            int j = index;
            while (j < matchedStr.length && !matchedStr[j].equals(entry.getKey())) {
                j++;
            }
            // 字符串匹配结束结果记录
            if (!node.list.isEmpty()) ans = Math.max(ans, node.list.get(0));
            if (j >= matchedStr.length) {
                continue;
            }
            ans = Math.max(ans, dfsFind(entry.getValue(), matchedStr, j));
        }
        return ans;
    }

    class Trie {
        // 存放hash的长度
        Map<String, Trie> children = new HashMap<>();
        // 本节点结尾的下标i
        List<Integer> list = new ArrayList<>();
    }
}

