package com.tomgrx.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tomgrx.shortlink.admin.dao.entity.GroupDO;
import com.tomgrx.shortlink.admin.dto.req.GroupSortReqDTO;
import com.tomgrx.shortlink.admin.dto.req.GroupUpdateReqDTO;
import com.tomgrx.shortlink.admin.dto.resp.GroupRespDTO;

import java.util.List;

/**
 * 短链接分组接口层
 */
public interface GroupService extends IService<GroupDO> {

    /**
     * 创建短链接分组
     *
     * @param groupName 短链接分组名称
     */
    void createGroup(String groupName);

    /**
     * 创建短链接分组
     *
     * @param userName 用户名
     * @param groupName 短链接分组名称
     */
    void createGroup(String userName, String groupName);

    /**
     * 查询用户短链接分组集合
     *
     * @return 用户短链接分组集合
     */
    List<GroupRespDTO> listGroup();

    /**
     * 修改短链接分组
     *
     * @param requestParam 修改短链接分组参数
     */
    void updateGroup(GroupUpdateReqDTO requestParam);

    /**
     * 删除短链接分组
     *
     * @param gid 短链接分组标识
     */
    void deleteGroup(String gid);

    /**
     * 短链接分组排序
     *
     * @param requestParam 短链接分组排序参数
     */
    void sortGroup(List<GroupSortReqDTO> requestParam);
}
