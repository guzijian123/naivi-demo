package com.gzj.demo.utils;

import java.util.Random;

public class ValidateCodeUtil {
    public static String generatorCode(int bit){
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < bit; i++) {
            code = code.append(new Random().nextInt(10));
        }
        return code.toString();
    }
}
