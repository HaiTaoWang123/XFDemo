package com.example.dxc.xfdemo.algorithm.tree;

/**
 * Created by wahaitao on 12/26/2017.
 */

public interface IQueue {
    public void clear();

    public boolean isEmpty();

    public int length();

    public Object peek();

    public void offer(Object x) throws Exception;

    public Object poll();
}
