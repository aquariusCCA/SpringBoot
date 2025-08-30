package com.test;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.OpType;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.test.pojo.BookDoc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ElasticSearchTest {
    @Autowired
    private ElasticsearchClient es;

    @Test
    void createDocument() throws Exception {
        String esId = "book_0001";  // ES 的 _id
        BookDoc doc = new BookDoc("book_0001", "springboot", "springboot", "springboot");

        IndexResponse resp = es.index(i -> i
                .index("books")
                .id(esId)               // 指定 _id
                .opType(OpType.Create)  // 僅允許新增；若 _id 已存在會拋 409
                .document(doc)
        );

        System.out.println("Index Response: " + resp);
    }

    @Test
    void getById() throws Exception {
        String esId = "book_0001"; // 這是 ES 的 _id，不是來源字段 id

        GetResponse<BookDoc> resp = es.get(g -> g
                        .index("books")
                        .id(esId),
                BookDoc.class);

        if (!resp.found()) {
            System.out.println("Not found: _id=" + esId);
            return;
        }

        BookDoc doc = resp.source();
        System.out.println("Found: _id=" + esId + ", name=" + doc.getName() + ", type=" + doc.getType());
    }

    @Test
    void searchByKeywordAndType() throws Exception {
        String keyword = "springboot"; // 中文可直接放，例如 "分布式 事務"
        String type    = "springboot"; // mapping 裡的 type 是 keyword，適合做 term 過濾

        SearchResponse<BookDoc> resp = es.search(s -> s
                        .index("books")
                        // 查詢條件：must 使用 analyzed 欄位，filter 用 keyword 精確比對
                        .query(q -> q.bool(b -> b
                                .must(m -> m.match(mm -> mm.field("all").query(keyword)))
                                .filter(f -> f.term(t -> t.field("type").value(type)))
                        ))
                        // 分頁：第 1 頁（from=0），每頁 10 筆
                        .from(0)
                        .size(10)
                        // 排序：先依 _score（預設即如此；此處顯式示範）
                        .sort(so -> so.field(f -> f.field("_score").order(SortOrder.Desc)))
                        // 為了得到精確的 total（非僅估算）
                        .trackTotalHits(t -> t.enabled(true)),
                BookDoc.class);

        // 輸出結果
        long total = resp.hits().total() != null ? resp.hits().total().value() : 0L;
        System.out.println("Total hits = " + total);

        for (Hit<BookDoc> hit : resp.hits().hits()) {
            BookDoc d = hit.source();
            System.out.println(String.format("_id=%s, score=%.4f, name=%s, type=%s",
                    hit.id(), hit.score() == null ? 0.0 : hit.score(), d.getName(), d.getType()));
        }
    }
}