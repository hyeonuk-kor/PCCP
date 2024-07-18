package 프로그래머스_메뉴리뉴얼;

import java.util.*;
import java.util.stream.Collectors;

public class Solution {
    public static void main(String[] args) {
        String[] orders = {"ABCFG", "AC", "CDE", "ACDE", "BCFG", "ACDEH"};
        int[] course = {2, 3, 4};
        String[] answer = solution(orders, course);
        System.out.println(Arrays.toString(answer));
    }

    private static class Course {
        public String course; // 코스명
        public int occurrences; // 등장 횟수

        public Course(String course, int occurrences) {
            this.course = course;
            this.occurrences = occurrences;
        }
    }

    /**
     * @param nextMenu      (상태 변수) 다음 메뉴
     * @param selectedMenus (상태 변수) 선택된 메뉴
     * @param orderList     주문목록 - 메뉴 조합을 검색하기 위해 List형식으로 작성
     * @param courses       구현 코스
     */
    static void getCources(char nextMenu, Set<String> selectedMenus,
                           List<Set<String>> orderList,
                           Map<Integer, List<Course>> courses) {
        // 조합한 메뉴들의 등장 횟수 구하기
        int occurrences = (int) orderList.stream()
                .filter(order -> order.containsAll(selectedMenus))
                .count();

        // 등장 횟수가 2회 미만인 조합은 진행하지 않음
        if (occurrences < 2)
            return;

        // 현재까지 구한 메뉴 조합에 포함된 메뉴 개수에 따라 코스 생성을 하거나 조합하지 않을 수도 있음
        int size = selectedMenus.size();
        if (courses.containsKey(size)) {
            // courses에는 구해야하는 코스 크기가 포함되어 있음
            List<Course> courseList = courses.get(size);

            // 현재까지 구한 코스는 selectedMenus를 통해 이용하여 생성함.
            String makeCourse = selectedMenus.stream()
                    .sorted()
                    .collect(Collectors.joining(""));

            // 구성된 코스 메뉴와 등장 횟수를 이용하여 Course 객체를 생성
            Course course = new Course(makeCourse, occurrences);

            Course original = courseList.get(0);

            if (original.occurrences < occurrences) {
                courseList.clear();
                courseList.add(course);
            } else if (original.occurrences == occurrences) {
                courseList.add(course);
            }
        }
        if (size >= 10)
            return;

        for (char menuChar = nextMenu; menuChar <= 'Z'; menuChar++) {
            String menu = String.valueOf(menuChar);
            selectedMenus.add(menu);
            getCources((char) (menuChar + 1), selectedMenus, orderList, courses);
            selectedMenus.remove(menu);
        }

    }

    static String[] solution(String[] orders, int[] course) {
        List<Set<String>> orderList = Arrays.stream(orders)
                .map(String::chars)
                .map(charStream -> charStream
                        .mapToObj(menu -> String.valueOf((char) menu))
                        .collect(Collectors.toSet()))
                .collect(Collectors.toList());

        Map<Integer, List<Course>> courses = new HashMap<>();
        for (int length : course) {
            List<Course> list = new ArrayList<>();
            list.add(new Course("", 0));
            courses.put(length, list);
        }

        getCources('A', new HashSet<>(), orderList, courses);

        return courses.values().stream()
                .filter(list -> list.get(0).occurrences > 0)
                .flatMap(List::stream)
                .map(c -> c.course)
                .sorted()
                .toArray(String[]::new);
    }
}
