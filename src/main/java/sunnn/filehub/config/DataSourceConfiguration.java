package sunnn.filehub.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import sunnn.filehub.util.FileHubProperties;

import javax.sql.DataSource;

@Configuration
@DependsOn("fileHubProperties")
public class DataSourceConfiguration {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:/mapper/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public DataSource dataSource() {
//        HikariDataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl(parseJdbcUrl());
        dataSource.setUsername(FileHubProperties.username);
        dataSource.setPassword(FileHubProperties.password);

        return dataSource;
    }

    private String parseJdbcUrl() {
        return "jdbc:mysql://"
                + FileHubProperties.host
                + "/" + FileHubProperties.database
                + "?characterEncoding=utf8&serverTimezone=UTC";
    }
}
