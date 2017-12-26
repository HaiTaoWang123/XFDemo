package com.example.dxc.xfdemo.algorithm.tree;

/**
 * Created by wahaitao on 12/25/2017.
 */
public interface IStack {
    public void clear();

    public boolean isEmpty();

    public int length();

    public Object peek();

    public void push(Object x) throws Exception;

    public Object pop();
}
