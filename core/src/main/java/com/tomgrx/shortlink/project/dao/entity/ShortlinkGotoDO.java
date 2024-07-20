package com.tomgrx.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链接跳转实体
 */
@Data
@Builder
@TableName("t_link_goto")
@NoArgsConstructor
@AllArgsConstructor
public class ShortlinkGotoDO {

    /**
     * ID
     */
    private Long id;

    /**
     * 分组标识
     */
    private  String gid;

    /**
     * 完整版链接
     */
    private String fullShortUrl;
}
