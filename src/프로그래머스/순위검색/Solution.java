package 프로그래머스.순위검색;

import 프로그래머스.MyUtil.ArrayParser;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 문제 플랫폼 : 프로그래머스
 * 문제 이름 : 순위 검색
 * 문제 링크 :  https://school.programmers.co.kr/learn/courses/30/lessons/72412
 * 난이도 :  Lv.2
 */
public class Solution {
    public static void main(String[] args) {
        String input;
        input = "[\"java backend junior pizza 150\",\"python frontend senior chicken 210\",\"python frontend senior chicken 150\",\"cpp backend senior pizza 260\",\"java backend junior chicken 80\",\"python backend senior chicken 50\"]";
        String[] info = ArrayParser.parseArray(input, String[].class);
        input = "[\"java and backend and junior and pizza 100\",\"python and frontend and senior and chicken 200\",\"cpp and - and senior and pizza 250\",\"- and backend and senior and - 150\",\"- and - and - and chicken 100\",\"- and - and - and - 150\"]";
        String[] query = ArrayParser.parseArray(input, String[].class);
        int[] output = solution(info, query);
        System.out.println(Arrays.toString(output));
    }

    public static int[] solution(String[] info, String[] query) {
        Map<String, List<Integer>> scoresMap = buildScoreMap(info);
        /**
        int[] answer = new int[query.length];
        for(int i=0; i<answer.length; i++) {
            answer[i] = count(query[i], scoresMap);
        }
        return answer;
        */
        return Stream.of(query).mapToInt(q -> count(q, scoresMap)).toArray();
    }

    static Map<String, List<Integer>> buildScoreMap(String[] info) {
        Map<String, List<Integer>> scoresMap = new HashMap<>();

        // 전처리
        for (String s: info) {
            String[] tokens = s.split(" ");
            int score = Integer.parseInt(tokens[tokens.length - 1]);
            forEachKey(0, "", tokens, key -> {
                scoresMap.putIfAbsent(key, new ArrayList<>());
                scoresMap.get(key).add(score);
            });
        }
        for(List<Integer> list : scoresMap.values()) {
            Collections.sort(list);
        }
        return scoresMap;
    }

    static void forEachKey(int index, String prefix, String[] tokens, Consumer<String> action) {
        if(index == tokens.length - 1) {
            action.accept(prefix);
            return;
        }

        forEachKey(index + 1, prefix + tokens[index], tokens, action);
        forEachKey(index + 1, prefix + "-", tokens, action);
    }

    static int count(String query, Map<String, List<Integer>> scoresMap) {
        String[] tokens = query.split(" (and )?");
        String key = String.join("", Arrays.copyOf(tokens, tokens.length -1));

        if(!scoresMap.containsKey(key))
            return 0;
        List<Integer> scores = scoresMap.get(key);

        int score = Integer.parseInt(tokens[tokens.length - 1]);
        return scores.size() - binarySearch(score, scoresMap.get(key));
    }

    static int binarySearch(int score, List<Integer> scores) {
        int start = 0;
        int end = scores.size() - 1;

        while(end > start) {
            int mid = (start + end) / 2;

            if(scores.get(mid) >= score) {
                end = mid;
            } else {
                start = mid + 1;
            }
        }

        if(scores.get(start) < score) {
            return scores.size();
        }

        return start;
    }
}
