package com.example.dxc.xfdemo.algorithm.tree;

/**
 * Created by wahaitao on 12/25/2017.
 */

public class BiTree {
    private BiTreeNode root;//树的根结点

    public BiTree() {//构造一颗空树
        this((BiTreeNode) null);
    }

    public BiTree(BiTreeNode root) {//构造一棵树
        this.root = root;
    }

    //由先根遍历和中根遍历序列创建一颗二叉树的算法
    public BiTree(String preOrder, String inOrder, int preIndex, int inIndex, int count) {

    }

    //由标明空子树的先根遍历序列创建一颗二叉树的算法
    private static int index = 0;//用于记录preStr的索引值

    public BiTree(String preStr) {

    }

    //先根遍历二叉树的递归算法
    public void preRootTraverse(BiTreeNode T) {
        if (T != null) {
            System.out.print(T.data);//访问根结点
            preRootTraverse(T.lchild);//先根遍历左子树
            preRootTraverse(T.rchild);//先根遍历右子树
        }
    }

    //先根遍历二叉树的非递归算法
    public void preRootTraverse() throws Exception {
        BiTreeNode T = root;
        if (T != null) {
            LinkStack S = new LinkStack();
            S.push(T);
            while (!S.isEmpty()) {
                T = (BiTreeNode) S.pop();//移除栈顶结点，并返回其值
                System.out.print(T.data);//访问结点
                while (T != null) {
                    if (T.lchild != null) {//访问左孩子
                        System.out.print(T.lchild.data);//访问结点
                    }
                    if (T.rchild != null) {//右孩子非空入栈
                        S.push(T.rchild);
                    }
                    T = T.lchild;
                }
            }
        }
    }

    //中根遍历二叉树的递归算法
    public void inRootTraverse(BiTreeNode T){

    }

    //中根遍历二叉树的非递归算法
    public void inRootTraverse(){

    }

    //后根遍历二叉树的递归算法
    public void postRootTraverse(BiTreeNode T){

    }

    //后根遍历二叉树的非递归算法
    public void postRootTraverse(){

    }

    //层次遍历二叉树的算法（自左向右）
    public void levelTraverse(){

    }

    public BiTreeNode getRoot() {
        return root;
    }

    public void setRoot(BiTreeNode root) {
        this.root = root;
    }
}