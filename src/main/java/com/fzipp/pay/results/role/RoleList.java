package com.fzipp.pay.results.role;

import com.fzipp.pay.entity.Role;
import lombok.Data;

import java.util.List;
@Data
public class RoleList {
    private Role role;
    private List<Integer> powerIds;
}
