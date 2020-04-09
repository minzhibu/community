package com.sjm5z.community.cache;

import com.sjm5z.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagCache {
    public static List<TagDTO> get() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTag(Arrays.asList("js", "php", "css", "java", "c++", "html", "node", "python", "c"));
        tagDTOS.add(program);
        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTag(Arrays.asList("laravel", "spring", "express", "django", "flask", "yii", "ruby-on-rails", "tornado", "koa", "struts"));
        tagDTOS.add(framework);
        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTag(Arrays.asList("linux", "nginx", "docker", "apache", "ubuntu", "centos", "tomcat", "负载均衡", "unix", "hadoop", "windows-server"));
        tagDTOS.add(server);
        TagDTO DB = new TagDTO();
        DB.setCategoryName("数据库");
        DB.setTag(Arrays.asList("mysql", "redis", "mongodb", "sql", "oracle", "nosql", "memcached", "sqlserver", "postgresql", "sqlite"));
        tagDTOS.add(DB);
        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTag(Arrays.asList("git", "github", "visual-studio-code", "vim", "sublime-text", "xcode", "intellij-idea", "eclipse", "maven", "ide", "svn", "visual-studio", "atom", "emacs", "textmate", "hg"));
        tagDTOS.add(tool);
        return tagDTOS;
    }
}
