package shared;

import java.util.Arrays;

public class Utils {
    static byte[] combineArrays(byte[]... arrays) {
        byte[] newArray = new byte[Arrays.stream(arrays).mapToInt(array -> array.length).sum()];
        int offset = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, newArray, offset, array.length);
            offset += array.length;
        }
        return newArray;
    }

    static byte[] asArray(byte... bytes) {
        return bytes;
    }

    static String nulTerminateString(String str) {
        return str.substring(0, str.indexOf('\0'));
    }
}
