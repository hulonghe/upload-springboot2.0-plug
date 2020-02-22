package com.hlh.util.httpclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HTTPClient配置
 */


@Configuration
public class HttpClientCfg {
    @Value("${mconfig.http.maxTotal}")
    private Integer maxTotal;

    @Value("${mconfig.http.defaultMaxPerRoute}")
    private Integer defaultMaxPerRoute;

    @Value("${mconfig.http.connectTimeout}")
    private Integer connectTimeout;

    @Value("${mconfig.http.connectionRequestTimeout}")
    private Integer connectionRequestTimeout;

    @Value("${mconfig.http.socketTimeout}")
    private Integer socketTimeout;

    @Value("${mconfig.http.staleConnectionCheckEnabled}")
    private boolean staleConnectionCheckEnabled;

    /**
     * @param
     * @Method getHttpClientConnectionManager
     * @Author Laohu
     * @Description 首先实例化一个连接池管理器，设置最大连接数、并发连接数
     * @Return org.apache.http.impl.conn.PoolingHttpClientConnectionManager
     * @Exception
     * @Date 2019/7/10 18:24
     * @Version 1.0
     */
    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        //最大连接数
        httpClientConnectionManager.setMaxTotal(maxTotal);
        //并发数
        httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return httpClientConnectionManager;
    }

    /**
     * @param httpClientConnectionManager
     * @Method getHttpClientBuilder
     * @Author Laohu
     * @Description 实例化连接池，设置连接池管理器。
     * 这里需要以参数形式注入上面实例化的连接池管理器
     * @Return org.apache.http.impl.client.HttpClientBuilder
     * @Exception
     * @Date 2019/7/10 18:25
     * @Version 1.0
     */
    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(@Qualifier("httpClientConnectionManager") PoolingHttpClientConnectionManager httpClientConnectionManager) {

        //HttpClientBuilder中的构造方法被protected修饰，所以这里不能直接使用new来实例化一个HttpClientBuilder，可以使用HttpClientBuilder提供的静态方法create()来获取HttpClientBuilder对象
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        httpClientBuilder.setConnectionManager(httpClientConnectionManager);

        return httpClientBuilder;
    }

    /**
     * @param httpClientBuilder
     * @Method getCloseableHttpClient
     * @Author Laohu
     * @Description 注入连接池，用于获取httpClient
     * @Return org.apache.http.impl.client.CloseableHttpClient
     * @Exception
     * @Date 2019/7/10 18:25
     * @Version 1.0
     */
    @Bean
    public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }

    /**
     * @param
     * @Method getBuilder
     * @Author Laohu
     * @Description Builder是RequestConfig的一个内部类
     * 通过RequestConfig的custom方法来获取到一个Builder对象
     * 设置builder的连接信息
     * 这里还可以设置proxy，cookieSpec等属性。有需要的话可以在此设置
     * @Return org.apache.http.client.config.RequestConfig.Builder
     * @Exception
     * @Date 2019/7/10 18:25
     * @Version 1.0
     */
    @Bean(name = "builder")
    public RequestConfig.Builder getBuilder() {
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout);
    }

    /**
     * @param builder
     * @Method getRequestConfig
     * @Author Laohu
     * @Description 使用builder构建一个RequestConfig对象
     * @Return org.apache.http.client.config.RequestConfig
     * @Exception
     * @Date 2019/7/10 18:26
     * @Version 1.0
     */
    @Bean
    public RequestConfig getRequestConfig(@Qualifier("builder") RequestConfig.Builder builder) {
        return builder.build();
    }
}
