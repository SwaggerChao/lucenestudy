package com.buydeem;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zengchao on 2019/2/1.
 */
public class IndexSearchDemo {
    public static void main(String[] args) throws IOException, ParseException {
        FSDirectory directory = FSDirectory.open(Paths.get("C:\\Users\\buydeem\\Desktop\\index"));
        DirectoryReader reader = DirectoryReader.open(directory);
        //构建query
        QueryParser queryParser = new QueryParser("content", new StandardAnalyzer());
        Query query = queryParser.parse("国家");
        //创建IndexSearcher
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs topDocs = searcher.search(query, 10);
        //
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0; i < scoreDocs.length; i++) {
            int docId = scoreDocs[i].doc;
            Document doc = reader.document(docId);
            System.out.println("doc = " + doc.toString());
        }


        reader.close();
    }
}
