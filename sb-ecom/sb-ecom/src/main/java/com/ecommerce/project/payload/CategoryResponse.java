package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private List<CategoryDTO> content;
}
