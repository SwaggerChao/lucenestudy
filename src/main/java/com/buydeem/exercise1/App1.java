package com.buydeem.exercise1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 索引一个文件夹中的txt文件
 * Created by zengchao on 2019/2/1.
 */
public class App1 {
    /**
     * 读取一个文件加下的所有txt文件内容
     * @param path 文件夹位置
     * @return
     * @throws IOException
     */
    public List<TxtFile> getAllList(Path path) throws IOException {
        TxtFileVisitor txtFileVisitor = new TxtFileVisitor();
        Files.walkFileTree(path,txtFileVisitor);
        List<Path> allPaths = txtFileVisitor.getPaths();
        List<TxtFile> txtFiles = new LinkedList<>();
        for (Path tempPath : allPaths) {
            List<String> list = Files.readAllLines(tempPath, Charset.forName("utf-8"));
            StringBuilder sb = new StringBuilder();
            list.forEach(sb::append);
            TxtFile temp = new TxtFile();
            //获取文件名称
            String fileName = tempPath.toString();
            BasicFileAttributes attributes = Files.readAttributes(tempPath, BasicFileAttributes.class);
            //获取文件创建时间
            long createTime = attributes.creationTime().to(TimeUnit.MILLISECONDS);
            //获取文件的修改时间
            long modifyTime = attributes.lastModifiedTime().to(TimeUnit.MILLISECONDS);
            temp.setFileName(fileName);
            temp.setContent(sb.toString());
            temp.setCreateTime(new Date(createTime));
            temp.setModifyTime(new Date(modifyTime));
            txtFiles.add(temp);
        }
        return txtFiles;
    }

    class TxtFileVisitor extends SimpleFileVisitor<Path>{
        private List<Path> paths = new LinkedList<>();
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            boolean isTxt = file.toString().endsWith(".txt");
            if (isTxt){
                paths.add(file);
            }
            return super.visitFile(file, attrs);
        }

        public List<Path> getPaths() {
            return paths;
        }
    }

    /**
     * 创建IndexWriter
     * @param path 索引路径
     * @param analyzer 分词器
     * @return
     */
    public IndexWriter createIndexWriter(Path path, Analyzer analyzer) throws IOException {
        FSDirectory directory = FSDirectory.open(path);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setInfoStream(System.out);
        return new IndexWriter(directory,config);
    }

    /**
     * TxtFile转化成Document
     * @param txtFile
     * @return
     */
    public static Document TxtFile2Document(TxtFile txtFile){
        Document doc = new Document();
        //创建Filed
        //创建文件名File
        FieldType fileNameFieldType = new FieldType();
        fileNameFieldType.setTokenized(false);
        fileNameFieldType.setStored(true);
        fileNameFieldType.setIndexOptions(IndexOptions.DOCS);
        fileNameFieldType.setOmitNorms(true);
        fileNameFieldType.freeze();
        Field fileNameField = new Field("fileName",txtFile.getFileName(),fileNameFieldType);
        //创建文件内容FieldType
        FieldType fileContentFieldType = new FieldType();
        fileContentFieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        fileContentFieldType.setTokenized(true);
        fileContentFieldType.setStored(true);
        fileContentFieldType.freeze();
        Field fileContentField = new Field("content",txtFile.getContent(),fileContentFieldType);
        // 创建
        LongPoint createTimeField = new LongPoint("createTime",txtFile.getCreateTime().getTime());
        StoredField createTimeStoredField = new StoredField("createTime",txtFile.getCreateTime().getTime());

        doc.add(fileNameField);
        doc.add(fileContentField);
        doc.add(createTimeStoredField);
        doc.add(createTimeField);

        return doc;
    }


    public static void main(String[] args) throws IOException {
        App1 app = new App1();
        List<TxtFile> list = app.getAllList(Paths.get("C:\\Users\\buydeem\\Desktop\\lucene测试文件夹"));
        List<Document> docList = list.stream().map(App1::TxtFile2Document).collect(Collectors.toList());

        IndexWriter writer = app.createIndexWriter(Paths.get("C:\\Users\\buydeem\\Desktop\\index"), new StandardAnalyzer());

        writer.addDocuments(docList);

        writer.close();
    }
}
