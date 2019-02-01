package com.buydeem;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zengchao on 2019/2/1.
 */
public class DocumentAndFieldDemo {
    /**
     * Document 是一个容器,里面用来存放Field,它的内部就是使用一个List来存放Field
     * Field 主要有三部分:name, type and value
     * 主要的域有 :
     * TextField: 索引并不分词,不包括term vectors,通常用于全文检索
     * StringField: 只索引但不分词，所有的字符串会作为一个整体进行索引，例如通常用于country或id等
     * IntPoint: 对int类型索引,不存储
     * LongPoint: 对long类型索引,不存储
     * FloatPoint: 对float类型索引,不存储
     * DoublePoint: 对double类型索引,不存储
     * SortedDocValuesField: 为每个Document存储一个这样的Field,这个field会存储一个String值，可以利用它来实现排序和打分
     * SortedSetDocValuesField: 为每个Document存储一个这样的Field,这个field会存储一组String值，可以利用它来实现排序和打分
     * NumericDocValuesField: 为每个Document存储一个这样的Field,这个field会存储一个long值，可以利用它来实现排序和打分
     * SortedNumericDocValuesField: 为每个Document存储一个这样的Field,这个field会存储一组long值，可以利用它来实现排序和打分
     * StoredField: 为字段创建一个只存储的Field
     *
     * 上面是Lucene默认提供的,但是我们自己可以自定义Field,通过给Field设置IndexableFieldType可以实现自己想要的效果
     * IndexableFieldType几个主要属性:
     *   stored 是否存储原值
     *   tokenized 是否分词,默认分词
     *   storeTermVectors 当lucene建立起倒排索引后，默认情况下它会保存所有必要的信息实施Vector Space Model。该Model需要计算文档中出现的term数，以及他们出现的位置。该属性仅当indexed为true时生效。他会为field建立一个小型的倒排索引
     *   storeTermVectorOffsets 是否存储词向量的偏移量
     *   storeTermVectorPositions 是否存储词向量的位置
     *   storeTermVectorPayloads 是否存储field中token的比重到term vectors中
     *   omitNorms 是否要忽略field的加权基准值，如果为true可以节省内存消耗，但是会牺牲打分质量，另外你也不能使用index-time  进行加权操作。
     *   indexOptions
     *      NONE：Not indexed 不索引
     *      DOCS: 反向索引中只存储了包含该词的 文档id，没有词频、位置
     *      DOCS_AND_FREQS: 反向索引中会存储 文档id、词频
     *      DOCS_AND_FREQS_AND_POSITIONS:反向索引中存储 文档id、词频、位置
     *      DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS :反向索引中存储 文档id、词频、位置、偏移量
     *   frozen 冻结,禁止修改
     *   docValuesType 为需要排序、分组、聚合的字段指定如何为该字段创建文档->字段值的正向索引的
     *      NONE 不开启docvalue
     *      NUMERIC 单值、数值字段，用这个
     *      BINARY 单值、字节数组字段用
     *      SORTED 单值、字符字段用， 会预先对值字节进行排序、去重存储
     *      SORTED_NUMERIC 单值、数值数组字段用，会预先对数值数组进行排序
     *      SORTED_SET 多值字段用，会预先对值字节进行排序、去重存储
     *   具体使用选择：
     *   字符串+单值 会选择SORTED作为docvalue存储
     *   字符串+多值 会选择SORTED_SET作为docvalue存储
     *   数值或日期或枚举字段+单值 会选择NUMERIC作为docvalue存储
     *   数值或日期或枚举字段+多值 会选择SORTED_SET作为docvalue存储
     *   注意：需要排序、分组、聚合、分类查询（面查询）的字段才创建docValues
     */

    public static void main(String[] args) throws IOException {
        //创建Directory
        FSDirectory directory = FSDirectory.open(Paths.get("C:\\Users\\buydeem\\IdeaProjects\\lucenestudy\\index"));
        //创建IndexWriterConfig
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        //索引过程信息写出到控制台
        config.setInfoStream(System.out);
        //创建IndexWriter
        IndexWriter writer = new IndexWriter(directory,config);

        //创建doc
        Document doc1 = new Document();
        //自定义域类型
        FieldType type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(true);
        type.setTokenized(true);
        type.freeze();
        //创建域 f1
        Field f1 = new Field("orderNo","I'm so good",type);
        //将域添加到文档
        doc1.add(f1);
        //将文档添加到索引
        writer.addDocument(doc1);
        //写索引的writer关闭,关闭前会自动commit 和 flush
        writer.close();
    }
}
