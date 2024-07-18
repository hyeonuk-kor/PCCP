package 프로그래머스.MyUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayParser {

    public static void main(String[] args) {
        // 예제 문자열
        String str1DInt = "[1,2,3,4,5]";
        String str1DString = "[\"apple\",\"banana\",\"cherry\"]";
        String str1DChar = "['a','b','c','d']";
        String str2DInt = "[[1,2,3],[4,5,6],[7,8,9]]";
        String str2DString = "[[\"one\",\"two\"],[\"three\",\"four\"]]";
        String str2DChar = "[['a','b'],['c','d']]";
        String str3DInt = "[[[1,2],[3,4]],[[5,6],[7,8]],[[9,10],[11,12]]]";
        String str3DString = "[[['a','b'],['c','d']],[['e','f'],['g','h']],[['i','j'],['k','l']]]";
        String str3DChar = "[[['A','B'],['C','D']],[['E','F'],['G','H']],[['I','J'],['K','L']]]";

        // 1차원 배열 변환 및 출력
        int[] array1DInt = parseArray(str1DInt, int[].class);
        System.out.println("1차원 int 배열:");
        System.out.println(Arrays.toString(array1DInt));

        String[] array1DString = parseArray(str1DString, String[].class);
        System.out.println("1차원 String 배열:");
        System.out.println(Arrays.toString(array1DString));

        char[] array1DChar = parseArray(str1DChar, char[].class);
        System.out.println("1차원 char 배열:");
        System.out.println(Arrays.toString(array1DChar));

        // 2차원 배열 변환 및 출력
        int[][] array2DInt = parseArray(str2DInt, int[][].class);
        System.out.println("2차원 int 배열:");
        System.out.println(Arrays.deepToString(array2DInt));

        String[][] array2DString = parseArray(str2DString, String[][].class);
        System.out.println("2차원 String 배열:");
        System.out.println(Arrays.deepToString(array2DString));

        char[][] array2DChar = parseArray(str2DChar, char[][].class);
        System.out.println("2차원 char 배열:");
        System.out.println(Arrays.deepToString(array2DChar));

        // 3차원 배열 변환 및 출력
        int[][][] array3DInt = parseArray(str3DInt, int[][][].class);
        System.out.println("3차원 int 배열:");
        System.out.println(Arrays.deepToString(array3DInt));

        String[][][] array3DString = parseArray(str3DString, String[][][].class);
        System.out.println("3차원 String 배열:");
        System.out.println(Arrays.deepToString(array3DString));

        char[][][] array3DChar = parseArray(str3DChar, char[][][].class);
        System.out.println("3차원 char 배열:");
        System.out.println(Arrays.deepToString(array3DChar));
    }

    // 배열 파싱 함수
    public static <T> T parseArray(String str, Class<T> clazz) {
        List<Object> list = new ArrayList<>();
        parseArrayElements(str, list);
        return convertToObjectArray(list, clazz);
    }

    // 배열 요소 파싱 함수
    private static void parseArrayElements(String str, List<Object> list) {
        // 배열이면
        if (str.startsWith("[[")) {
            // 2차원 배열 처리
            String[] rows = str.substring(2, str.length() - 2).split("],\\[");
            for (String row : rows) {
                List<Object> sublist = new ArrayList<>();
                parseArrayElements("[" + row + "]", sublist);
                list.add(sublist.toArray());
            }
        } else if (str.startsWith("[")) {
            // 1차원 배열 처리
            String[] elements = str.substring(1, str.length() - 1).split(",");
            for (String element : elements) {
                list.add(parseElement(element.trim()));
            }
        } else {
            throw new IllegalArgumentException("Invalid array format: " + str);
        }
    }

    // 요소 파싱 함수
    private static Object parseElement(String element) {
        // 따옴표로 묶인 문자열인 경우 따옴표를 제거하고 실제 문자열 값을 반환
        if (element.startsWith("\"") && element.endsWith("\"")) {
            return element.substring(1, element.length() - 1);
        } else {
            // 정수로 변환 가능한 경우 정수로 변환하여 반환
            try {
                return Integer.parseInt(element.trim());
            } catch (NumberFormatException e) {
                // 정수로 변환할 수 없는 경우 예외 처리
                throw new IllegalArgumentException("Invalid element format: " + element);
            }
        }
    }


    // Object 배열을 지정된 클래스로 변환하는 함수
    @SuppressWarnings("unchecked")
    private static <T> T convertToObjectArray(List<Object> list, Class<T> clazz) {
        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            if (componentType.isPrimitive()) {
                // 기본 데이터 타입 배열 처리
                return convertToPrimitiveArray(list, componentType);
            } else {
                // 객체 타입 배열 처리
                Object[] array = (Object[]) java.lang.reflect.Array.newInstance(componentType, list.size());
                for (int i = 0; i < list.size(); i++) {
                    Object element = list.get(i);
                    if (element instanceof List) {
                        array[i] = convertToObjectArray((List<Object>) element, componentType);
                    } else {
                        array[i] = element;
                    }
                }
                return (T) array;
            }
        } else {
            throw new IllegalArgumentException("Class must be an array type");
        }
    }

    // 기본 데이터 타입 배열 변환 함수
    @SuppressWarnings("unchecked")
    private static <T> T convertToPrimitiveArray(List<Object> list, Class<?> componentType) {
        if (componentType == int.class) {
            int[] array = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = (int) list.get(i);
            }
            return (T) array;
        } else if (componentType == char.class) {
            char[] array = new char[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = (char) list.get(i);
            }
            return (T) array;
        } else {
            throw new IllegalArgumentException("Unsupported primitive array type: " + componentType.getName());
        }
    }

    // 배열을 "[ , , ]" 형태의 문자열로 출력하는 함수
    public static <T> String printArray(T array) {
        if (array.getClass().isArray()) {
            if (array instanceof Object[]) {
                return "[" + Arrays.stream((Object[]) array)
                        .map(ArrayParser::printArray)
                        .reduce((s1, s2) -> s1 + "," + s2)
                        .orElse("") + "]";
            } else if (array instanceof int[]) {
                return Arrays.toString((int[]) array);
            } else if (array instanceof byte[]) {
                return Arrays.toString((byte[]) array);
            } else if (array instanceof short[]) {
                return Arrays.toString((short[]) array);
            } else if (array instanceof long[]) {
                return Arrays.toString((long[]) array);
            } else if (array instanceof float[]) {
                return Arrays.toString((float[]) array);
            } else if (array instanceof double[]) {
                return Arrays.toString((double[]) array);
            } else if (array instanceof boolean[]) {
                return Arrays.toString((boolean[]) array);
            } else if (array instanceof char[]) {
                return Arrays.toString((char[]) array);
            } else {
                return "Unsupported array type";
            }
        } else {
            return String.valueOf(array);
        }
    }
}