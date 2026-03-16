package util;

import java.util.Arrays;
import java.util.List;

public class CategoryUtil {
    public static String[] names = {
            "sports", "travel", "music", "coding", "cooking",
            "reading", "gaming", "fitness", "photography", "art",
            "dance", "yoga", "cycling", "fishing", "camping",
            "language", "movie", "crafts", "volunteering", "investment"
    };
    public static List<String> list = Arrays.stream(names).map(name -> name).toList();
    public static boolean existCategory(String name){
        return list.contains(name);

    }
}
