package com.apiinterface.common;

import java.io.Serializable;
import lombok.Data;

/**
 * 删除请求
 *
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 批量操作
     */
    private Long[] ids;

    private static final long serialVersionUID = 1L;
}