package com.orca.attack.orca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VirtualMachine {
    private String vm_id;
    private String name;
    private List<String> tags;
}
