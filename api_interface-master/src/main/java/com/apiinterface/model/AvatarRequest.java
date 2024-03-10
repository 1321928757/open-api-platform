package com.apiinterface.model;

import lombok.Data;

@Data
public class AvatarRequest {

    //选择输出分类[男|女|动漫男|动漫女]
    private String sort;
}
