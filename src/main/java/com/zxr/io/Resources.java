package com.zxr.io;

import java.io.InputStream;

/**
 * @author zhaoxiangrui
 * @date 2020/11/8 19:53
 */
public class Resources {

    //根据配置文件路径，将配置文件加载成字节输入流，存储在内存中
    public static InputStream getResourceAsStream(String path){
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}
