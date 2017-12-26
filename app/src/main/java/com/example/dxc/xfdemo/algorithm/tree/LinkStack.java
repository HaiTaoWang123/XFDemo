package com.example.dxc.xfdemo.algorithm.tree;

/**
 * 链栈类
 * Created by wahaitao on 12/25/2017.
 */
public class LinkStack implements IStack {
    private Node top;//栈顶元素的引用

    //将栈置空
    @Override
    public void clear() {
        top = null;
    }

    //判链栈是否为空
    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public int length() {
        Node p = top;//初始化，p指向栈顶元素，length为计数器
        int length = 0;
        while (p != null) {//从栈顶元素开始向后查找，直到p指向空
            p = p.next;//p指向后继结点
            ++length;//长度增加1
        }
        return length;
    }

    @Override
    public Object peek() {
        if (!isEmpty()) {//栈非空
            return top.data;//返回栈顶元素的值
        } else {
            return null;
        }
    }

    @Override
    public void push(Object x) throws Exception {
        Node p = new Node(x);//构造一个新结点
        p.next = p;
        top = p;//新结点成为当前的栈顶结点
    }

    @Override
    public Object pop() {
        if (isEmpty()) {
            return null;
        } else {
            Node p = top;//p指向被删结点(栈顶结点)
            top = top.next;//修改链指针，使栈顶结点从链栈中移去
            return p.data;//返回栈顶结点的数据域的值
        }
    }
}
