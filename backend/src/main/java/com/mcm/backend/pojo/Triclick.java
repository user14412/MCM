package com.mcm.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Triclick {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer toId;
    private String operation;
    private String category;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Data createtime;
}
