package cn.itcast.logistics.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchGenerationException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.bulk.BulkProcessor.Listener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ScrollableHitSource;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.javatuples.Triplet;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class ESHelper {


	private Builder builder;
    private RestHighLevelClient highLevelClient;

    
    public ESHelper(List<HttpHost> hosts) {
    	initial();
    }

    public ESHelper(Builder builder) {
		this.builder = builder;
		initial();
	}
    
    public void initial() {
    	if (null!=builder) {
			Map<String, Integer> hosts = builder.getHosts();
			String host = builder.getHost();
			int port = builder.getPort();
			if(null != hosts && hosts.size() > 0) {
				HttpHost[] httpHosts = new HttpHost[hosts.size()];
				int index = 0;
				for (Entry<String, Integer> hostEntry : hosts.entrySet()) {
					httpHosts[index] = new HttpHost(hostEntry.getKey(), hostEntry.getValue());
					index ++;
				}
			} else {
				if (!StringUtils.isEmpty(host) && port > 0) {
					highLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port)));
				} else {
					throw new RuntimeException("无法初始化ES的highLevelClient！");
				}
			}
		} else {
			throw new RuntimeException("无法初始化ES的highLevelClient！");
		}
    }
    
    public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String driver = "org.elasticsearch.xpack.sql.jdbc.EsDriver";
		private String url = "jdbc:es://http://itcast-logistics03:9200?timezone=UTC";
		private String host;
		private int port;
		private Map<String,Integer> hosts;
		private Properties prop = new Properties();
		protected String geDriver() {
			return driver;
		}
		public Builder withDriver(String driver) {
			this.driver = driver;
			return this;
		}
		protected String getUrl() {
			return url;
		}
		public Builder withUrl(String url) {
			this.url = url;
			return this;
		}
		protected String getHost() {
			return host;
		}
		public Builder withHost(String host) {
			this.host = host;
			return this;
		}
		protected int getPort() {
			return port;
		}
		public Builder withPort(int port) {
			this.port = port;
			return this;
		}
		protected Map<String, Integer> getHosts() {
			return hosts;
		}
		public Builder withHosts(Map<String, Integer> hosts) {
			this.hosts = hosts;
			return this;
		}
		public Properties getProp() {
			return prop;
		}
		public void withProp(Properties prop) {
			this.prop = prop;
		}
		public ESHelper build() {
			return new ESHelper(this);
		}
	}
	
	/**
	 * 验证索引是否存在
	 * @param indexName
	 * @return
	 */
	public boolean existIndex(String indexName) {
		boolean status = false;
		GetIndexRequest request = new GetIndexRequest(indexName);
		try {
			status = highLevelClient.indices().exists(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return status;
	}
	
	/**
	 * 创建索引
	 * @param indexName
	 * @return
	 */
	public boolean createIndex(String indexName) {
		boolean status = false;
		CreateIndexRequest request = new CreateIndexRequest(indexName);
		try {
			if (!existIndex(indexName)) {
				CreateIndexResponse response = highLevelClient.indices().create(request, RequestOptions.DEFAULT);
				if (response.isAcknowledged() || response.isShardsAcknowledged()) {
					status = true;
				}
			} else {
				System.err.println("==== 索引["+indexName+"]不存在！ ====");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return status;
	}
    
	/**
	 * 创建索引
	 * @param indexName
	 * @return
	 */
	public boolean asyncCreateIndex(String indexName) {
		boolean status = false;
		CreateIndexRequest request = new CreateIndexRequest(indexName);
		try {
			if (!existIndex(indexName)) {
				highLevelClient.indices().createAsync(request, RequestOptions.DEFAULT, new ActionListener<CreateIndexResponse>() {
					@Override
					public void onResponse(CreateIndexResponse response) {
					}
					@Override
					public void onFailure(Exception e) {
					}
				});
			} else {
				System.err.println("==== 索引["+indexName+"]不存在！ ====");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * 删除索引
	 * @param indexName
	 * @return
	 */
	public boolean deleteIndex(String indexName) {
		boolean status = false;
		DeleteIndexRequest request = new DeleteIndexRequest(indexName);
		try {
			if (existIndex(indexName)) {
				highLevelClient.indices().delete(request, RequestOptions.DEFAULT);
			} else {
				System.err.println("==== 索引["+indexName+"]不存在！ ====");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return status;
	}
	
	public void close() {
		try {
			if (null!=highLevelClient) {
				highLevelClient.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * 批量处理数据
     * @param requests
     * @return
     */
    public BulkResponse bulkDocument(List<DocWriteRequest<?>> requests) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        requests.forEach(req->{
        	bulkRequest.add(req);
        });
        return highLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }


    public void indexDocument(IndexRequest indexRequest) throws IOException {
        highLevelClient.index(indexRequest,RequestOptions.DEFAULT);
    }

    /**
     * 异步批量数据处理
     * @param requests
     */
    public void bulkAsyncDocument(List<DocWriteRequest<?>> requests) {
        bulkAsyncListenerDocument(requests, actionListener());
    }

    /**
     * 异步批量数据处理-自定义响应
     * @param requests
     * @param actionListener
     */
    public void bulkAsyncListenerDocument(List<DocWriteRequest<?>> requests, ActionListener<BulkResponse> actionListener) {
        BulkRequest bulkRequest = new BulkRequest();
        requests.forEach(req->{
        	bulkRequest.add(req);
        });
        highLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, actionListener);
    }


    private ActionListener<BulkResponse> actionListener() {
        ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkResponse) {
                if (bulkResponse.hasFailures()) {
                    System.err.println("Increased resource failure causes："+ bulkResponse.buildFailureMessage());
                }
            }
            @Override
            public void onFailure(Exception e) {
                System.err.println("Asynchronous batch increases data exceptions："+ e.getLocalizedMessage());
            }
        };
        return listener;
    }


    /**
     * 检索
     * @param searchRequest
     * @return
     */
    public SearchResult searchDocument(SearchRequest searchRequest) {
        List<Map<String, Object>> list = new ArrayList<>();
        SearchResponse searchResponse;
        try {
            searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] searchHit = searchResponse.getHits().getHits();
            searchResponse.getTook().getMillis();
            long totalHits = searchResponse.getHits().getTotalHits().value;
            long took = searchResponse.getTook().getMillis();
            for (SearchHit document : searchHit) {
                Map<String, Object> item = document.getSourceAsMap();
                if (item == null) {
                    continue;
                }
                Map<String, HighlightField> highlightFields = document.getHighlightFields();
                if (!highlightFields.isEmpty()) {
                    for (String key : highlightFields.keySet()) {
                        Text[] fragments = highlightFields.get(key).fragments();
                        if (item.containsKey(key)) {
                            item.put(key, fragments[0].string());
                        }
                        String[] fieldArray = key.split("[.]");
                        if (fieldArray.length > 1) {
                            item.put(fieldArray[0], fragments[0].string());
                        }
                    }
                }
                list.add(item);
            }
            Map<String, Map<String, Long>> aggregations = getAggregation(searchResponse.getAggregations());
            return new SearchResult(totalHits, list, took, aggregations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SearchResult();
    }


    private Map<String, Map<String, Long>> getAggregation(Aggregations aggregations) {
        if (aggregations == null) {
            return Collections.EMPTY_MAP;
        }
        Map<String, Map<String, Long>> result = new HashMap<>();
        Map<String, Aggregation> aggregationMap = aggregations.getAsMap();
        aggregationMap.forEach((k, v) -> {
            Map<String, Long> agg = new HashMap<>();
            List<? extends Terms.Bucket> buckets = ((ParsedStringTerms) v).getBuckets();
            for (Terms.Bucket bucket : buckets) {
                agg.put(bucket.getKeyAsString(), bucket.getDocCount());
            }
            result.put(k, agg);
        });
        return result;
    }
    
    /**
     * Bulk监听器
     * @return
     */
    private static Listener getBulkListener(){
        Listener listener =  new Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
            }
            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
            }
            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
            }
        };
        return listener;
    }

    /**
     * 保存index，将传入Object类型的JavaBean解析成List<Triplet<String, String, Object>>(fieldName, fieldType, fieldValue)
     * 默认使用id作为ElasticSearch中index的id，不使用ES的随机ID
     * @param obj
     */
    public void save(Object obj) {
    	try {
			List<Triplet<String, String, Object>> triplets = ReflectUtil.objToTriplet(obj, false);
			save(1, triplets, "id");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 保存index，将传入Object类型的JavaBean解析成List<Triplet<String, String, Object>>(fieldName, fieldType, fieldValue)
     * @param obj	JavaBean实例
     * @param id	ES中index的id
     */
    public void save(Object obj, String id) {
    	try {
			List<Triplet<String, String, Object>> triplets = ReflectUtil.objToTriplet(obj, false);
			save(1, triplets, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 保存index
     * @param bulkcnumber	批量写入时的index数量
     * @param triplets		三元组类型Triplet<String, String, Object>(fieldName, fieldType, fieldValue)
     * @param id			ES中index的id，不使用ES的随机ID
     */
    public void save(int bulkNumber, List<Triplet<String, String, Object>> triplets, String id) {
    	Listener listener = getBulkListener();
    	BulkProcessor bulkProcessor = BulkProcessor.builder(
    			(request, bulkListener) -> highLevelClient.bulkAsync(request, RequestOptions.DEFAULT, bulkListener), listener).build();
    	try {
			BulkProcessor.Builder builder = BulkProcessor.builder(
			        (request, bulkListener) -> highLevelClient.bulkAsync(request, RequestOptions.DEFAULT, bulkListener), listener);
			builder.setBulkActions(bulkNumber);
			builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));
			builder.setConcurrentRequests(0);
			builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
			builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));
			Map<String, String> source = new HashMap<String, String>();
			triplets.forEach(tpl->{
				source.put(tpl.getValue0(), tpl.getValue2().toString());
			});
			String docId = source.get(id);
			IndexRequest request = new IndexRequest(docId);
			request.id(docId);
			request.source(source);
			bulkProcessor.add(request);
		} catch (ElasticsearchGenerationException e) {
			e.printStackTrace();
		} finally {
			bulkProcessor.close();
		}
    }

    /**
     * 删除
     * @param request
     * @return
     */
    public Boolean deleteDocument(DeleteRequest request) throws IOException {
        DeleteResponse deleteResponse = highLevelClient.delete(request, RequestOptions.DEFAULT);
        if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
            System.out.println("not found doc id: "+deleteResponse.getId());
            return false;
        }
        if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
            return true;
        }
        System.out.println("deleteResponse Status: "+deleteResponse.status());
        return false;
    }


    /**
     * 异步查询更新
     * @param request
     */
    public void updateByQueryDocument(UpdateByQueryRequest request) {
        try {
            highLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

    private ActionListener<BulkByScrollResponse> bulkByScrolllistener() {
        return new ActionListener<BulkByScrollResponse>() {
            @Override
            public void onResponse(BulkByScrollResponse bulkResponse) {
                List<BulkItemResponse.Failure> failures = bulkResponse.getBulkFailures();
                if (!failures.isEmpty()) {
                    System.err.println("BulkByScrollResponse failures: "+StringUtils.join(failures, "@"));
                }
                List<ScrollableHitSource.SearchFailure> searchFailures = bulkResponse.getSearchFailures();
                if (!failures.isEmpty()) {
                    System.err.println("BulkByScrollResponse searchFailures: "+StringUtils.join(searchFailures, "@@"));
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("BulkByScrollResponse Exceptions："+ e.getLocalizedMessage());
            }
        };
    }

    /**
     * 个数查询
     * @param countRequest
     * @return
     */
    public Long countDocument(CountRequest countRequest) {
        try {
            CountResponse countResponse = highLevelClient.count(countRequest, RequestOptions.DEFAULT);
            return countResponse.getCount();
        } catch (IOException e) {
            System.err.println("CountResponse Exceptions："+ e.getLocalizedMessage());
            return 0L;
        }
    }

    public boolean updateDocument(UpdateRequest updateRequest) {
        try {
            highLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void lambdaUpdateDocument(String index, String docId, Map<?,?> fields) throws IOException {
        UpdateRequest request = new UpdateRequest(index, docId);
        request.doc(fields);
        highLevelClient.update(request, RequestOptions.DEFAULT);
    }

    public class SearchResult {
        /** 命中总数  **/
        public Long totalHits;
        /** 返回文档集合 **/
        public List<Map<String,Object>> documents;

        /** 查询耗时 单位毫秒 **/
        public Long took;
        /** 聚合结果 **/
        public Map<String,Map<String,Long>> aggregations;
        public SearchResult(Long totalHits,
                            List<Map<String, Object>> documents,
                            Long took,
                            Map<String,Map<String,Long>> aggregations) {
            this.totalHits = totalHits;
            this.documents = documents;
            this.took = took;
            this.aggregations = aggregations;
        }
        public SearchResult() {
            this.totalHits=0L;
            this.documents =new ArrayList<>();
            this.took=0L;
        }
    }
    
    public static void main(String[] args) {
		ESHelper helper = ESHelper.builder().withHost("itcast-logistics03").withPort(9200).build();
		String indexName = "example";
		// System.out.println(helper.createIndex(indexName));
		// System.out.println(helper.existsIndex(indexName));
		helper.close();
	}
}