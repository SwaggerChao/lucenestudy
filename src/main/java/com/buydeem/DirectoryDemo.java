package com.buydeem;

import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 存储索引相关
 * Created by zengchao on 2019/1/31.
 */
public class DirectoryDemo {
    /**
     *
    文件目录
    FSDirectory

    并发性不好,多线程读取同一文件时,会进行同步操作
    SimpleFSDirectory
    多线程读时,不会存在同步问题.但是在windows时有bug
    NIOFSDirectory
    通过内存映射进行读，通过FSIndexOutput进行写的FSDirectory实现类。使用该类时要保证用足够的虚拟地址空间
    MMapDirectory

    内存目录
    在内存中暂存索引文件，只对小索引好，大索引会出现频繁GC
    RAMDirectory ramDirectory = new RAMDirectory();
    包装了RAMDirectory 主要用在近实时搜索
    NRTCachingDirectory directory = new NRTCachingDirectory(fsDirectory,5.0,30.0);

    代理类以及工具类
    文件切换的Directory实现，是针对lucene的不同的索引文件使用不同的Directory
      FileSwitchDirectory
    访问一个组合的数据流。仅适用于读操作
      CompoundFileDirectory
    Directory的代理类，用于记录哪些文件被写入和删除
      TrackingDirectoryWrapper
    通过IOContext来限制读写速率的Directory封装类
      RateLimitedDirectoryWrapper

    常用的  基本上使用FSDirectory.open()就行
     */
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("C:\\Users\\buydeem\\IdeaProjects\\lucenestudy\\index\\");
        FSDirectory directory = FSDirectory.open(path);
    }
}
