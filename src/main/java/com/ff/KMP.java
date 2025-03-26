package com.ff;

/**
 * kmp算法
 * 字符串匹配，从一个字符串中寻找是否存在另一个子串
 */
public class KMP {

    public static void main(String[] args) {
        String mainStr = "ABACABADABACABAB";
        String matchStr = "ABACABAB";
        int kmp = kmp(mainStr.toCharArray(), matchStr.toCharArray());
        System.out.println(kmp);
    }

    public static int kmp(char[] mainStr, char[] matchStr) {
        int[] next = buildNext(matchStr);

        int i = 0;
        int j = 0;
        while (i < mainStr.length) {
            if (mainStr[i] == matchStr[j]) {
                i++;
                j++;
            }else if(j > 0){
                j = next[j-1];
            }else{
                i++;
            }

            if (j == matchStr.length) {
                return i - j;
            }
        }

        return -1;
    }

    public static int[] buildNext(char[] matchStr) {
        int[] next = new int[matchStr.length];
        next[0] = 0;

        int i = 1;
        int pre_len = 0;
        while (i < next.length) {
            if(matchStr[i] == matchStr[pre_len]){
                pre_len ++;
                next[i] = pre_len;
                i++;
            }else{
                if (pre_len == 0) {
                    next[i] = 0;
                    i++;
                }else{
                    pre_len = next[pre_len-1];
                }
            }
        }

        return next;
    }
}
