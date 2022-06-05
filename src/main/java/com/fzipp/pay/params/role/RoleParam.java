package com.fzipp.pay.params.role;

import com.fzipp.pay.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class RoleParam {
    private Role role;
    private List<Integer> powerIds;
}
