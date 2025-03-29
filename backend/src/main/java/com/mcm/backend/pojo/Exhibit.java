package com.mcm.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exhibit {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String comment;
    @TableField(value = "exhibition_id")
    private Integer exhibitionId;
}
