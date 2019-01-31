package com.buydeem;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zengchao on 2019/1/31.
 */
public class IndexWriterDemo {
    /**
     * IndexWriter:主要用来创建和维护索引,可以通过 IndexWriterConfig 配置写索引的一些设置
     * IndexWriterConfig: IndexWriter的配置类
     *
     *
     *
     *
     *
     */
    public static void main(String[] args) throws IOException {
        FSDirectory directory = FSDirectory.open(Paths.get("C:\\Users\\buydeem\\IdeaProjects\\lucenestudy\\index"));
        IndexWriterConfig config = new IndexWriterConfig();
        //设置打开模式
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        //commit point的管理策略 KeepOnlyLastCommitDeletionPolicy(默认)/NoDeletionPolicy/SnapshotDeletionPolicy
        //config.setIndexDeletionPolicy();
        //设置关闭时,是否提交
        //config.setCommitOnClose();
        //设置打开时的提交点
        //config.setIndexCommit();
        //
        //config.setCheckPendingFlushUpdate();

        IndexWriter indexWriter = new IndexWriter(directory,config);
    }
}
