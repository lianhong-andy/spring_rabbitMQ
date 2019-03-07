package com.andy.domain;

import java.io.Serializable;

public class Goods implements Serializable {
    private Long id;
    private String nodeName;
    private Long pid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", nodeName='" + nodeName + '\'' +
                ", pid=" + pid +
                '}';
    }
}
