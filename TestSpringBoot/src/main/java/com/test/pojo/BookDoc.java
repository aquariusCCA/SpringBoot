package com.test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDoc {
    private String id;          // 注意：這是來源文件的字段，不等於 ES 的 _id
    private String name;
    private String type;
    private String description;
}
