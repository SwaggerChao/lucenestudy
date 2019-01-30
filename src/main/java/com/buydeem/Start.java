package com.buydeem;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by zengchao on 2019/1/30.
 */
public class Start {
    /**
     * 创建索引
     * @param content 索引名称
     * @throws IOException
     */
    public void createIndex(String content) throws IOException {
        //设置索引存放位置
        Directory directory = FSDirectory.open(Paths.get("C:\\Users\\buydeem\\IdeaProjects\\lucenestudy\\index"));
        //设置分词器
        Analyzer analyzer = new StandardAnalyzer();
        //设置创建索引配置
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //创建写文件的write
        IndexWriter writer = new IndexWriter(directory,config);
        //创建Document
        Document doc = new Document();
        doc.add(new Field("content",content,TextField.TYPE_STORED));
        writer.addDocument(doc);
        writer.close();
    }

    public List<String> queryContent(String key) throws IOException, ParseException {
        //读取索引
        Directory directory = FSDirectory.open(Paths.get("C:\\Users\\buydeem\\IdeaProjects\\lucenestudy\\index"));
        DirectoryReader reader = DirectoryReader.open(directory);
        //创建IndexSearch
        IndexSearcher searcher = new IndexSearcher(reader);
        //创建query
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("content", analyzer);
        Query query = parser.parse(key);
        TopDocs docs = searcher.search(query, 10);
        ScoreDoc[] hits = docs.scoreDocs;
        List<String> list = new LinkedList<>();
        for (int i = 0; i < hits.length; i++) {
            Document doc = searcher.doc(hits[i].doc);
            list.add(doc.get("content"));
        }
        return list;
    }

    public static void main(String[] args) throws IOException, ParseException {
        Start start = new Start();
        //start.createIndex("Start study lucene,I'm so happy");
        List<String> list = start.queryContent("happy");
        System.out.println(list);
    }
}
