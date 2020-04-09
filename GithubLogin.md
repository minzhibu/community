##gitbub授权登录的过程
1.调用 GET https://github.com/login/oauth/authorize
携带下列参数
redirect_uri=http://localhost:8080/callback&scope=user&state=1
redirect_uri返回地址    
scope要获取的信息    
state状态   
2.第一次调用完上面地址后会返回一个code,和state
在一次调用GitHub的api POST https://github.com/login/oauth/access_token 来获取token
我们需要使用其他人的jar包来帮助我们发出post的请求我们使用OkHttp的jar，里面需要将对象转换
为json字符串，我们使用阿里的fastJSON来转换
maven依赖为
```xml
     <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>3.14.1</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.30</version>
    </dependency>
```
这时我们就可以发出请求来获取Token，但还有携带参数
client_id:这是github注册apps后生成的
client_secret:这是github注册apps后生成的
code:这就是上面获取的code
redirect_uri:回调的接口
state:上面获取的
3.获取到Token并处理完后调用最后的api
GET https://api.github.com/user
把token拼接在后面，返回来的就是我们需要的user信息


##mybatis Generator的使用:
   1.在pom.xml文件的plugins中配置mybatis Generator的插件
   ```xml
    <plugin>
       <groupId>org.mybatis.generator</groupId>
       <artifactId>mybatis-generator-maven-plugin</artifactId>
       <version>1.4.0</version>
       <dependencies>
           <dependency>
               <groupId>mysql</groupId>
               <!--在使用mysql時需要配置对应的mysql的驱动-->
               <artifactId>mysql-connector-java</artifactId> 
               <version>8.0.15</version>
           </dependency>
       </dependencies>
   </plugin>
   ```
   2.mybatis Generator的默认配置文件的名称为generatorConfig.xml，在resource下创建该文件，文件内容如下
   ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE generatorConfiguration
            PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
            "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
    <generatorConfiguration>
        <context id="DB2Tables" targetRuntime="MyBatis3">
            <!--配置分页对象-->
            <plugin type="org.mybatis.generator.plugins.RowBoundsPlugin"></plugin>
            <!--配置数据库的连接-->
            <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                            connectionURL="jdbc:mysql://localhost:3306/community"
                            userId="root"
                            password="321261949">
                <property name="nullCatalogMeansCurrent" value="true" />
            </jdbcConnection>
            <javaTypeResolver >
                <property name="forceBigDecimals" value="false" />
            </javaTypeResolver>
            <!--配置实体类生成的位置-->
            <javaModelGenerator targetPackage="com.sjm5z.community.model" targetProject="src/main/java">
                <property name="enableSubPackages" value="true" />
                <property name="trimStrings" value="true" />
            </javaModelGenerator>
            <!--配置对应的xml文件生成的位置-->
            <sqlMapGenerator targetPackage="mapper"  targetProject="src/main/resources">
                <property name="enableSubPackages" value="true" />
            </sqlMapGenerator>
            <!--配置接口所在的位置-->
            <javaClientGenerator type="XMLMAPPER" targetPackage="com.sjm5z.community.mapper"  targetProject="src/main/java">
                <property name="enableSubPackages" value="true" />
            </javaClientGenerator>
            <!--需要自动生成的表-->
            <table tableName="user" domainObjectName="User" >
            </table>
            <table tableName="question" domainObjectName="Question" >
            </table>
        </context>
    </generatorConfiguration>
   ```
   3. 在控制台执行该命令 mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate



        