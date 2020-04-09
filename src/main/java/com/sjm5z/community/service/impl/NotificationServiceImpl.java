package com.sjm5z.community.service.impl;

import com.sjm5z.community.dto.NotificationDTO;
import com.sjm5z.community.dto.PaginationDTO;
import com.sjm5z.community.enums.NotificationStatusEnum;
import com.sjm5z.community.enums.NotificationTypeEnum;
import com.sjm5z.community.exception.CustomizeErrorCode;
import com.sjm5z.community.exception.CustomizeException;
import com.sjm5z.community.mapper.NotificationMapper;
import com.sjm5z.community.model.Notification;
import com.sjm5z.community.model.NotificationExample;
import com.sjm5z.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl {

    @Autowired
    private NotificationMapper notificationMapper;


    public PaginationDTO list(Long userId, Integer page, Integer size) {
        if(size < 2 || size > 20){
            size = 7;
        }
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        int totalCount = 0;
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        totalCount = Math.toIntExact(notificationMapper.countByExample(notificationExample));
        if(totalCount != 0){
            //初始分页对象的属性
            paginationDTO.setPagination(totalCount, page, size);
            //获取总记录数
            Integer page1 = paginationDTO.getPage();
            int offset = size * (page1 - 1);
            notificationExample.setOrderByClause("gmt_create desc");
            List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));
            List<NotificationDTO> notificationDTOS = new ArrayList<>();
            for (Notification notification : notifications) {
                NotificationDTO notificationDTO = new NotificationDTO();
                BeanUtils.copyProperties(notification, notificationDTO);
                notificationDTO.setTypeName(NotificationTypeEnum.nameOf(notification.getType()));
                notificationDTOS.add(notificationDTO);
            }
            paginationDTO.setQuestions(notificationDTOS);
            return paginationDTO;
        }
        return paginationDTO;
    }
    //查询有多少个通知未读
    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(0L);
        return notificationMapper.countByExample(notificationExample);

    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!notification.getReceiver().equals(user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        if(notification.getStatus().equals(NotificationStatusEnum.UNRAED.getStatus())){
            notification.setStatus(NotificationStatusEnum.READ.getStatus());
            notificationMapper.updateByPrimaryKey(notification);
        }
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOf(notification.getType()));
        return notificationDTO;
    }
}
