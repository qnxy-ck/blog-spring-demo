create table blog.blog_info
(
    id                bigint auto_increment
        primary key,
    user_id           bigint                             not null comment '所属用户',
    title             varchar(256)                       not null comment '博客标题',
    content           longtext                           not null comment '博客内容',
    reads_count       bigint   default 0                 not null comment '阅读数量',
    collections_count bigint   default 0                 not null comment '收藏数量',
    stars_count       bigint   default 0                 not null comment '点赞数量',
    created_time      datetime default CURRENT_TIMESTAMP not null,
    updated_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '博客文章信息';

create table blog.blog_info_blog_tags
(
    id           bigint auto_increment
        primary key,
    blog_id      bigint                             not null comment '博客id',
    tags_id      bigint                             not null comment '标签id',
    created_time datetime default CURRENT_TIMESTAMP not null,
    constraint blog_info_blog_tags_pk_2
        unique (blog_id, tags_id)
)
    comment '博客信息和博客标签关联表';

create table blog.blog_tags
(
    id           bigint auto_increment
        primary key,
    user_id      bigint                             not null comment '该标签所属用户',
    tag_name     varchar(32)                        not null comment '标签名称',
    created_time datetime default CURRENT_TIMESTAMP not null,
    updated_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint blog_tags_pk
        unique (tag_name, user_id)
)
    comment '博客标签';

create table blog.favorite_blog
(
    id           bigint auto_increment
        primary key,
    group_id     bigint                   not null comment '所属组id',
    blog_id      bigint                   not null comment '收藏的那个博客',
    created_time datetime default (now()) not null
)
    comment '收藏博客信息';

create table blog.favorite_blog_group
(
    id            bigint auto_increment
        primary key,
    user_id       bigint                               not null comment '该收藏组属于哪个用户',
    cover         varchar(128)                         null comment '收藏组封面图',
    name          varchar(64)                          not null comment '组名, 同一用户下不可重复',
    open          tinyint                              not null comment '是否为公开, 公开为true',
    default_group tinyint(1) default 0                 not null comment '默认分组, 不可删除, 不可修改',
    description   varchar(512)                         null comment '分组描述',
    created_time  datetime   default CURRENT_TIMESTAMP not null,
    updated_time  datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint favorite_blog_group_pk
        unique (name, user_id)
)
    comment '收藏列表分组';

create table blog.follow_info
(
    id          bigint auto_increment
        primary key,
    user_id     bigint                   not null comment '谁关注的',
    follow_id   bigint                   not null comment '关注的谁',
    follow_time datetime default (now()) not null comment '关注时间',
    constraint follow_info_pk
        unique (follow_id, user_id)
)
    comment '用户关注信息';

create table blog.stars_blog_record
(
    id           bigint auto_increment
        primary key,
    user_id      bigint                   not null comment '点赞的用户',
    blog_id      bigint                   not null comment '被点赞的博客',
    created_time datetime default (now()) not null,
    constraint stars_blogs_pk_2
        unique (user_id, blog_id)
)
    comment '博客点赞记录';

create table blog.user_info
(
    id                   bigint auto_increment
        primary key,
    username             varchar(64)                        not null comment '用户名',
    password             varchar(128)                       not null comment '用户密码',
    phone_num            varchar(128)                       not null,
    user_avatar          varchar(128)                       not null comment '用户头像',
    gender               tinyint                            not null comment '用户性别',
    birthday             date                               not null comment '生日',
    profession           varchar(128)                       not null comment '职业',
    personal_description varchar(512)                       null comment '个人描述',
    deleted              tinyint  default 0                 not null comment '是否删除, 删除为true(1)',
    created_time         datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_time         datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint user_info_pk_2
        unique (username)
);

