package 프로그래머스.모의고사;

import 프로그래머스.MyUtil.ArrayParser;
import java.util.stream.IntStream;
/**
 *  문제 플랫폼 : 프로그래머스
 *  문제 이름 : 모의고사
 *  문제 링크 :  https://school.programmers.co.kr/learn/courses/30/lessons/42840
 *  난이도 :  Lv.1
 */
public class Solution {
    public static void main(String[] args) {
        String[] input = {"[1,2,3,4,5]", "[1,3,2,4,2]"};
        for (String str : input) {
            int[] answer = ArrayParser.parseArray(str, int[].class);
            String output = ArrayParser.printArray(solution(answer));
            System.out.println(output);
        }
    }

    static final int[][] RULES = {
            {1, 2, 3, 4, 5},
            {2, 1, 2, 3, 2, 4, 2, 5},
            {3, 3, 1, 1, 2, 2, 4, 4, 5, 5}
    };

    static int getPicked(int person, int problem) {
        int[] rule = RULES[person];
        int index = problem % rule.length;
        return rule[index];
    }

    public static int[] solution(int[] answers) {
        int[] corrects = new int[3];
        int max = 0;
        for (int problem = 0; problem < answers.length; problem++) {
            int answer = answers[problem];
            for(int person = 0; person < 3; person++) {
                int picked = getPicked(person, problem);
                if(answer == picked) {
                    if(++corrects[person] > max) {
                        max = corrects[person];
                    }
                }
            }
        }
        final int maxCorrects = max;
        return IntStream.range(0, 3)
                .filter(i -> corrects[i] == maxCorrects)
                .map(i -> i+1)
                .toArray();
    }
}
