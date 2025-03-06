package com.emotionalcart.application;

import com.emotionalcart.domain.RedisProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.args.SortingOrder;
import redis.clients.jedis.search.FTSearchParams;
import redis.clients.jedis.search.SearchResult;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisProductService {

    private final JedisPooled jedisPooled;

    private static final String PRODUCT_PREFIX = "product:";

    /**
     * 상품 저장 (검색용 필드 + KNN 벡터 저장)
     */
    public void saveProduct(RedisProduct product) {
        String key = PRODUCT_PREFIX + product.getId();
        // 개별 필드 저장
        jedisPooled.hset(key, "name", product.getName());
        jedisPooled.hset(key, "description", product.getDescription());
        jedisPooled.hset(key, "category", Long.toString(product.getCategoryId()));

        // 벡터 데이터를 바이트 배열로 변환하여 저장
        byte[] vectorBytes = convertVectorToBytes(product.getVector());
        jedisPooled.hset(key.getBytes(), "vector".getBytes(), vectorBytes);
    }

    /**
     * 상품 검색 (RediSearch 기반)
     */
    public List<RedisProduct> searchProducts(String keyword) {
        String query = "@name:" + keyword;
        List<RedisProduct> results = new ArrayList<>();

        // RediSearch를 사용한 상품 검색
        jedisPooled.ftSearch("product_index", query).getDocuments().forEach(doc -> results.add(new RedisProduct(
            Long.parseLong(doc.getId()),
            doc.getString("name"),
            doc.getString("description"),
            Long.parseLong(doc.getString("category")),
            null // 벡터는 검색 시 제외
        )));

        return results;
    }

    /**
     * 유사한 상품 추천 (KNN 벡터 검색)
     */
    public List<RedisProduct> recommendProducts(String productId) {
        String key = "product:" + productId;
        byte[] queryVector = jedisPooled.hget(key.getBytes(), "vector".getBytes());
        if (queryVector == null) {
            throw new RuntimeException("상품의 벡터 데이터가 없습니다.");
        }
        // KNN 검색 파라미터 설정
        FTSearchParams params = new FTSearchParams()
            .addParam("query_vector", queryVector)  // 벡터 검색 파라미터 추가
            .dialect(2) // Redis 7 이상에서 KNN 검색을 올바르게 실행하기 위해 필요
            .sortBy("vector", SortingOrder.ASC) // 벡터 유사도 기준 정렬 (높을수록 유사)
            .returnFields("name", "vector") // 결과에 `name`과 `score` 포함
            .limit(0, 5); // 상위 5개 추천

        SearchResult searchResult = jedisPooled.ftSearch(
            "product_index", "*=>[KNN 5 @vector $query_vector AS score]", params);
        List<RedisProduct> recommendations = new ArrayList<>();
        searchResult.getDocuments().forEach(doc -> recommendations.add(new RedisProduct(
            Long.parseLong(doc.getId()),
            doc.getString("name"),
            doc.getString("description"),
            Long.parseLong(doc.getString("category")),
            null
        )));

        return recommendations;
    }

    public List<RedisProduct> searchSimilarProducts(String keyword) {
        String query = "@name:" + keyword; // 상품명 기준 검색
        List<RedisProduct> results = new ArrayList<>();

        // RediSearch 실행
        SearchResult searchResult = jedisPooled.ftSearch("product_index", query);

        searchResult.getDocuments().forEach(doc -> results.add(new RedisProduct(
            Long.parseLong(doc.getId()),
            doc.getString("name"),
            doc.getString("description"),
            Long.parseLong(doc.getString("category")),
            null // 벡터 정보는 제외
        )));

        return results;
    }

    /**
     * 벡터 데이터를 바이트 배열로 변환 (Redis 저장용)
     */
    private byte[] convertVectorToBytes(List<Float> vector) {
        ByteBuffer buffer = ByteBuffer.allocate(vector.size() * Float.BYTES);
        vector.forEach(buffer::putFloat);
        return buffer.array();
    }

}
