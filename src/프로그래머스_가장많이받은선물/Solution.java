package 프로그래머스_가장많이받은선물;

import MyUtil.ArrayParser;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Solution {
    public static void main(String[] args) {
        String[][] input = {
                {
                        "[\"muzi\", \"ryan\", \"frodo\", \"neo\"]",
                        "[\"muzi frodo\", \"muzi frodo\", \"ryan muzi\", \"ryan muzi\", \"ryan muzi\", \"frodo muzi\", \"frodo ryan\", \"neo muzi\"]"
                },
                {
                        "[\"joy\", \"brad\", \"alessandro\", \"conan\", \"david\"]",
                        "[\"alessandro brad\", \"alessandro joy\", \"alessandro conan\", \"david alessandro\", \"alessandro david\"]"
                },
                {
                        "[\"a\", \"b\", \"c\"]",
                        "[\"a b\", \"b a\", \"c a\", \"a c\", \"a c\", \"c a\"]"
                }
        };
        for (String[] str : input) {
            String[] friends = ArrayParser.parseArray(str[0], String[].class);
            String[] gifts = ArrayParser.parseArray(str[1], String[].class);
            int answer = solution(friends, gifts);
            System.out.println(answer);
        }
    }

    public static int solution(String[] friends, String[] gifts) {
        int size = friends.length;
        int[][] giftMatrix = new int[size][size];
        Arrays.stream(gifts)
                .map(gift -> gift.split(" "))
                .forEach(parts -> {
                    String giver = parts[0];
                    String receiver = parts[1];
                    int giverIndex = Arrays.asList(friends).indexOf(giver);
                    int receiverIndex = Arrays.asList(friends).indexOf(receiver);
                    giftMatrix[giverIndex][receiverIndex]++;
                });

        int[][] giftIndicesMatrix = new int[size][3];

        // 각 친구의 주는 선물 수 (행 단위 합)
        for (int i = 0; i < size; i++) {
            int sumGive = Arrays.stream(giftMatrix[i]).sum();
            giftIndicesMatrix[i][0] = sumGive;
        }

        // 각 친구의 받는 선물 수 (열 단위 합)
        for (int i = 0; i < size; i++) {
            int sumReceive = 0;
            for (int j = 0; j < size; j++) {
                sumReceive += giftMatrix[j][i];
            }
            giftIndicesMatrix[i][1] = sumReceive;
        }

        // 각 친구의 선물 지수 계산 및 저장
        for (int i = 0; i < size; i++) {
            giftIndicesMatrix[i][2] = giftIndicesMatrix[i][0] - giftIndicesMatrix[i][1];
        }

        return IntStream.range(0, size)
                .map(i -> IntStream.range(0, size)
                        .filter(j -> i != j) // 자기 자신은 제외
                        .map(j -> {
                            if (giftMatrix[i][j] != 0 && giftMatrix[j][i] != 0 && giftMatrix[i][j] != giftMatrix[j][i]) {
                                if (giftMatrix[i][j] > giftMatrix[j][i]) {
                                    return 1; // 선물을 많이 받은 경우
                                }
                            } else {
                                if (giftMatrix[i][j] == 0 && giftMatrix[j][i] != 0) return 0; // 상대방이 준 경우는 계산에서 제외
                                if (giftIndicesMatrix[i][2] > giftIndicesMatrix[j][2]) {
                                    return 1; // 선물 지수가 높은 경우
                                }
                            }
                            return 0; // 기본적으로 0 반환
                        })
                        .sum()) // 각 친구별로 받은 선물 수를 합산
                .max() // 최대값 계산
                .orElse(0); // 만약 최대값이 없으면 기본값으로 0 반환;
    }
}
