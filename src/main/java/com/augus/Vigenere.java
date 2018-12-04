package com.augus;

import java.util.ArrayList;
import java.util.HashMap;

public class Vigenere {
    private String ciphertext;

    public Vigenere(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    // Friedman测试法确定密钥长度
    public int Friedman() {
        int keyLength = 1;
        ArrayList cipherGroup = new ArrayList();
        Double avgIc = 0D;
        while ((avgIc < 0.06)) {
            cipherGroup.clear();
            for (int i = 0; i < keyLength; i++) {
                // 根据密钥长度分组
                String subCipher = cipherGroup(keyLength, i);
                //计算每一组的重合指数
                avgIc = coincidence(subCipher);
                cipherGroup.add(subCipher);
                cipherGroup.add(avgIc);
            }
            keyLength++;
        }
        System.out.println("密文分组及其重合指数为：");
        for (int i = 0; i < cipherGroup.size(); i = i + 2) {
            System.out.println(cipherGroup.get(i) + "   重合指数: " + cipherGroup.get(i + 1));
        }
        System.out.println("密钥长度为： " + (keyLength - 1));
        return keyLength - 1;
//        int lastNum = avgIc.size() - 1;
//        return (avgIc.get(lastNum - 1) - 0.065) > (avgIc.get(lastNum) - 0.065) ? lastNum : lastNum - 1;
    }

    private double coincidence(String subCipher) {
        double ic = 0;
        HashMap<Character, Integer> occurrenceNumber = new HashMap<>(); // 字母及其出现的次数
        char[] charCipher = subCipher.toCharArray();
        //统计每个字母出现的次数
        for (char cipher : charCipher) {
            Integer times = occurrenceNumber.get(cipher);
            times = (times == null || times == 0) ? 1 : ++times;
            occurrenceNumber.put(cipher, times);
        }
        //计算重合指数
        for (Integer times : occurrenceNumber.values()) {
            ic += times * (times - 1);
        }
        double combinNum = Math.pow(subCipher.length(), 2);
        return ic /= combinNum;
    }

    private String cipherGroup(int keyLength, int i) {
        int diff = (int) Math.ceil(Double.valueOf(ciphertext.length() + "") / keyLength);
        int startIndex = diff * i;
        int endIndex = diff * (i + 1);
        endIndex = Math.min(endIndex, ciphertext.length());
        StringBuilder tempGroup = new StringBuilder();
        for (int j = 0; i + j * keyLength < ciphertext.length(); ++j) {
            tempGroup.append(ciphertext.charAt(i + j * keyLength));
        }
        return tempGroup.toString();
    }

    private boolean isExpectVul(ArrayList<Double> avgIc) {
        if (avgIc.size() < 2) {
            return false;
        }
        int lastNum = avgIc.size() - 1;
        return (avgIc.get(lastNum - 1) < 0.065 && avgIc.get(lastNum) > 0.065)
                || avgIc.get(lastNum - 1) > 0.065 && avgIc.get(lastNum) < 0.065;
    }

}