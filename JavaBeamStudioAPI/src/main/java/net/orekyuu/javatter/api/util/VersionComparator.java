package net.orekyuu.javatter.api.util;

import java.util.Arrays;
import java.util.Comparator;

public class VersionComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        int[] version1 = Arrays.stream(o1.split("\\.")).mapToInt(Integer::parseInt).toArray();
        int[] version2 = Arrays.stream(o2.split("\\.")).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < Math.max(version1.length, version2.length); i++) {
            int v1 = version1.length > i ? version1[i] : 0;
            int v2 = version2.length > i ? version2[i] : 0;
            if (v1 == v2) {
                continue;
            }
            return v1 - v2;
        }

        return 0;
    }
}
