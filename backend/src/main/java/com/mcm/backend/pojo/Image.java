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
public class Image {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String url;
    @TableField(value = "exhibit_id")
    private Integer exhibitId;
    @TableField(value = "is_primary")
    private boolean isPrimary;
}
