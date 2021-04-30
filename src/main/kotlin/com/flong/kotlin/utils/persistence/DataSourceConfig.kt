package com.flong.kotlin.utils.persistence

import com.alibaba.druid.pool.DruidDataSource
import com.alibaba.druid.support.http.StatViewServlet
import com.alibaba.druid.support.http.WebStatFilter
import com.baomidou.mybatisplus.entity.GlobalConfiguration
import com.baomidou.mybatisplus.enums.IdType
import com.baomidou.mybatisplus.mapper.LogicSqlInjector
import com.baomidou.mybatisplus.plugins.PaginationInterceptor
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean
import com.flong.kotlin.modules.properties.DataSourceProperties
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.annotation.MapperScan
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import java.sql.SQLException
import javax.servlet.Filter
import javax.servlet.Servlet
import javax.sql.DataSource


@Configuration
@MapperScan(basePackages = arrayOf(DataSourceConfig.PACKAGE), sqlSessionFactoryRef = "sessionFactory")
open class DataSourceConfig {
    private val log: Logger = LoggerFactory.getLogger(DataSourceConfig::class.java)
    @Autowired
    lateinit var dataSourceProperties: DataSourceProperties
    //静态常量
    companion object {
        //const 关键字用来修饰常量，且只能修饰  val，不能修饰var,  companion object 的名字可以省略，可以使用 Companion来指代
        const val PACKAGE = "com.flong.kotlin.*.mapper";
        const val TYPEALIASESPACKAGE = "com.flong.kotlin.modules.entity";
    }

    final var MAPPERLOCATIONS = "classpath*:mapper/*.xml";

    @Primary
    @Bean("dataSource")
    open fun dataSource(): DataSource {
        var datasource = DruidDataSource();
        datasource.url = "jdbc:mysql://106.14.204.36:3308/kotlin?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone =Asia/Shanghai"
        datasource.username = "root"
        datasource.password = "123456"
        datasource.driverClassName = "com.mysql.jdbc.Driver"
        datasource.initialSize = 10;
        datasource.minIdle = 10
        datasource.maxIdle = 200
        datasource.maxActive = 6000
        datasource.timeBetweenEvictionRunsMillis = 45000
        datasource.validationQuery = "select 1"
        datasource.isTestWhileIdle = true
        datasource.isTestOnBorrow = true
        datasource.isTestOnReturn = false
        datasource.removeAbandonedTimeout = 3600
        try {
            datasource.setFilters("stat")
            datasource.init()
        } catch (e: SQLException) {
            log.error("druid configuration initialization filter", e)
        }
        return datasource;
    }


    @Bean
    open fun transactionManager(@Qualifier("dataSource") dataSource: DataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource);
    }

    @Bean
    open fun sessionFactory(dataSource: DataSource): SqlSessionFactory {
        //===1.mybatis-plus globalConfig配置
        var cfg = GlobalConfiguration();

        // 字段的驼峰下划线转换
        cfg.isDbColumnUnderline = true;
        // 全局主键策略
        cfg.setIdType(IdType.AUTO.key);
        // 全局逻辑删除
        cfg.sqlInjector = LogicSqlInjector()
        cfg.logicDeleteValue = "1"
        cfg.logicNotDeleteValue = "0"

        //===2.构造sessionFactory(mybatis-plus)
        var sf = MybatisSqlSessionFactoryBean();
        sf.setDataSource(dataSource);
        sf.setMapperLocations(PathMatchingResourcePatternResolver().getResources(MAPPERLOCATIONS));
        sf.setGlobalConfig(cfg);
        sf.setTypeAliasesPackage(TYPEALIASESPACKAGE)
        // 分页插件
        sf.setPlugins(arrayOf(PaginationInterceptor()))
        //return sf.getObject();
        return sf.`object`
    }


    /**
     * @Description 初始化druid
     * @Author        liangjl
     * @Date        2018年1月17日 下午4:52:05
     * @return 参数
     * @return ServletRegistrationBean 返回类型
     */
    @Bean
    open fun druidServlet(): ServletRegistrationBean<Servlet> {
        var bean: ServletRegistrationBean<Servlet> = ServletRegistrationBean(StatViewServlet(), "/druid/*");
        /** 初始化参数配置，initParams**/
        //登录查看信息的账号密码.
        bean.addInitParameter("loginUsername", "root");
        bean.addInitParameter("loginPassword", "root");
        //IP白名单 (没有配置或者为空，则允许所有访问)
        bean.addInitParameter("allow", "");
        //IP黑名单 (共存时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        bean.addInitParameter("deny", "192.88.88.88");
        //禁用HTML页面上的“Reset All”功能
        bean.addInitParameter("resetEnable", "false");
        return bean;
    }


    @Bean
    open fun filterRegistrationBean(): FilterRegistrationBean<Filter> {
        var filterRegistrationBean = FilterRegistrationBean<Filter>()
        filterRegistrationBean.setFilter(WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }


}